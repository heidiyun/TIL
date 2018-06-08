
# 코틀린 생성자
```java
	public class User {
		private String name;
		private int age;
		private String birthday;

		public User(String name, int age, String birthday) {
			this.name = name;
			this.age = age;
			this.birthday = birthday
		}
	}
```
자바의 생성자는 클래스 이름과 같아야 한다.
생성자를 통해서 필드를 초기화 합니다.
	-> 초기화 하지 않은 필드를 선언하고 생성자의 파라미터로 필드의 초기화 정보를 받아온다.

위와 같이 생성자가 하나만 있을 경우에는 그렇게 까지 큰 보일러플레이트를 발생시키지 않는다고 생각할 수 있습니다.
```java
public class User {
	private String name;
	private int age;
	private String birthday;

	public User(String name) {
		this.name = name;
	}

	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public User(String name, int age, String birthday) {
		this.name = name;
		this.age = age;
		this.birthday = birthday;
	}
}
	// 생성할 객체의 정보에 따라서 여러개의 생성자가 필요한 경우가 있다.
	// 오버로딩을 통해서 이를 해결할 수 있다.
	// 생성자의 인자가 많으면 많을수록 더 큰 보일러 플레이트가 발생합니다.
	// 자바에서는 builder패턴을 고려하게 됩니다.
	// 이것은 초기화 구문이 계속 중복되는 보일러 플레이트를 발생시킨다.
```
코틀린은 자바에서의 보일러 플레이트를 해결하기 위해서 아래와 같은
문법을 제공합니다.

```java
	class User constructor(val name: String, 
		val age: Int,
		val birthday: Sring) 
	//코틀린의 생성자는 위와 같이 씁니다.
	// 클래스 이름 뒤에 오는 괄호로 둘러싸인 코드가 주 생성자 입니다.

	class User(val name: String, val age: Int, val birthday: String)
	// 애노테이션, 가시성 변경자가 없다면,
	// constructor 키워드를 생략할 수도 있다.

	// 주생성자는 클래스 이름 뒤에 바로 오기 때문에 별도의 코드를 포함시킬 수가 없다.
	// 필드를 초기화하기 위해서는 별도의 초기화 블록이 필요합니다.

	class User(val name: String = "",
		val age: Int = 0,
		val birthday: String = "")
	// 디폴트 값을 넣어주면 따로 생성자 오버로딩의 문제를 해결할 수 있다.

	>> val user = User("Tom", 42, "0602")
	>> val user2 = User("Tom", 42)
	>> val user3 = User("Tom")
	//다 가능하다.
```

```java
	class User constructor(val name: String,
		val age: Int,
		vla birthday: String) {
		init {
			println("create User Instance")
		}
	}

	fun main(args: Array<String>) {
		val user = User("Tom", 22, "0602")
	}

	//main을 실행시키면 create User Instance라는 구문이 출력됩니다.

```

```java
	class User constructor(val name: String,
		val age: Int,
		val birthday: String) {
		init {
			println("create User Instance")
		}

		constructor (name: String) {
			println("name $name")
		}

		constructor(name: String, age: Int) {
			println("name $name, age $age")
		}

		}
		// 위와 같은 코드가 될까?
```











안됩니다.
이유 : 부 생성자는 명시적으로 주 생성자를 호출해줘야합니다.
primary constructor called expected
단, 주 생성자가 디폴트로 선언되었다면, 생략 가능 합니다.
```java
	class User constructor(val name: String,
		val age: Int,
		val birthday: String) {
		init {
			println("create User Instance")
		}

		constructor (name: String): this(name, 22, "0602") {
			println("name $name")
		}

		constructor(name: String, age: Int): this(name, age, "0602") {
			println("name $name, age $age")
		}

		}

		//부 생성자에는 val/var을 쓸 수 없다. 
		//즉, 부생성자에서는 필드를 선언할 수 없습니다.
		//부 생성자를 시작하는 구문은 constructor
		//부 생성자에는 별도의 코드를 추가할 수 있습니다.
		// init블록은 주생성자의 초기화 블럭이므로, 부 생성자에서는 쓸 수 없다.
	

	fun main(args: Array<String>) {
		val user = User("Tom")
	}

	>> create User Instance
	>> name Tom
```

