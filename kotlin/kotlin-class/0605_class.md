# kotlin 수업

## 프로퍼티
1. 프로퍼티 - Backing Field(메모리) + 접근자 메소드(Getter + Setter = Accessor Method)

```java
	class User(var name: String, var age: Int) {
		fun getName1(): String { 
			return name 
		}

		fun setName1(name: String) {
			this.name = name
		}
	}
```

2. 프로퍼티를 메소드처럼 사용할 수 있다.
> 필드를 만들지 않고, 접근자 메소드만을 제공하는 기능

_var_ : getter + setter
_val_ : getter

```java
	class User(var firstName: String, var lastName: String) {
		var fullName: String
			get() = "$firstName, $lastName"
			set(value) {
				//"heidi, Yun"
				//"heidi,Yun"
				//"heidi Yun"
				val arr = value.split(", ", " ", ",")
				firstName = arr[0]
				lastName = arr[1]
			}
	}
```
* 변수를 초기화 하지 않았을 경우에는 backing field를 제공하지 않고
변수를 부를때마다 계산합니다.

2. 정규 표현식 (Regular Expression, Regex, 패턴매칭)
> 형식을 검증하거나, 형식과 매칭할때 씁니다.

## 확장 함수
> 기존 클래스를 깨뜨리지않고 확장하는 방법

1. 수직 확장 - 상속
> 부모의 구현을 물려받는 것.
* 부모의 구현이 변경될 경우, 자식도 영향을 받는다. 
	* 깨지기 쉬운 기반 클래스 문제 => 포함(composition)으로 해결

2. 수평 확장
> 새로운 함수를 마치 기존 클래스의 멤버 메소드 인것 처럼 사용할 수 있다.
* 정적(컴파일러) 바인딩
* 확장 함수를 사용하는 추가적인 비용이 없다.

```java
	open class View {
		open fun showOff() = println("I'm View")
	}

	class Button : View() {
		override fun showOff() = println("I'm Button")
	}

	>>> val view: View = Button()
	>>> view.showOff()
	// I'm Button
	// 동적 바인딩.

	fun View.showOff() = println("I'm View")
	fun Button.showOff() = println("I'm Button")
	>>> val view: View = Button()
	>>> view.showOff()
	// I'm View
	// 정적 바인딩

```

_정적 바인딩_
* 선언된 타입에 의해서 결정된다.
* 함수 호출을 위한 별도의 비용이 소모되지 않는다.
  (이미 컴파일 시점에 결정)

_동적 바인딩_
* 선언된 타입이 아닌, 실제로 가지고 있는 객체의 타입에 의해서 결정된다.

### 함수형 프로그맹이 다중 스레드에서 안전한 이유

OS
1. process - 실행하는 환경
2. Thread - 실제 기계어를 실행

Multi Process
가상 메모리
	: 각각의 프로세스는 자신만의 페이지 테이블을 갖고 다른 물리 메모리에 매핑된다.
	: 프로세스 간에 데이터를 교환하기 위해서는 IPC를 사용해야 한다.

Thread - LWP(Light Weight Process)
1. 프로세스 환경 안에서 생성되기 때문에,
   같은 프로세스에서 동작하는 모든 스레드는 환경을 공유한다.
2. IPC가 필요 없다.
3. 동기화에 대해서 고려해야 한다.
	-> 동시에 여러 개의 스레드가 접근 가능한 함수를 만들어야 한다.
	-> 스레드 안전성
4. 멀티 코어
	확장성 :  더 많은 코어를 가지고 있는 환경에서 수행한다면 수능은 향상되어야 한다.

_동기화_
	:race condition으로 인한 문제를 해결하기 위해 필요. 
	(원자적이지 않은 연산)
	: 여러개의 스레드가 접근 가능한 메모리를 임계 영역으로 보호해야 한다.
		=> Mutex(상호 배제)

```java
	class IncThread(val lock: Lock): Thread() {
		companion object {
			var n = 0
		}

		override fun run() {
			for (e in 1..1000000) {
				synchronized(lock) {
					++n
				}

			/*	
				lock.lock()
				try {
					++n
				} finally {
					lock.unlock()
				}
			*/	
			// 데드락 문제. 
			// 다른 스레드가 락을 풀지 않고 스레드가 종료된 경우.
			}
		}
	}
```
* 락을 쓸 경우, 같은 락을 써야한다.

## 순수 함수
> 인자를 통해서 함수의 결과가 결정된다.
같은 인자를 전달했다면 함수의 결과는 항상 같아야 한다.
```java
	fun inc(n: Int): Int {
		var result = 0
		for (e in 1..n)
			++result

		return result
	}

```
## data class
> 불변 객체를 만들 수 있는 기능을 제공한다.

```java
	data class User(val name: String, val age: Int) 

	fun main(args: Array<String>) {
    	val user = User("yun", 32)
    	val user1 = user.copy(name = "bob")
    	println(user)
    	println(user1)
	}

	>> User(name=yun, age=32)
	>> User(name=bob, age=32)
```
