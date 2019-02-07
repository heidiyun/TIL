# 7. 함수
## 7.1 함수와 메소드
* 함수 : 타입(객체)와 상관없이 전역적으로 사용할 수 있는 함수
* 메소드 : 타입(객체)에 연관되어 사용하는 함수
정의하는 위치와 호출되는 범위만 다를 뿐이다.

## 7.2 함수의 정의와 호출
오버라이딩과 오버로딩을 모두 지원한다.

### 7.2.1 기본적인 함수의 정의와 호출
> 스위프트의 함수는 자유도가 높다.  
```swift
func <함수이름>(<매개변수>) -> <반환타입> {
	//.. 실행구문
	return <반환 값>
}

func hello(name: String) -> String {
	return "Hello \(name)"
}

let helloJenny: String = hello(name: "Jenny")
```

### 7.2.2 매개변수
* ::매개변수 : 함수를 정의할 때 외부로부터 받아들이는 전달 값의 이름::
* ::전달인자 : 함수를 실제로 호출할 때 전달하는 값::

``` swift
func sayHello(myName: String, yourName: String) -> String {
	return "Hello \(yourName) | I'm \(myName)"
}

print(sayHello(myName: "yun", youName: "ya"))
```


#### 매개변수 이름과 전달인자 레이블
```swift
func <함수이름>(전달인자 레이블 매개변수 이름: 매개변수 타입) -> <반환타입> {
	//.. 실행구문
	return <반환 값>
}
func sayHello(from myName: String, to yourName: String) -> String {
	return "Hello \(yourName) | I'm \(myName)"
}

print(sayHello(from: "yun", to: "ya"))
```
함수 내부에서는 전달인자 레이블을 사용할 수 없다.
함수를 호출할 때는 매개변수 이름을 사용할 수 없다.

* 전달인자 레이블을 쓰고싶지 않다면? -> ( _ ) 식별자를 사용하자.
```swift
func sayHello(_ myName: String, _ yourName: String) -> String {
	return "Hello \(yourName) | I'm \(myName)"
}
print(sayHello("yun", "ya"))
```

::전달인자 레이블만 달라도 오버로딩할 수 있다.::

#### 매개변수 기본값
```swift
func sayHello(_ myName: String = "yun", _ yourName: String) -> String {
	return "Hello \(yourName) | I'm \(myName)"
}
print(sayHello("yun", "ya"))
```
매개변수를 전달받지 않으면 기본값으로 지정한 값을 사용합니다.

#### 가변 매개변수
> 매개변수로 몇 개의 값이 들어올지 모를때 가변 매개변수를 사용한다.  

* 가변 매개변수는 0개 이상의 값을 받아올 수 있다.
* 가변 매개변수로 들어온 인자 값은 배열처럼 사용할 수 있다.
* 함수마다 가변 매개변수는 하나만 가질 수 있다.

``` swift
func sayHello(me: String, friends names: String...) -> String {
	var result: String = ""

	for friend in names {
		result += "Hello \(friend)!" + " "
	}

	result += "I'm " + me + "!"
	return result
}

print(sayHello(me: "yun", friends: "john", "jessy")
```

#### 입출력 매개변수
값이 아닌 참조를 전달

```swift
var numbers: [Int] = [1, 2, 3]

func referenceParameter(_ arr: inout [Int]) {
	arr[1] = 1
}

referenceParameter(&numbers)
print(numbers)
> [1, 1, 3]
```

### 7.2.3 반환타입
반환 값이 없으면 반환 타입을 생략하거나, Void를 명시해준다.

### 7.2.4 데이터 타입으로서의 함수
```
(매개변수 타입의 나열) -> 반환타입
```

* (Void)  -> Void
* () -> Void
* () -> ()