주 생성자에서 필드를 val로 선언했다면 실제로 부생성자에서 필드를 초기화 할 수는 없습니다.

프로퍼티를 초기화하는 식이나, 초기화 블록 안에서만 주 생성자의 파라미터를 참조 할 수 있습니다.

```java
	/*자바*/
	public class User{
		private String name;
		private int age;
		private String birthday;

		public User(String name) {
			this.name = name;
		}
	}

	class TwitterUser extends User {
		private String name;
		private int age;
		private String birthday;

		TwitterUser(String name, int age, String birthday) {
			super(name);
			this.age = age;
			this.birthday = birthday;
		}

	}
	

	/*코틀린*/
	open class User(val name: String)

	class TwitterUser(val name: String,
			val age: Int,
			val birthday: Sring): User(name) //이렇게 안된다.

	// 상속하는 클래스가 있다면, 부모클래스의 생성자를 명시적으로 호출해야 한다.
	//클래스를 상속할 때에는 클래스 이름뒤에 괄호가 붙습니다.

	class TwitterUser: User 
	// 하위 클래스에 주 생성자가 없이 부모 클래스의 생성자를 부르지 못합니다.

	class TwitterUser: User {
		constructor(name: String): this(name, 0) 
		constructor(name: String, age: Int): super(name)
	}
	// 하위 클래스에 주 생성자가 없고 부생성자만 있을 경우,
	// 모든 부생성자는 다른 생성자에게 위임하거나 부모 생성자를 불러야 한다.
```

클래스의 인스턴스화를 막고싶다면, 생성자의 가시성을 private로 설정해주세요.

