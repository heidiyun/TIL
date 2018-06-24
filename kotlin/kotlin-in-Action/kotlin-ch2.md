## 2.1 기본 요소 : 함수와 변수
- 타입 선언 생략
- 불변객체 장려

### 2.1.1 Hello, World!
	- 함수 선언시 fun 키워드를 사용
	- 파마리터 이름 뒤에 파라미터 타입을 쓴다.
	- 함수를 클래스 안에 넣을 필요가 없다.
	- 배열 처리를 위한 문법이 따로 존재하지 않는다.
	- 문장의 끝에 세미콜론을 붙이지 않는다.

### 2.1.2 함수
	문(statement)
		자신을 둘러싸고 있는 가장 안쪽 블록의 최상위 요소로서, 값을 만들어내지 않는다.
	식(expression)
		다른 식의 하위 요소로 계산에 참여할 수 있으며, 값을 만들어 낼 수 있다.

* 자바에서는 모든 제어 구조가 문인 반면, 코틀린에서는 대부분의 제어 구조가 식이다.

* 식이 본문인 함수
	``` 
		fun max(a: Int, b: Int) = if (a > b) a else b
	```
	* 중괄호와 return문이 없어졌다.
	* 반환 타입 명시를 생략해도 된다. 
	(컴파일러가 본문을 분석해 식의 겨로가 타입을 함수 반환 타입으로 정해준다.)
* 블록이 본문인 함수
	* 중괄호, return문 그리고 함수의 반환 타입을 반드시 명시해야 한다.

### 2.1.3 변수
* 변수 타입을 명시해도 되고 생략해도 된다.
	- 그러나, 변수를 생성할 때 초기화하지 않는다면 반드시 타입을 명시해주어야 한다.

* val : 변경불가능한 참조를 저장하는 변수 
		(자바의 final 변수에 해당한다. 상수.)
	- 참조가 가리키는 객체의 내부 값은 변경될 수 있다.

* var : 변경가능한 참조를 저장하는 변수
	- 변수의 값을 변경할 수 있지만, 변수의 타입은 바꿀 수 없다.

### 2.1.4 문자열 템플릿
* 문자열 안에서 변수를 사용할 수 있다.
	```
		val name = "yunjeong"
		println("Hello, $name")
	```
* java ver.
	```
		String name = "yunjoeng"
		System.out.println("Hello," + name);
	```

* '$' 문자를 문자열안에 넣고 싶다면 '\$' 해주면 된다.
* 문자열 안에 식을 쓰고 싶다면 {}를 사용하면 된다.
	``` 
		fun main(args: Array<String>) {
			if (args.size > 0) {
				println("Hello, ${args[0] "someone"}")
			}
		}
	```
	- 중괄호 안에 ""를 사용할 수 있다.

* 문자열안에 한글을 사용할 경우 변수를 {}로 감싸주어야 예기치 않은 오류가 발생하지 않는다.
  

## 2.2 클래스와 프로퍼티

- 필드가 늘어날 수록 생성자에서 파라미터 대입문의 길이가 길어진다.

- 코드가 없이 데이터만 저장하는 클래스를 값 객체(value object)라고 한다.

### 2.2.1 프로퍼티

#### 클래스의 목적
> 데이터 캡슐화
>> 멤버 필드 보통 private
>>> 데이터 접근을 위해 접근자 메소드를 제공한다.

#### 프로퍼티란?
> 필드와 접근자를 통칭하는 말.

##### kotlin에서의 프로퍼티 
> 언어의 기본 기능으로 프로퍼티를 제공한다.
>> 자바의 필드와 접근자 메소드를 대체한다.
>>> (따로 필드를 생성하고 접근자 메소드를 만들지 않아도 된다.)

-  val : 읽기 전용 프로퍼티
	(비공개 필드와 공개 getter를 만들어낸다.)

- var : 변경가능한 프로퍼티
	(비공개 필드와 공개 getter/setter를 만들어낸다.)

* backing field
	```
	 프로퍼티의 값을 저장하기 위한 필드.
	* 식별자 field를 제공한다.
		* field 식별자를 통해 접근할 수 있는 automatic backing field를 제공한다.
			=> 프로퍼티의 accessor(getter/setter)에서만 사용가능하다.
	```
	
	* 생성조건
		* accessor중 1개라도 기본 구현을 사용하는 경우
		* custom accessor에서 field 식별자를 참조하는 경우
	```
		var counter = 0
			set(value) {
				if (value >= 0) field = value
			}
	```

	``` 
		val isEmpty: Boolean
		get() = this.size == 0
	```
	

