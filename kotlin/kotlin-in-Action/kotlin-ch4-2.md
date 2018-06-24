# 4장. 클래스, 객체, 인터페이스 (2)
## 4.3 컴파일러가 생성한 메소드 : 데이터 클래스와 클래스 위임
### 4.3.1 모든 클래스가 정의해야 하는 메소드

고객 이름과 우편번호를 저장하는 클래스를 만들어보자.
```java
	class Client(val name: String, val postalCode: Int)
```
* 문자열 표현: toString()
```java
	class Client(val name: String, val postalCode: Int) {
		override fun toString() = "Client(name=$name, postalCode=$postalcode)"
	}
```

* 객체의 동등성: eqauls()
```java
	val client1 = Client("오", 4122)
	val client2 = Client("오", 4122)
	println(client1 == client2) // 객체의 동등성 판단
	>> false
	// equals를 오버라이딩하지 않아서 Client클래스의 동등성판단 요구사항을 만족하지 않는다.
```
	* 참조 동등성을 비교하기 위해서는 '===' 연산자를 쓴다.

```java
	class Client(Val name: String, val postalCode: Int) {
		override fun equals(other: Any?): Boolean {
			if (ohter == null || other !is Client)
				return false
			return name == ohter.name &&
			 postalCode == other.postalCode
		}

		override fun toString() = "Client(name=$name, postalCode=$postalcode)"
	}
```
hashCode를 구현하지 않아서 제대로 동작하지 않을수 있다.

해시 컨테이너: hashCode()
> equals()가 true를 반환하는 두 개개체는 반드시 같은 hashCode()를 반환해야 한다.
> hashSet은 먼저 객체의 해시 코드를 비교하고 해시 코드가 같은 경우에만 실제 값을 비교한다.

```java
	class Client(val name: String, val postalCode: Int) {
		...
		override fun hashCode(): Int = name.hashCode() * 31 + postalcode
	}		
```

### 4.3.1 데이터 클래스 : 모든 클래스가 정의해야 하는 메소드 자동 생성
> 클래스가 데이터를 저장하는 역할만을 수행한다면 toString, equals, hashCode를 반드시 오버라이드 해야 한다.
> data라는 변경자를 클래스 앞에 붙이면 컴파일러가 자동으로 만들어준다.
```java
	data class Client(val name: String, val postalCode: Int)
```

* 데이터 클래스와 불변성: copy()
컬렉션에 데이터 클래스 객체를 담는 경우엔 불변성이 필수적.
	* 객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해준다.

다중스레드 프로그램에서 하나의 스레드가 사용 중인 데이터를 다른 스레드가 변경할 수 없으므로 동기화 문제에 좀 더 자유롭다.
불변객체를 더 쉽게 만들 수 있게 해준다.

### 4.3.3 클래스 위임 : by 키워드 사용
 객체지향 프로그래밍의 취약성은 구현 상속에 의해 발생한다.
	:  하위 클래스가 상위 클래스의 세부 구현 사항에 의존하게 되어 상위 클래스가 바뀌면 하위 클래스가 깨지게 되는 문제.
	: 그래서 코틀린에서는 클래스를 기본적으로 final로 취급한다.

**상속을 허용하지 않는 클래스에 새로운 동작 추가해야 한다면?**
	:  주로 데코레이터 패턴을 사용한다.
	    1. 대신 사용할 새로운 클래스(데코레이터)를 만들어서 기존 클래스와 같은 인터페이스를 제공하고, 기존 클래스를 데코레이터 내부에 필드로 유지한다.
	    2. 추가되는 기능은 데코레이터의 메소드로 정의한다. 
	    3. 기존 기능을 사용할 때에는 데코레이터의 메소드가 기존 클래스의 메소드에게 요청을 전달한다.
	

```java
	class DelegatingCollection<T> : Collection<T> {
		private val innterList = arrayListOf<T>()

		override val size: Int get() = innterList.size
		override fun isEmpty(): Boolean = innerList.isEmpty()
		override fun contains(element: T): Boolean = innerList.contains(element) 
	}
 ```

 **인터페이스를 구현할 때 by키워드를 통해 구현이 다른 객체에 위임 중이라는것을 명시할 수 있다.**
 
 ```java
	class DelegatingCollection<T>(
	innterList: Collection<T> = ArrayList<T>()
	) : Collection<T> by innerList{} // 인터페이스 Collection<T>의 구현을 위임했다.
 ```
클래스 안에 있던 모든 메소드 정의가 없어짐.


// 내용 추가하기


