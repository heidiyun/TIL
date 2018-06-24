# 4장. 클래스, 객체, 인터페이스
## 4.1 클래스 계층 정의
> 코틀린에 새로 도입한 sealed변경자에 대해서 
> 코틀린 가시성/ 접근 변경자와 자바의 가시성/접근 변경자의 차이점에 대해서
### 4.1.1 코틀린 인터페이스
> 코틀린 인터페이스는 자바 8 인터페이스와 비슷하다.
	default 메소드를 정의할 수 있다.
	필드는 선언할 수 없다.

* 인터페이스를 구현하는 클래스는 인터페이스의 추상메소드를 구현해야 한다.

``` java
	interface Clickable {
		fun click()
	}

	/* 자바에서 인터페이스 구현하기*/
	class Button implements Clickable {
		@Override
		void click() {
		System.out.println("I was clicked");
		}
	}
	
	/* 코틀린에서 인터페이스 구현하기*/
	class Button : Clickable {
		override fun click() = println("I was clicked")
		// oveeride 변경자를 꼭 사용해야 한다.
	}
```
* 자바에서는 클래스를 상속할 경우 extends 키워드를 사용하지만, 
	코틀린에서는 콜론(:)으로 상속과 인터페이스 구현을 모두 처리한다.
* 자바에서와 마찬가지로 상속은 하나의 클래스만, 인터페이스는 갯수 상관없이 구현 가능하다.
* 자바에서는 @Override 애노테이션을 생략할 수 있었지만, 코틀린에서는 생략이 불가능하다.
	* 부모 클래스에 있는 메소드와 시그니처(메소드 이름, 파라미터 갯수와 타입)가 같은 메소드를 하위 클래스에서 선언하는 경우를 방지하기 위함이다.

``` java
	interface Clickable {
		fun click()
		fun showOff() = println("I'm clickable")
	}
	interface Focusable {
		fun showOff() = println("I'm focusable!")
	}
```
한 클래스가 위의 두 인터페이스를 모두 구현할 경우, showOff 메소드를 호출하면 어느 쪽의 함수도 호출 되지 않는다.
showOff 메소드를 오버라이딩한 메소드를 작성하지 않으면, 다음과 같은 컴파일 오류가 발생한다. 
```
	The class must override public open fun showOff() 
	because it inherits many implementations of it.
```
* 코틀린 컴파일은 중복된 메소드를 오버라이딩 하도록 강제한다.
``` java
	class Button : Clickable, Focusable {
		override fun click() {
			println("I am clicked")
		}
		override fun showOff() {
			super<Clickable>.showOff()
			super<Focusable>.showOff()
			// 상위 클래스의 showOff를 명시적으로 호출한다.
		}
	}
	/* 자바에서 상위 클래스 메소드 호출하기 */
	class Button implements Clickable, Focusable {
		@Override void showOff() {
			Clickable.super.showOff()
			Focusable.super.showOff()
		}
	}
```

### 4.1.2 open, final, abstract 변경자: 기본적으로 final
* 취약한 기반 클래스 
	:	 클래스를 상속하는 방법에 대한 정확한 규칙을 제공하지 않는다면 기반 클래스의 작성 의도와 다른 방식으로 메소드를 오버라이드할 위험성이 있다. 
		기반 클래스를 변경하는 경우 하위 클래스의 동작이 예기치 않게 바뀔 수도 있다는 면에서 기반 클래스는 취약하다.

* java effective "상속을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 상속을 금지하라" 라고 말한다.
	* 오버라이드하게 의도된 클래스와 메소드가 아니라면 모두 final로 만들어라.

* 코틀린에서 상속을 허용하려면 클래스, 메소드와 프로퍼티 앞에 'open' 변경자를 붙여야 한다.

``` java
	open class RichButton : Clickable {
		fun disable() {}	// 하위 클래스가 이 메소드를 오버라이딩 할 수 없다.
		open fun animate() {}	// 하위 클래스가 이 메소드를 오버라이딩 할 수 있다.
		override fun click() {...}	// 오버라이드한 메소드는 기본적으로 열려있다. 
								// 즉, 오버라이딩 할 수 있다.
	}
	// 오버라이드한 함수를 오버라이딩 하지 못하게 금지하려면 메소드 앞에 final을 붙여라.
	final override fun click() {...}
```
* 클래스의 기본적인 상속 가능 상태를 final로 함으로서 스마트 캐스트가 가능하다는 이점이 있다.
	* 스마트 캐스트는 변경할 수 없는 변수에만 적용이 가능하다. 
	* 그러므로, 프로퍼티가 final이어야 한다는 뜻이다.  
	* 프로퍼티가 final이 아니라면 다른 클래스가 상속하면서 커스텀 접근자를 정의함으로서 스마트 캐스트의 요구 사항을 깰 수 있다.

