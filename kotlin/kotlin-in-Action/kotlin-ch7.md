# 연산자 오버로딩과 기타 관례
> 관례 : 특정 언어 기능과 미리 정해진 이름의 함수를 연결해주는 기법
기존 자바 클래스에 코틀린 언어를 적용하기 위한 기법

## 7.1 산술 연산자 오버로딩

자바에서는 primitive 타입에만 산술 연산자를 사용할 수 있다.
(String 타입은 + 연산자만 적용가능)

### 7.1.1 이항 산술 연산 오버로딩

```java
	data class Point(val x: Int, val y: Int) 
```

```java
// 두 점을 더하는 연산
	data class Point(val x: Int, val y: Int) {
		operator fun plus(other: Point): Point {
			return Point(x + other.x, y + ohter.y)
		}
	}

	>> val p1 = Point(10, 20)
	>> val p2 = Point(30, 40)
	>>> println(p1 + p2)
	Point(x=40, y=60)
```

* 연산자를 오버로딩하는 함수 앞에는 **'operator'** 키워드를 붙인다.
* 이제 **'+'** 기호로 Point 객체를 더할 수 있다.
* p1 + p2는 p1.plus(p2)와 같이 동작한다.

```java
	/* 연사자를 확장 함수로 정의하기*/
	operator fun Point.plus(other: Point): Point {
		return Point(x + other.x, y + other.y)
	}
```

외부 함수의 클래스에 대한 연산자를 정의할 때는 관례를 따르는 이름의 확장 함수로 구현하는게 일반적인 패턴이다.

* 개발자가 직접 연산자를 만들어 사용할 수 없고, 코틀린에서 정해진 연산자만 오버로딩할 수 있다.

| 식    | 함수 이름         |
|-------|-------------------|
| a * b | times             |
| a / b | div               |
| a % b | mod (1.1부터 rem) |
| a + b | plus              |
| a - b | minus             |

* 우선 순위는 숫자 타입에 대한 연산자 우선 순위와 같다.

```java
	/* 두 피연산자의 타입이 다른 연산자 정의하기 */
	operator fun Point.times(scale: Double): Point {
		return Point((x * scale).toInt(), (y * scale).toInt())
	}

	>> val p = Point(10, 20)
	>>> println(p * 1.5)
	Point(x=15, y=30)
```

_비트 연산자에 대해 특별한 연산자 함수를 사용하지 않는다._

| 식  | 함수 이름 |
|-----|-----------|
| <<  | shl       |
| >>  | shr       |
| >>> | ushr      |
| &   | and       |
| |   | or        |
| ^   | xor       |
| ~   | inv       |

### 7.1.2 복합 대입 연산자 오버로딩
> 연산자를 오버로딩하면 코틀린은 그와 관련 있는 연산자도 자동으로 함께 지원한다.
+, +=

* 변수가 변경가능한 경우에만 복합 대입 연산자를 쓸 수 있다.
```java
	var point = Point(3, 4)
	point += Point(5, 6)
```
* 복합 대입 연산자가 객체의 내부 상태를 바꾸고 싶은 경우
	-> ex) 켈렉션 원소 추가
```java
	val number = ArrayList<Int>()
	numbers += 42
	>>println(numbers[0])
	42
```

* 연산자 이름 + Assign 함수를 정의하면 '연산자=' 계산에 사용된다.
ex) plusAssign, minusAssign

```java
	operator fun <T> MutableCollection<T>.plusAssign(element: T) {
		this.add(element)
	}
```
_+=은 plus와 plusAssign 양쪽으로 컴파일 될 수 있다._
*연산자 오버로딩과 복합 대입 연산자를 같이 정의하지 말아라*
혹은 변수를 val로 선언해서 연산자 오버로딩에 해당하는 연산만 가능하게 하는 것이 좋다.


* +, -는 항상 새로운 컬렉션을 반환한다.
* +=, -= 연산자는 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킨다.
  읽기 전용 컬렉션에서는 변경을 적용한 복사본을 반환한다. 

```java
	val list = arrayListOf(1, 2)
	list += 3
	val newList = list + listOf(4, 5) // 원소 타입이 일치하는 다른 컬렉션 
	>>>println(list)
	>> [1, 2, 3]
	>>> println(newList)
	>> [1, 2, 3, 4, 5]
```

