# lambda
> Function Literal, 함수를 선언하지 않고 곧바로 식으로 전달해서 표현한다.

_사용 이유_
> 동작을 변수에 저장하거나 함수의 인자로 넘겨야 할 경우 익명 클래스 또는 람다를 씁니다.
ex) 버튼을 누르면 콘솔에 text가 출력됐으면 좋겠다.

```java
	/* 자바 - 익명 클래스 */
	button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View view) {
			// 동작 
		}
	});
	/* 자바 - 람다 식 */
	button.setOnclickListener{ /* 동작 */ }
```
익명클래스는 해당 메소드를 호출할 때마다 매번 새로운 객체를 생성한다는 부담이 있습니다. 또한, 준비코드가 길어져서 익명 클래스를 쓰는 메소드가 많아지면 질수록 보일러플레이트가 증가합니다. 

람다 식은 메소드의 첫 호출시 익명 클래스로 컴파일 되고 이때 생성된 객체를 이후 호출에도 재사용합니다. 별도의 준비 코드가 없어 코드가 간결해집니다.

## inline 함수
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

_규칙_
1. 파라미터는 (->) 왼쪽에 선언됨. 
2. 람다 본문은 (->) 오른쪽에 정의됨.
3. 람다 식은 중괄호{}로 시작하고 끝난다.
4. 람다의 반환값은 람다 본문의 마지막 식이다.

```java
	fun sayHello(name: String) {
		println("Hello, $name")
	}

	sayHello("Heidi")
	/*람다로 표현*/
	val sayHello = {name: String -> println("Hello, $name")}
	sayHello("Heidi")

```
_특성_
1. 람다 식을 변수에 저장할 수 있다.
```java
	val sum = { x: Int, y: Int -> x + y}
	println(sum(1, 2))
```
2. 람다 식을 직접 호출할 수 있다.
```java
	{ println(42) }()
	>> 42
```
위와 같은 구문은 가독성이 떨어지고 굳이 쓸 이유도 없다.
그러나, 코드의 일부분을 블록으로 둘러싸 실행할 필요가 있다면 run함수를 쓰자.
```java
	run { println(42) }
	>> 42
```
run함수는 인자로 받은 람다 식을 실행해 주는 라이브러리 함수입니다.

_제공해주는 기능_

```java
	data class Person(val name: Strng, val age: Int) 
	val people = listOf(Person("Eric", 24), Person("Kelly", 40))
	println(people.maxBy({ p: Person -> p.age }))
```

1. 맨 뒤의 인자가 람다 식이라면 그 람다를 괄호 밖으로 빼낼 수 있다.
```java
	println(people.maxBy() { p: Person -> p.age })
```
2. 람다 식이 유일한 인자이고 괄호 뒤에 람다를 썼다면 괄호를 생략할 수 있다.
```java
	println(people.maxBy { p: Person -> p.age })
```
3. 컴파일러는 람다 파라미터의 타입도 추론할 수 있다. (인자 타입 생략 가능)
	**람다를 변수에 저장할 때는 인자의 타입을 꼭 명시해야 한다.**
```java
	println(people.maxBy{ p -> p.age })

	val maxAge = people.maxBy{ p: Person -> p.age }
```

4. 람다 식의 파라미터가 하나뿐이고, 그 타입을 컴파일러가 추론할 수 있다면 it을 바로 쓸 수 있다. (it은 자바에서의 this와 같은 역할)
```java
	println(people.maxBy { it.age })
```

## 람다식을 파라미터로 사용하기

1. 람다식 자체를 메소드의 파라미터로 사용할 수 있다.
```java
	fun downloadData(url: String, completion: () -> Unit) {
		completion()
	}

	>> downloadData("myUrl.com", {
		println("completion이 완료돼야 이 글이 출력됩니다.")
	})
```

2. 람다식이 하나 이상의 파라미터를 가질 수 있다.
```java
fun downloadWeather (url: String, completion: (String) -> Unit) {
	val weatherData = "Cloudy, -3"
	completion(weatherData)
}
```

## 람다 식에서 로컬 변수에 접근

자바의 람다 식
* 람다 식안에서는 final 변수에만 접근할 수 있다.
  즉, 외부 변수의 값을 변경할 수 없다.

