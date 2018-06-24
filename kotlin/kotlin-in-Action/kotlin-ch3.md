
# 함수 정의와 호출
## 3.1 코틀린에서 컬렉션 만들기

``` java
	val set = hashSetOf(1, 7, 53)
	val list = arrayListOf(1, 7, 53)
	val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
```

* 각각의 객체가 어떤 클래스일까?
``` java
		set.javaClass
		>> java util.HashSet
		list.javaClass
		>> java.utio.ArrayList
		mpa.javaClass
		>> java.util.HashMap
```
* 이를 통해 코틀린은 기존 자바 컬렉션을 활용한다는 것을 알 수 있다.
	  (코틀린만의 자바 컬렉션 라이브러리를 제공하지 않는다.)

* 표준 자바 컬렉션을 활용하여 자바 코드와의 상호운용성이 극대화 되었다.

* 단, 코틀린 컬렉션이 자바에서보다 더 많은 기능을 지원한다.

## 3.2 함수를 호출하기 쉽게 만들기
* 자바에서는 기존 함수를 수정하기 위해서 Guava, Apache Commons와 같은 서드파티 프로젝트를 추가하거나 직접 관련 로직을 수정해야 한다.

* 코틀린은 위와 같은 일을 처리할 수 있는 표준 라이브러리를 제공한다.

```
	val list = listOf(1, 2, 3)
	println(list)
	>> [1, 2, 3]
```
> 출력의 형태를 바꾸고 싶다. (toString 바꾸기)

* 코틀린의 기능을 사용하지 않고 구현한 코드

``` java
	fun <T> joinToString(collection: Collection<T>,
                     separator: String, 
                     prefix: String,
                     postfix: String): String {
    val result = StringBuilder(prefix)
    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}
	fun main(args: Array<String>) {
		val list = listOf(1, 2, 3)
		println(joinToString(list, ";", "(", ")"))
	}
```

### 3.2.1 이름 붙인 인자
* main함수에서 joinToString을 호출 할 때, 파라미터의 역할을 구분할 수 없다.
	* 자바에서는 각 파라미터 앞에 주석으로 파라미터의 이름을 명시하거나 enum을 사용할 것을 요구한다.
	* 코틀린에서는 함수에 전달하는 인자의 이름을 명시할 수 있도록 설계되어 있다.

``` java
		joinToString(collection, separator=";", prefix = "(", postfix = ")")
```
* 단, 파라미터 중 어느 하나라도 이름을 명시한다면 그 뒤의 모든 파라미터에는 이름을 꼭 명시해야 한다.

### 3.2.2 디폴트 파라미터 값
* 자바의 일부 클래스에서 오버로딩한 메소드가 많아진다는 문제가 있다.
* 코틀린에서는 함수 선언에서 파라미터의 디폴트 값을 지정할 수 있다.
``` java
		fun <T> joinToString(collection: Collection<T>,
		 	separator: String = ",", 
		 	prefix: String = "",
		  	postfix: String ="") : String
```
* 위와 같이 코드를 수정하게 되면, 반드시 모든 인자를 다 쓸 필요가 없다.
``` java
	println(joinToString(list))
	>> 1, 2, 3
	println(joinToString(list,";")
	>> 1; 2; 3
	println(joinToString(list, postfix = ";", prefix = "#")
	>> # 1, 2, 3;
```
* 코틀린 함수에 '@JvmOverloads' 라는 어노테이션을 추가하면 자동으로 오버로딩한 자바 메소드를 추가해준다.
*
### 3.2.3 정적인 유틸리티 클래스 없애기 : 최상위 함수와 프로퍼티
* 자바에서는 모든 함수가 클래스 안에 위치해야 한다.
	* 특별한 상태나 인스턴스 메소드가 없이 정적 메소드를 모아두기만 하는 클래스가 생긴다.
* 코틀린에서는 함수를 소스 파일의 최상위 수준, 클래스의 밖에 위치시킬 수 있다.
	* 다른 패키지에서 해당 함수를 사용하고 싶다면, 함수가 정의된 패키지를 import하면 된다.
``` java
/* 코틀린 */
	// @file:JvmName("StringFunctions")
	// : 이 구문을 추가하면 ""안에 있는 이름으로 클래스가 만들어진다.
	package strings
	fun joinToString(...): String {...}


/* 자바 */
	package strings;
	public class JoinKt {
	public static String joinToString(...) {...}
	} // 코틀린의 joinToString을 컴파일한 결과. 
	  // 함수가 들어 있는 코틀린 소스 파일의 이름과 대응한다.
	
	package string;
	import strings.JoinKt;
	JoinKt.joinToString(list, ",", "", "");

```
* 프로퍼티도 파일의 최상위 수준에 놓을 수 있다.
	*  최상위 수준의 프로퍼티가 자바에서 컴파일 되면, getter/setter가 생긴다.
		* 상수처럼 보이는 필드를 getter/setter를 사용하는 것은 이상하다.