### 7.1.3 단항 연산자 오버로딩
> 이항 연산자 오버로딩가 문법은 같다.

```java
	operator fun Point.unaryMinus(): Point {
		return Point(-x, -y)
	}

	>> val p = Point(10, 20)
	>> println(-p)
	Point(x=-10, y=-20)
```
* 함수의 파라미터가 없다.

_코틀린에서 지원하는 단항 연산자_

| 식       | 함수 이름  |
|----------|------------|
| +a       | unaryPlus  |
| -a       | unaryMinus |
| !a       | not        |
| ++a, a++ | inc        |
| --a, a-- | dec        |

전위와 후위 연산을 처리하기 위해 별다른 처리를 해주지 않아도 제대로 작동한다.

## 7.2 비교 연산자 오버로딩
> 모든 객체에 대해 비교 연산을 수행할 수 있다.

자바에서는 equals, compareTo를 호출해야 했지만, 코틀린에서는 '==' 연산자를 직접 사용할 수 있다.
코드가 간결해진다.

### 7.2.1 동등성 연산자 : equals

* '=='와 '!=' 연산자는 컴파일시 equals가 호출된다.
	* 내부에서 인자가 null인지 판단하므로 nullable값에도 적용할 수 있다.
	* a==b라면 a가 null이 아닐 경우에만 a.equals(b)를 호출한다.
	* a== b -> a?.equlas(b) ?: (b == null) //a가 null인경우 b도 null이면 true
* '===' 연산자는 오버로딩할 수 없다.

* equals는 Any에 정의된 메소드이므로 override가 필요하다.
	* Any의 equals는 operator가 붙어있어, 오버라이딩 하는 메소드에는 operator를 붙이지 않아도 적용된다.
	* ==오버라이딩된 메소드가 확장함수보다 우선순위가 높기 때문에, equals는 확장함수로 정의할 수 없다.==

### 7.2.2 순서 연산자 : compareTo
> 자바에서 정렬이나 값을 비교해야 하는 클래스는 Comparable 인터페이스를 구현했다.
Comparable의 compareTo 메소드는 객체의 크기를 비교해 정수로 나타내준다.

* 코틀린도 자바와 같은 Comparable 인터페이스를 제공한다.
	* compareTo 메소드를 호출하는 관례를 제공한다.
	* <, >, <=, >= 연산자는 compareTo 호출로 컴파일 된다.
	* compareTO의 반환값은 Int 타입이다.
	* p1 < p2 -> p1.compareTo(p2) < 0

```java
	class Person(val firstName: String, val lastName: String) : Comparable<Person> {
		override fun compareTo(other: Person): Int {
			return compareValuesBy(this, other,
				Person::lastName, Person::firstName) // lastName이 우선순위
		}
	}

	val p1 = Person("Alice", "Smith")
	val p2 = Person("Bob", "Johnson")
	>>> println(p1 < p2)
	false
```

compareValuesBy 함수는 두 객체와 여러 비교 함수를 인자로 받는다.
첫 번째 비교 함수에 두 객체가 같지 않다는 결과(0이 아닌 값)이 나오면 결과 값을 반환하고 ,
두 객체가 같다는 결과가 나오면 두번째 함수를 호출한다.

_필드를 직접 비교하면 코드는 복잡해지지만 속도는 빨라진다._

Comparalbe을 구현하는 모든 자바 클래스를 코틀린에서는 간결한 연산자 구문으로 비교할 수 있다.

## 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례

### 7.3.1 인덱스로 원소에 접근 : get/set
>코틀린에서 맵의 원소에 접근하거나, 자바에서 배열 원소에 접근할때 각괄호[]를 사용한다.

```java
	val value = map[key] // key의 value 가져오기.
	map[key] = newValue // key에 value 넣기.
```
코틀린에서 인덱스 연산자([])도 관례를 따른다.

* 인덱스 연산자를 사용해 원소를 읽는 연산은 get 연산자 메소드로 변환.
  인덱스 연산자를 사용해 원소를 쓰는 연산은 set 연산자 메소드로 변환.
* Map, MutableMap 인터페이스에 get/set 메소드가 선언되어 있다.


