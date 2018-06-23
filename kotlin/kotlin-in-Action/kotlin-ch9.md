# 9장. 제네릭스

## 9.1 제네릭 타입 파라미터
> 제네릭스를 사용하면 타입 파라미터를 받는 타입을 정의할 수 있다.
제네릭 타입의 인스턴스를 만들려면 타입 파라미터를 궤적인 타입 인자로 치환해야 한다.

자바와 달리 코틀린은 제네릭 타입의 타입 인자를 프로그래머가 명시하거나 컴파일러가 추론할 수 있어야 한다.
```java
	List list = new ArrayList<>();
	// 자바에서 타입인자가 없는 제네릭 선언 가능.
```
### 9.1.1 제네릭 함수와 프로퍼티
> 제네릭 함수를 호출할 때는 반드시 구체적 타입으로 타입 인자를 넘겨야 한다.

1. 함수 호출시 타입 인자를 명시적으로 지정할 수 있다.

```java
	fun <T> List<T>.slice(indices: IntRange): List<T>

	>> val letters = ('a'..'z').toList()
	>> println(letters.slice<Char>(0..2))
```

2. 제네릭 확장 프로퍼티

```java
	val <T> List<T>.penultimate: T
		get() = this[size - 2]
```
* 일반 프로퍼티는 타입 파라미터를 가질 수 없다. 

### 9.1.2 제네릭 클래스 선언

1. 클래스나 인터페이스 뒤에 <> 기호를 넣으면 클래스를 제네릭하게 만들 수 있다.
2. 클래스 본문 안에서 타입 파라미터를 다른 일반 타입처럼 사용할 수 있다.

```java
	interface List<T> {
		operator fun get(index: Int): T // 타입 파라미터를 일반 타입처럼 사용
	}
```

3. 제네릭 클래스를 확장하는 클래스를 정의하려면 기반 타입의 제네렉 파라미터에 대해 타입 인자를 지정해야 한다.
> 구체적인 타입을 넘길 수 있고, 타입 파라미터로 받은 타입을 넘길 수도 있다.

```java
	class StringList: List<String> {
		override fun get(index: Int): String = ...}
	// 타입 인자를 String으로 지정해 List구현

	class ArrayList<T>: List<T> {
		override fun get(index: Int): T = ...
	}
	// ArrayList의 제네릭 타입 파라미터 T를 List의 타입 인자로 넘긴다.
```

4. 클래스가 자기 자신을 타입 인자로 참조할 수 있다.

```java
	interface Comparable<T> {
		fun compareTo(other: T): Int
	}

	class String: Comparable<String> {
		override fun compareTo(other: String): Int = ...
	} // String 클래스가 타입 인자로 String을 사용한다.
```

### 9.1.3 타입 파라미터 제약
> 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능

타입 파라미터에 대한 상한을 지정하면 그 제네릭 타입을 인스턴스화 할 때 사용하는 타입 인자는 반드시 그 상한 타입이거나 하위 타입이어야 한다.

```java
	fun <T: Number> List<T>.sum(): T
	// 타입 파라미터 이름 뒤에 :을 붙이고 상한 타입을 지정한다.

	/* 자바 */
	<T extends Number> T sum(List<T> lisg)
```
_타입 파라미터 T에 대한 상한을 정하면 T타입의 값을 그 상한 타입의 값으로 취급한다._
ex) 메소드 호출.


### 9.1.4 타입 파라미터를 null이 될 수 없는 타입으로 한정
> 상한을 정하지 않은 타입 파라미터는 Any?를 상한으로 지정한 것과 같다.

* 타입 파라미터의 값을 쓸 때에는 안전한 호출을 사용해야 한다.
* String?, Int?와 같은 타입도 타입 파라미터로 지정할 수 있다.

==null이 될 수 없는 타입만 타입 인자로 받으려면 <T: Any>제약을 걸어준다.==
Any가 아니더라도 ?아닌 타입을 상한으로 걸면 nullalbe 인자를 받을 수 없다.

```java
	class Processor<T: Any> {
		fun process(value: T) {
			value.hashCode() // 안전한 호출하지 않아도 됨.
		}
	}
```