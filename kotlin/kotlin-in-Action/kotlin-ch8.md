# 8. 고차함수

## 8.1 고차 함수 정의
> 다른 함수를 인자로 받거나 함수를 반환하는 함수.

### 8.1.1 함수 타입
```java
	val sum = { x: Int, y: Int -> x + y }
	val action = { println(42) }
```
코틀린 컴파일러는 타입 추론을 하므로 타입을 명시하지 않아도 된다.

위의 코드의 함수 타입을 명시적으로 표기하면 아래와 같다.

```java
	val sum: (Int, Int) -> Int = { x, y -> x+ y }
	val action: () -> Unit = { println(42) }
```
* 함수 타입을 선언할 때는 반환 타입 Unit을 반드시 명시해야 한다.

```java
	val canReturnNull: (Int, Int) -> Int? = { x, y => null }
```
* 반환 타입에 nullable이 들어갈 수 있다.

```java
	val funOrNull: ((Int, Int) -> Int)? = null
```
* 함수 전체가 null이 될 수 있다.

```java
	fun performRequest (
		url: String
		callback: (code: Int, content: String) -> Unit)
	) { ... }
	// 함수의 타입의 각 파라미터에 이름을 붙인다.

	>>> val url = "hp://kotlin.com"
	>>> performRequest(url) { code, content -> ... }
	>>> performRequest(url) { code, page -> ... }
```
* 함수 타입에서 파라미터 이름을 지정할 수 있다.
* 함수 호출시 함수 타입 선언의 파라미터 이름과 동일하지 않아도 됨.
* 함수 타입 검사 시 파라미터 이름은 컴파일러가 확인하지 않는다.

### 8.1.2 인자로 받은 함수 호출
> 고차 함수 구현하기.

```java
	fun twoAndThree(operatrion: (Int, Int) -> Int) {
		val result = operation(2, 3)
		println("The result is $result")
	}
	>>> towAndThree { a, b -> a + b }
	The result is 5
```
* 인자로 받은 함수 호출문은 일반 함수 호출문과 같다.

### 8.1.3 자바에서 코틀린 함수 타입 사용
> 컴파일된 코드 안에서 함수 타입은 일반 인터페이스로 바뀐다.

인자가 없는 함수 : Fuction0<R>
인자가 하나인 함수 : Fuction1<P1, R>.. 등
인자의 갯수에 따라서 : FuctionN

각 인터페이스에는 invoke 메소드가 정의되어 있다.

```java
	/* 코틀린에서 선언 */
	fun processTheAnswer(f: (Int) -> Int) {
		println(f(42))
	}

	/* 자바8 이상에서 사용 */
	processTheAnswer(number -> number + 1)
	>> 43

	/* 자바7 이하에서 사용 */
	processTheAnser(
		new Function1<Integer, Integer>() {
			@Override
			public Integer invoke(Integer number) {
				return number + 1
			}
		});
	>> 43
```
자바에서 코틀린 확장 함수를 쉽게 호출할 수 있다.
* 수신 객체를 확장 함수의 첫 번째 인자로 명시적으로 넘겨야 한다.

```java
	List<String> strings = new ArrayList<>();
	>> strings.add("42")
	>> CollectionsKt.forEach(strings, s -> {
		System.out.pirntln(s);
		return Unit.INSTANCE; // Unit 타입의 값을 명시적으로 반환해야 한다.
	})
```

* 코틀린 Unit 타입은 값이므로 자바에서 그 값을 명시적으로 반환해줘야 한다.
* 반환 타입이 Unit인 함수 타입의 파라미터 위치에 void를 반환하는 자바 람다를 넘길 수 없다.

### 8.1.4 디폴트 값을 지정한 함수 타입 파라미터나 널이 될 수 있는 함수 타입 파라미터

1. 파라미터를 함수 타입으로 선언할 때도 디폴트 값을 정할 수 있다.
```java
	fun <T> Collection<T>.joinToString(
		transFomr: (T) -> String = { it.toString() })

	val letters = listOf("Alpha", "Beta")
	println(letters.joinToString())
	>> Alpha, Beta
```
파라미터 람다를 지정해주지 않으면 함수에 정의되어 있는 기본 값(toString)으로 동작.

```java
	println(letters.joinToString { it.toLowerCase()} )
	>> alpha, beta
```
전달된 람다 파라미터의 동작을 수행한다.

2. null이 될 수 있는 함수 타입을 사용할 수 있다.

_null이 될 수 있는 함수 타입으로 인자를 받으면 그 함수를 직접 호출할 수 없다._