### 2.2.2 커스텀 접근자
>클라이언트가 프로퍼티에 접근할 때마다 프로퍼티 값을 매번 다시 계산한다.
	( 프로퍼티의 자체 값을 저장하는 필드가 없다.)

![property](/kotlin/assets/커스텀접근자.png)

'블록문을  제거한  코드'
	: 이러한  코드는 setter에서는  적용  불가능하다.

![property](/kotlin/assets/커스텀접근자2.png)

### 2.2.3 코틀린  소스코드  구조 : 디렉토리와  패키지

-   모든  코틀린  파일의  맨  앞에  package문을  넣을  수  있다.

		: 같은  패키지에  있다면  다른  파일에서  정의한  선언을  직접  사용  할  수  있다.
		다른  패키지에  있다면  import를  통해  선언을  불러와야  한다.

![property](/kotlin/assets/패키지1.png)

![property](/kotlin/assets/패키지2.png)
-   자바에서는  패키지  구조와  일치하는  디렉터리  계층  구조를  만들고, 클래스의  소스코드를  해당  패키지와  디렉토리에  위치시켜야  한다.
	But, 코틀린은  패키지가  다른  여러  클래스를  한  파일에  위치  시켜도  된다.

## 2.3 선택  표현과  처리: enum과  when

### 2.3.1. enum  클래스  정의

-   enum은  soft keyword (class 앞에서만  기능을  하고, 다른  곳에서는  이름으로  사용할  수  있다.)
-   enum class도  프로퍼티와  메소드를  가질  수  있다.
	![property](/kotlin/assets/enum.png)

### 2.3.2 when으로  enum 클래스  다루기

*   when (expression)
	*   자바의 switch문을 대체하는 구문.
	*   각 분기의 끝에 break; 구문을 넣지 않아도 된다.
	*   한 분기 안에서 여러 값을  매치하려면 값 사이를(,)로 분리한다.

	![property](/kotlin/assets/when.png)

	![property](/kotlin/assets/when2.png)
		
	-   그  외의  모든  값은  else로  처리한다.

	![property](/kotlin/assets/when3.png)

### 2.3.3 when과  임의의  객체를  함께  사용
>자바의  switch문은  분기  조건으로  상수만을  허용했다면, 코틀린의  when은  분기  조건으로  임의의  객체를  허용한다.

### 2.3.4. 인자가  없는  when 사용
> 각  분기의  조건이  불리언  결과를  계산하는  식이어야  하므로, 가독성이  떨어진다.

### 2.3.5. 스마트  캐스트 :  타입  검사와  타입  캐스트를  조합
> 유저가  명시적으로  캐스팅해주지  않아도  컴파일러가  캐스팅을  해준다.
*  불필요한  코드의  중복을  줄일  수  있다.

![property](/kotlin/assets/smartcast.png)

*   Is를  사용할  수  있는  경우
	-   프로퍼티에  대한  스마트  캐스트를  사용한다면
	-   프로퍼티는  반드시  val이어야  한다.
	-   커스텀  접근자를  사용한  것이  아니어야  한다.

	// 86page, 스마트  캐스트는  is로  변수에  든  값의  타입을  검사한  다음에  그  값이  바뀔  수  없는  경우에만  작동한다.

	-> 있는  경우  아닌가?

	-   명시적  타입  캐스팅은  'as' 키워드를  사용한다.

### 2.3.6 리팩토링 : if를 when으로  변경
  1. 코틀린에서의  if문은  값을  만들어  내는  식이므로, 'return'  키워드를  생략할  수  있다.

  2. 블록의  마지막  식이  그  분기의  결과  값이다.

  3.  2.3.5의  if문을  when으로  변경한  코드
		> 스마트  캐스팅이  일어남.
		![property](/kotlin/assets/if.png)

## 2.4 대상을 이터레이션 : while과 for 루프
### 2.4.1 while 루프
* 코틀린에는 while과 do-while문이 있다.
	- 자바의 while과 do-while문과 동일한 문법을 취한다.

### 2.4.2. 수에 대한 이터레이션: 범위와 수열