``` java
	abstract class Animated {
		abstract fun animate() // 추상 함수는 기본적으로 열려있다.
		open fun stopAnimating()
		fun animateTwice()
	}
```
* 인터페이스에는 final, open, abstract을 사용하지 않는다.
	* 인터페이스 멤버는 항상 열려있으며 final로 변경할 수 없다.
	* 구현부분이 없어도 abstract키워드를 붙일 필요가 없다.

### 4.1.3 가시성 변경자 : 기본적으로 공개
> 가시성 변경자는 클래스 외부 접근을 제어한다.
* 자바
	: public, protected, default, private
	 자바의 기본 가시성 변경자는 default이다.
	 (같은 패키지 내에서만 접근가능하다.)
* 코틀린
	: public, internal, protected, private
	코틀린의 기본 가시성 변경자는 public이다.
	(모든 곳에서 접근할 수 있다.)

차이점
* 자바의 default(패키지 전용)은 코틀린에 없다. 
* 코틀린은 패키지를 네임스페이스를 관리하기 위한 용도로만 사용한다.

```
	네임스페이스란?
		하나의 이름 공간에서는 하나의 이름이 단 하나의 개체만을 가르키게 된다.
```

* internal이라는 새로운 가시성 변경자가 도입됐다.
 (모듈 내부에서만 접근 할 수 있다.)
```
 모듈이란?
	 한 번에 한꺼번에 컴파일되는 코틀린 파일들
```
자바에서는 패키지가 같은 클래스를 선언하기만 하면 어떤 프로젝트의 외부에 있는 코드라도 패키지 내부에 있는 패키지 전용 선언에 쉽게 접근할 수 있어서 모듈의 캡슐화가 쉽게 깨진다.

* 코틀린에서는 클래스, 함수, 프로퍼티 등에 private 가시성을 허용한다.
	(protected는 불가능)

* 자바에서는 protected가 같은 패키지 내에서 자유롭게 접근가능했지만, 코틀린에서는 해당 클래스나 하위 클래스에서만 접근가능하다.

| 변경자    | 클래스 멤버                       | 최상위 선언                      |
|-----------|-----------------------------------|----------------------------------|
| public    | 모든 곳에서 볼 수 있다.           | 모든 곳에서 볼 수 있다.          |
| internal  | 같은 모듈 안에서만 볼 수 있다.    | 같은 모듈 안에서만 볼 수 있다.   |
| protected | 하위 클래스 안에서만 볼 수 있다.  | (최상위 선언에 적용할 수 없다.)  |
| private   | 같은 클래스 안에서만 볼 수 있다.  | 같은 파일 안에서만 볼 수 있다.   |

_규칙_
	1. 클래스의 가반 타입 목록에 들어있는 타입이나 제네릭 클래스의 타입 파라미터에 들어있는 타입의 가시성은 그 클래 자신의 가시성과 같거나 더 높아야 한다.
	2.  메소드의 시그니처에 사용된 모든 타입의 가시성은 그 메소드의 가시성과 같거나 더 높아야 한다.

``` java
	fun class.funName() {...}
		// class에 쓰이는 클래스는 해당 함수보다 가시성이 높거나 같아야 한다.
	class Sample : Generic<GenericType>
	-> GenericType에 들어가는 클래스는 해당 클래스보다 가시성이 높거나 같아야 한다.
	
	class Sampel : class
	-> 기반 클래스는 해당 클래스보다 가시성이 높거나 같아야 한다.

```

* 자바와 코틀린의 변경자 상호운용성
	* 코틀린에서 private로 선언된 클래스는 자바 바이트 코드 안에서 default(package)로 컴파일 된다.
	* 코트린의 internal은 자바에서 public으로 변경된다.
```
	문제점  :
		- 다른 모듈에 정의된 internal 클래스나 internal 최상위 선언을 
		  모듈 외부의 자바 코드에서 접근할 수 있다. 
		- 코틀린에서 protected로 정의한 멤버를 코틀린 클래스와 
		  같은 패키지에 속한 자바 코드에서 접근 할 수 있다.
	해결 : 
		코틀린 컴파일러가 internal멤버의 이름을 맹글링한다.
		1. 모듈에 속한 어떤 클래스를 모듈 밖에서 상속하는 경우, 
		   하위 클래스 내부의 메소드 이름이 우연히 상위 클래스의 internal메소드와 같아져서 
		   오버라이드하는 경우 방지
		2. internal클래스를 모듈 외부에서 사용하는 일을 막기 위함.
```
		