1) 명시적인 null 체크하기.
```java
	fun foo(callback: (() -> Unit)?) {
		if (callback != null) {
			callback()
		}
	}
```

2) invoke함수 사용하기.
```java
	fun foo(callback: (() -> Unit)?) {
		callback?.invoke()
	}
```
### 8.1.5 함수를 함수에서 반환
> 프로그램 상태나 다른 조건에 따라 달라질 수 있는 로직.

```java
	/* 배송수단에 따라 배송비 계산하는 방법 */

	enum class Delivery { STANDARD, EXPEDITED }

	class Order(val itemCount: Int) 

	fun getShippingCostCalculator(
		delivery: Delivery): (Order) -> Double { // 반환 타입 = 함수 타입
		if (delivery == Delivery.EXPEDITED) {
			return { order -> 6 + 2.1 * order.itemCount }
			// 함수 타입 반환.
		}
		return { order -> 1.2 * order.itemCount }
	}
```
반환 타입으로 함수 타입을 지정해야 한다.
반환 : 람다 식, 함수식을 담은 변수.

_람다 식과 함수 타입은 보일러 플레이트를 제거하기에 좋은 방법이다._

## 8.2 인라인 함수 : 람다의 부가 비용 없애기.
> 람다를 익명 클래스로 컴파일해서 재사용한다.
> 람다가 변수를 포획한 경우 호출시마다 익명클래스를 생성한다.