## 4.4 object 키워드 :  클래스 선언과 인스턴스 생성
> object 키워드를 사용하는 모든 경우 클래스를 정의하면서 동시에 객체를 생성한다는 공통점이 있다.

* 객체 선언은 싱글톤을 정의하는 방법 중 하나.
* 동반 객체는 인스턴스 메소드는 아니지만 어떤 클래스와 관련있는 메소드와 팩토리 메소드를 담을 때 쓰인다.
* 객체 식은 자바의 익명 내부 클래스 대신 쓰인다.

### 4.4.1 객체 선언: 싱클톤 쉽게 만들기
객체 선언 : 클래스 선언 + 단일 인스턴스 선언

* 객체 접근 시점에 객체가 생성된다.
* 주/부 생성자를 사용할 수 없다.
   (객체 선언과 동시에 생성자 호출 없이 바로 만들어진다.)
* 상속과 인터페이스 구현할 수 있다.
* 프로퍼티, 메소드, 초기화 블록이 들어갈 수 있다.
* 클래스 안에서 객체 선언을 사용하더라도 객체 선언의 인스턴스는 단 하나만 생성된다.
```java
	obejct DataProviderManager {
		fun registerDataProvider(provider: DataProvider) {...}

		val allDataProviders: Collection<DataProvider>
		get() = ...
	}
>> DataProviderManager.registerDataProvider(..)
	// 객체를 사용할 때는 이름 그대로 사용하면 된다.
```
* 싱클톤 객체를 함수의 인자로 넘겨줄 수 있다.
*  클래스 안에서 객체를 선언할 수 있다. (중첩 객체)
```java
	data class Person(val name: String) {
		object NameComparator : Comparator<Person> {
			override fun compare(p1: Person, p2: Person): Int =
				p1.name.compareTo(p2.name)
		}
	} 
```

### 4.4.2 동반 객체 : 팩토리 메소드와 정적 멤버가 들어갈 장소
> 코틀린은 static 키워드를 지원하지 않는 대신, 최상위 함수와 객체 선언을 활용한다.

**최상위 함수를 사용할 때, 주의할 점**
==최상위 함수는 클래스의 private 멤버에 접근할 수 없다.==
클래스의 private 멤버에 접근해야 하는 함수가 필요할 때는 클래스에 중첩된 객체 선언의 멤버 함수로 정의한다.
ex ) 팩토리 메소드

클래스 안에 정의된 객체에 _companion_ 키워드를 붙이면 그 클래스의 동반 객체가 된다.
> 동반 객체의 프로퍼티나 메소드에 접근하려면 해당 클래스 이름을 사용하면 된다.
> 객체의 이름은 따로 지정할 필요가 없다.
> 자바의 정적 메소드 호출, 정적 필드 사용 구문과 같다.

```java
	class A {
		companion object {
			fun bar() {
				println("Companion object called")
			}
		}
	}

	A.bar()
	>> Companion object called
```
* 동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근할 수 있다.
	(private 생성자를 호출할 수 있다.)
```java
	class User {
		val nickname: String
		constructor(email: String) {
			nickname = email.substringBefore('@')
		} // 부 생성자

		constructor(facebookAccountId: Int) {
			nickname = getFaceboockName(facebookAccountId)
		} // 부 생성자
	}
```

위의 코드를 팩토리 메소드로 구현해보자.

```java
	class User private constructor(val nickname: String) { 
	// 생성자의 가시성이 private이다. 외부에서 User클래스의 인스턴스 생성 불가능
		companion object { 
			// 동반 객체를 사용해서 User 인스턴스 생성해야 함.
			fun newSubscribingUser(email: String) = 
				User(email.substringBefore('@'))
			fun newFacebookUser(accountId: Int) = 
				User(getFacebookName(accountId))
		}
	}

	>>> val subscribingUser = User.newSubscribingUser("bob@gamil.com")
	>>> val facebookUser = User.newFacebookUser(4)
	>>> println(subscribingUser.nickname)
	bob
```
* 팩토리 메소드가 선언된 클래스의 하위 클래스 객체를 반환할 수 있다.
* 생성할 필요가 없는 객체를 생성하지 않을 수도 있다.
	이미 존재하는 인스턴스에 해당하는 정보를 받을 경우, 기존 인스턴스를 반환할 수 있다.

**클래스를 확장해야 하는 경우 동반 객체 멤버를 하위 클래스에서 오버라이드할 수 없으므로 여러 생성자를 사용하는 편이 더 낫다.**