### 4.1.4 내부 클래스와 중첩 클래스
> 내부 클래스를 캡슐화하거나 코드 정의를 사용부분과 가까이 두고싶을 때 유용하다.
* 자바와 차이점	
	* 중첩 클래스는 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다.
		(명시적으로 접근권한을 요청해야 한다.)
	
	``` java
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
	직렬화하려는 변수는 ButtonState 타입의 state였는데, Button을 직렬화할 수 없다는 이유는?
	* 자바에서 내부클래스는 암묵적으로 외부 클래스에 대한 참조를 포함한다.
		* Button을 직렬화 할 수 없으므로 Button에 대한 참조가 ButtonState의 직렬화를 방해한다.
	* 내부 클래스를 static으로 선언하면 외부 클래스에 대한 암묵적인 참조가 사라진다.
	
``` java
		class Button : View {
			override fun getCurrentState(): State = ButtonState()
			override fun restoreState(state: State){...}
			class ButtonState : State {...} // 중첩 클래스
		}
```
코틀린에서 내부에 정의된 클래스 앞에 아무런 변경자가 붙어 있지 않으면 자바의 static 중첩 클래스와 같은 기능을 한다.
외부 클래스에 대한 참조를 포함하고 싶다면 inner변경자를 붙이면 된다.
``` java
	class Button : View {
			override fun getCurrentState(): State = ButtonState()
			override fun restoreState(state: State){...}
			inner class ButtonState : State {...} // 내부 클래스
		}
```

내부 클래스에서 외부 클래스 참조에 대해서 접근하려면 아래와 같이 쓴다.
``` java
	class Outer {
		inner class Inner {
			fun getOuterReference(): Outer = this@Outer
		}
	}
```

### 4.1.5 봉인된 클래스 : 클래스 계층 정의 시 계층 확장 제한
> sealed 클래스

부모 클래스에 sealed변경자를 붙이면 해당 클래스를 상속하는 자식 클래스를 정의할 때,
반드시 부모 클래식 안에 중첩시켜야 한다.

``` java
	sealed class Expr {
		class Num(val value: Int) : Expr()
		class Num(val left: Expr, val right: Expr) : Expr()
	}
	fun eval(e: Expr): Int = 
	when (e) {
		is Expr.Num -> e.value
		is Expr.Sum -> eval(e.right) + eval(e.left)
		// else 분기가 필요 없다. 모든 하위 클래스를 검사하므로.
		// 원래는 상속하는 하위 클래스를 모두 검사했다는 것을 확신 할 수 없으므로 
		// 그 외를 처리하는 else분기를 넣어줬다.
	}
```

sealed 클래스는 자동적으로 open이다.

## 4.2 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언
> 코틀린은 주 생성자와 부 생성자를 구분한다.

### 4.2.1. 클래스 초기화 : 주 새성자와 초기화 블록
* 클래스 이름 뒤에 오는 괄호로 둘러싸인 코드가 주 생성자이다.
```java
 class User(val name: String, val age: Int)
