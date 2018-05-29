# 코틀린 기초(1)
## 코틀린의 특성

### 코틀린의 장점
1. 실용성
	특정 프로그래밍 언어 스타일 사용을 강제하지 않는다.
	-> 코틀린과 여러 언어들이 혼재하여 사용이 가능하다. 
	-> 코틀린은 DSL(Domain-Specific-Language) 기능을 제공한다. 

2. 간결성
	getter/setter, 생성자 파라미터의 필드 저장 등의 보일러플레이트를 발생시키는 코드를 줄였다.
	-> 준비 코드가 적고, 코드 파악이 쉬운 구문 구조를 제공한다.

3. 안정성
	- NullPointerException에 의한 오류를 감소시켰다. 
		: null이 될 수 있는 타입을 지원한다.
			-> 컴파일 시점에 널 포인터 예외 발생 여부를 검사 할 수 있다. 
	- ClassCastException
		: 'is'로 타입검사와 캐스트를 한번에 처리 할 수 있다.

```
val s:  String? = null (null을 가질 수 있다.)
val s2: String = " " (null을 가질 수 없다.)
```

```
**자바
if(object.getClass == this.class)
	Class obj = (Class) object
	obj.fun

**코틀린
(object is Class) 
	object.fun
```

4. 상호운용성
	- 코틀린은 자바 표준 라이브러리 클래스에 의존한다. (자체 컬렉션 라이브러리 제공x)
	- 자바 코드와 코틀린 소스 파일이 혼재해도 컴파일이 가능하다.
	- 자바 메소드를 리팩토링해도 그 메소드와 관련 있는 코틀린 코드까지 제대로 변경됨.

	```
	class Person(val name: String, val age: Int) {
    @Override
    override fun equals(other: Any?): Boolean
    ```
	