코틀린의 람다 식
* 람다 식 안에서 일반 변수에도 접근할 수 있다.
* 람다 식 안에서 외부 변수의 값을 변경할 수 있다.
```java
	fun printProblemCounts(responses: Collection<String>) {
		var clientErrors = 0
		var serverErrors = 0
		responses.forEach {
			if (it.startsWith("4") {
				clientErrors++ // 외부 변수 변경 (람다가 포획한 변수)
			} else if (it.startsWith("5")) {
				serverErrors++ // 외부 변수 변경 (람다가 포획한 변수)
			}
		}
	}
```
람다가 사용하는 외부 변수를 **람다가 포획한 변수**라고 합니다.

일반적으로 로컬 변수의 라이프는 함수가 반환되기 까지 입니다.

_로컬 변수의 생명주기가 함수의 생명주기와 다를 경우_

1. 함수가 자신의 로컬 변수를 포획한 람다를 반환할 떄
2. 자신의 로컬 변수를 포획한 람다를 다른 변수할 때
* 파이널 변수를 포획한 경우, 람다코드를 변수 값과 함께 저장한다.
* 파이널이 아닌 변수를 포획한 경우, 변수를 특별한 래퍼로 감싸서 나중에 변경하거나 읽을 수 있게 한 다음, 래퍼에 대한 참조를 저장한다.

단, 람다가 변수를 포획한 경우에는 매번 새로운 익명 클래스 객체를 생성합니다.

```java
fun tryToCountButtonClicks(button: Button): Int {
		var clicks = 0
		button.onClick { clicks++ }
		return clicks
	}
	// 항상 0을 반환한다.
	// 핸들러는 tryToCountButtonClicks가 clicks를 반환한 다음 호출된다.
	// 제대로 구현하려면 clikcs변수를 클래스의 프로퍼티나 전역 프로퍼티 등의 위치로 빼내야 한다.
```
위와 같이 람다를 이벤트 핸들러나 다른 비동기적으로 실행되는 코드로 활용할 경우 제대로 작동하지 않을 수 있다.

## 멤버 참조
> 람다를 사용해 넘기려는 코드가 이미 함수로 선언된 경우에는 멤버 참조를 이용하자.
멤버 참조는 프로퍼티나 메소드를 단 하나만 호출하는 함수 값을 만들어준다.

참조 대상이 함수인지 프로퍼티인지와는 관계없이 멤버 참조 뒤에는 괄호를 사용할 수 없다.

```java
	fun salute() = println("Salute")
	>> run(::salute)
``` 
최상위에 선언된 함수(다른 클래스의 멤버가 아닌)나 프로퍼티를 참조할 수 있다.

```java
	val action = { person: person, message: String -> sendEmail(person, message)}
	val nextAction = ::sendEmail
	val action2 = {person: Person, message: String -> nextAction }
```
람다가 인자가 여럿인 다른 함수한테 작업을 위임하는 경우 람다를 정의하지 않고 직접 위임 함수에 대한 참조를 제공하면 편리하다.


# Sequence - lazy 연산
> 컬렉션 함수를 사용하는 경우, 결과 컬렉션을 즉시 생성한다.

## 사용 이유
```java
	people.filter( {it.age > 24 }).map({ it.map })
```
위와 같이 컬렉션 연산을 연속해서 사용하는 경우, 중간 연산에 대한 컬렉션이 만들어진다.
즉, filter의 결과를 담는 컬렉션 1개 map의 결과를 담는 컬렉션 1개 총 2개가 만들어 진다.

**컬렉션의 크기가 클수록 중간 연산의 결과를 담은 새로운 컬렉션의 생성은 매우 비효율적이다.**

이 경우 Sequence를 쓰는 것이 좋습니다.

## Sequnce 연산 순서
### 중간 연산
> 다른 시퀀스를 반환한다. 
항상 지연계산한다.

### 최종 연산
> 결과를 반환한다.
결과 : 최초의 컬렉션에 대해 변환을 적용한 시퀀스로부터 일련의 계산을 수행한 객체또는 숫자 등입니다.

최종 연산이 호출되면 지연되어있던 중간 연산이 수행됩니다.


```java
people.map(Person::name).filter { it.startsWith("A") }

	people.asSequence() // 원본 컬렉션을 시퀀스로 변환한다.
		.map(Person::name)				// -> 시퀀스도 컬렉션과 같은 API를 제공한다. 
		.filter { it.startsWith("A") }
		.toList() // 결과 시퀀스를 다시 리스트로 변환한다.(최종 연산)
```