``` java
			const val LINE = "\n"
			//앞에 const 변경자를 추가하면 public static final로 컴파일된다.
```
## 메소드를 다른 클래스에 추가 : 확장 함수와 확장 프로퍼티
* 확장 함수 
	* 기존 자바 API를 재작성하지 않고도 코틀린이 제공하는 여러 편리한 기능을 사용할 수 있게 해준다.
	* 어떤 클래스의 멤버 메소드인 것처럼 호출 할 수 있다. 그러나 해당 클래스 밖에 선언된 함수다.
	*  확장 함수 만드는 법
		함수 이름 앞에 그 함수가 확장할 클래스의 이름을 붙인다.
``` java
	package strings
	fun String.lastChar(): Char = this.get(this.length -1)
	// this 생략 가능
	// 덧붙인 클래스 이름은 수신 객체 타입 (receiver type)
	// 확장 함수가 호출되는 대상이 되는 값(객체)는 수신 객체(receiver object) : this
	
	println("Kotlin".lastChar())
	>> n
	//"Kotlin"이 수신 객체이다.
```
* 자바 클래스로 컴파일한 클래스 파일이 있는 한 확장 함수를 추가할 수 있다.
* 확장 함수의 내부에서 수신 객체의 메소드나 프로퍼티를 바로 사용할 수 있다.
* private와  protected로 선언된 멤버에는 접근할 수 없다.

### 3.3.1 임포트와 확장 함수
* 확장 함수를 자동으로 프로젝트 안의 모든 소스코드에서 사용가능한 것이 아니다.
	* 확장 함수를 import해주어야 한다.
``` java
	import strings.lastChar
	val c = "Kotlin".lastChar()

	import strings.lastChar as last 
	// as를 사용하면 as 뒤의 단어로 import한 함수를 부를 수 있다.
	// 다른 패키지에 이름이 같은 함수가 있을 때 사용하면 편리하다.
	// 이름 충돌을 해결하는 유일한 방법이다.
	val c  = "kotlin".last()
```

### 3.3.2 자바에서 확장 함수 호출
* 내부적으로 확장 함수는 수신 객체를 인자로 받는 정적 메소드다.
	* 확장 함수를 호출해도 다른 어댑터 객체나 실행 시점 부가 비용이 들지 않는다.
* 자바에서 확장 함수를 사용하려면,
	* 정적 메소드를 호출하면서 첫 번째 인자로 수신 객체를 넘기면 된다.
	* 확장 함수가 들어있는 자바 클래스 이름도 확장 함수가 들어있는 파일 이름에 따라 결정된다.
	``` java
	/*자바*/
		//lastChar함수가 StringUtil.kt에 정의되어 있다면
		char C = StringUtilKt.lastChar("java")
	```
	
### 확장 함수로 유틸리티  함수 정의
``` java
	fun <T> Collection<T>.joinToString(
		separator: String = ",",
		prefix: String = "",
		postfix: String = ""
		): String {
		val result = StringBuilder(prefix)
		
		for ((index, element) in this.withIndex()) {
			if(index > 0) result.append(separator)
			result.append(element)
		}
		result.append(postfix)
		return result.toString()
	
	---------------------------------------------------------------------------
	val list = listOf(1, 2, 3)
	println(list.joinToString(separaotr = ";", prefix = "(", postfix = ")")
	>> (1; 2; 3)
``` 

### 3.3.4 확장 함수는 오버라이드할 수 없다.
* 수신 객체로 지정한 변수의 정적 타입에 의해  어떤 확장 함수가 호출될 지 결정된다.
	* button 클래스가 view 클래스를 상속한다고 가정하자.
	``` java
		fun View.showOff() = println("I'm a View!")
		fun Button.showOff() = println("I'm a button!")
		val view: View = Button() // 변수 view의 타입은 View이다.
		view.showOff()
		>> I'm a View! // 수신 객체가 View로 설정된 확장 함수가 호출된다.
	```

* 확장 함수와 클래스의 멤버 함수의 이름, 파라미터가 같다면 멤버 함수가 우선적으로 호출된다. 
	``` java
		class User {  
		    fun join (name: String): String {  
	        return name  
		    }	  
		}	  
		fun User.join(name: String): Int {  
		    return name.length  
		}  
		fun main(args: Array<String>) {  
		    val user = User()  
		    println(user.join("name"))  
		}
		>> name
	```

