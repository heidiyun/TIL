# 0622 코틀린 수업

## 위임
> 객체 지향 설계에서 기존의 객체를 재사용한다.

1. 클래스 위임

```java
	interface UIElement {
		fun getHeight(): Int
		fun getWidth(): Int
	}

	class Rectangle(val x1: Int, val x2: Int, val y1: Int, val y2: Int): UIElement {
		override fun getHeight() = y2 - y1
		override fun getWidth() = x2 - x1
	}

	//Composite: 복합적으로 구성되어 있다.
	//	-> 자신과 동일한 속성을 가지는 객체를 포함하는 형태의 구조
	class Panel(val rectangle: Rectangle): UIElement {
		override fun getHeight() = rectangle.getHeight()
		override fun getWidth() = rectangle.getWidth()
	} 
	// Rectangle과 Panel이 같은 역할을 함.

	class Panel(rectangle: Rectangle) : UIElement by rectangle
	// 인터페이스의 구현을 rantangle에게 위임함.
```

2. 프로퍼티 위임

```java
	class SampleDelegate {
		operator fun getValue(thisRef: Any, property: KProperty<*>): String {
			return "getter - $thisRef - ${property.name"
		} 
		// thisRef : 위임을 신청한 객체의 참조
		// property : 위임을 신청한 객체의 해당 프로퍼티
	}

	class User {
		var name: String by SampleDelegate()
		// 프로퍼티의 초기화 및 접근자 메소드를 SampleDelegate에 위임.
	}

	fun main(args: Array<String>) {
		val user = User()
		println(user.name) // user.getName()
		user.name = "Tom" // user.setName()

	}
```

* 코틀린 표준 라이브러리가 제공하는 프로퍼티 위임

lazy : 지연 초기화
> 객체가 생성되는 시점에 초기화되는 것이 아니라, 
처음 해당 프로퍼티에 접근할 때 초기화 하고 싶다.

```java
	class User {
		var name: String? = null
		// 초기값을 설정할 수 없어 null을 가져야할 때 nullable 타입으로 선언해야 한다.
		// -> 프로퍼티 접근 시마다 null check를 해야 한다.

		lateinit var name: String 
		// 초기화하기 전에 접근하면 NullPointerExceptino이 발생

		val age: Int
		// lateinit은 var에만 사용할 수 있다.

		val age by lazy {
			42
		}

	}
```

2.  KVO(Key Value Observation)
> 프로퍼티 값의 변경을 통보받는 기술

```java
	class TextView {
    var text: String = ""
        set(value) {
            println("Update TextView's text to $value")
        }
	}

	class Activity {
    // name의 값이 변경될 때마다, nameTextView의 내용을 업데이트 하고 싶다.
    // var name: String = ""
    	var name by Delegates.observable("") { _, old, new ->
        	println("$old -> $new")
        	nameTextView.text = new
    	}
    	val nameTextView = TextView()
	}
	
	fun main(args: Array<String>) {
    	val activity = Activity()
    	activity.name = "Tom"
    	// activity.nameTextView.text = "Tom"
    	activity.name = "Bob"
    	// activity.nameTextView.text = "Bob"
	}
```

3. KVC(Key Value Coding) 

```java
	var json = { "name": "Tom", "age": 42, "address": Suwon }
	var user = User(json["name"], json["age"], json["address"])

	// property name - key
	// property value - value

	class User(map: Map<String, Any>) {
		val name: String by map // name = map["name"]
		val age: Int by map
		val address: String by map
	}

	// 초기화를 map에게 위임.
```

4. vetoable: validation
> 특정한 프로퍼티가 가져야하는 제약을 위임을 통해서 처리할 수 있다.

```java
	class User {
		var password: String by Delegates.vetoable("Hello") {
			_, old, new -> new length >=5
		}

		// 비밀번호의 길이가 5 이상 이어야 한다.
	}

	fun main(args: Array<String>) {
		val uses = User()
		user.password = "Shadow"
		user.password = "Red" // 변경되지 않는다.
	}

```

## 인라이닝
> 인라이닝되지 않은 함수는 람다를 변수에 저장할 수 있고,
바깥쪽 함수로부터 반환된 뒤에 저장해 둔 람다가 호출될 수 있다.

인라인: 호출하지 않고 치환한다.

호출 : CPU Context
	CPU - Register 변수
	1. SP(Stack Point) -> top 저장
	-> 지역 변수, 인자의 전달, 함수의 호출
	2. IP(PC) - Istruction Pointer(Program Counter)
	-> 다음에 실행해야 하는 명령을 가르키는 레지스터 변수

main 함수에서 함수를 실행한 후 다시 main으로 돌아오기위해 복귀 주소를 IP에 저장한다.

* 인라인 함수 : 함수가 별도의 주소에 저장될 필요가 없다.
	* 인라인 : 컴파일 최적화 옵션
	* 변수 : 런타임에 변하는 값
	-> 둘은 상충되므로 인라이닝 되는 함수가 변수에 저장될 수 없다.

```java
	inline fun filter(data: List<Int>, predicate: (Int) -> Boolean): List<Int> {
		val result = mutableListOf<Int>()
		// val p = predicate 
		// 위와 같은 식은 불가능하다.
		// 람다가 인라인되기 때문에 주소를 저장할 수 없다.
		for(e in data) {
			if (predicate(e))
				result.add(e)
		}

		return result
	}

	fun main(args: Array<String>) {
		val data = listOf(1, 2, 3, 4, 5, 6)
		filter(data) {
			it > 5
		} // 5이하의 수는 걸러줘.
	}
```