# iOS
UIApplication -> app 전체의 상태 관리
앱의 상태가 변경되었음을 통보받는 AppDelegate를 만들어야 한다.  (Java의 Listener/ 인터페이스 형태로 약속된다. )
UIApplicationDelegate가 앱의 상태 변화를 관리.  UI Window 생성 담당.
	-> UIViewController
		-> UIView

Java interface는 모든 메소드를 반드시 오버라이딩 해야 하는데,
ObjC protocol은 optional  메소드가 있어서 해당 메소드는 반드시 오버라이딩 하지 않아도 된다.
 -> 동적 디스패치 (Duck Typing) 

정적 다형성 : 컴파일 타임에 검사가 끝나서, 부모 메소드에 오버라이딩하고자 하는 메소드가 없으면 에러가 난다.

동적 다형성 : Duck Typing
장점 : 코드를 작성하는 데에 있어 유연하다. (독립된 객체여도 타입의 상관없이 동일한 함수를 호출할 수 있다.)
단점 : 코드가 어디서 잘 못 됐는지 모른다.

Unrecognized selector -> 등록하지 않은 함수 호출.
a 라는 receiver에 foo라는 메시지를 보내고 그 동작을 수행하는 것에 대한 것을 selector????라고 부른다. 

Storyboard 에는 UIview제공

info.plist - Launch screen interface file base 로 아이폰4와 아이폰5를 나눈다.

Storyboard에서 rootview연결의 책임은 storyboard가 가진다.

Storyboard 
	-> 많은 코드가 사라진다.
	-> 많은 xib 파일이 사라진다.
단점 : conflict 확률이 높다. (동시 작업이 힘들다.)
	-> Storyboard를 분리할 수 있다. 
		도메인 마다 분리한다..

## 180103 
iOS - Cocoa Touch Framework
Cocoa Design Pattern : 이벤트를 처리하는 방법 3가지
1) Target-Action
2) Delegate Pattern : 내부적으로 상태가 여러가지 일 때. (인터페이스를 기반으로 하는 리스너 타입)
3) NotificationCenter

UICompnent
	-> PickerView
	-> TableView / CollectionView (타일 형태)
		-> Delegate Pattern으로 이벤트를 처리한다.

## 180110
ios는 objective c 라이브러리를 바로 쓸 수 없다. 브리지를 제공해주는 라이브러리만 사용할 수 있다.

의존성 관리
1. Cocoapods - 스위프트 / objc
2. carthage - 스위프트
3. Swift package manager - ?

Android 
Retrofit - okHttp - son

Swift
AFNetworking(Alamofire) - objc
Alamofire

라이브러리 - 보일러 플레이트를 줄여 줄 수 있거나 편의 기능을 제공한다
		 지속적으로 유지 보수 되는지 확인해야 한다.

실행파일이 라이브러리에 의존한다.
라이브러리 = 함수 + 클래스의 집합
* 동적
라이브러리가 업데이트 되어도 다시 컴파일 할 필요가 없다.
* 정적
실행파일이 의존하고 있는 library가 없을 수도 있다.

Ios -> 
Library + Header + Etc-> framework

일련의 흐름을 가지고 구동하는 프로그램

1. Sudo gem  install cocoapods
2. Brew install Carthage

생성한 xcode 폴더에 들어가서
```
$ Pod init
$vi popfile
	미니멈 ios 주석 풀고 10.0으로 작성
	pod 'Alamofire', '~> 5.0.0.beta.1' (alamofire - cocoapods install)
저장
pod install
```
소스까지 포함하기 때문에 빌드할때마다 같이 빌드

```
kingfisher - carthage로 적용하기
$vi Cartfile
	github "onevcat/Kingfisher" ~> 5.0
$ xcdoe 설정 locations - command Line Tools
$ carthage update
$ carthage update --platform ios
xcode - Linked frameworks  and libraries
이렇게 추가하면 컴퓨터엔 있는데 폰에는 없어서
Embedded binaries에 추가해야 한다.
이러면 같이 빌드되지는 않지만 같은 실행 파일로 들어간다.
```
빌드 속도에 영향을 안준다.


Android
	Project
		module

SampleApp(Project)
	workspace(SampleApp.scworkspace)
		project(SampleApp.xcodeproj)
		project(Pods)

Visual
	solution
		project

Android - Glide
IOS - Kingfisher

#ios