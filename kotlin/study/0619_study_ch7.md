# 연산자 오버로딩
> 관례 : 특정 언어 기능과 미리 정해진 이름의 함수를 연결해주는 기법

_규칙_
1. 연산자를 오버로딩하는 함수 앞에는 'operator'키워드를 붙입니다.
2. 원래 함수에 있던 파라미터를 누락하는 경우에도 오류가 발생합니다.
2. 사용자가 직접 연산자를 만들어 사용할 수 없습니다.
	(코틀린에서 정해진 연산자만 오버로딩할 수 있다.)
3. 우선 순위는 숫자 타입에 대한 연산자 우선 순위와 같다.
4. 클래스의 멤버 메소드로도 선언할 수 있고,
	확장 함수로도 정의할 수 있다.
5. operator 함수도 오버로딩이 가능하기 때문에 같은 이름에 파라미터 타입이 서로 다른 연산자 함수를 여러개 만들 수 있다.
```java
	operator fun plus(other: Point): Point {
		return Point(x + ohter.x, y + ohter.y)
	}
```

## 1. 산술 연산자 오버로딩
> 자바에서는 primitive 타입에만 산술 연산자를 사용할 수 있다.
(String 타입은 + 연산자만 적용가능하다.)

_주의할 점_
교환법칙이 성립하지 않는다.
```java
	data class Point(val x: Int, val y: Int)

	operator fun Point.plus(other: Int): Int {
		return Point(x + other.x, y + ohter.y)
	}

	fun main(args: Array<String>) {
		val p = Point(19, 20)
		val p1 = p + 16
		// val p2 = 16 + p xxxxx
	}
```
15 + a, a + 15는 같지 않음.

```java
	val list = arrayListOf(1, 2)
	list += 3
	println(list)
	[1, 2, 3]
```


## 2. 복합 대입 연산자 오버로딩
> 연산자를 오버로딩하면 코틀린은 그와 관련있는 연산자도 자동으로 함께 지원합니다.

_규칙_
1. 변수가 변경가능한 경우에만 복합 대입 연산자를 쓸 수 있습니다.
2. '연산자 이름' + 'Assign' 함수를 정의하면 '연산자=' 계산에 사용됩니다.

_주의할 점_
+= 연사자와 같은 경우 plus, plusAssign 양쪽으로 컴파일이 가능하다.
두 함수를 함께 정의한 경우, 컴파일 오류가 난다.

1. 연산자 오버로딩과 복합 대입 연산자 오버로딩을 같이 정의하지 말것.
2. 변수를 val로 선언해서 연산자 오버로딩에 해당하는 연산만 가능하게 하는것이 좋다.

## 3.단항 연산자 오버로딩
이항 연산자 오버로딩가 문법은 같다.

	operator fun Point.unaryMinus(): Point {
		return Point(-x, -y)
	}

	>> val p = Point(10, 20)
	>> println(-p)
	Point(x=-10, y=-20)

함수의 파라미터가 없다. (파라미터를 쓰면 컴파일 오류가 납니다.)

## 4. 비교 연산자 오버로딩

```java
	class Point(val x: Int, val y: Int) {
		override fun equals(obj: Any?): Boolean {
			if (obj == this) return true
			if (obj !is Point) return false
			return obj.x == x && obj.y == y
		}
	}
```
equals의 경우 Any의 메소드이고, Any안에서 operator로 정의되어 있으므로
operator키워드를 붙이지 않아도 됩니다.

오버라딩되는 메소드가 확장 함수보다 우선순위가 높아서 확장 함수로 정의할 수 없습니다.

## 5. 순서 연산자 : compareTo

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