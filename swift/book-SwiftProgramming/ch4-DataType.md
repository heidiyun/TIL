# 4. 데이터 타입 고급
## 4.1 데이터 타입 안심
> 타입 안전성  
다른 타입끼리의 데이터 교환은 반드시 타입캐스팅을 해주어야 합니다.
더 정확히 말하자면 값 타입의 데이터 교환은 새로운 인스턴스를 생성하여 할당하는 것이다.

### 4.1.1 데이터 타입 안심이란
스위프트는 컴파일 오류로 잘못된 타입이 할당됐다는 것을 알려주므로 실수를 줄일 수 있습니다. -> 타입 확인

### 4.1.2 타입 추론
타입을 명시하지 않아도 컴파일러가 할당된 값을 기준으로 변수나 상수의 타입을 결정한다.

## 4.2 타입 별칭
이미 존재하는 데이터 타입에 임의로 다른 이름을 부여할 수 있다.
기본 타입 이름과 타입 별칭을 모두 사용할 수 있습니다.
```swift
typealias MyInt = Int
typealias YourInt = Int
typealias MyDouble = Double

let age: MyInt = 100
var year: YourInt = 2080
let month: Int = 7
let percentage: MyDouble = 99.9

year = age
```

## 4.3  튜플(Tuple)
튜플은 타입의 이름이 따로 지정되어 있지 않고, 프로그래머가 만드는 타입 입니다. : **타입의 나열** 최대 10개까지 
```swift
var person: (String, Int, Double) = ("yagom", 100, 182.5)

print("이름: \(person.0), 나이: \(person.1), 신장: \(person.2)")

person.1 = 99
person.2 = 178.5
```
위와 같이 인덱스를 통해서 값에 접근하는 것은 편리할 수 있으나 가독성이 떨어집니다.

```swift
var person: (name: String, age: Int, height: Double) = ("yagom", 100, 182,5)

print("이름: \(person.name), 나이: \(person.age), 신장: \(person.height)")

person.age = 42
person.height = 177.2
```
각 요소의 이름을 지정해주면 이름을 통해서 값에 접근할 수 있습니다.
물론 인덱스를 통해서도 값에 접근할 수 있습니다.

**튜플에는 타입 이름에 해당하는 키워드가 없어 불편함이 있습니다.**
타입 별칭을 사용하여 더 간편하게 코드를 작성할 수 있다.
```swift
typealias PersonTuple = (name: String, age: Int, height: Double)

let yagom: PersonTuple = ("yagom", 100, 178.5)
```

## 4.4  컬렉션 타입
### 4.4.1 배열(Array)
같은 타입의 데이터를 순서대로 저장하는 컬렉션 타입.
값이 중복될 수 있다.

스위프트의 배열은 선언시 크기가 고정되는 버퍼가 아니라 자동으로 버퍼의 크기를 조절해줍니다. -> **요소의 삽입 및 삭제가 자유롭다.**

배열의 사이즈를 벗어나는 인덱스에 접근하면 오류가 발생합니다.

* 배열 생성하는 법
```swift
var names: Array<String> = ["yun", "heidi", "joe", "han"]
var names: [String] = ["yun", "heidi", "joe", "han"]

var emptyArray1: [Any] = [Any]()
var emptyArray2: [Any] = Array<Any>()
var emptyArray3: [Any] = [] //배열의 타입을 정확히 명시한다면 []으로 빈 배열 생성가능

names[2] // 배열은 각 요소의 인덱스를 통해 접근.
names.append("elsa") // 배열의 마지막에 elsa가 추가됨.
names.append(contentsOf: ["john", "max"]) // 배열의 마지막에 john과 max 추가
names.insert("anna", at: 2) // 인덱스 2에 추가됨.

names.index(of: "yun") // yun의 인덱스를 찾을 수 있다.
names.first
name.last

let firstItem: String = names.removeFirst() //  0번째 인덱스가 반환된다.

let indexOneItem: String = names.remove(at: 1) 

print(names[0 ... 2])
> ["heidi", "joe", "han"]
// ... 연산자를 사용해서 범위를 표현할 수 있다.

names[0 ... 2] = ["hh", "ww", "we"]
print(names)
> ["hh", "ww", "we", "elsa", "john", "max"]
```

### 4.4.2 딕셔너리(Dictionary)
> 순서 없이 키와 값의 쌍으로 구성되는 컬렉션 타입.  
> 키는 중복될 수 없다.  
> Hash  

딕셔너리 안에 없는 키를 접근해도 오류가 발생하지 않습니다. 단지 nil을 반환합니다.

```swift
var numberForName: Dictionary<String, Int> = Dictionary<String, Int>()

var numberForName2: [String: Int] = [String: Int]()
var numberForName3: [String: Int] = [:]
var numberForName4: [String: Int] = ["yun": 100, "heidi": 200, "anna": 300]

print(numberForName4["yun"])
> 100

numberForName4["max"] = 999 // max 키와 999 값을 추가.

print(numberForName4.removeValue(forKey: "yun"))
> Optional(100)
// 반환타입이 Int?

print(numberForName4.removeValue(forKey: "yun") ??  0)
> 100
// defaultValue를 0으로 지정
// Optional 벗기기

print(numberForName4["heidi", default: 0])
// heidi에 해당하는 값이 없으면 0이 반환됩니다.
```

### 4.4.3 세트(Set)
같은 타입의 데이터를 순서 없이 저장하는 컬렉션 타입.
중복 값을 허용하지 않습니다.

