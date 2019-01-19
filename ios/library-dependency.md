#  IOS Library 의존성
> CocoaPod, Carthage   
ios는 objective c 라이브러리를 바로 쓸 수 없다. 브리지를 제공해주는 라이브러리만 사용할 수 있다.

## 의존성 관리
1. Cocoapods - swift / objc
2. carthage - swift
3. Swift package manager - swift

* Android 
Retrofit - okHttp - son
* Swift
 AFNetworking(Alamofire) - objc (x)
Alamofire

### 라이브러리  (함수 + 클래스의 집합)
Windows - DLL (Dynamic Linking Library)
Linux - SO(Shared Object)
macOS - dylib
* 장점 : 보일러 플레이트를 줄여 줄 수 있거나 편의 기능을 제공한다
* 단점 : 실행파일이 라이브러리에 의존하기 때문에, 지속적으로 유지 보수 되는지 확인해야 한다.

* 동적 라이브러리
라이브러리가 업데이트 되어도 다시 컴파일 할 필요가 없다.
실행파일의 크기가 늘어난다.
* 정적 라이브러리
실행파일의 크기가 줄어든다.
실행파일이 의존하고 있는 라이브러리가 없을 수도 있다.

### Framework
Android에서 Framework는 일련의 흐름을 가지고 구동하는 프로그램이라는 의미를 가지고 있습니다.
하지만, IOS에서는 Library와 Header 그리고 그 외의 필요한 것들을 모두 합친것을 Framework라고 부릅니다. 
(Library + Header + Etc-> framework)

## 실습
 특정 버튼을 누르면 url을 이용하여 네트워크로 이미지를 다운받아 ImageView로 보여줍시다.
1. URLSession 사용하기.
각 단계별로 에러 처리를 해주어야 합니다.
```swift
 *@IBAction* *func* download(*_* sender: UIButton) {
        *guard* *let* url = URL(string : "https://pbs.twimg.com/profile_images/917378812056182791/VACPa125.jpg") *else*{
            *return*
        }
        /// 코드의 중첩을 없앴다./
        *let* session = URLSession.shared
        /// Closure를 만들때 항상 마지막 인자로 error 객체가 전달된다/
        *let* downloadTask = session.dataTask(with: url) { (data, response, error) *in*
            *guard* error == *nil* *else* {
                print(error?.localizedDescription ?? "")
                *return*
            }
            
            *guard* *let* response = response *as*? HTTPURLResponse *else* {
                *return*
            }
            
            print("statusCode: \(response.statusCode)")
            
            *guard* (200..<300 ~= response.statusCode) *else* {
                *return*
            }
            
            *guard* *let* data = data *else* {
                *return*
            }
            
            /// Android Main Thread를 통해서 처리하는 방법/
            /// Activity/
            /// Handler 를 Activity가 포함하고 있으므로 runOnUiThread()/
            /// 2. RxAndroid/
            /// AndroidScheduler.main/
            
            /// IOS/
            /// 1. GCD Library (Grand Central Dispatch)/
            /// 2. RxCocoa/
            ///  Main Thread Scheduler/
            
            DispatchQueue.main.async {
                *self*.imageView.image = UIImage(data: data)
            }   
        }
        downloadTask.resume()
    
    }
```

2. Alamofire 이용하기 (CocoaPods를 이용하여 의존성을 추가하자.)
CocoaPods를 이용하면 Alamofire Project를 생성해줍니다. (모든 소스를 포함한다.)
정적 라이브러리의 형태로 추가가 된것이며, 우리의 애플리케이션을 빌드할 때마다 라이브러리도 함께 빌드되어 실행파일에 추가됩니다.
* CocoaPods 설치
생성한 xcode 폴더에 들어가서
```
$ Sudo gem  install cocoapods
$ Pod init
$vi popfile
	미니멈 ios 주석 풀고 10.0으로 작성
	pod 'Alamofire', '~> 5.0.0.beta.1' (alamofire - cocoapods install)
저장
$ pod install
```

```swift
 *@IBAction* *func* download(*_* sender: UIButton) {
        *guard* *let* url = URL(string : "https://pbs.twimg.com/profile_images/917378812056182791/VACPa125.jpg") *else*{
            *return*
        }
        
        /// Optional : Monad/
///         var n: Int? = nil/
///        switch n {/
///        case .none:/
///            <#code#>/
///        case .some(let value)/
///        }/
        
        
        AF.request(url)
            .validate(statusCode: 200..<500) /// 성공 실패의.code를 정할 수 있다./
            .responseData { (response) *in*
            
            *switch* (response.result) {
            *case* .success(*let* data): *do* {
///                DispatchQueue.main.async {/
                    *self*.imageView.image = UIImage(data: data)
///                }/
                }
            *case* .failure(*let* error): *do* {
                    print(error.localizedDescription)
                }
            }
        }
        
    }
```

3. Kingfisher 사용하기(Carthage를 이용하여 의존성을 추가하자.)
안드로이드에 Glide가 있다면 IOS에는 Kingfisher가 있습니다.
Carthage를 이용하면 모든 빌드가 끝난 상태로 우리의 애플리케이션에 추가되므로, 빌드 할 때마다 같이 빌드되지 않습니다. (동적 라이브러리)
따라서, 빌드 속도가 더 빨라질 수 있습니다.
다만, 애플리케이션을 AppStore에 등록할 때 추가적인 작업이 필요합니다.
```
/* 생성한 xcode 폴더로 경로 이동 */
$ brew install Carthage
kingfisher - carthage로 적용하기
$vi Cartfile
	github "onevcat/Kingfisher" ~> 5.0
$ xcdoe 설정 locations - command Line Tools
$ carthage update
$ carthage update --platform ios
xcode - Linked frameworks and libraries
이렇게 추가하면 컴퓨터엔 있는데 폰에는 없어서 error가 발생하는 경우가 있습니다.
그럴경우에는 Embedded binaries에 추가해야 한다.
이러면 같이 빌드되지는 않지만 같은 실행 파일로 들어간다.
```

```swift
 *@IBAction* *func* download(*_* sender: UIButton) {
        *guard* *let* url = URL(string : "https://pbs.twimg.com/profile_images/917378812056182791/VACPa125.jpg") *else*{
            *return*
        }
        imageView.kf.setImage(with: url)
			// extension의 형태로 메소드를 제공합니다.
    }
    
```

Android
	* Project
		* module

IOS
	* workspace(SampleApp.scworkspace)
		* project(SampleApp.xcodeproj)
		* project(Pods)

VisualStudio
	* solution
		* project

#ios