* 코틀린의 for문은 범위를 사용하여 루프를 돈다.
	- 범위는 양끝을 포함한다.
	- ex) 1~10까지의 수로 반복을 한다면
			for ( i in 1..10)

	- 수열(progression) : 어떤 범위에 속한 값을 일정한 순서로 이터레이션하는 것 

	![for](/kotlin/assets/for.png)

	- downTo : 역방향 수열을 만든다. (기본 증가 값은 -1이다)
	- step : 증가 값을 설정해준다. (증가 값의 절대값을 설정해줄뿐, 기본 증가 값의 방향이 바뀌지는 않는다.)
	- 범위의 끝값을 포함하고 싶지 않다면, 'until'을 사용하자.
			for( i in 1 until 10)
			(==) for ( i int 1..9) 

### 2.4.3 맵에 대한 이터레이션
* Map의 for문을 이용한 이터레이션은 아래와 같이 작성한다.

	![for-map](/kotlin/assets/for-map.png)

	```
		val map = TreeMapOf()

- 코틀린에서는 자료구조 map을 사용할 때 get, put을 사용하지 않는다.
	* value를 가져올 때, map[key]
		(java ver. map.get(key))
	* Key값에 해당하는 value를 넣을때, map[key] = value 
		(java ver. map.put(key, value)
	* 다른 컬렉션을 이터레이션할 경우에도 동일하게 사용할 수 있다.
	```
	fun main(args: Array<String>) {
    val arrayList = ArrayList<String>()
    for (s in arrayList) {
        println(s)
    }
	val list = arrayListOf("10", "11", "12", "13")
	for ((index, element) in list.withIndex()) 
    // 리스트를 맵처럼 인덱스와 함께 뽑고싶다면 withIndex()를 이용하여 루프를 돌리자.
    {
        println("$index , $element")
    }
}
	```

### 2.4.4 in으로 컬렉션이나 범위의 원소 검사

* in 연산자를 사용해 어떤 값이 범위에 속하는지 검사할 수 있다.
	```
			fun isLetter(c: Char) = c in 'a'..'z'
			println(isLetter('q'))
				결과 : true 
	```

* !in 연사자를 사용해 어떤 값이 범위에 속하지 않는지 검사할 수 있다.
	``` 
		fun isNotDigit(c: Char) = c !in '0'..'9'
		println(isNotDigit('x'))
			결과 : true
	```

* in 연사자를 when식 안에서 사용할 수 있다.
	``` 
		fun recognize(c: Char) = when(c) {
			in '0'..'9' -> "It's a digit!"
			in 'a'..'z' -> "It's letter!"
			else -> "I don't know.."
		}
	```

* 비교 가능한 클래스라면 해당 클래스의 인스턴스 객체를 사용해 범위를 만들 수 있다.
	(java.lang.Comparable interface를 구현한 클래스)

	```
	println("Kotlin" in "Java".."Scala")
		>> "Java" <= Kotlin" && "Kotlin" <= "Scala"
		>> true
	```

* 컬렉션에서도 in 연산자를 사용할 수 있다.
 
 	```
 	println("Kotlin" in setOf("Java", "Scala"))
 		>> false
 	```

## 2.5 코틀린의 예외 처리
>> 코틀린의 예외처리는 자바와 비슷하다.

* 예외 인스턴스를 생성할 때도 new를 붙일 필요가 없다.
	```
	throw IllegalArgumentException("Exception!")
	```
* 자바와 달리 throw는 식이므로 다른 식에 포함될 수 있다.

### 2.5.1. try, catch, finally
* 함수가 던질 수 있는 예외를 명시할 필요가 없다. 
	(자바의 throws문을 쓰지 않는다.)
	- checked exception과 unchecked exception을 구별하지 않는다.
	- 발생한 예외를 잡아내도 되고 잡아내지 않아도 된다.
	  (자바에서 checked exception은 무조건 처리해주어야 했다.)

* try-with-resouce기능을 위한 문법이 없다.
	- 라이브러리 함수로 같은 기능을 구현하게 한다.

	```java
		val writer = FileWriter("test.txt")
		writer.use {
			writer.write("something")
		}

		val writer = FileWriter("test.txt").use { w -> w.write("something")}

		val writer = FileWirter("test.txt").user { it.write("something") }
	```

### 2.5.2. try를 식으로 사용
* try의 값을 변수에 대입할 수 있다.
	- if와 달리 try의 본문에 반드시 {}를 써야 한다. 
	- try문의 마지막 식의 값이 전체 결과 값이다.
	- catch문을 실행했다면 catch블이 값이 결과 값이 된다.

	```
		fun readNumber(reader: BufferedReader) {
			val number = try {
				Integer.parseInt(reader.readLine())
			} catch (e: NumberFormatException) {
				return
			}
			println(number)
		}
	```
