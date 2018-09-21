# 6. 안드로이드 SDK 버전과 호환성
## 안드로이드 SDK 버전
![android SDK version](https://i.stack.imgur.com/XMLou.png)

## 호환성과 안드로이드 프로그래밍
안드로이드 버전이 계속 업데이트 되지만, 구 버전을 쓰는 사용자도 많기 때문에 호환성이 안드로이드 프로그래밍에 중요한 문제이다. 

안드로이드의 SDK 버전 속성은 app/build.gradle 에서 설정한다.

```
android {
    compileSdkVersion 28
    defaultConfig {
        minSdkVersion 22
        targetSdkVersion 28
    }
```

### 최소 SDK 버전
앱을 설치하는 기준인 최소한의 안드로이드 버전이다. 
최소 SDK 버전 이상의 장치에서 애플리케이션을 설치하고 실행할 수 있다.

### 목표 SDK 버전
개발된 애플리케이션이 실행되는 API 레벨을 안드로이드에 알려준다. 
(개발자가 실제로 개발할 때 사용하는 코드의 API 레벨)
대부분 이 값은 가장 최신 안드로이드 버전으로 사용한다.
앱이 하위 버전에서도 여전히 잘 동작하는지 확인할 때 목표 SDK를 하위버전으로 지정한다. 

### 컴파일 SDK 버전
안드로이드의 기능들은 SDK의 클래스와 메소드로 구현된다. 
컴파일 SDK 버전은 코드를 빌드할 때 사용할 버전을 나타낸다. 
즉,  import 문으로 참조되는 클래스나 메소드를 찾을 때 어떤 버전의 SDK에서 찾을 것인가를 결정한다.

### 주의할 점
컴파일 SDK 버전과 목표 SDK 버전은 반드시 맞춰줘야 한다.
-> 최신 버전으로 유지하는 이유는` 더 효율적으로 컴파일하고 코드를 생성할 수 있기 때문이다.

### support library
> 이전 버전과 호환되는 새 기능을 버전을 제공한다.   
이전 버전의 Android 플랫폼에서 실행 중인 앱이 새 버전의 플랫폼에서 사용 가능하도록 기능을 지원해준다. 
ex) Android 5.0 이전 버전은 material design 요소를 표시할 수 없지만 해당 라이브러리를 사용하면 material dsign에 대해 지원해준다.

## 상위 버전의 API 코드를 안전하게 추가하기
현재 실습중인 GeoQuiz/QuizApp은 컴파일 SDK와 최소 SDK 버전이 차이가 나므로 호환성 문제를 고려해야 한다.
최소 SDK 버전 보다 상위 버전의 코드를 사용하면 안드로이드 Lint 에러를 감지하여 알려준다.
ex ) Call requires API level 21 (current min is 16):

이런 에러 메시지가 떴을 경우, 최소 SDK버전을 올리는 것은 호환성 문제를 완전히 해결하는 방법은 아니다. 이 경우에는 장치의 안드로이드 버전을 확인하는 조건문으로 상위 버전의 API 코드를 둘러싸도록 하자.

```java

/* 현재 최소 SDK 버전이 16이고, if 문 안의 코드가 그 이상의 API 레벨을 사용하는 코드라면 */

if (Build.VERSION./SDK_INT/>= Build.VERSION_CODES./LOLLIPOP/) {
} else {    
}

```

* Build.VERSION.SDK_INT :  현재 장치의 안드로이드 버전

[개발자 문서 참조하기](http://developer.android.com/)

**질문 최소 SDK 버전보다 목표 SDK 버전이 다른건 괜찮은가?**





#android/책