### 8.2.1 인라이닝이 작동하는 방식
```java
	inline fun...
```
(
함수를 inline으로 선언하면 그 함수의 본문이 inline됩니다.
즉, 함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트코드로  컴파일한다.

```java
	inline fun <T> synchronized(lock: Lock, action: () -> T): T {
		lock.lock()
		try {
			return action()
		}
		finally {
			lock.unlock())
		}
	}

	val l = Lock()
	synchronized(l) {
		//..action
	}

	fun foo(l: Lock) {
		println("Before sync")

		synchronized(l) {
			println("Action")
		}
		println("After sync")
	}

	//foo가 컴파일 되면

	fun _foo_(l: Lock) {
		pirntln("Before sync")
		l.lock() // synchronized가 인라이닝 됨.
		try {
			println("Action") 
			// 람다 본문도 인라이닝 된다.
		}
		finally {
			l.unlock()
		}
		println("After sync")
	}
```
람다가 호출하는 코드 정의의 일부분으로 간주되어 익명 클래스를 만들지 않는다.

### 8.2.2 인라인 함수의 한계
> 파라미터로 받은 람다를 다른 변수에 저장하고 그 변수를 사용할 경우 람다를 표현하는 객체가 존재해야하기 때문에 람다를 인라이닝할 수 없다.

'noinline' 키워드를 파라미터 이름 앞에 붙여 인라이닝을 금지할 수 있다.

### 8.2.3 컬렉션 연산 인라이닝
asSequence를 사용하여 중간 리스트로 인한 부가 비용은 줄어들게 할 수 있다.
그러나, 중간 시퀀스는 람다를 필드에 저장하는 객체로 표현되며, 최종 연산은 중간 시퀀스에 있는 여러 람다를 연쇄 호출한다. 
따라서 시퀀스는 람다를 인라인하지 않는다.

크기가 작은 컬렉션은 일반 컬렉션 연산이 더 성능이 나을 수 있다.

시퀀스는 크기가 큰 컬렉션 연산을 수행할 때만 쓰도록 하자.

### 8.2.4 함수를 인라인으로 선언해야 하는 경우

1. 일반함수 호출의 경우 JVM이 강력한 인라이닝을 지원한다.
JVM은 코드 실행을 부넉해 가장 이익이 되는 방향으로 호출을 인라이닝한다.
바이트코드를 실제 기계어 코드로 번역하는 과정에서 동작한다.
그렇다면 바이트코드에서 각 함수 구현이 한번만 있으면 되고, 그 함수를 호출하는 부분에서 함수 코드를 중복할 필요가 없다.
반면, 코틀린 이라인 함수는 바이트콛에서 각 함수 호출 지점을 함수 본문으로 대치하기 때문에 코드 중복이 생긴다.

2. 람다를 인자로 받는 함수를 인라이닝하면 이익이 많다.
함수 호출 비용을 줄일 수 있다.
람다를 표현하는 클래스와 람다 인스턴스에 해당하는 객체 생성의 부담이 없다.
인라이닝을 사용하면 non-local 반환 기능을 제공한다.

_주의할 점_
인라이닝하는 함수가 큰 경우 바이트코드가 커질 수 있다.

### 8.2.5 자원 관리를 위해 인라인된 람다 사용
> 람다로 중복을 없앨 수 있는 일반적인 패턴 : 자원 획득, 자원 해제 => 자원 관리

코틀린에서는 함수 타입의 값을 파라미터로 받는 함수를 제공하므로 try-with-resoure을 언어 구성 요소로 제공하지 않는다.
대신 같은 기능을 하는 use를 라이브러리를 통해 제공한다.
-> use함수도 인라인 함수이다.

## 8.3 고차 함수 안에서 흐름 제어

### 8.3.1 람다 안의 return문
```java
	data class Person(val name: String val age: Int) 

	val people = listOf(Person("Alice", 29), Person("Bob", 31))

	fun lookForAlice(people: List<Person>) {
		for (person in people) {
			if (person.name == "Alice") {
				println("Found!")
				return
			}
		}
	}// lookForAlice가 반환된다.

	/* forEach 문으로 바꿔쓰기 */
	fun lookForAlice(people: List<Person>) {
		people.forEach {
			if (it.name == "Alice") {
				println("Found!")
				return
			}
		}
	}// lookForAlice가 반환된다.
```
람다 안에서 return을 사용하면 람다를 호출하는 함수가 실행을 끝내고 반환된다.

_자신을 둘러싸고 있는 블록보다 바깥에 있는 다른 블록을 반환하게 만든느 return문을 non-local return이라 부른다._

* non-local return을 쓸수 있는 경우는 람다를 인자로 받는 함수가 인라인 함수인 경우 뿐이다. (forEach는 인라인 함수)

인라이닝되지 않는 함수는 람다를 변수에 저장할 수 있고, 바깥쪽 함수로부터 반환된 뒤에 저장해 둔 람다가 호출될 수도 있다.

### 8.3.2 람다로부터 반환 : 레이블을 사용한 return
> 람다 식에서도 local return을 사용할 수 있다.
> 자바의 break와 같은 역할

* local return은 람다의 실행을 끝내고 람다를 호출했던 코드의 실행을 이어간다.

* local return 과 non-local return을 구분하기 위해서는 레이블을 사용해야 한다.

1. 람다 식앞에 label을 붙인다.
2. return뒤에 label을 붙인다.

```java
	fun lookForAlice(people: List<Person>) {
		people.forEach label@{
			if (it.name == "Alice") return@label
				// 앞에서 정의한 레이블을 참조한다.
		}
		println("Alice might be somewhere")
		// 항상 이 구문이 출력된다.
	}
```

label을 붙이는 대신 함수의 이름을 return뒤에 레이블로 사용할 수 있다.
```java
	fun lookForAlice(people: List<Person>) {
		people.forEach {
			if (it.name == "Alice") return@forEach
		}
		println("Alice might be somewhere")

	}
```

_레이블이 붙은 this식_
```java
	println(StringBuilder().apply sb@ {
		// 이 람다의 묵시적 수신 객체에 접근할 수 있다.
		listOf(1, 2, 3).apply {
			this@sb.apppend(this.toString())
			//그냥 this는 가장 안쪽 영역의 묵시적 수신 객체를 가리킨다.
			// 바깥쪽 수신 객체에 접근할 때는 레이블을 명시해야 한다.
		}
	})
```
_단점_
넌로컬 반환문은 가독성이 떨어지고, 람다 안의 여러 위치에 return식이 들어갈 경우 불편하다.

해결 방안 : 익명 함수

### 8.3.3 익명 함수 : 기본적으로 local return
> 코드 블록을 함수에 넘길때 사용하는 방법

```java
	fun lookForAlice(people: List<Perosn>) {
		people.forEach(fun (person) {
			if (person.name == "Alice") return
			println("${person.name} is not Alice")
		})
	}
```
익명 함수는 함수 이름과 파라미터 타입을 생략한다.
```java
	people.filter(fun (person): Boolean {
		return person.age < 30
	})

	people.filter(fun (person) = person.age < 30)
```
블록이 본문인 함수는 반환 타입을 명시해야 하고, 식이 본문인 함수는 반환 타입을 생략할 수 있다.

_특징_
익명 함수 안에서 label이 붙지 않은 return식은 익명 함수만 반환한다.
```java
fun look(people: List<Person>) {
    people.filter(fun(person): Boolean {
        return@filter person.age < 30
        }) 

    println("return")
    // 항상 출력됨.
}
```