```java
	/* 점은 좌표를 읽을 때 인덱스 연산을 사용할 수 있다.*/
	operator fun Point.get(index: Int): Int {
		return when(index) {
			0 -> x
			1 -> y
			else -> 
				throw IndexOutOfBoundsException("Invalid coordinate $index")
		}
	}

	val p = Point(10, 20)
	println(p[1])
```
파라미터가 Int가 아니어도 된다.
파라미터의 개수가 1개 이상이어도 된다.

* set 메소드는 불변가능한 클래스에 대해서만 함수를 정의 할 수 있다.
```java
	data class MutablePoint(var x: Int, var y: Int)

	operator fun MutablePoint.set(index: Int, vlaue: Int) {
		when(index) {
			0 -> x = value
			1 -> y = value
			else ->
				throw IndexOutOfBoundsExceptoin("Invalid coordinate $index")
		}
	}

	val p = MutablePoint(10, 20)
	p[1] = 42 // = p.set(1, 42)
	println(p)
	MutablePoint(x=10, y=42)
```

### 7.3.2 in 관례
> in은 객체가 컬렉션에 들어이는지 검사한다.
in 연산자의 관례 함수 이름은 contains다.

```java
	/* 어떤 점이 사각형 영역에 들어가는지 판단하기. */
	data class Rectangle(val upperLeft: Point, val lowerRight: Point) 

	operator fun Rectangle.contains(p: Point): Boolean {
		return p.x in upperLeft.x until lowerRight.x &&
		p.y in upperLeft.y until lowerRight.y
	}

	val rect = Rectangle(Point(10, 20), Point(50, 50))
	println(Point(20, 30) in rect) // rect contains(Point(20, 30))
	>> true
```

* in의 우항에 있는 객체는 contains 메소드의 수신 객체가 되고,
  in의 좌항에 있는 객체는 contains 메소드의 인자로 전달된다.

### 7.3.3 rangeTo 관례
> ..연산자의 관례 함수 이름은 rangeTo이다.

start..end -> start.rangeTo(end)

* 이 연산자를 아무 클래스에나 정의할 수 있다.
* Comparable 인터페이스를 구현하면 rangeTo를 정의할 필요가 있다.
	* 코틀린 ㅍ준 라이브러리에는 Comparable 객체에 대해 적용 가능한 rangeTo함수가 있다.
```java
	operator fun <T: Comparable<T>> T.rangeTo(that: T): ClosedRange<T>
	// 범위를 반환한다.
```

* rangeTo 연산자는 다른 산술 연산자보다 우선순위가 낫다.
  혼동을 피하기 위해 괄호로 인자를 감싸주면 된다.
 ```java
 	val n = 9
 	println(0..(n) + 1)
 	>> 0..10
 ```

* 0..n.forEach {}와 같은 식은 컴파일 할 수 없다. 
  범위 연산자가 우선 순위가 낮기 때문에.
  꼭 범위 연산자를 괄호로 감싸주어라.

### 7.3.4 for 루프를 위한 iterator 관례
> 코틀린의 for 루프는 in 연산자를 사용한다.

* for (x in list) -> list.iterator()
* iterator 또한 관례이므로 확장 함수로 정의할 수 있다.
	* 일반 자바 문자열에 대한 for 루프도 가능하다.
* 코틀린 표준 라이브러리는 String의 상위 클래스인 CharSequence에 대한 iterator확장 함수를 제공한다.

## 7.4 구조 분해 선언과 component 함수
```java
	val p = Point(10, 20)
	val (x, y) = p
	println(x)
	>> 10

	// val (x, y) = p -> val a = p.component1()
	//					 val b = p.component2()

	// 클래스내에 함수 정의하기.
	class Point(val x: Int, val y: Int) {
		operator fun component1() = x
		operator fun component2() = y
	}
```

* 구조 분해 선언은 함수에서 여러 값을 반환할 때 유용하다.
	* 반환해야 하는 모든 값이 들어갈 데이터 클래스를 정의하고 
	  함수의 반환 타입을 그 데이터 클래스로 바꾼다.
```java
	data class NameComponents(val name: String, val extension: String) 

	fun splitFilename(fullName: String): NameComponents {
		val result = fullName.split('.', limit = 2)
		return NameComponents(result[0], result[1])
	}

	>>> val (name, ext) = splitFilename("example.kt")
	>>>println(name)
	example
	>>> println(ext)
	kt
```

