# 5. 연산자
스위프트는 특정 연산자의 역할을 변경할 수 있다.

## 5.1 연산자의 종류
### 비교 연산자
참조가 같다 : A === B 
참조가 같지 않다. : A !== B 
패턴매치 : A ~= B
```swift
// 패턴 매치 예시
let number = 9
if number >= 0 && number <= 100 {
    print("result = \(number)")
}

if 0...100 ~= number {
    print("result = \(number)")
}

```

### 범위 연산자
* 폐쇄 범위 연산자 : A ... B 
A부터 B까지 범위. A와 B를 포함.
* 반폐쇄 범위 연산자 : A ..< B
A부터 B미만까지 범위 A 포함 B 미포함
* 단방향 범위 연산자
A … : A 이상의 범위 A 포함
… A : A 이하의 범위 A 포함
.. < A : A 미만의 범위 A 미포함

### 비트 연산자
* NOT 비트 연산자 : ~A
: A 의 비트를 반전한 결과 반환
* AND 비트 연산자 : A & B
: A와 B 비트 AND 논리 연산 실행
* OR 비트 연산자 : A | B
: A와 B 비트 OR 논리 연산 실행
* XOR 비트 연산자 : A ^ B
: A와 B 비트 XOR 논리 연산 실행
* 비트 이동 연산자 : A >> B , A << B
:  A의 비트를 B만큼 비트를 이동합니다.

### 오버플로 연산자
오버플로 더하기 : &+
오버플로 빼기 : &-
오버플로 곱하기 : &*

```swift
var integer: UInt8 = 0
let value: UInt8 = integer - 1 // error
let result: UInt8 = integer &- 1

print(result)
> 255
```

### 기타 연산자
* nil 병합 연산자 : A ?? B
A가 nil이 아니면 A를 반환, nil이면 B 반환
* 부호변경 연산자 :  -A
A의 부호를 변경
* 옵셔널 강제 추출 연산자 : Object!
* 옵셔널 연산자 : Object?
```swift
let value: Int? = 2
let result: Int = value != nil ? value : 0

let result2: Int = value ?? 0

// result와 result2는 같은 표현이다.
```

## 5.2 연산자 우선순위와 결합방향
## 5.3 사용자정의 연산자
> = 과 ?: 연산자 이외에는 역할을 수정할 수 있습니다.  
기존 연산자의 역할을 변경하거나 새로운 역할을 추가하기 위해서는 전위, 중위, 후위 연산자인지 알아야 한다.
* prefix : 전위
* infix : 중위
* postfix : 후위
* operator :  연산자
* associativity :  연산자 결합방향
* precedence : 우선순위

?, !는 사용자 정의 연산자에 포함시킬 수 있지만 그 자체만으로는 안된다.
전위 연산자는 ?,!로 시작하는 사용자정의 연산자를 만들 수 없다.

토큰 : =, - >,  , _*_ */,.,
전위 연산자 : <, &, ?
중위 연산자 : ?
후위 연산자 : > ,!, ?
들도 재정의 할 수 없고, 사용자정의 연산자로 사용할 수 없다.

### 5.3.1 전위 연산자 정의와 구현
Int 타입의 제곱을 구하는 연산자로 을 전위 연산자로 사용하려고 한다.

1. 연산자 정의 (새로운 연산자의 경우에만)
```swift
prefix operator **
```

2. 연산자 구현
```swift
prifix func **(value: Int) -> Int {
	return value * value
}
```

중복 정의도 가능
```swift
prefix func **(value: String) -> String {
	return value + " " + value
}
```

### 5.3.2 후위 연산자 정의와 구현
1. 연산자 정의
```swift
postfix operator **
```
2. 연산자 구현
```swift
postfix func **(value: Int) -> Int {
	return value + 10
}
```

**하나의 피연산자에 전위 연산과 후위 연산을 한 줄에 사용하게 되면 후위 연산을 먼저 수행한다.**
```swift
prefix operator **
postfix operator **

prifix func **(value: Int) -> Int {
	return value * value
}
postfix func **(value: Int) -> Int {
	return value + 10
}

let five: Int = 5
let sqrtFivePlusTen: Int = **five**
print(sqrtFivePlusTen)
> 225
// (5 + 10) * (5 + 10)
```

### 5.3.3 중위 연산자 정의와 구현
중위 연산자는 우선순위 그룹을 명시해줄 수 있다.

* 연산자 우선순위 정의하기
```swift
precedencegroup : [우선 순위 그룹 이름] {
	higherThan : [더 낮은 우선 순위 그룹 이름]
	lowerThan : [더 높은 우선 순위 그룹 이름]
	associativity : [결합방향(left / right / none) 
	// default : none
	assignment: [할당방향 사용(true / false)]
}
// 우선순위를 명시하지않으면 가장 높은 DefaultPrecedence로 지정된다.
```
결합방향이 있는 연산자는 1 + 2 + 3과 같이 연산가능
결합방향이 없는 연산자는 1 < 2 < 3과 같은 연산 불가능

1. 연산자 정의
```swift
infix operator ** : MultiplicationPrecedence
```

2. 연산자 구현
```swift
import Foundation // String의 contains 메서드 사용

func **(lhs: String, rhs: String) -> Bool {
	return lhs.contains(rhs)
}
```

**사용자정의 연산자는 Class, Struct 내부에 구현할 수 있습니다.**

#swift/book-programming