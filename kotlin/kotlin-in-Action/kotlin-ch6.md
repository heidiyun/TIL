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

## 6.2 코틀린의 원시 타입
> 코틀린은 원시 타입과 래퍼 타입을 구분하지 않는다.
ㅏ
### 6.2.1 원시 타입 : Int, Boolean
코틀린은 항상 같은 타입을 사용한다.
```java
	val i: Int = 1
	val list: List<Int> = listOf(1, 2, 3)
```

원시 타입의 값에 대해 메소드를 호출할 수 있다.
```java
	fun showProgress(progress: Int) {
		val percent = progress.coerceIn(0, 100)
	}
```

실행 시점에서 숫자 타입은 가능한 가장 효츌적인 방식으로 표현된다.
변수, 프러퍼티, 파라미터 등에서 Int타입은 자바의 int타입으로 컴파일 되고,
컬렉션 등에서 Int타입은 자바의 java.lang.Integer 객체로 컴파일 된다.

**Int와 코틀린 타입은 nullable이 될 수 없어서 자바 원시 타입으로 컴파일 할 수 있고, 자바의 원시 타입 또한 null이 들어갈 수 없으므로 코틀린에서 null이 될 수 없는 타입으로 취급할 수 있다.**

### 6.2.2 널이 될 수 있는 원시 타입: Int?, Boolean? 등

코틀린에서 null이 될 수 있는 원시 타입은 자바의 래퍼 타입으로 컴파일 된다.

클래스의 타입 인자로 원시 타입을 넘기면 코틀린은 그 타입에 대한 박스 타입을 사용한다.(래퍼 타입)
```java
	val listOfInts = listOf(1, 2, 3)
	// 1, 2, 3은 java.lang.Integer 타입으로 컴파일 된다.
```

### 6.2.3 숫자 변환
> 코틀린은 숫자 타입을 자동 변환 해주지 않는다.

```java
	val i = 1
	val l: Long = i // 컴파일 오류 type mismatch

	/* 정상 작동 코드 */
	val i = 1
	val l: Long = i.toLong()
	// 직접 변환 메소드를 호출해야 한다.
```
* 코틀린은 모든 원시 타입 (Boolean 제외)에 대한 변환 함수를 제공한다.

_원시 타입 리터럴_
1. Long : 123L
2. Double : 0.12, 2.0
3. Float : 12.4f, 234F
4. 16진 리터럴 : 0xCAFE, 0XbcdL
5. 2진 리터럴 : 0b00000101, 0B00000101

코틀린 1.1 부터 숫자 리터럴 중간에 밑줄을 넣을 수 있다.
(1_234, 1_0000_0000)

* 숫자 리터럴을 사용할 때는 보통 변환 함수를 호출할 필요가 없다.
* 숫자 리터럴을 타입이 알려진 변수에 대입하거나 함수에게 인자로 넘기면 컴파일러가 자동 변환을 지원해준다.
* 산술 연산자는 대부분의 타입을 받아들일 수 있게 오버로드되어 있다.
```java
	fun foo(l: Long) = println(l)

	>>> val b: Byte = 1 
	>>> val l = b + 1L // 산술 연산자 오버로드
	>>> foo(42) // 컴파일러는 42를 Long값으로 해석.

```

### 6.2.4 Any, Any? : 최상위 타입
> 코틀린에서 Any타입이 모든 널이 될 수 없는 타입의 최상위 타입이다.

자바에서 원시 타입이 Object의 하위 타입이 되려면 래퍼 타입으로 변환해야 했는데, 코틀린에서는 Any가 모든 원시 타입의 부모 타입이다.

* 코틀린에서 원시 타입 값을 Any타입의 변수에 대입하면 자동으로 래퍼 타입으로 변환해준다.
```java
	val answer: Any = 42
```
* Any 타입은 java.lang.Object에 대응한다.
	* 자바의 Object 타입은 코틀린의 Any!로 취급된다.

* java.lang.Object에 있는 다른 메소드는 Any에서 사용할 수 없다.
사용하려면, Any를 java.lang.Object 타입으로 캐스팅해야 한다.

### 6.2.5 Unit 타입 :  코틀린의 void
> 코틀린의 Unit 타입은 자바 void와 같은 기능을 한다.

```java
	fun f(): Unit { ... }
	fun f() { ... } // 위의 함수와 같다.
```

