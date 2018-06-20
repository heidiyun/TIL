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
