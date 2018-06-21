# 6. 코틀린 타입 시스템

## 6.1 널 가능성
> NullPointerException 오류를 피하기위한 코틀린 기법이다.

null 여부를 타입 시스템에 추가하여 컴파일러가 오류를 감지할 수 있도록 한 것.

### 6.1.1 널이 될 수 있는 타입
> 널이 될 수 있는 타입을 명시적으로 지원한다.

```java
	fun strLen(s: String) = s.length
	>>> strLen(null)
	Error : Null can not be a value of a non-null type String
```
s의 타입이 String이다. 
즉, s가 항상 String의 인스턴스여야 한다. 
	-> s의 값에 null이 들어갈 수 없다.
	-> NullPointerException가 발생하지 않는다는 것을 보장한다.

```java
	fun strLen(s: String?) = s!!.length
	>>> strLen(null)
```
* 타입 뒤에 '?'를 붙이면 null 참조를 저장할 수 있다.

_주의할 점_

null이 될 수 있는 타입의 변수는 수행할 수 있는 연산이 제한된다.
```java
	fun strLen(s: String?) = s.length
	// 컴파일 오류 발생.
```
1. 위와 같이 메소드를 직접 호출 할 수 없다.

```java
	val x: String? = null
	val y: String = x
	//컴파일 오류 발생.
```

2. nullable 타입의 변수를 null을 저장할 수 없는 변수에 대입할 수 없다.

```java
	fun strLen(s: String) = s.length
	val x: String? = "hello"
	>>> strLen(x)
	// 컴파일 오류 발생
```

3. nullable 타입의 값을 nullable이 아닌 타입을 파라미터로 받는 함수에 인자로 전달할 수 없다. 

### 6.1.2 타입의 의미
> 타입은 어떤 값들이 가능한지와 그 타입에 대해 수행할 수 있는 연산의 종류를 결정한다.

_자바의 null_
1. 자바에서 특정 변수에 대해서 null 체크를 하기 전까지 해당 변수가 어떤한 연산을 수행할 수 있는지 알 수 없다. 
2. String 타입의 경우 null을 저장할 수 있지만, null을 instanceof연산자로 검사할 경우 String타입에 들어갈 수 없다.

-> null을 다루기가 힘들다.

_코틀린의 null_
1. nullable을 구분하면 각 타입의 값에 대해 어떤 연산이 가능할지 명확히 알 수 있다.
2. 실행 시점에 예외를 발생시킬 수 있는 연산을 판단할 수 있다.

### 6.1.3 안전한 호출 연산자 : ?.
> null 검사와 메소드 호출을 한 번의 연산으로 수행한다.

1. 메소드 호출
```java
	val s: String? = "hello"

	/* ?. 안쓰고 코드 작성 */
	if (s != null) s.toUpperCase()
	else null

	/* ?. 적용 */
	s?.toUpperCase()
```
호출하려는 값이 null이 아니면 메소드가 호출되고, 
null이라면 호출이 무시되고 null이 결과 값이 된다.

2. 프로퍼티 접근
```java
	class Employee(val name: String, val manager: Employee?)

	fun managerName(employee: Employee): String? 
		= employee.manager?.name

	>>> val ceo = Employee("yu", null)
	>>> println(managerName(ceo))
	null
```

### 6.1.4 엘비스 연산자 : ?:
> null 대신 사용할 디폴트 값을 지정할 때 유용하다.
```java
	fun foo(s: String?) {
		val t: String = s ?: ""
	}
```
엘비스 연산자를 기준으로 좌항의 값이 null이 아니면 좌항의 값이 결과 값이 되고, 좌항의 값이 null이면 우항의 값이 결과 값이 된다.

```java
	/* 안전한 호출 연산자와 연쇄하여 쓰기 */
	fun strLen(s: String?): Int = s?.length ?: 0
	>>> println(strLen("hello"))
	5
	>>> println(strLen(null))
	0
```

* 코틀린에서는 return이나 throw등의 연산도 식이다. 
	* 엘비스 연산자의 우항에 return, throw등의 연산을 넣을 수 있다.
	* 좌항이 null이면 즉시 어떤 값을 반환하거나 예외를 던질 수 있다.
=> 함수의 전제 조건을 검사하는 경우 유용하다.
	ex ) 함수를 실행하는 데 필요한 정보가 없다는 등..

### 6.1.5 안전한 타입 캐스트 : as?
> 대상 값을 as로 지정한 타입으로 바꿀 수 없으면 ClassCasException이 발생한다.

* as? 연산자는 값을 지정한 타입으로 캐스트하고, 캐스트할 수 없으면 null을 반환한다.

### 6.1.6 null이 아님을 단언 : !!
> 어떤 타입이든 널이 될 수 없는 타입으로 강제 변환한다.

값에 null이 들어가면 NullPointerException이 발생한다.

```java
	fun strLen(s: String?) = s!!.length
	>>> strLen(null)
```

null단언문을 한줄에 여러번 쓰게 되면 어떤 곳에서 NullPointerException이 발생했는지 알 수 없으므로 권장하지 않는다.

### 6.1.7 let 함수
> 널이 될 수 있는 값을 널이 될 수 없는 값만 파라미터로 받는 함수의 인자로 넘길때 사용한다.


