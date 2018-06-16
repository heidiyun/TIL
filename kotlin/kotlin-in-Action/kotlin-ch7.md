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