```

* construct 키워드는 주 생성자나 부 생성자 정의를 시작할 때 사용한다.
	*  주 생성자는 별도의 코드를 포함할 수 없으므로 init 블록이 필요하다.
* init 키워드는 초기화 블록을 시작한다.  (클래스가 인스턴스화 될 때 실행될 초기화 코드가 들어감)
	* 초기화 블록은 주 생성자와 함께 사용된다.
```java
	 class User(_nickname: String) {
		 val nickname: String
	init {
		nickname - _nickname
	}

	or
	
	class User(nickname: String) {
		val nickname: String
	
		init {
			this.nickname = nickname
		}
	}
```

* nickname프로퍼티를 초기화하는 코드를 nickname 프로퍼티 선언에 포함시킬 수 있어서 초기화 코드를 초기화 블록에 넣을 필요가 없다. 
*  주 생성자 앞에는 애노테이션이나 가시성 변경자가 없다면 construct를 생략해도 된다.

```java
	class User(_nickname: String) {
		val nickname = _nickname
	}
```
* 프로퍼티를 초기화 하는 식이나, 초기화 블록 안에서만 주 생성자의 파라미터를 참조할 수 있다.

```java
	class User(val nickname: String)
	
	// 프로퍼티의 디폴트 값 정의 가능
	class User(val nickname: String = "")
	
```
* 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야 할 필요가 있다.
```java
	open class User(val nickname: String)

	class TwitterUser(nickname: String) : User(nickname)
```

* 클래스를 정의할 때 별도의 생성자를 정의하지 않으면 인자가 없는 디폴트 생성자를 만들어준다.
	* 생성자가 없는 클래스의 하위클래스는 명시적으로 부모 클래스의 생성자를 호출해야 한다.
```java
		open class Button
		class RadioButton: Button()
		// 클래스를 상속할 때는 꼭 클래스 이름뒤에 괄호를 붙여야 한다.
```

* 클래스의 인스턴스화를 막고 싶다면 모든 생성자를 private로 만들면 된다.
```java
 class User private constructor() {...}
```

### 4.2.2 부 생성자
> 자바의 생성자 오버로딩은 코틀린에서 디폴트 파라미터 값과 이름 붙은 인자 문법을 사용해 해결할 수 있다.

```java
	open class View {
		constructor(ctx: Context) { ... }
		constructor(cts: Context, attr: AttributeSet) { ... }
	}
class MyButton : View {
	constructor(ctx: Context) : this(ctx, MY_STYLE) { ... }
	// 생성을 같은 클래스의 다른 생성자에게 위임한다.
	constructor(ctx: Context, attr: AttributeSet): super(ctx, attr) { ... }
	// 위임받은 생성자는 부모 클래스의 생성자를 호출한다.
}
```
* 클래스에 주 생성자가 없다면 모든 부 생성자는 반드시 부모 클래스의 생성자를 호출하거나,
   다른 생성자에게 생성을 위임해야 한다.

* 부 생성자가 필요한 이유는 자바와의 상호운용성이다.

### 4.2.3 인터페이스에 선언된 프로퍼티 구현
> 코틀린에서는 인터페이스에 추상 프로퍼티 선언을 넣을 수 있다.
 인터페이스는 상태를 저장할 수 없으므로 backing field를 참조할 수 없다.

```java
	interface User {
		val nicknmae: String
	}
```
* 인터페이스에 있는 프로퍼티는 backing field와 getter 등이 제공되지 않는다.
* 인터페이스에 선언된 프로퍼티는 하위 클래스에서 구현해야 한다.
```java
	interface User {  
	    val nickname: String  
	}  
  
	class PrivateUser (override val nickname: String) : User   
		// 인터페이스의 프로퍼티를 구현할 경우에는 oveeride 키워드를 꼭 붙인다.
		
	class SubscribingUser(val email: String) : User {  
	    override  val nickname: String  
	        get() = email.substringBefore('@')  
	}  
	  // 커스템 게터로 프로퍼티를 구현하여 backing field는 없다.
	  
	class FaceboockUser(val accountId: Int) : User {  
	    override val nickname = "Yun"  
	}
	// 초기화 식을 통해 구현했다.
```
* getter/setter를 가지는 프로퍼티를 선언할 수 있다.
```java
	interface User {
		val email: String // 구현 클래스에서 overriding이 필수.
		val nickname: String
			get() = email.substringBefore('@')
				// 구현 클래스에서 overriding이 안해도 됨.
				// backing field는 없고, 매번 결과를 계산해 반환해준다.
	}
```

### 4.2.4 게터와 세터에서 뒷받침하는 필드에 접근
> backing field가 있고, getter/setter에 접근할 때마다 정해진 로직을 실행하는 프로퍼티를 정의해보자.

```java
	class User(val nameL String) {
		var address: String = "unspecified"
			set(value: String) {
				println(""" Address was changed for $name:
				"$field" -> "$value".""".trimIndent())
				field = value
			}
	}

	>> val user = User("Heidi")
	>> user.address = "happydong 47" // setter를 호출한다.

```
* getter에서는 field값을 읽을 수만 있고, setter에서는 field의 값을 읽거나 쓸 수 있다.

### 4.2.5 접근자의 가시성 변경
* getter/setter의 가기성은 기본적으로 프로퍼티의 가시성과 같다.
  getter/setter앞에 가시성 변경자를 추가하여 가시성을 변경할 수 있다.

```java
	class LengthCounter {
		var counter: Int = 0
			private set // 클래스 외부에서 counter의 setter에 접근할 수 없다.

			fun addWord(word: String) {
				counter += word.length // 해당 함수를 통해 word를 add할 경우에만 counter를 변경할 수 있도록 하였다.
			}

		}
```