* 배열과 컬렉션에도 componentN 함수가 있다.
	* 크기가 정해진 컬렉션을 다루는 경우 구조 분해가 더 유용하다.

```java
	/* 컬렉션에 대해 구조 분해 선언 사용하기 */
	data class NmaeComponents(val name: String, val extension: String)

	fun splitFilename(fullName: String): NameComponents {
		val (name, extension) = fullName.split('.', limit = 2)
		return NameComponents(name, extension)
	}
```
코틀린 표준 라이브러리에서는 맨 앞의 다섯 원소에 대한 componentN을 제공한다.

### 7.4.1 구조 분해 선언과 루프
> 함수 본문 내의 선언문뿐 아니라 변수선언이 들어갈 수 있는 장소라면 어디든 구조 분해 선언을 사용할 수 있다.

```java
	/* 구조 분해 선언을 사용해 맵 이터레이션하기 */
	fun printEntries(map: Map<String, String>) {
		for ((key, value) in map) {
			println("$key -> $ value")
		}
	}

```
* 맵은 이터레이션하는 관례와, 구조 분해 선언 관례를 제공한다.

## 7.5 프로퍼티 접근자 로직 재활용 : 위임 프로퍼티.
> 관례에 의존하는 특성

위임 : 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기는 디자인 패턴
위임 객체 : 작업을 도와주는 객체

### 7.5.1 위임 프로퍼티 소개

위임 프로퍼티의 문법
```java
	class Foo {
		var p: Type by Delegate()
		// p 프로퍼티는 접근자 로직을 다른 객체에게 위임한다.
		// Delegate 클래스의 인스턴스를 위임 객체로 사용한다.
		// by 뒤에 있는 식을 계산해서 위임에 쓰일 객체를 얻는다.
	}
```

```java
	private val delegate = Delegate() // 도우미 객체 생성
	var p: Type // 자신의 작업을 위임한다.
	set(value: Type) = delegate.setValue(...)
	get() = delegate.getValue(...)
```
* 프로퍼티 위임 관례를 따르는 도우미 클래스는 getValue/setValue 메소드를 제공해야 한다.
(변경가능한 프로퍼티일 경우메나 setValue 제공)
	* 멤버 메소드거나 확장 함수일 수 있다.

```java
	class Delegate {
		operator fun getValue(...) {...}
		operator fun setValue(...) {...}
	}

	class Foo {
		var p: Type by Delegate()
	}
```

### 7.5.2 위임 프로퍼티 사용 : by lazy()를 사용한 프로퍼티 초기화 지연
지연 초기화 : 객체의 일부분을 초기화하지 않고 실제로 그 부분이 필요할 경우 초기화 한다.

```java
	/* bacing field를 사용해서 지연 초기화 구현 */
	class Person(val name: String) {
		private var _emails: List<Email>? = null
		val emails: List<Eamil>
		  get() {
		  	if (_emails == null) {
		  		_emails = loadEmails(this)
		  	}
		  	return _emails!!
		  }
	}

	>>> val p = Person("Alice")
	>>> p.emails
	Load emails for Alice

```
_단점_
1. 지연 초기하해야 하는 프로퍼티가 많아지면 코드의 가독성이 떨어진다.
2. 스레드 안전하지 않아 항상 정상 작동을 보장할 수 없다.

```java
	/* 코틀린에서 제공하는 위임 프로퍼티 */
	class Person(val name: String) {
		val emails by lazy { loadEmails(this) }
	}
```
위임 객체를 반환하는 표준 라이브러리 함수 **lazy**
1. getValue 메소드가 들어있는 객체를 반환한다.
2. lazy 함수의 인자는 값을 초기화할 때 호출할 람다.
3. 스레드 안전하게 동작함.

### 7.5.3 위임 프로퍼티 구현
> 어떤 객체의 프로퍼티가 바뀔 때마다 리스너에게 변경 통지를 보낼 클래스.
PropertyChangeSupport 인스턴스를 저장하고 프로퍼티 변경 시 인스턴스에게 처리를 위임하는 방식으로 구현한다.