```java
	fun sendEmailTo(email: String) {...}

	val email: String? = "brand@gamil.com"
	sendEmailTo(email)
	// 컴파일 오류가 난다.

	if (email != null)
		sendEmailTo(email)
	// null check를 해주어야 한다.
```
let 함수는 자신의 수신 객체를 인자로 전달받은 람다에게 넘긴다. 
* 수신 객체가 null이 아닐 경우 람다 식 안에서 널이 될 수 없는 타입의 값이 된다.
* 수신 객체가 null일 경우 람다를 실행하지 않는다.

```java
	email?.let { email -> sendEmailTo(email) }
	email?.let { sendEmailTo(it) }
```

* 긴 식이 있고 그 값이 널이 아닐때 수행해야 하는 로직이 있을때 let을 쓰면 유용하다.

```java
	val person: Person? = getTheBestPersonInTheWorld()
	if (person != null) sendEmailTo(person.email)
	// person 변수를 만들 필요 없다.

	getTheBestPersonInTheWorld?.let { sendEmailTo(it.email) }
```
* let을 중첩시킬 경우 코드의 가독성이 떨어진다.
	* if문을 사용해 검사하는 것이 더 낫다.

### 6.1.8 나중에 초기화할 프로퍼티
> 코틀린에서 널이 될 수 없는 프로퍼티는 생성자 안에서 초기화하지 않고 특별한 메소드 안에서 초기화할 수 없다.

lateinit 키워드를 val/var 앞에 붙이면 당장 초기화 하지 않고, 특정 메소드 안이나 나중에 초기화 할 수 있다.
	-> 널검사를 수행하지 않고 프로퍼티를 수행할 수 있다.

```java
	class MyService {
		fun performAction(): String = "foo"
	}

	class MyTest {
		private lateinit var myService: MyService
	}
```

_규칙_
* 나중에 초기화하는 프로퍼티는 항상 **var**이어야 한다.
	* val 프로퍼티는 반드시 생성자안에서 초기화 되어야 한다.

프로퍼티가 초기화되기전에 프로퍼티에 접근하면 "lateinit property..." 예외가 발생한다.

### 6.1.9 널이 될 수 있는 타입 확장
> 확장 함수인 메소드가 알아서 null을 처리한다.

```java
	fun verifyUserInput(input: String?) {
		if (input.isNullOrBlank()) { // 안전한 호출을 안해도 됨.
			println("please fill")
		}
	}

	>>> verifyUserInput(null) // 수신객체로 null을 지정해도 된다.
```
isNullOrBlank는 null을 명시적으로 검사해서 null인 경우 true를 반환하고, null이 아닌 경우 isBlank를 호출한다.

* 코틀린에서 널이 될 수 있는 타입의 확장 함수 안에서는 this가 null이 될 수 있다. (자바와의 차이점)

### 6.1.10 타입 파라미터의 널 가능성
> 코틀린에서 함수나 클래스의 모든 타입 파라미터는 기본적으로 nullable하다.

```java
	fun <T> printHashCode(t: T) {
		println(t?.hashCode()) // null가능성이 있으므로 안전한 호출 필수.
	}
	>>>printHashCode(null)// null을 인자로 받을 수 있다.
```
T를 클래스나 함수 안에서 타입 이름으로 사용하면 이름 끝에 ?가 없어도 nullable하다.

```java
	fun <T: Any> printHashCode(t: T) { // T는 null이 될 수 없다.
		println(t.hashCode()) // null 검사를 안해도 됨.
	}

	>>>printHashCode(null) 
	//컴파일 오류가 발생.
```
* 타입 파라미터가 널이 아님을 보장하려면 타입 상한을 지정해야 한다.

### 6.1.11 널 가능성과 자바
> 코틀린 nullable 자바와의 상호 운용성 고려.

1. 자바의 어노테이션 사용.
@Nullable String -> String?
@NotNull String -> String

==위와 같은 널 가능성 어노테이션이 소스코드에 없는 경우 자바의 타입은 코틀린의 플랫폼 타입이 된다.==

_플랫폼 타입_
> 코틀린이 널 관련 정보를 알 수 없는 타입.

* null이 될 수 잇는 타입으로 처리해도되고, null이 될 수 없는 타입으로 처리해도 된다.
	-> 컴파일러는 모든 연산을 허용.
	-> 플랫폼 타입에 대해 수행하는 모든 연산은 사용자 책임이 된다.

```java
	public class Person {
		private final String name;

		public Person(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
```
위와 같은 경우 코틀린 컴파일러는 String 타입의 널 가능성을 알 수 없다.

_코틀린과 자바를 혼합한 클래스계층 선언_
* 코틀린에서 자바 메소드를 오버라이드할때 그 메소드의 파라미터와 반환 타입의 널 가능성을 결정해야 한다.

```java
//자바
	interface StringProcessor {
		void process(String value);
	}
//코틀린
	class StringPrinter : StringProcessor {
		override fun process(value: String) {
			println(value)
		}
	}

	class NullableStringPrinter : StringProcessor {
		overrdie fun process(value: String?) {
			if (value != null) {
				println(value)
			}
		}
	}
```
위와 같은 두 개의 코틀린 클래스는 정상적으로 컴파일 된다.

* 코틀린 컴파일러는 널이 될 수 없는 타입으로 선언한 모든 파라미터에 대해 널이 아님을 검사하는 던언문을 만든다.
	* 자바 코드가 해당 메소드에게 null을 넘기면 단언문에 의해 예외가 발생한다.