```swift
typealias CalculateTwoInts = (Int, Int) -> Int

func addTowInts(_ a: Int, _ b: Int) -> Int {
	return a + b
}

var mathFunction: CalculateTwoInts = addTowInts

func printMathResult(_ mathFunction: CalcultaeTwoInts, _ a: Int, _ b: Int) {
	print("result:\(mathFunction(a,b))")
}

func chooseMathFunction( _ toAdd: Bool) -> CalculateTwoInts {
	return toAdd ? addTwoInts : multiplyTwoInts
}

printMathResult(chooseMathFunction(true), 3, 5)
```

#### 전달인자 레이블과 함수 타입
전달인자 레이블은 함수 타입의 구성요소가 아니므로 함수 타입을 작성할 때는 전달인자 레이블을 써줄 수 없다.
* let someFunction: (lhs: Int, rhs: Int) -> Int (XXXXXXX)
* let someFunction: (_ lhs: Int, _ rhs: Int) -> Int (OK)
* let someFunction: (Int, Int) -> Int (OK)

## 7.3 중첩 함수
> 함수 안에 함수  
* 중첩 함수는 상위 함수의 몸통 블록 내부에서만 함수를 사용할 수 있다.
* 상위 함수가 중첩 함수를 반환하면 밖에서도 사용 가능하다.

```swift
*typealias* MoveFunc = (Int) -> Int

*func* goRight(*_* currentPosition: Int) -> Int {
    *return* currentPosition + 1
}

*func* goLeft(*_* currentPosition: Int) -> Int {
    *return* currentPosition - 1
}

*func* functionForMove(*_* sholdGoLeft: Bool) -> MoveFunc {
    *return* sholdGoLeft ? goLeft : goRight
}

*var* position = 3

*let* moveToZero: MoveFunc = functionForMove(position > 0)

*while* position != 0 {
    position = moveToZero(position)
}
```

위의 코드를 중첩 함수를 사용해서 바꿔보자.
```swift
*typealias* MoveFunc = (Int) -> Int


*func* functionForMove(*_* sholdGoLeft: Bool) -> MoveFunc {
    *func* goRight(*_* currentPosition: Int) -> Int {
        *return* currentPosition + 1
    }
    
    *func* goLeft(*_* currentPosition: Int) -> Int {
        *return* currentPosition - 1
    }
    *return* sholdGoLeft ? goLeft : goRight
}

*var* position = 3

*let* moveToZero: MoveFunc = functionForMove(position > 0)

*while* position != 0 {
    position = moveToZero(position)
}
*
```

* 전역함수가 많은 프로젝트에서 전역으로 사용이 불필요한 함수의 사용 범위를 명확하고 깔끔하게 표현해줄 수 있다.

## 7.4  종료되지 않는 함수
> 정상적으로 끝나지 않는 함수  
> 비반환 함수 / 비반환 메서드  

이 함수를 사용하면 프로세스 동작이 끝났다고 보는게 맞습니다.
비반환 함수 안에서는 오류를 던지거나, 시스템 오류를 보고하는 등의 일을 하고 프로세스를 종료해 버린다.

비반환 함수는 어디선든 호출이 가능하고 guard 구문의 else 블록에서도 호출할 수 있습니다. 

비반환 메서드는 오버라이딩할 수 있지만 비반환 타입이라는 것을 변경할 수는 없다.

```swift
func crashAndBurn() -> Never {
	fatalError("Something ery, very bad happend")
}

crashAndBurn() // 프로세스 종료 후 오류 보고

func someFunction(_ isAllIsWell: Bool) {
	guard isAllIsWell else {
		print("마을에 도둑이 들었다.")
		crashAndBurn()
	}
}

someFunction(true)
someFunction(false)
> 마을에 도둑이 들었습니다. 
```

## 7.5 반환 값을 무시할 수 있는 함수
> @discardableResult  
프로그래머가 의도적으로 함수의 반환 값을 사용하지 않을 경우 컴파일러가 함수의 결과 값을 사용하지 않았다는 경고를 보낼 때가 잇다.
이런 경우 함수의 반환 값을 무시해도 된다는 선언 속성을 이용하자.

```swift
@discardableResult func discardableResultSay(_ something: String) -> String {
	print("something")
	return something
}

discardableResultSay("hello")
```




























#swift/book-programming