```java
	/* PropertyChangeSupport를 사용하기 위한 도우미 클래스 */
	open class PropertyChangeAware {
		protected val changeSupport = PropertyChangeSupprot(this)

		fun addPropertyChangeListener(listener: PropertyChangeListener) {
			changeSupport.addPropertyChangeListener(listener)
		}

		fun removePropertyChangeListener(listener: PropertyChangeListener) {
			changeSupport.removePropertyChangeListener(listener)
		}
	}

	class Person(val name: String, age: Int, salary: Int): PropertyChangeAware() {
		var age: Int = age
		set(newValue) {
			val oldValue = field
			field = newValue
			changeSupport.firePropertyChange(
				"age", oldValue, newValue)
		}

		var salary: Int = salary
		set(newValue) {
			val oldValue = field
			fiedl = newValue
			changeSupprot.firePropertyChnage(
				"salary", oldValue, newValue)
		}
	}
```
```java
	/* 도우미 클래스 */
	class ObservableProperty(
		val propName: String, var propValue: Int,
		val changeSupport: PropertyChangeSupport) {
		fun getValue(): Int = propValue
		fun setValue(newValue: Int) {
			val oldValue = propValue
			propValue = newValue
			changeSupport.firePropertyChnage(propName, oldValue, newValue)
		}
	}

	->

	class ObservableProperty(
		val propName: String, var propValue: Int,
		val changeSupport: PropertyChangeSupport) {

		operator fun getValue(p: Person, prop: Kproperty<*>): Int = propValue
		operator fun setValue(p: Person, prop: Kproperty<*>, newValue: Int) {
			val oldValue = propValue
			propValue = newValue
			changeSupport.firePropertyChnage(prop.name, oldValue, newValue)
		}
	}
	// getValue, setValue에도 operator가 붙는다.
	// getValue와 setValue는 프로퍼티가 포함된 객체와 프로퍼티를 표현하는 객체를 파라미터로 받는다.
	// KProperty 인자를 통해 프로퍼티 이름을 전달받으르모 주 생성자에서는 name프로퍼티를 없앤다.


	class Person(val name: String, age: Int, salary: Int): PropertyChangeAware() {
		val _age = ObservableProperty("age", age, changeSupport)
		var age: Int
			get() = _age.getValue()
			set(value) { _age.setValue(value) }

		val _salary = ObservableProperty("salary", salary, changeSupport)
		var salary: Int
			get() = _salary.getValue()
			set(value) { _salary.setValue(value) }
	}
```

### 7.5.4 위임 프로퍼티 컴파일 규칙
```java
	class C {
		private val <delegate> = MyDelegate()

		var prop: Type
		get() = <delegate>.getValue(this, <property>)
		set(value: Type) = <delegate>.setValue(this, <property>, value)
	}
```
val x = c.prop -> val x = <delegate>.getValue(c, <property>)
c.prop = x -> <delegate>.setValue(c, <property>, x)

### 7.5.5 프로퍼티 값을 맵에 저장
> 자신의 프로퍼티를 동적으로 정의할 수 있는 객체를 만들 때 위임 프로퍼티를 활용하는 경우가 자주 있다.
> 위와 같은 객체를 확장 가능한 객체라 한다.

```java
	/* 일반 API 사용
		값을 맵에 저장하는 프로퍼티 정의하기. */
	class Person {
		private val _attributes = hashMapOf<String, String>()

		fun setAttribute(attrName: String, value: String) {
			_attributes[attrName] = value
		}

		val name: String
		get() = _attributes["name"]!!
	}

	/* 특정 프로퍼티를 처리하기 위해 구체적인 개별 API제공
		값을 맵에 저장하는 위임 프로퍼티 정의하기. */

	class person {
		private val _attributes = hashMapOf<String, String>()

		fun setAttribute(attrName: String, value: String) {
			_attributes[attrName] = value
		}

		val name: String by _attributes
	}

```

### 7.5.6 프레임워크에서 위임 프로퍼티 활용
>객체 프로퍼티를 저장하거나 변경하는 방법을 바꿀 수 있으면 프레임워크를 개발할 때 유용하다.

```java
	/* 위임 프로퍼티를 사용해 데이터베이스 칼럼 접근하기 */
	object Users : IdTable() { // 단하나만 존재하는 테이블이므로 싱글턴 객체로 선언. 
		val name = varchar("name", length = 50).index()
		val age = integer("age")
	}

	class User(id: EntityID) : Entity(id) {
		var name: String by Users.name
		var age: Int by Users.age
	}
```
