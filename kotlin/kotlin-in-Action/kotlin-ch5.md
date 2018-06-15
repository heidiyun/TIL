
# 5. 람다로 프로그래밍
> 람다 식(람다) : 함수에 넘길 수 있는 작은 코드 조각
람다를 사용하면 공통 코드 구조를 라이브러리 함수로 뽑아낼 수 있다.

## 5.1 람다 식과 멤버 참조
### 5.1.1 람다 소개 : 코드 블록을 함수 인자로 넘기기

동작을 변수에 저장하거나 함수의 인자로 넘겨야 할 경우, 익명 클래스 또는 람다 식을 사용한다.
익명 클래스의 경우 코드의 반복이 잦아 보일러 플레이트를 야기한다.
람다 식을 사용하면 함수를 선언할 필요없이 코드 블록을 직접 함수의 인자로 넘길 수 있다.

```java
	버튼 클릭에 따른 동작을 처리하는 리스너.
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

### 5.1.2 람다와 컬렉션
```java
	// 사람의 이름과 나이를 저장하는 Person class
	data class Person(val name: String, val age: Int)
	
	// 연장자를 찾아보자
	fun findTheOldest(people: List<Person>) {
		var maxAge = 0
		var theOldest: Person? = null
		for (person in people) {
			if (person.age > maxAge) {
				maxAge = person.age
				theOldest - person
			}
		}
		println(theOldest)
	}

	>> val people = listOf(Person("Eric", 24), Person("Brown", 27))
	>> findTheOldest(people)

	/* 람다 식을 활용해보자 */
	>> val people = listOf(Person("Eric", 24), Person("Brown", 27))
	>> println(people.maxBy { it.age })
	// maxBy는 컬렉션에서 가장 큰 원소를 찾기 위해 비교에 사용할 값을 돌려주는 함수를 인자로 받는다.
	>> println(people.maxBy(Person::age)
	// 함수를 호출하는 컬렉션의 class와 함수 인자의 class가 같고, 
	// 인자의 개수가 하나라면 멤버 참조를 사용할 수 있다.
```
### 5.1.3 람다 식의 문법
코틀린의 람다 식은 중괄호로 둘러싸여 있다.
```java
	{x: Int, y: Int -> x + y }
```
* 화살표(->)로 인자 목록과 람다 식의 본문을 구분한다.

람다 식을 변수에 저장할 수 있다.
```java
	val sum = { x: Int, y: Int -> x + y }
	println(sum(1,2))
	>> 3
```
람다 식을 직접 호출할 수 있다.
```java
	{ println(42) }()
	>> 42
	// 이런 구문은 쓰기를 권장하지 않는다.
```
위와 같이 코드의 일부분을 블록으로 둘러싸 실행할 필요가 있다면 run을 사용한다.

```java
	run { println(42) }
	>> 42
```
가장 기본적인 람다 식 표현방법
```java
	val people = listOf(Person("Eric", 24), Person("Brown", 27))
	>> println(people.maxBy({ p: Person -> p.age })
```
위의 코드의 단점
1. 구분자가 많아 가독성이 떨어진다.
2. 컴파일러가 추론할 수 있는 인자 타입을 적을 필요가 없다.
3. 인자가 하나뿐인 경우 인자에 이름을 붙이지 않아도 된다.

```java
	people.maxBy() { p: Person -> p.age }
```
맨 뒤의 인자가 람다 식이라면 그 람다를 괄호 밖으로 빼낼 수 있다.

```java
	people.maxBy {p: Person -> p.age }
```
람다 식이 유일한 인자이고 괄호 뒤에 람다를 썼다면 괄호를 생략할 수 있다.

```java
	people.maxBy { p -> p.age }
```
컴파일러는 람다 파라미터의 타입도 추론할 수 있다. (인자 타입 생략 가능)

```java
	people.maxBy { it.age }
```
람다 식의 파라미터가 하나뿐이고, 그 타입을 컴파일러가 추론할 수 있다면 it을 바로 쓸 수 있다.
람다 안에 람다가 중첩되는 경우 it을 쓰지 않는 것을 권장한다.

```java
	val getAge = { p: Person -> p.age }
```
람다를 변수에 저장할 때는 꼭 인자의 타입을 명시하도록 하자.

### 5.1.4 로컬 영역에 있는 변수에 접근
람다를 함수 안에서 정의하면 함수의 파라미터와 로컬 변수를 람다 안에서 사용할 수 있다.

```java
	fun printMessageWithPrefix(messages: Collection<String>, prefix: String) {
		messages.forEach {
			println("$prefix $it")
			//it은 messages를 가르킴.
		}
	}
```
자바와의 차이점
* 코틀린 람다 안에서는 final 변수가 아닌 일반 변수에도 접근할 수 있다.
* 람다 안에서 바깥의 변수를 변경해도 된다.

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
람다가 사용하는 외부 변수를  **'람다가 포획(capture)한 변수'** 라고 부른다.
일반적으로 로컬 변수의 생명주기는 함수가 반환되면 끝난다.

_로컬 변수의 생명주기가 함수의 생명주기와 다를 경우_
* 함수가 자신의 로컬 변수를 포획한 람다를 반환할 떄
* 자신의 로컬 변수를 포획한 람다를 다른 변수할 때
	* 파이널 변수를 포획한 경우, 람다코드를 변수 값과 함께 저장한다.
	* 파이널이 아닌 변수를 포획한 경우, 
	변수를 특별한 래퍼로 감싸서 나중에 변경하거나 읽을 수 있게 한 다음, 래퍼에 대한 참조를 저장한다.

```java
	fun tryToCountButtonClicks(button: Button): Int {
		var clicks = 0
		button.onClick { clicks++ }
		return clicks
	}
	// 항상 0을 반환한다.
	// 핸들러는 tryToCountButtonClicks가 clicks를 반환한 다음 호출된다.ㅈㅈ
	// 제대로 구현하려면 clikcs변수를 클래스의 프로퍼티나 전역 프로퍼티 등의 위치로 빼내야 한다.
```
위와 같이 람다를 이벤트 핸들러나 다른 비동기적으로 실행되는 코드로 활용할 경우 제대로 작동하지 않을 수 있다.

### 5.1.5 멤버 참조
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
```
람다가 인자가 여럿인 다른 함수한테 작업을 위임하는 경우 람다를 정의하지 않고 직접 위임 함수에 대한 참조를 제공하면 편리하다.

```java
	data class person(val name: String, val age: Int)
	>> val createPerson = ::Person // Person의 인스턴스를 만드는 동작을 값으로 저장한다.
	>> val p = createPerson("Alice", 30)
	>> println(p)
	Person(name=Alice, age=29)
```
**생성자 참조**를 사용하면 클래스 생성 작업을 연기하거나 저장해둘 수 있다. 
::뒤에 클래스 이름을 넣으면 된다.

```java
	fun Person.isAdult() = age >= 21
	val predicate = Person::isAdult
```
확장 함수도 똑같은 방법으로 멤버 참조를 사용할 수 있다.

## 5.2 컬렉션 함수형 API
### 5.2.1 필수적인 함수 : filter, map
 > filter와 map은 컬렉션을 활용할 때 기반이 되는 함수다.

1. filter
> 컬렉션을 이터레이션하면서 주어진 람다에 각 원소를 넘겨서 람다가 true를 반환하는 원소만 모은다.
```java
	val list = listOf(1, 2, 3, 4)
	>> println(list.filter { it % 2 == 0 })
	[2, 4] 
	// 해당 컬렉션으로 반환한다.
```

2. map
> 주어진 람다를 컬렉션의 각원소에 적용한 겨로가를 모아서 새 컬렉션을 만든다. 
```java
	>> val list = listOf(1, 2, 3, 4)
	>> println(list.map { it * it }
	[1, 4, 9, 16]

	/* 사람의 이름 리스트만 출력하고 싶다면 */
	data class Person(val name: String, val age: Int)
	>> val people = listOf(Person("kelly", 28), Person("Ally", 40))
	>> println(list.map { it.name })
	[Kelly, Ally]
	>> println(list.map(Person::name)
```
3. filter & map
```java
	peopler.filter { it.age > 30 }.map(Person::name)
	>> [Ally]
```
4. 자료구조 map에 적용하기
key : filterKeys, mapKeys
value : filterValues, mapValues

### 5.2.2 all, any, count, find : 컬렉션에 술어 적용 
> 술어 : 참/ 거짓을 반환하는 함수

1. all / any
> 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단
2. count
> 조건을 만족하는 원소의 개수를 반환
3. find
> 조건을 만족하는 첫번째 원소를 반환

```java
	val canBeInClub27 = { p: Person -> p.age <= 27 } // 27세 이하인지 판단
	val people = listOf(Person("kelly", 26), Person("Ally", 40))
	println(people.all(canBeInClub27)) // 모든 원소가 27세이하인지 판단
	>> false
	println(people.any(canBeInClub27)) // 27세 이하 원소가 하나라도 있는지 판단
	>> true
	println(people.count(canBeInClub27)) // 27세 이하 원소가 몇개 있는지
	>> 1
	println(people.find(canBeInClub27)) // 27세 이하 원소를 반환
	>> Person(name=kelly, age = 26)
	// 조건에 만족하는 원소가 없으면 null을 반환한다.
	
```

### 5.2.3 groupBy : 리스트를 여러 그룹으로 이뤄진 맵으로 변경
> 컬렉션의 원소를 기준에 따라 여러 그룹으로 나누고 싶을 때 사용한다.

```java
	val people = listOf(Person("Alice", 31), ... Person("Bob", 29), Person("Carol"), 31)
	println(people.groupBy{ it.age })
	>> {29=[Person(name=Bob, age=29)], 31=[Person(name=Alice,age=31), Person(name=Carol, age=31)]}
	//Map<Int, List<Person>>형태로 반환.
```

### 5.2.4 flatMap과 flatten : 중첩된 컬렉션 안의 원소 처리
1. flatMap
주어진 람다를 컬렉션의 모든 객체에 적용하고(또는 매핑) 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스트로 한데 모은다.
```java
	val strings = listOf("abc", "def")
	println(strings.flatMap { it.toList() })
	>> [a, b, c, d, e, f]
	// abc를 리스트로 변경 def를 리스트로 변경
	// 두 리스트를 합친다.
```
중첩된 리스트의 원소를 한 리스트로 모아야 할 때 쓴다.
변환해야 할 내용이 없다면 -> flatten()함수를 사용할 수 있다.

## 5.3 lazy 컬렉션 연산
> 대부분의 함수는 결과 컬렉션을 즉시 생성한다.
  컬렉션 함수를 연속적으로 사용하면 매 단계마다 계산의 결과를 새로운 컬렉션에 담는다.

시퀀스(sequence)를 사용하면 중간 계산 결과를 새로운 컬렉션에 담지 않고 컬렉션 연산을 연속적으로 사용할 수 있다.
```java
	people.map(Person::name).filter { it.startsWith("A") }

	people.asSequence() // 원본 컬렉션을 시퀀스로 변환한다.
		.map(Person::name)				// -> 시퀀스도 컬렉션과 같은 API를 제공한다. 
		.filter { it.startsWith("A") }
		.toList() // 결과 시퀀스를 다시 리스트로 변환한다.
```
interface인 Sequence안에는 iterator라는 메소드만 존재한다. 
이 메소드를 통해 원소 값을 얻을 수 있다.

시퀀스에 대한 연산을 지연계산하기 때문에 정말 계산을 실행하게 만들려면 최종 시퀀스의 원소를 하나씩 이터레이션하거나 최종 시퀀스를 리스트로 변환해야 한다.

### 5.3.1 시퀀스 연산 실행 : 중간 연산과 최종 연산

1. 중간연산
> 다른 시퀀스를 반환한다. (최초 시퀀스의 원소를 변환하는 방법을 안다.)
2. 최종 연산
> 결과를 반환한다. (최초 컬렉션에 대해 변환을 적용한 시퀀스로부터 일련의 계산을 수행해 얻을 수 있는 컬렉션이나 원소, 숫자 또는 객체다.)

```java
	people.asSequence() 
		.map(Person::name)				// -> 중간 계산
		.filter { it.startsWith("A") }
		.toList() // 최종계산
```
* 중간 연산은 항상 지연 계산된다.
  즉, 최종 연산이 호출될 때 실행된다.

_연산 수행 순서_
직접 연산을 구현한다면 map연산이 끝난후, filter연산이 수행된다.
시퀀스 연산에서는 원소에 대해 순차적으로 수행된다. (첫번째 원소가 map, filter가 수행되고 두번째 원소가 map, filter가 수행되고..)

**자바 스트림과 코틀린 시퀀스는 같은 기능을 제공한다.**
**자바 스트림 연산을 여러 CPU에서 병렬적으로 실행가능하다.**

### 5.3.2 시퀀스 만들기
generateSequence 함수를 사용해 컬렉션을 시퀀스로 만들 수 있다.

```java
	/* 0~100까지의 합 구하기 */
	val naturalNumbers = generateSequence (0) { it + 1 }
	val numbersTo100 = naturalNumbers.takeWhile { it <= 100}
	println(numbersTo100.sum()) // 시퀀스의 지연계산은 sum이 호출될 때 수행된다.
	5050
```

어떤 객체의 상위가 자신과 같은 타입이고 모든 상위의 시퀀스에서 어떤 특성을 알고싶을 때 시퀀스를 주로 사용한다.

```java
	/* 상위 디렉토리의 시퀀스를 생성하고 사용하기 */
	fun File.isInsideHiddenDirectory() =
		generateSequence(this) { it.parentFile }.any { it.isHidden }
		//find를 사용하면 어떤 디렉토리인지 알 수 있다.

	val file = File("/Users/svtk/aaa.txt")
	println(file.isInsideHiddenDirectory)
	>> true
```

## 5.4 자바 함수형 인터페이스 활용
> 추상 메소드가 단 하나만 있는 이넡페이스를 functional interface/ SAM(single abstract method) interface라고 한다.

### 5.4.1 자바 메소드에 람다를 인자로 전달
함수형 인터페이스를 인자로 원하는 자바 메소드에 코틀린 람다를 전달할 숭 ㅣㅆ다.

```java
	void postponeComputation(int delay, Runnable computation);

	/* 코틀린 */
	postponeComputation(1000) { println(42) } // 프로그램 전체에서 Runnable 인스턴스는 단 하나만 만들어진다.

	postponeComputation(1000, object : Runnable {
		override fun run() {
			println(42)
		}
	}) // 객체 식을 함수형 인터페이스 구현으로 넘긴다. 익명 클래스 구현.
```
* 람다를 사용하는 경우 : 컴파일러가 자동으로 람다를 Runnable 인스턴스로 변환해준다. 
						익명 객체를 반복 사용한다.

* 객체 식을 사용하는 경우 : 메소드를 호출할 때마다 새로운 객체가 생성된다.

```java
	val runnable = Runnable { println(42) } // SAM 생성자
	fun handleComputation() {
		postponeComputation(1000, runnable)
	}
```
모든 handleComputation 호출에 같은 객체를 사용한다.

```java
	fun handleComputation(id: String) {
		postponeComputation(1000) { println(id) }
	}
```
람다가 주변 영역의 변수를 포획한다면 매 호출마다 같은 인스턴스를 사용할 수 없다.
-> 컴파일러는 매번 주변 영역의 변수를 ㅍ획한 새로운 인스턴스를 생성해준다.

컬렉션을 확장한 메소드에 람다를 넘기는 경우 코틀린은 inline을 사용해 아무런 익명 클래스도 만들지 않는다.

### 5.4.2 SAM 생성자 : 람다를 함수형 인터페이스로 명시적으로 변경
> SAM 생성자는 람다를 함수형 인터페이스의 인스턴스로 변환할 수 있게 컴파일러가 자동으로 생성한 함수다.
컴파일러가 자동으로 람다를 함수형 인터페이스 익명 클래스로 바꾸지 못하는 경우 SAM생성자를 사용할 수 있다.

ex) 함수형 인터페이스의 인스턴스를 반환하는 메소드가 있다면 람다를 직접 반환할 수 없고, 
	반환하고픈 람다를 SAM 생성자로 감싸야 한다.

```java
	fun createAllDoneRunnable(): Runnable {
		return Runnable { println("All done!") }
	}
	>> createAllDoneRunnable().run()
	All done!
```

SAM 생성자의 이름은 사용하려는 함수형 인터페이스의 이름과 같다.
함수형 인터페이스의 유일한 추상 메소드의 본문에 사용할 람다만을 인자로 받아서 함수형 인터페이스를 구현하는 클래스의 인스턴스를 반환한다.
==질문하기==

람다로 생성한 함수형 이넡페이스 인스턴스를 변수에 저장해야 하는 경우에도 SAM 생성자를 사용할 수 있다.

## 5.5 수신 객체 지정 람다 : with과 apply
> 수신 객체를 명시하지 않고 람다의 본문 안에서 다른 객체의 메소드를 호출할 수 있게 하는 것.

### 5.5.1 with 함수
```kotlin
	fun alphabet(): String {
		val result = StringBuilder()
		for (letter in 'A'..'Z') {
			result.append(letter)
		}
		result.append("\nNow I know the alphabet!")
		return result.toString()
	}
	>>println(alphabet())
```
위의 코드에서는 result에 대해 다른 여러 메소드를 호출하면서 매번 result를 반복 사용한다.

```java
	fun alphabet(): String {
		val stringBuilder = StringBuilder()
		return with(stringBuilder) { // 메소드를 호출하려는 수신 객체를 지정한다.
			for (letter in 'A'..'Z') {
			this.append(letter) // this를 명시해서 앞에서 지정한 수신 객체의 메소드를 호출한다.
			}
		append("\nNow I know the alphabet!") // this를 생략하고 메소드 호출
		this.toString() // 람다에서 값을 반환
		}
	}
```

with문은 파라미터가 2개인 함수이다. 
* 첫번째 파라미터 : 수신 객체(stringBuilder)
* 두번째 파라미터 : 람다

첫번째 파라미터에서 받은 객체를 두 번째 파라미터에서 받은 람다의 수신 객체로 지정한다.
* 람다 본문에서는 this를 사용해 수신 객체에 접근할 수 있다.
* this를 생략해도 메소드를 호출할 수 있다.

```java
	fun alphabet() = with(StringBuilder()) {
		for (letter in 'A'..'Z') {
			append(letter)
		}
		append("\nNow I know the alphabet!")
		toString()
	}
```
stringBuilder 변수를 없애고 alphabet 함수가 식의 결과를 바로 반환하게 한다.
-> 식을 본문으로 하는 함수로 표현

**람다 식의 본문에 있는 마지막 식이 반환 값이다.**

### 5.5.2 apply 함수
> apply는 항상 자신에게 전달된 객체를 반환한다.

```java
	fun alphabet() = StrigBuilder().apply {
		for (letter in 'A'..'Z') {
			append(letter)
		}
		append("\nNow I know the alphabet!")
	}.toString()
```
* apply는 확장 함수이다.
* apply의 수신 객체가 람다의 수신 객체가 된다.

_객체의 인스턴스를 만들면서 즉시 프로퍼티 중 일부를 초기화해야 하는 경우 유용하다._
* 자바에서의 Builder 객체와 유사한 역할 수행.

```java
	fun createViewWithCustomAttributes(context: Context) =
		TextView(context).apply { // 새로운 TextView 인스턴스를 만들고 즉시 인스턴스를 apply에 넘긴다.
			text = "Sample Text"
			textSize = 20.0
			setPadding(10, 0, 0, 0)
		} // 람다에 의해 초기화된 TextView 인스턴스를 반환한다.
```
apply 함수를 사용하면 함수의 본문에 간결한 식을 사용할 수 있다.

with와 apply는 수신 객체 지정 람다를 사용하는 일반적인 함수이다.
더 구체적인 함수를 유사한 패턴으로 활용할 수 있다.

```java
	fun alphabet() = buildString {
		for(letter in 'A'..'Z') {
			append(letter)
		}
		append("\nNow I know the alphabet!")
	}
```