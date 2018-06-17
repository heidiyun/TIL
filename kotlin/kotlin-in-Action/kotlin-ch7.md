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