```java
	/*자바*/
	public class User {
		private Stirng name;
		private int age;

		private User(String name, int age) {
			this.name = name;
			this.age = age;
		}
	}

	/*코틀린*/
	class User private constructor() {...}

	class User private constructor(val name: String) {
		constructor(name: String, age: Int): this(name)
	} 

	>> val user = User("Tom", 22)
	//된다.
	//인스턴스화를 완전히 봉인 시키려면 모든 부생성자의 가시성도 private로
	// 설정해주어야 한다.
```
[참고문헌](https://thdev.tech/kotlin/2017/03/09/Kotlin-Constructor-Init.html)


# 자바의 정적팩토리메소드
> 객체 생성을 캡슐화 하는 기법
> static 메소드인데 객체 생성 역할을 하는 메소드.

java-effective
	규칙1. 생성자 대신 정적 팩토리 메소드를 사용할 수 없는지 생각해 보라.

* 장점
	1. 이름이 있으므로 생성자에 비해 가독성이 좋다.
	2. 호출할 때마다 새로운 객체를 생성할 필요가 없다.
	3. 하위 자료형 객체를 반환할 수 있다.
	4. 형인자 자료형 객체를 만들 때 편리하다.
* 단점
	1. 정적 팩토리 메소드만 있는 클래스라면, 생성자가 없으므로 하위 클래스를 만들지 못한다.
	2. 정적 팩토리 메소드는 다른 정적 메소드와 잘 구분되지 않는다.

**가독성이 좋다**
```java
class Character {

    int intelligence, strength, hitPoint, magicPoint;

    public Character(int intelligence, int strength, int hitPoint, int magicPoint) {
        this.intelligence = intelligence;   // 지능
        this.strength = strength;           // 힘
        this.hitPoint = hitPoint;           // HP
        this.magicPoint = magicPoint;       // MP
    }

    // 정적 팩토리 메소드
    public static Character newWarrior() {
        return new Character(5, 15, 20, 3);     // 전사는 힘과 HP가 높다
    }

    // 정적 팩토리 메소드
    public static Character newMage() {
        return new Character(15, 5, 10, 15);    // 마법사는 지능과 MP가 높다
    }
}

```

만약 생성자를 사용해 전사나 마법사를 생성한다면 다음과 같을 것이다.

```java
Character warrior = new Character(5, 15, 20, 3);
Character mage = new Character(15, 5, 10, 15);
----------------------------------------------

Character warrior = Character.newWarrior();
```
==이런점을 코틀린에서는 파라미터에 이름을 쓸 수 있게 함으로서 해결했다.==

```java
	Character warrior = Character(
	intelligence = 5,
	strength = 15,
	htiPoint = 20,
	magicPoint = 3)
```

**매번 새로운 객체 생성x** 
_flyweight 패턴과 비슷하다._
위와 같이 정적 팩토리 메소드를 호출하면 매번 new연산자를 통해서 캐릭터 객체를 새롭게 생성하게 된다.
기존의 객체를 반환하는 형식으로 이러한 문제점을 줄일 수 있다.

**정적 팩토리 메소드가 구현되어 있는 클래스를 상속하는 하위 클래스의 객체를 반환할 수 있다.**

```java
	public class User {
		private String name;
		private int age;
		private Map<String, Integer> twitterUserList = new HashMap<>();
		private Map<String, Integer> facebookUserList = new HashMap<>();


		public User() {

		}

		public User(String name) {
			this.name = name;
		}

		public User(String name, int age) {
			this.name = name;
			this.age = age;
		}


		public TwitterUser(String name, int age) {
			this.name = name;
			this.age = age;
		} // 생성자의 이름을 바꿀 수 없다. 

		public FacebookUser(String name, int age) {
			this.name = name;
			this.age = age;
		} // 생성자의 이름을 바꿀 수 없다.

		public static TwitterUser(String name, int age) {
			User user = twitterUserList.get(name);
			if (user == null) {
				user = new User(name, age);
			} 

			return user;
		} 

		public static FacebookUser(Stringn ame, int age) {
			User user = facebookUserList.get(name);
			if (user == null) {
				user = new User(name, age);
			} 

			return user;
		}

	}
```

## factory 패턴
> 다른 객체를 생성하는 역할을 하는 클래스
```java
	interface Dog {
		public void speak();
	}

	class Poodle implements Dog {
		private Poodle() { ... }
		public void speak() {
			System.out.println("The poodle says");
		}
	}

	class Rottweiler implements Dog {
		private Rottweiler() { ... }
		public void speak() {
			System.out.println("The Rottweiler says");
		}
	}

	class DogFactory {
		public static Dog getDog(String criteria) {
			if (criteria.equals("samll")) return new Poodle();
			else if (criteria.equals("big")) return new Rottweiler();

			return null;
		}
	}

	public class Main {
		public static void main(String[] args) {
			Dog dog = DogFactory.getDog("small");
			dog.speak();

			dog = DogFactory.getDog("big");
			dog.speak();
		}
	}
```

## fly-weight 패턴
> 수많은 작은 객체를 생성해야 할 때 
> 사용되는 많은 객체의 생성 관리하는 객체를 따로 두어 이를 통해 필요한 객체를 참조형태로 사용하도록.
> 메모리 사용량을 최소화 하도록.

데이터를 공유 사용하여 메모리를 절약할 수 있는 패턴이다.
객체를 매번 생성해서 사용하지 않고 공유한다.
한번 생성된 객체는 두 번 생성되지 않고 풀에 의해서 관리 및 사용된다.


```JAVA
	public class Flyweight {
		private String data;

		public FlyWeight(String data) {
			this.data = data;
		}

		public String getData() {
			return data;
		}

	}// 객체를 생성하는 클래스

	public class FlyweightFactory {
		Map<String, Flyweight> pool;

		public FlyweightFactory() {
			pool = new TreeMap();
		}

		public Flyweight getFlyweight(String key) {
			Flyweight flyweight = pool.get(key);

			if (flyweight == null) {
				flyweight = new Flyweight(key);
			} //pool 안에 해당 객체가 없다면 새로 생성한다.

			else {
				System.out.println("재사용")
			}	// pool안에 해당 객체가 있다면 그 객체를 반환한다.

			return flyweight;
		}
	} // Flyweight 객체를 관리하는 곳.

	public class Main {
		public static void main(String[] args) {
			FlyweightFactory factory = new FlayweightFactory();

			Flyweight flyweight = factory.getFlyweight("A");

			System.out.pirntln(flyweight);
		}
	}

```

# 내부클래스와 중첩클래스
중첩클래스를 알기위해서는 내부클래스에 대해서 먼저 알아야 합니다.

**자바의 이너클래스**
	: 안쪽 클래스를 인스턴스 변수처럼 사용하기 위해 사용한다.

외부 클래스에서 내부 클래스를 사용하려면 바깥 클래스의 객체를 사용하여 안쪽 클래스를 객체화하고 그 객체로 내부 클래스의 자원을 사용한다.

내부 클래스에서는 외부 클래스의 자원을 직접 사용가능합니다.
```java
	public class outer {
		public class Inner{

		}
	}

	>> outer out = new outer();
		outer.Inner in = out.new Inner();
		//in을 통해서 사용한다.
```

**자바의 중첩클래스**
	: 내부의 클래스를 static 변수 처럼 사용합니다.

외부 클래스의 static이 아닌 영역은 사용할 수 없습니다.
```java
	class Outer {
		static class Nested {
			static int k;
			public static void inMethod() {...}
		}
	}

	>> Nested.k = 10;
	>> Nested.inMethod();
```

**내부 클래스와 중첩 클래스의 차이점**

```java
	interface State: Serializable  

 	interface View {  
 		fun getCurrentState(): State  
 		fun restoreState(state: State) {}
 	}

 /*자바*/
 	public class Button implements View {
 		@Override
 		pubic State getCurrentState() {
 			return new ButtonState();
 		}

 		@Override 
 		public void restoreState(State state) {...}
 		public class ButtonState implements State {...} // 내부 클래스
 	}

 // 버튼의 상태를 직렬화하면 
 // java.io.NotSerializableException: Button 오류가 발생한다.		
```

자바에서 내부클래스는 암묵적으로 외부 클래스에 대한 참조를 포함합니다.
	외부 클래스인 Button을 직렬화 할 수 없기 때문에, Button에 대한 참조가 ButtonState의 직렬화를 방해하게 됩니다.

그래서 자바에서는 이러한 문제를 내부 클래스를 static으로 선언하는 것으로 해결합니다.
	-> 외부 클래스에 대한 암묵적인 참조가 사라집니다.

```java
	public static class ButtonState implements State{ }
```

**코틀린**
코틀린에서는 내부에 정의된 클래스 앞에 아무런 변경자가 붙어있지 않으면 자바의 중첩클래스와 같은 기능을 합니다.

외부 클래스에 대한 참조를 포함하고싶고, 자바의 이너클래스와 같은 기능을 하고싶다면 클래스 앞에 inner변경자를 붙이면 됩니다.

```java
class Button : View {
			override fun getCurrentState(): State = ButtonState()
			override fun restoreState(state: State){...}
			inner class ButtonState : State {...} // 내부 클래스
		}
```

내부 클래스에서 외부 클래스의 참조에 접근하려면
```java
class Outer {
	private val a: Int = 1
	fun funName(): Int {...}
		inner class Inner {
			val a = this@Outer // Outer's this
			val b = this@Inner // Inner's this
			val c = this@Outer.a // 외부 클래스의 필드에 접근
			val d = this@Outer.funName() // 외부 클래스의 메소드에 접근
			fun getOuterReference(): Outer = this@Outer
		}
	}

	>> val demo = Outer().Innter().getOuterFeference()
```
