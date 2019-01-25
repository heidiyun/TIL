# 2. 스위프트 처음 시작하기
## 2.1 기본 명명 규칙
* 이름은 유니코드에서 지원하는 어떤 문자라도 사용할 수 있다. 
**예외**
	1. 스위프트에서 정한 키워드
	2. 연산자로 사용하는 기호
	3. 숫자로 시작하는 이름
	4. 공백이 포함된 이름
* 함수, 메소드, 인스턴스 이름은 소문자로 시작합니다.(Lower Camel Case)
* 클래스, 구조체, 익스텐션, 프로토콜, 열거형 이름은 첫 글자를 대문자로 사용합니다. (Uppder Camel Case)
* 대소문자를 구별합니다.

## 2.2 콘솔 로그
로그 : 애플리케이션 상태 또는 내부 로직의 흐름을 관찰할 수 있도록 출력한 저보

콘솔 로그 : 디버깅 중 디버깅 콘솔에 보여줄 로그
print(), dump()

### 2.2.1  print() 함수
‘\n’을 자동으로 삽입해서 줄바꿈을 해준다.

#### dump() 함수와 차이점
print() : 디버깅 콘솔에 간략한 정보 출력
	      출력하고자 하는 인스턴스의 description 프로퍼티에 해당하는 내용
dump() : 디버깅 콘솔에 자세한 정보 출력
		 출력하고자 하는 인스턴스의 자세한 내부 콘텐츠

```swift
struct BasicInformation {
	let name: String
	var age: Int
}
var yagomInfo: BasicInformation = BasicInformation(name: "yagom", age: 99)

class Person {
	var height: Float = 0.0
	var weight: Float = 0.0
}

let yagom: Person = Person()
yagom.height = 182.5
yagom.weight = 78.5

print(yagomInfo)
> BasicInformation(name: "yagom", age: 99)
dump(yagomInfo)
> BasicInformation
	- name: "yagom"
	- age: 99

```

### 2.2.2 문자열 보간법(String Interpolation)
> 변수 또는 상수 등의 값을 문자열 내에 나타내고 싶을 때 사용한다.  
문자열 내에 ‘\’의 형태로 표기하면 문자열로 치환해줍니다.
``` swift
let name: String = "heidi"
print("My name is \(name)")
> My name is heidi
```

## 2.3 주석
> 프로그램 소스 코드에 정보를 남기는 목적  
Xcode에는 말풍선의 형태로 레퍼런스 문서의 요약된 내용을 보여주는 퀵헬프라는 기능이 있다.
option + 키워드

### 2.3.1 주석 남기기
* 한 줄 주석
`//`
* 여러 줄 주석
`/* */`
* 중첩 주석
```
/*
//
*/
```

### 2.3.2 마크업 문법을 활용한 문서화 주석
> 변수, 상수, 클래스 등을 설명하고자 하는 경우 일정하 ㄴ마크업 형식에 따라 주석을 작성하면 퀵헬프를 통해 확인할 수 있습니다.  
Editor -> Structure -> Add Documentation (command + option + /)

* 한 줄 주석 :  -> /
* 여러 줄 주석 : _* _** -> **_* _
* 텍스트 간에 한 줄을 비워놓으면 줄바꿈이 된다.
* ‘-‘, ‘+’, ‘*’를 사용하여 원형 글머리 기호를 사용할 수 있습니다.
* -를 세 개 이상 사용하면 긴 줄로 문단을 나눠준다.
* 기울기 `* Text *`
* 굵게 `__Text__`
* 다른 텍스트보다 네 칸 이상 들여쓰기하면 코드 블록을 만들어준다. 

더 자세한 마크업 문법은 아래 링크를 참고하세요.
[Markup Formatting Reference: Markup Overview](https://developer.apple.com/library/archive/documentation/Xcode/Reference/xcode_markup_formatting_ref/index.html)

## 2.4 변수와 상수
> 변수 또는 상수를 사용해 데이터를 메모리에 임시로 저장합니다.  

### 2.4.1 변수
var 키워드를 사용합니다.
```swift
var name: String = "heidi"
// var [변수 이름]: [데이터 타입] = [값]
```

**데이터 타입을 생략하면 컴파일러가 타입을 추론하여 지정합니다.**
```swift
var name = "heidi"
```

### 2.4.2 상수
let 키워드를 사용합니다.
```swift
let num: Int = 1
// let [상수 이름]: [데이터 타입] = [값]
```


#swift/book-programming