### 4.4.3 동반 객체를 일반 객체처럼 사용
동반 객체는 클래스 안에 정의된 일반 객체이다.
1. 동반 객체에 이름을 붙일 수 있다.
2. 동반 객체가 인터페이스를 상속할 수 있다.
3. 동박 객체 안에 확장 함수와 프로퍼티를 정의할 수 있다.

회사 급여 명부를 제공하는 웹서비스를 만든다고 가정해보자.
객체를 직렬화하거나 역직렬화해야 한다. (직렬화 로직을 동반 객체 안에 넣을 수 있다.)
```java
	class person(val name: String) {
		companion object Loader { // 동반 객체에 이름을 붙인다.
			fun fromJSON(jsonText: String) : Person = ...
		}
	}

	>> val person = Person.Loader.fromJSON("{name: 'Dmitry'}")
	// Person.fromJSON("..") 가능
	>> person.name
	Dmitry // 왜 이런결과가 나오는걸까?

```
이름을 짓지 않은 동반 객체 이름은 자동으로 Companion이 된다. (동반 객체 확장에서 더 자세히 다룸)

**동반 객체에서 인터페이스 구현하기**
인터페이스를 구현하는 동반 객체를 참조할 때 객체를 둘러싼 클래스의 이름을 바로 사용할 수 있다.

```java
	interface JSONFactory<T> {
		fun fromJSON(jsonText: String): T
	}

	class Person(val name: String) {
		companion object : JSONFactory<Person> { // 동반객체에서 인터페이스 구현
			override fun fromJSON(jsonText: String): Person = ...
		}
	}
```

```java
	fun loadFromJSON<T>(factory: JSONFactory<T>): T {...}

	loadFromJSON(person) // 동반 객체가 구현한 JSONFactory의 인스턴스를 넘길 때 클래스 이름을 사용한다.

	/*자바*/
	<T> loadFromJSON<T>(JSONFactory<T> factory) {
		...
	}

	loadFromJSON(new JSONFactory{
		...
	})
```
클래스의 동반 객체는 클래스에 정의된 인스턴스를 가리키는 정적 필드로 컴파일 된다. 
이름을 붙이지 않았다면 Companion이라는 이름으로 참조할 수 있다.

```java
	class A {
		@JvmStatic 
		fun...

		@JvmField
		val field...
	}
```

**동반 객체 확장**
클래스에 동반 객체가 있으면 그 객체 안에 함수를 정의함으로써 클래스에 대해 호출할 수 있는 확장 함수를 만들 수 있다.

```java
	class Person(val firstName: String, val lastName: String) {
		companion object {

		}
	}

	fun Person.Compaion.fromJSON(json: String) : Person {

	}

	>> val p = Person.fromJSON(json)
```
동반 객체에 대한 확장 함수를 작성하려면 원래 클래스에 동반 객체를 꼭 선언해야 한다.

### 4.4.4 객체 식 : 익명 내부 클래스를 다른 방식으로 작성
익명 객체는 자바의 익명 내부 클래스를 대신한다.

```java
	window.addMouseListener(
		object : MouseAdapter() { // MouserAdapter를 확장하는 익명 객체를 선언한다.
			override fun mouseClicked(e: MouseEvent) {

			}
				// MouseAdapter의 메소드를 오버라이드 한다.

			override fun mouseEntered(e: MouseEvent) {

			}
		})

```
객체 식은 클래스를 정의하고 그 클래스에 속한 인스턴스를 생성한다.
그러나, 클래스나 인스턴스에 이름을 붙이지는 않는다.

이름을 붙이고 싶다면 변수에 무명 객체를 대입하면 된다.
```java
	val listener = object : MouseAdapter() {
		override fun mouseClicked(e: MouseEvent) {...}
			override fun mouseEntered(e: MouseEvent) {...}
	}
```

자바의 익명 클래스는 한 인터페이스만 구현하거나 한 클래스만 확장할 수 있다.
코틀린의 익명 클래스는 여러 인터페이스를 구현하거나 클래스를 확장할 수 있다.

객체 선언과 달리 익명 객체는 객체 식이 쓰일 때마다 새로운 인스턴스가 생성된다.

자바와 달리 final이 아닌 변수도 객체 식 안에서 사용할 수 있다.
	-> 객체 식 안에서 변수 값을 변경할 수 있다.

```java
	fun countClicks(window: Window) {
		var clickCount = 0 //로컬 변수를 정의한다.

		window.addMouseListener(object : MouseAdapter() {
			override fun mouseClicked(e: MouseEvent) {
				clickCount++ // 로컽ㄹ 변수의 값을 변경한다.
			}
		})
	}
```