Sequence는 컬렉션의 원소를 이터레이션 하면서 하나씩 가져와서 원소 순서대로 연산을 수행합니다.
즉, 첫번째 원소가 filter와 map의 연산을 거쳐서 결과 컬렉션에 담겨지고
그 다음으로 두번째 원소가 filter와 map의 연산을 거쳐서 결과 컬렉션에 담겨지는 순서를 따릅니다.
결과적으로 컬렉션은 최종적으로 1개만 만들어지게 됩니다.

_자바의 스트림_
* 자바 8부터 시퀀스와 대응하는 스트림 기능을 제공합니다.
	* 코틀린은 자바6와 호환되기 때문에 따로 구현을 제공하는 것.
* 코틀린 시퀀스는 안드로이드 등 예전 자바 버전과 호환된다는 장점이 있다.
* 자바의 스트림은 filter/map등의 연산을 cpu에서 병렬적으로 실행한다는 장점이 있습니다.

_주의할점_
시퀀스는 크기가 큰 컬렉션에 대해서만 사용하도록 하자.
filter()/map()은 inline으로 선언된 함수이기 때문에 사용시 함수 본문이 인라이닝되어 추가 객체나 클래스를 생성하지 않습니다.
시퀀스를 사용하게 되면, 중간 시퀀스가 람다를 필드에 저장하는 객체로 표현되며 최종 연산은 중간 시퀀스에 있는 여러 람다를 연쇄 호출하는 형태로 이루어지기 때문에 람다를 저장해야 해서 인라이닝 하지 않습니다.


## sequence만들기
```java
/* 0~100까지의 합 구하기 */
	val naturalNumbers = generateSequence (0) { it + 1 }
	// 0부터 시작해서 1을 더하는 시퀀스
	val numbersTo100 = naturalNumbers.takeWhile { it <= 100}
	// naturalNumbers가 100까지만 루프를 돌아라.
	println(numbersTo100.sum()) // 시퀀스의 지연계산은 sum이 호출될 때 수행된다.
	5050
```

#자바의 스트림
> 컬렉션의 저장 요소를 하나씩 참조해서 람다식으로 처리할 수 있도록 해주는 반복자 입니다.

## 반복자 스트림
자바 7 이전까지는 List<String> 컬렉션에서 요소를 순차적으로 처리하기 위해 Iterator 반복자를 아래와 같이 사용해왔습니다.
```java
	List<String list = Arrays.asList("Kelly", "Josh", "Bob");
	Iterator<String> Iterator = list.Iterator();

	while (iterator.hasNext()) {
		String name = Iterator.next();
		System.out.println(name);
	}
```

스트림을 사용한 코드는 아래와 같습니다.
```java
	List<String> list = Arrays.asList("Kelly", "Josh", "Bob");
	Stream<String> stream = list.stream(); //스트림 객체 얻기.

	stream.forEach( name -> System.out.println(name) );
```

```java
	void forEach(Consumer<T> action) 
	// 함수형 인터페이스를 파라미터로 갖는다.
```

## 스트림 특징
1. 람다식으로 요소 처리 코드를 제공한다.
2. 내부 반복자를 사용해 병렬 처리가 쉽다.
* 외부 반복자란 개발자가 코드로 직접 컬렉션의 요소를 가져오는 코드를 말한다.
	* index for문, iterator while문
* 반복은 컬렉션에게 맡겨두고 처리 문만 개발자가 전달해주면 된다.
* parallelStream을 사용하면 병렬처리 스트림으로 만들 수 있다.
3. 중간 연산과 최종 연산을 수행한다.
* 최종 연산이 호출되기 전까지 중간 연산을 수행되지 않는다.
4. 요소를 저장하지 않는다. 요소는 스트림을 지원하는 컬렉션에 저장된다.
5. 스트림 연산은 원본을 변경하지 않는다.

## 스트림 생성
```java
	String str = "i'm your father";

	// 배열을 이용한 스트림 생성
	Stream<String> stream = Stream.of(str.split(" "));
	Stream<String stream1 = Stream.of("a", "b", "c");

	//빈 stream
	Stream<String> stream3 = Stream.empty();

	//컬렉션에서 생성
	Stream<String> stream4 = Arrays.asList(str.split(" ").stream());
```