### 3.3.5 확장 프로퍼티
* 기존 클래스의 인스턴스 객체에 필드를 추가할 방법은 없다.
* 실제로 확장 프로퍼티는 아무 상태도 가질 수 없다.
``` java
	 val String.lastChar: Char
		 get() = get(length - 1) 
		 // backing field가 없기 때문에 getter는 꼭 정의해야 한다.
		 // 초기화의 결과 값을 저장할 장소가 없으므로 초기화도 할 수 없다.
```

* 변경 가능한 확장 프로퍼티 선언하기
``` java
	var StringBuilder.lastChar: Char // StringBuilder의 마지막 문자는 변경가능
	get() = get(length - 1)
	set(value: Char) {
	this.setCharAt(length - 1, value)
	// 마지막 인덱스의 값을 value로 바꿔준다.
```

* 확장 프로퍼티 사용하기
``` java
	println("Kotlin".lastChar)
	>> n
	
	val sb = StringBulider("Kotlin?")
	sb.lastChar = '!'
	println(sb)
	>> Kotlin!
```

* 자바에서 확장 프로퍼티를 사용하고 싶다면 ,
	* StringUtilKt.getLastChar("Java")와 같이 명시적으로 getter/setter를 호출해야 한다.
	

## 3.4 컬렉션 처리: 가변 길이 인자, 중위 함수 호출, 라이브버리 지원

### 3.4.1 자바 컬렉션 API 확장
> 코틀린 컬렉션은 자바와 같은 클래스를 사용하지만 더 확장된 API를 제공한다.
* 확장함수를 통해서 더 확장 된 API를 제공한다.

### 3.4.2 가변 인자 함수 : 인자의 개수가 달라질 수 있는 함수 정의
> 가변 길이 인자는 메소드를 호출할 때 원하는 개수만큼 값을 인자로 넘기면 자바 컴파일러가 배열에 그 값들을 넣어주는 기능이다. 

``` java
	/* 자바 가변 길이 인자 표기 방법 */
	public static void register(String...strings) {...}

	/* 코틀린 가변 길이 인자 표기 방법 */
	fun listOf<T>(vararg values: T): List<T> {...}
```

* 코틀린에서는 파라미터 앞에 'vararg' 변경자를 붙인다. 

``` java
	/* 자바 자변 길이 인자를 가지는 함수에 배열을 파라미터로 넘기기 */
	public class Main {     
	    public static void mian(String[] args) {  
		    String[] strings = new String[]{"hello", "hi", "how are you"};  
			register(strings);  
	  }  
      
	    private static void register(String... strings) {  
	        for (String s : strings) {  
	            System.out.println(s);  
			  }  
	    }  
	}
------------------------------------------------------------------------------------
	/* 코틀린 */
	fun main(args: Array<String>) {  
	    val strings = arrayOf("hello", "hi")  
	    register(*strings) 
	}  
  
	fun register(vararg strings: String) {  
	    for (i in strings)  
	        System.out.println(i)  
	}
```
* 자바에서는 배열을 이름으로 넘기면 된다.
* 코틀린에서는 배열의 각 원소가 인자로 전달되게 해야 한다.
	* 배열 앞에 * (스프레드 연산자)를 써주면 위와 같은 작업을 자동으로 해준다.

### 3.4.3 값의 쌍 다루기 : 중위 호출과 구조 분해 선언
* 중위 호출 시에는 수신 객체와 유일한 메소드 인자 사이에 메소드 이름을 넣는다.
	* 객체, 메소드 이름, 유일한 인자 사이에는 공백이 들어가야 한다.
``` java
	val map = mapOf(1 to "one", 2 to "two", 3 to "three")
	
	1.to("one") // to 메소드의 일반적 호출 방식
	1 to "one"	// to 메소드의 중위 호출 방식
```
* 인자가 하나뿐인 일반 메소드나 인자가 하나뿐인 확장 함수에 중위 호출을 사용할 수 있다.
* 함수를 중위 호출에 사용하게 허용하고 싶으면 _infix_ 변경자를 메소드 선언 앞에 추가해야 한다.
``` java
	infix fun Any.to(other: Any) Pair(this, other)
	// Pair의 인스턴스를 반환한다. 
	// Pair는 코틀린 표준 라이브러리 클래스로, 두 원소로 이뤄진 순서쌍을 표현한다.

	val (number, name) = 1 to "one"
	//두 변수를 즉시 초기화 할 수 있다.
```
* 위와 같은 기능을 구조 분해 선언(destructuring declaration)이라 한다.
``` java
	for ((index, element) in collection.withIndex())
	// withIndex를 구조 분해 선언과 조합하면 컬렉션 원소의 인덱스와 값을 따로 변수에 담을 수 있다.
```
* to 함수는 확장 함수다. 
	* 타입과 관계없이 임의의 순서쌍을 만들 수 있다.