코틀린 함수의 반환 타입이 Unit이고 그 함수가 제네릭 함수를 오버라이드하지 않는다면 내부에서 자바 void 함수로 컴파일된다.

_void와 다른점_
1. Unit을 타입 인자로 쓸 수 있다.
2. Unit타입의 함수는 Unit 값을 암묵적으로 반환한다.
-> 제네릭 파라미터를 반환하는 함수를 오버라이드하면서 반환 타입으로 Unit을 쓸 때 유용한 특징

```java
	interface Processor<T> {
		fun process(): T
	}

	class NoResultProcessor: Processor<Unit> {
		override fun process() { // Unit을 반환하지만 타입을 지정할 필요 x
			... 
		}
	}
```

### 6.2.6 Nothing 타입 : 함수가 정상적으로 끝나지 않는다.
> 무한 루프를 도는 함수, 예외를 던지는 함수.

```java
	fun fail(message: String): Nothing {
		throw IllegalStateException(message)
	}
	>> fail("Error occurred")
	java.lang.IllegalStateException: Error occurred
```
* Nothing 타입은 아무 값도 포함하지 않는다.
	* 함수의 반환 타입이나 반환 타입으로 쓰일 타입 파라미터로만 쓸 수 있다.
	* Nothing 타입의 변수를 선언해도 아무 값도 저장할 수 없다.
```java
	val address = company.address ?: fail("No address")
	// 전제 조건을 검사할 떄 유용하다.
```

## 6.3 컬렉션과 배열

### 6.3.1 널 가능성과 컬렉션

* List<Int?> 
	* List 전체는 null이 될 수 없으나 각 원소의 값은 null이 될 수 있다.
* List<Int>?
	* List 전체는 null이 될 수 있으나 각 원소의 값은 null이 될 수 없다.
* List<Int?>?
	* List 전체도 null이 될 수 있고, 각 원소의 값도 null이 될 수 있다.

### 6.3.2 읽기 전용과 변경 가능한 컬렉션
> 코틀린은 컬렉션 안의 데이터에 접근하는 인터페이스와 컬렉션 안의 데이터를 변경하는 인터페이스를 분리했다.

* kotliln.collections.Collection 인터페이스를 사용하면 데이터를 읽는 연산을 수행할 수 있다.  (수정 불가능)
* kotlin.collections.MutableCollection 인터페이스를 사용하면 컬렉션의 데이터를 수정할 수 있다.

읽기 전용 컬렉션이어도 그에 대한 참조는 mutable할 수 있기 때문에 항상 스레드 안전하지 않다.

### 6.3.3 코틀린 컬렉션과 자바
> 코틀린에서 읽기전용 컬렉션이라도 자바 코드에서는 컬렉션 객체의 내용을 변경할 수 있다.

### 6.3.4 컬렉션을 플랫폼 타입으로 다루기
> 자바에서 선언한 컬렉션 타입의 변수를 코틀린에서는 플랫폼 타입으로 본다.

컬렉션 타입이 시그니처에 들어간 자바 메소드 구현을 오버라이드할 경우 널 가능성에 대해서 고려해야 한다.

### 6.3.5 객체의 배여로가 원시 타입의 배열

```java
	fun main(args: Array<String>) {
		for (i in args.indices) {
			println(args[i])
		}
	}
```

**배열 만드는 법**
1. arrayOf 함수에 원소를 넘긴다.
2. arrayOfNulls 함수에 정수 값을 인자로 넘기면 모든 원소가 null이고, 인자로 넘긴 값과 크기가 같은 배열을 생성한다. 
(원소 타입이 nuallable 해야 한다.)
3. Array 생성자는 배열 크기과 람다를 인자로 받아서 각 배열 원소를 초기화 할 수 있다.
```java
	val letters = Array<String>(26) { i -> ('a' + i).toString }
```
4. 컬렉션을 toTypedArray 메소드를 사용하여 배열로 바꿀 수 있다.


* 배열 타입의 타입 인자도 항상 객체 타입이 된다. 
Array<Int>는 java.lang.Integer[]이 된다.

* 박싱하지 않은 원시 타입의 배열이 필요하다면, IntArray(int[]), ByteArray (byte[]) 등을 사용하면 된다.

* 박싱된 값이 들어있는 컬렉션이나 배열이 있다면 toIntArray등의 함수를 이용하자.

* 컬렉션에 사용할 수 있는 모든 확장 함수를 배열에도 제공한다.



