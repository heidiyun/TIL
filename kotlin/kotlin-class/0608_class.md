# 0608 코틀린 수업

## 싱글톤 (singleton)
> 오직 한개의 객체를 만들고 언제 어디서든 동일한 방법으로 접근하는 객체

1. 외부에서 객체 생성이 불가능해야 한다.
	* 생성자의 접근 지정자를 private로 설정한다.
2. 동일한 방법을 제공해야 한다.
	* Thread safety를 고려해야 한다.

```java
	/* enum singleton */
	enum Cursor {
		INSTANCE;
	
		private int x;
		private int y;

		public void move(int x, int y) {
			this.x = x;
			this.y = y;
			System.out.println("move");
		}

		public int getX() {
			return x;
		}
	}	

	public class Program {
		public static void main(String[] args) {
			Cursor c = Cursor.INSTANCE;
			c.move(10, 32);

			System.out.println(c.getX());
		}
	}

	class Cursor {
		private static final Cursor INSTANCE = new Cursor();
		private Cursor() { }

		public static Cursor getInstance() {
			return INSTANCE;
		} 
	}

	public class Program {
		public static void main(String[] args) {
			Cursor c1 = Cursor.getInstance();
			Cursor c2 = Cursor.getInstance();

			System.out.pirnltn(c1);
			System.out.println(c2);
		}
	}
```
**위 코드의 문제점**
1. static 객체 : Cursor.class를 JVM에 로드하는 시점에 초기화된다.
	Cursor 객체의 생성 비용이 클 경우, 프로그램의 시작에 악영향을 미친다.
	=> Lazy Initialization(지연 초기화)
		: 미리 생성하는 것이 아니라, 처음 접근될 떄 생성한다.

2. 두 개 이상의 객체가 생성될 소지가 있다.
	1) Reflection
	2) Serialization

**Thread Safety**
```java
	class Cursor {
		// private static Cursor instance;
		// 락의 비용은 크다. 
		 //  => DCLP(Double Checked Locking Pattern) - 잘 동작한다.
    	//    선언적인 코드가 아니다.
    	//    => 언어의 특성을 이용하는 것이 좋다. - Idioms
    	//       JLS: 중첩 클래스의 정적 final 필드는 생성 시점에 	
    	//		초기화되는 것이 아니라
    	//            첫 접근 시점에 초기화된다.
    	//       => IODH(Initialization On Demand Holder) - Java Idioms

    	/* 
		public static Cursor getInstance() {
			if (instance == null) {
				synchronized (Cursor.class) {
					if (instance == null) {
						instance = new Cursor();
					}
				}
			}
			return instance;
		}
		*/

		private Cursor() {
			System.out.println("Cursor()");
		}

		private static class Singleton {
			private static final Cursor INSTANCE = new Cursor();
		}

		public static Cursor getInstance() {
			return Singleton.INSTANCE;
		}
	}
```

## 코틀린 싱글톤
```java
	// object 선언 : 오직 한개의 객체를 생성한다.
	//   			 언제 어디서든 접근이 가능하다.
	class User {
		// companion object -> static final
		companion object {
			// const: 컴파일 타임 상수
			// 런타임 상수 : 메모리 공간을 차지한다.
			val typename = User::class.java.simpleName
		}
	}

	object MyFilter : Predicate<Int> {
 	   override fun test(t: Int): Boolean {
        return t % 2 == 1
    	}
	}
```

## 제네릭
```java
// 알고리즘은 동일하고, 타입만 다른 경우 제네릭을 도입하는 것이 좋다.
// : 아래의 설계 방식
//   => 동작 파라미터화


// fun <T> List<T>.xfilter( /*data: List<T>*/, predicate:
//	 Predicate<T>): List<T> {
//    	val result = mutableListOf<T>()

//    	for (e in this) {
//        	if (predicate.test(e))
//            	result.add(e)
//    	}

//    	return result
// }



	inline fun <T> List<T>.xfilter(/*data: List<T>,*/ predicate: (T) -> Boolean): List<T> {
    	val result = mutableListOf<T>()

    	for (e in this) {
        	if (predicate(e))
            	result.add(e)
    	}

    	return result
	}


	fun main(args: Array<String>) {
    	val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)  // ArrayList<Int>

    	// val result = xfilter(list, MyFilter())
    	// val result = list.xfilter(MyFilter())
    	// val result = list.xfilter(MyFilter)

    	// val result = list.xfilter(Predicate<Int> { t -> t % 2 == 1 })
    	//    val result2 = list.xfilter { object: Predicate<Int> {
    	//         override fun test(e: Int): Boolean {
    	//         }
    	//    }}


    	fun isOdd(e: Int): Boolean = e % 2 == 1

    	// val result = list.xfilter(::isOdd)
    	val result = list.xfilter {
        	it % 2 == 1
    	}

    	println(result)

	}
```

## 코틀린 생성자

* Flyweight Pattern : 속성이 동일한 객체는 공유하자.
* Memoization : 동일한 계산을 반복해야 하는 경우 한 번 계산한 결과를 메모리에 저장하여 중복 계산을 방지할 수 있게 하는 기법.

```java
	class Image(val url: Stirng) {
		// Factory
		companion object {
			private val map = mutableMapOf<String, Image>()

			fun create(url: String): Image {
				return map.getOrPut(url) {
					Image(url)
					// 비지역 반환
				}
			}
		}

		init {
			Thread.sleep(2000)
		}

		fun draw() {
			println("Draw image - $url")
		}
	}

	fun main(args: Array<String>) {
		val image1 = Image.create("https://google.com/IU.png")
		image1.draw()
	}
```

정적 팩토리 메소드 : 생성자의 한계를 극복한다.
	-> 이름을 변경할 수 없다.
	-> 객체 생성의 정책을 변경할 수 없다.

```java
	class Color private constructor(val r: In, val g: Int, val b: Int) {
		// private 생성자 : 외부에서 객체 생성이 불가능하다.

		companion object {
			val orange = Color(255, 255, 0)
			fun orange() = Color(255, 255, 0)
		}
	}

	fun main(args: Array<String>) {
		Color.orange
		Color.orange()
	}
```

## Inner class, Nested class
> java에서는 inner class가 기본이고, kotlin에서는 nested class가 기본이다.

* inner class는 외부 클래스를 암묵적으로 참조하고 있다.

```java
	class User(val name: String, val age: Int) {
		// Memento Pattern : 객체의 상태를 저장하고 복원하는 패턴.
		class Memento(val name: String, val age: Int): Serializable
		// inner class로 선언되면 런타임 에러가 발생한다.
		// 외부 클래스를 암묵적으로 참조하기 때문에 User class가 
		// Serializable을 구현하지 않아 오류가 발생한다.

		fun snapshot() : Memento = Memento(name, age)
	}

	fun main(args: Array<String>) {
		val user = User("Tom", 42)

		val memento = user.snapshot()

		val fos = FileOutputStream("user.dat")
		val oos = ObjectOutputStream(fos)

		fos.use {
			oos.user {
				oos.writeObject(memento)
			}
		}
	}
```