## 3.5 문자열과 정규식 다루기
> 코틀린 코드가 만들어낸 문자열을 아무 자바 메소드에 넘겨도 되며, 
> 자바 코드에서 받은 문자열을 아무 코틀린 표준 라이브러리 함수에 전달해도 전혀 문제 없다.

### 3.5.1 문자열 나누기
* java의 split메소드로는 '.'을 사용해 문자열을 분리할 수 없다.
	* split의 구분 문자열은 실제로는 정규식이기 때문이다. 
	* 마침표는 모든 문자를 나타내는 정규식으로 해석된다.
```
	정규식(regular expression)?
		특정한 규칙을 가진 문자열의 집합을 표현하는데 사용하는 형식 언어이다.
		문자열의 검색과 치환을 위해 지원하고 있다.
```
* 코틀린은 split 확장 함수를 제공한다.
	* 정규식을 파라미터로 받는 함수는 String이 아닌 Regex 타입의 값을 받는다.
	* split 함수에 전달하는 값의 타입에 따라 정규식이나 일반 텍스트 중 어느 것으로 문자열을 분리하는지 쉽게 알 수 있다.

// 이후 부족한 내용 더 채울 것.

### 3.5.2 정규식과 3중 따옴표로 묶은 문자열
String 확장 함수를 사용해 경로 파싱해보자.
``` java
	fun parsePath(path: String) {
	val directory = path.substringBeforeLst("/")
	val fullName = path.substringAfterLast("/")
	val fileName = fullName.substringBeforeLst(".")
	val extension = fullName.substringAfterLast(".")
	println("Dir: $directory, name: $fileName, ext: $extension")
	}
	>> parsePath("/Users/yole/kotlin-book/chapter.adoc")
	Dir: /Users/yole/kotlin-book, name chapter, ext: adoc
```
코틀린은 정규식을 사용하지 않고 문자열을 쉽게 파싱할 수 있다.
정규식은 강력하지만 나중에 알아보기 힘든 경우가 많다.

정규식이 필요할 때는 코틀린 라이브러리를 사용하면 편하다.
``` java
	fun parsePath(path: String) {
		val regex = """(.+)/(.+)\.(.+)""".toRegex()
		val matchResult - regex.matchEntire(path)
		if (matchResult != null)
	}
```
## 3.6 로컬 함수와 확장 (중첩함수)
> 자바와 달리 코틀린은 함수 안에 함수를 생성할 수 있습니다.
* 코드가 중복 될 경우, 함수를 추출하는 경우 유용하게 사용할 수 있습니다.
	* 자바에서 메소드의 재활용을 위해 함수를 추출할 경우 작은 코드 조각이 무수히 많이 생겨 가독성이 떨어지는 경우가 있습니다.
``` java
class User(val id: Int, val name: String, val address: String)  
  
	fun saveUser(user: User) {  
	    if (user.name.isEmpty()) {  
	        throw IllegalArgumentException(
	        "Can't save user ${user.id}: empty Name")  
	    }  
  
	    if (user.address.isEmpty()) {  
	        throw IllegalArgumentException(
	        "Can't save user ${user.address}: empty Name")  
	    }  
	}

// 검증해야할 사항이 많이질수록 if문이 늘어난다.(보일러 플레이트 발생)
```
로컬 함수를 사용해 코드 중복을 줄이자.

``` java
	class User(val id: Int, val name: String, val address: String) 
	fun saveUser(user: User) {
		fun validate(user: User, value: String, fieldName: String) {
			if (value.isEmpty()) {
				throw IllegalArgumentException(
				 "Can't save user ${user.address}: empty Name")
			}
		}
		validate(user, user.name, "Name")
		validate(user, user.address, "Address")
	}
	
```
로컬 함수 각각에게 User객체를 전달해야 한다는 점이 아쉽다.
그러나 로컬 함수는 자신이 속한 바깥 함수의 모든 파라미터와 변수를 사용할 수 있다.

``` java
	class User(val id: Int, val name: String, val address: String)
	fun saveUser(user: User) {
		fun validate(value: String, fieldName: String) {
			if (value.isEmpty()) {
				throw IllegalArgumentException(
				"Can't save user ${user.id}: " + "empty $fileName")
			}
		}
		validate(user.name, "Name")
		validate(user.name, "Name")	
	}
```
* 중첩된 함수의 깊이가 깊어지면 가독성이 떨어진다. 
	* 한 단계만 함수를 중첩시키는 것을 권장한다.