**순서가 중요하지 않거나 각 요소가 유일한 값**이어야 하는 경우 사용.
Set 요소로는 해시 가능한 값이 들어와야 한다.
스위프트의 기본 데이터 타입은 모두 해시 가능하다.

```swift
var names: Set<String> = ["yun", "heidi", "elsa", "anna"]
var names2: Set<String> = Set<String>()
var names3: Set<String> = []

// Array와 같은 대괄호를 쓰기 때문에 타입 명시 없이 []만 쓰면 Array로 타입 추론 됩니다.

var numbers = [100, 200, 300]
print(type(of: numbers))
> Array<Int>

names.insert("john")

print(names.remove("yun"))
> yun
// 해당 요소가 삭제 된 후 반환 됨.
// 삭제하고자 하는 요소가 없으면 nil 반환
```

## 4.5 열거형
연관된 항목을 묶어서 표현할 수 있는 타입.
프로그래머가 정의해준 항목 값 외에는 추가/수정이 불가하다.

**사용하는 경우**
* 제한된 선택지를 주고 싶을 때
* 정해진 값 외에는 입력받고 싶지 않을 때
* 예상된 입력 값이 한정되어 있을 때

C언어의 열거형은 각 항목 값이 정수 타입으로 기본 지정되지만, 스위프트의 열거형은 각 항목이 그 자체로 고유의 값이 될 수 있다.

### 4.5.1 기본 열거형
> enum  
```swift
enum School {
	case primary
	case elementary
	case middle
	case high
	case college
	case university
	case graduate
}

enum School2 {
	case primary, elementary, middle, high, college, university, graduate
}

var highestEducationLevel: School = School.university

var highestEducationLevel2: School = .university
// highestEducationLevel과 highestEducationLevel2는 같은 표현.

```

### 4.5.2 원시 값
열거형의 각 항목은 원시 값도 가질 수 있다. 즉, 특정 타입으로 지정된 값을 가질 수 있다.
```swift
enum School: String {
	case primary = "유치원"
	case elementary = "초등학교"
	case middle = "중학교"
	case high = "고등학교"
	case college = "대학"
	case university = "대학교"
	case graduate = "대학원"
}

let highestEducationLevel: School = School.university
print("저는 \(highestEducationLevel.rawValue)에 다니고 있습니다.")
```

일부 항목만 원시 값을 가질 수 있습니다.
```swift
enum School: String {
	case primary = "유치원"
	case elementary = "초등학교"
	case middle = "중학교"
	case high 
	case college 
	case university 
	case graduate = "대학원"
}

let highestEducationLevel: School = School.university
print("저는 \(highestEducationLevel.rawValue)에 다니고 있습니다.")
> 저는 university에 다니고 있습니다.
```

```swift
enum Numbers: Int {
    case zero
    case one
    case two
    case ten = 10
}
print(Numbers.zero.rawValue)
> 0
// 정수 타입의 원시 값을 갖게되면 첫 항목이 기준으로 0부터 1씩 늘어난 값을 갖는다.
```

열거형의 원시 값 정보를 안다면 원시 값을 통해 열거형 변수 또는 상수를 생성해줄 수 있다.
```swift
let primary = School(rawValue: "유치원") // primary
let graduate = School(rawValue: "석박사") // nil

```

### 4.5.3 연관 값
```swift
enum MainDish {
    case pasta(taste: String)
    case pizza(dough: String, topping: String)
    case chicken(withSauce: Bool)
    case rice
}

*var* dinner: MainDish = MainDish.pasta(taste: "토마토")
dinner = .pizza(dough: "치즈크러스트", topping: "파인애플")
dinner = .chicken(withSauce: *true*)
dinner = .rice
```

```swift
// 연관 값을 제한하고 싶을 때
*enum* PastaTaste {
    *case* cream, tomato
}

*enum* PizzaDough {
    *case* cheeseCrust, thin, original
}

*enum* PizzaTopping {
    *case* pepperoni, cheese, bacon
}

*enum* MainDish {
    *case* pasta(taste: PastaTaste)
    *case* pizza(dough: PizzaDough, topping: PizzaTopping)
    *case* chicken(withSauce: Bool)
    *case* rice
}
```

### 4.5.4 순환 열거형
> 열거형 항목의 연관 값이 자기 자신일 때 사용.  
* enum 키워드 앞에 indirect 키워드 사용
* 특정 항목에만 한정하고 싶다면  case 키워드 앞에 indirect 사용
```swift
*enum* ArithmeticExpression {
    *case* number(Int)
    *indirect* *case* addition(ArithmeticExpression, ArithmeticExpression)
    *indirect* *case* multiplecation(ArithmeticExpression, ArithmeticExpression)
}

*let* five = ArithmeticExpression.number(5)
*let* four = ArithmeticExpression.number(4)
*let* sum = ArithmeticExpression.addition(five, four)
*let* final = ArithmeticExpression.multiplecation(sum, ArithmeticExpression.number(2))

*func* evaluate(*_* expression: ArithmeticExpression) -> Int {
    *switch* expression {
    *case* .number(*let* value):
        *return* value
    *case* .addition(*let* left, *let* right):
        *return* evaluate(left) + evaluate(right)
    *case* .multiplecation(*let* left, *let* right):
        *return* evaluate(left) * evaluate(right)
    }
}

*let* result: Int = evaluate(final)
print(result)
> 18
```



#swift/book-programming