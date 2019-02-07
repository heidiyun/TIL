# 6. 흐름 제어
## 6.1 조건문
> if / switch / guard  

### 6.1.1 if 구문
스위프트의 if 구문은 조건의 값이 꼭 Bool 타입이어야 합니다.
```swift
let first: Int = 5
let second: Int = 7

if first > second {
	print("first > second")
} else if ifrst < second {
	print("first < second")
} else {
	print("first == second")
}
```

 코틀린과 달리 if 조건문을 괄호로 감싸지 않아도 됩니다. (선택사항)

### 6.1.2 switch 구문
```swift
let integerValue: Int = 5

switch integerValue {
	case 0:
		print("Value == zero")
	case 1...10: {
		print("Value == 1~10")
		fallthrough
	}
	case Int.min..<0, 101<Int.max:
		print("Value < 0 or Value > 100")
		break
	default:
		print("10 < value <= 100")
}
```
* fallthrough : 다음 case도 실행되도록 한다. 
```swift
let stringValue: String = "yun"

switch stringValue {
	case "yagom":
		print("The person is yagom")
	case "Jay":
		print("The person is jay")
	case "Jenny", "Jo", "Nova":
		print("The person is \(stringValue)")
	default:
		print("\(stringValue) said I don't konw who you are")
}
```
```swift
typealias NameAge = (name: String, age: Int)

let tupleValue: NameAge = ("yagom", 99)

switch tupleValue {
	case ("yagom", 99):
		print("정확히 맞췄습니다.!")
	case ("yagom", _):
		print("이름만 맞았습니다.")
	case (let name, 99):
		print("나이만 맞았습니다. 이름은 \(name)입니다.")
	default:
		print("누굴 찾나요?")
}
```
* 숫자 표현이 아닌 문자, 문자열, 열거형, 튜플, 범위, 패턴이 적용된 타입 등을 사용할 수 있습니다.
* 여러 개의 항목을 한 번에 case로 지정해주는 것도 가능하다.
* case문 다음에 실행될 코드가 없으면 컴파일 오류가 납니다. 

## 6.2 반복문
### 6.2.1 for-in 구문
> 코틀린과 for문과 비슷하다.  

```swift
for i in 0...2 {
	print(i)
}
// 0
// 1
// 2

for i in 0...5 {
	
	if i % 2 == 0 {
		print(i)
		continue
	}

	print("\(i) == 홀수 ")
}
```
* continue : 밑의 남은 구문을 실행하지 않고 바로 다음 조건으로 넘어갑니다.
```swift
let hello: String = "Hello"

for char in hello.characters {
	print(char)
}

var result: Int = 1
for _ in 1...3 {
	result *= 10
}
```
* 시퀀스에 해당하는 값이 필요 없다면 와일드카드 식별자( _ )를 사용한다.

### 6.2.2 while 구문
```swift
var names: [string] = ["yun", "ha", "ho", "sin"]

while names.isEmpty == false {
	print("Good \(names.removeFirst())")
}
```

### 6.2.3 repeat-while 구문
> do-while문과 비슷하다.  
```swift
var names: [string] = ["yun", "ha", "ho", "sin"]

repeat {
	print("Good \(names.removeFirst())")
} while names.isEmpty == false
```

### 6.3 구문 이름표
> 반복문을 중첩으로 사용할 때 구문 이름을 통해 어떤 반복문을 제어할 지 지정할 수 있습니다.  
```swift
var numbers: [Int] = [3, 2342, 6, 3252]

numbersLoop: for num in numbers {
	if num > 5 || num < 1 {
		continue numbersLoop
	}

	var count: Int = 0
	
	printLoop: while true {
		print(num)
		count += 1
		
		if count == num {
			break printLoop
		}
	}

	removeLoop: while true {
		if numbers.first != num {
			break numbersLoop
		}
		numbers.removeFirst()
	}
}
```

#swift/book-programming