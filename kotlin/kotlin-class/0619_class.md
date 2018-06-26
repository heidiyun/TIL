# 0616 코틀린 수업

## 멤버 참조 
```java
	class Stack {
		var data: Array<Int> = Array<Int>(32) {0}
		var top: Int = -1

		// 함수의 시그니처 : 함수가 어떤 인자를 받고 어떤 타입을 반환하는지.
		// fun push(this: Stack, a: Int)
		// 어떤 스택에 쌓을 것인지.
		fun push(a: Int) {
			data[++top] = a
		}

		fun pop(): Int = data[top--]
	}


	fun main(args: Array<String>) {
		var p = ::Stack
		var f = ::String

		val s = Stack()

		// 특정한 인자가 s로 고정되어 있다. - 바인드 되어 있다.(바운드 참조)
		// this가 s 객체로 고정되어 있다.
		val fp = s::push
		fp(10)
		fp(20)

		val fp2 = s::pop
	}
```

## 생성자 참조
> java 8 부터 사용가능, kotlin에서 지원

* Refactoring : 기능을 변경하지 않고 코드의 구조를 개선하는 작업
	* 마틴 파울러가 만듬
	* 유지보수를 해치는 코드 설계를 code smells라고 한다.

* 부모 클래스 -> 자식 클래스의 공통된 기능을 포함한다.
* LSP : 자식의 공통된 기능은 반드시 부모로부터 비롯되어야 한다.
		부모의 참조를 통해서 자식의 기능을 온전하게 활용할 수 있다. 
			=> '다형성'

```java
	abstract class Shape {
		abstract fun draw()
	}

	class RegisterShape(type: Int, fn: () -> Shape) {
		init {
			ShapeFactory.RegisterShape(type, fn)
		}

	class Rect : Shape() {
		override fun draw() = println("Draw Rect")
	}

	class Circle : Shape() {
		override fun draw() = println("Draw circle")
	}

/*
	fun main(args: Array<String>) {
		val shapes = mutableListOf<Shape>()

		val scanner = Scanner(System.'in')

		while (true) {
			val cmd = scanner.nextInt()

			if (cmd == 1) {
				shapes.add(Rect())
			} else if (cmd == 2) {
				shapes.add(Circle())
			} else if (cmd == 9) {
				for (e in shapes) {
					e.draw()
				}
			}
		}

*/
	} 
// 위와 같은 경우 새로운 도형이 추가될 때마다 코드의 수정이 필요하다.
// 수정이 발생하는 지점을 한곳에 모은다.
// Factory pattern
// : 객체의 생성을 담당하는 Factory를 만든다.
// Factory 클래스는 싱글톤으로 만드는 경우가 많다.

	/* object ShapeFactory {
		var map: MutableMap<Int, (() -> Shape)> = mutableMapOf()

		fun registerShape(type: Int, fn : () -> Shape){
			map[type] = fn
		}

		fun createShape(type: Int): Shape? {
        	val fn = map[type]
        	// println("$type -> $fn")

        	return fn?.invoke()
    	}

	}
	/*

	/*

	fun main(args: Array<String>) {

    	// 등록을 해야 한다.
    	ShapeFactory.registerShape(1, ::Rect)
    	ShapeFactory.registerShape(2, ::Circle)
    	ShapeFactory.registerShape(3, ::Triangle)

    	val shapes = mutableListOf<Shape>()

    	val scanner = Scanner(System.`in`)

    	while (true) {
        	val cmd = scanner.nextInt()

        	// 객체의 생성에 대해서 OCP를 만족하게 할 수 없을까?
        	if (cmd in 1..8) {
            	val shape = ShapeFactory.createShape(cmd)
            	shape?.let {
                	shapes.add(shape)
            	}

        	} else if (cmd == 9) {
            	for (e in shapes) {
                	e.draw()
            	}
        	}
    	}
	}
	*/

	// 새로운 도형이 추가될 때마다 메인 함수에서 등록하는 코드가 길어진다.

	object ShpaeFactory {
		fun createShape(type: Int) = when(type) {
			0 -> Rect()
			1 -> Circle()
			2 -> Triangle()
			else -> throw IllegalArgumentException("wrong type")
		}
	}


	fun main(args: Array<String>) {
		val shapes = mutableListOf<Shape>()

		val scanner = Scanner.nextInt()

		while (true) {
			val cmd = scanner.nextInt()

			if (cmd in 1..8) {
				val shape = ShapeFactory.createShape(cmd)
				shapes.add(shape)
			} else if (cmd == 9) {
				for (e in shapes) {
					e.draw()
				}
			}
		}
	}
```

## 전역 함수 vs 메소드 함수
* 전역 함수 : 연관된 객체가 없다. this가 없다. static 메소드
* 메소드 함수 : 연관된 객체가 있다. this가 있다.

```java
	fun add(a: Int, b: Int) = a + b
	// 전역 함수.

	fun main(args: Array<String>) {
		var result = add(10, 32)

		val fp = ::add // 함수 참조(멤버 참조)
		result = fp(10, 32)
	}
```

## 연산자 오버로딩
> 일반 객체를 사용하는 방법을 직관적으로 표현할 수 있다.
> 특정한 함수를 연산자를 통해 호출하는 방법

**문제점**
 : 통상적인 개념과 다르게 만들었을 경우 혼란을 야기할 수 있다.

```java
	data class Point(val x: Int, val y: Int) {
		operator fun unaryMinus(): Point {
			return copy(x = -x, y= -y)
		}

		operator fun get(key: String): Int {
			when (key) {
				"x" -> return x
				"y" -> return y
				else -> throw IllegalArgumentException()
			}
		}

		operator fun plus(value: Int): Point {
			return copy(x = x + value, y = y + value)
		}
	}

	class Rect {
		operator fun contains(point: Point) = false
	}

	// += (복합 대입 연산자)
	// plus : Point -> 새로운 객체를 생성한다.
	// plusAssign : Unit -> 기존 객체의 상태를 변경한다.

	fun main(args: Array<String>) {
		var list = listOf(10, 20)
		list += 3
		println(list)

		var p1 = Point(10, 32)
		var p2 = Point(10, 32)

		var rect = Rect()
		if (rect.contains(p2)) {

		}
		// 위의 if문과 아래 if문은 같다.

		if (p2 in rect) {

		}

		p1 += 2
		// p1객체의 내부 상태를 변경하는 것이 가능한 경우
		// x += 2
		// y += 2

		// p1의 참조를 변경하는 것이 가능할 경우
		// x,y 의 값을 2 증가시킨 새로운 객체를 생성한다.


	}
```

## 지연초기화 (lazy initialization)
> 객체가 생성될 때 초기화하지 않고 접근할 때 초기화 된다.

```java
	fun getName(): String {
		println("getName()")
		return "Tom"
	}

	class User {
		val name: String by lazy {
			getName()
		}
		// 지연 초기화 : name에 처음 접근할 때 값을 초기화 한다.

		// 프로퍼티가 var로 선언되었을 경우, 초기화 값을 정할 수 없을 때 
		lateinit var address: String

		init {
			println("user()")
		}
	}

	fun main(args: Array<String>) {
		user.address = "suwon"
	}
```