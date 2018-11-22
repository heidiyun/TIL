# 22. 인텐트와 태스크
> 암시적 인텐트를 사용해서 안드로이드 기본 런처앱을 대체하는 애플리케이션을 만들 것이다.  

```kotlin
fun setupAdapter() {
    val startupIntent = Intent(Intent./ACTION_MAIN/)
    startupIntent.addCategory(Intent./CATEGORY_LAUNCHER/)

    val pm = /activity/?./packageManager/
val activities = pm?.queryIntentActivities(startupIntent, 0)

    Log.i(TAG, "Found ${activities?.size} activities.")
}
```

MAIN_LAUNCHER 인텐트 필터는 startActivity(…)로 전달되는 MAIN_LAUNCHER 암시적 인텐트와 일치할 수 도 있고 일치하지 않을 수도 있다.

startActivity(intent)는 “이 암시적 인텐트와 일치하는 액티비티를 시작시켜라. “를 의미하는 것이 아니라 “이 암시적 인텐트와 일치하는 디폴트 액티비티를 시작시켜라”를 의미한다.
암시적 인텐트를 stsartActivity()를 통해 전달할 때 안드로이드 운영체제는 내부적으로 Intent.CATEGORY_DEFAULT 카테고리를 인텐트에 추가한다. 

따라서 startActivity(…)로 전달되는 암시적 인텐트와 일치하는 인텐트 필터를 원한다며 그 인텐트 필터에 DEFAULT 카테고리를 포함시켜야 한다. 

MAIN/LAUNCHER ㅊ인텐트 필터를 갖는 액티비티는 CATEGORY_DEFAULT  카테고리를 포함시킬 필요가 없다. 그러므로 이러한 액티비티들도 암시적 인텐트의 요청에 응답할 수 있게 하려면 startActivity(...) 대신 PackageManager를 직접 쿼리하는 인텐트를 사용해야 한다. 

액티비티 라벨은 사용자가 알 수 있도록 표시하는 이름이다.
앱의 시작될 때 실행되는 런처 액티비티의 경우에는 라벨이 애플리케이션 이름과 동일하다.
액티비티 라벨은 PackageManager가 반환하는 ResolveInfo 객체에서 알 수 있다. 
```kotlin
val pm = /activity/?./packageManager/
val appName = resolveInfo.loadLabel(pm).toString()
```

## 런타임 시에 명시적 인텐트 생성하기
실행할 액티비티를 정확히 알고 있을 때는 명시적 인텐트를 사용하여 시작시킬 수 있다.
명시적 인텐트를 생성하려면 ResolveInfo로부터 해당 액티비티의 패키지 이름과 클래스 이름을 가져와야 한다. 
-> ResolveInfo의 필드로 포함된 ActivityInfo 내부 클래스 인스턴스로 부터 얻을 수 있다. 

```kotlin
val activityInfo = resolveInfo.activityInfo
val intent = Intent(Intent./ACTION_MAIN/).setClassName(activityInfo.applicationInfo.packageName,
        activityInfo.name)
startActivity(intent)
```

componentName :  패키지 이름 + 클래스 이름
intent를 생성하기 위해 Activity와 Class를 인자로 전달하면 생성자에서 Context 인자로 전달된 Activity로 부터 전체 경로의 패키지 이름을 결정한다.

우리 애플리케이션에서는 setClassName 메소드를 사용하여 컴포넌트 이름을 내부적으로 생성해주었다.

## Task & Back Stack
안드로이드는 실행 중인 각 애플리케이션 내부에 사용자의 상태를 기록 유지하기 위해 태스크를 사용한다. 

### Task
> 사용자와 관련되는 액티비티들을 갖는 스택이다.   
스택의 제일 아래에 있는 액티비티를 기본 액티비티(base activity)라고 한다.
제일 위에 있는 액티비티가 사용자가 현재 화면으로 보고 있는 액티비티가 된다. 

기본적으로 새로운 액티비티들은 현재의 태스크에서 시작된다. 
CriminalIntent 애플리케이션에서 범죄 용의자를 선택하기 위해 연락처 액티비티를 시작했을 때처럼 액티비티가 CriminalIntent 앱의 일부가 아니더라도 현재 태스크에서 시작된다. 

**새로 시작되는 액티비티를 현재의 태스크에 추가할 때의 장점**
사용자가 Back 버튼을 눌렀을 때 현재 애플리케이션의 계층구조 대신 태스크를 통해서 이전에 사용하던 앱으로 돌아갈 수 있다. 

### 태스트 전환하기
오버뷰(overview) 화면을 사용하면 각 태스크의 상태에 영향을 주지 않고 태스크 간 전환을 할 수 있다. 
-> 최근 화면, 최근 앱 화면
ex) 새로운 연락처를 입력하다가 트위터 피드의 확인으로 전환한다면 두 개의 태스크가 시작된 것이다. 이 때 다시 연락처 입력으로 돌아오면 앞의 두 태스크 모두 스택에 저장된다.
오버뷰 화면에서 보이는 것은 각 앱의 태스크가 태스크 리스트로 나타나는 것이다. 

사용자는 오버뷰 화면에서 각 항목을 닫아서 해당 앱의 태스크를 삭제할 수 있다. 태스크가 삭제되면 해당 애플리케이션의 Back Stack에 들어 있는 애플리케이션의 모든 액티비티가 제거된다.

### 새로운 태스크 시작하기
애플리케이션(1)에서 다른 애플리케이션(2)을 시작시키면 애플리케이션(1)의 태스크에 애플리케이션(2)가 포함된다. 

다른 애플리케이션을 시작시킬 때 새로운 태스크로 시작시키려면 어떻게 해야 할까.
```kotlin
val intent = Intent(Intent./ACTION_MAIN/).setClassName(activityInfo.applicationInfo.packageName,
        activityInfo.name).addFlags(Intent./FLAG_ACTIVITY_NEW_TASK/)

```

시작시키려는 intent에 **Intent.FLAG_ACTIVITY_NEW_TASK**flag를 추가해주면 된다.
이 flag에 의해 액티비티당 하나의 태스크만 생성되기 때문에, 기존에 실행중인 태스크가 있으면 기존 태스크로 전환된다.

## NerdLauncher를 홈 화면으로 사용하기.
```xml
<intent-filter>
    <category android:name="android.intent.category.HOME"/>
    <category android:name="android.intent.category.DEFAULT"/>
</intent-filter>

```

해당 앱을 홈화면으로 쓰고 싶다면, AndroidManifest.xml에서 activity의 intent-filter에 위의 두개 카테고리를 추가하자.

## 프로세스 vs 태스크
안드로이드의 프로세스는 애플리케이션 객체들이 메모리에 존재하면서 실행될 수 있도록 운영체제가 생성하는 것이다. 

프로세스는 운영체제에 의해 관리되는 자신의 리소스를 가질 수 있다.
ex)  메모리, 네트워크 소켓, 파일...
프로세스는 최소한 하나 이상의 실행 스레드를 갖는다.

모든 액티비티 인스턴스는 정확히 하나의 프로세스에 존재하며, 하나의 태스크로 참조된다.
그러나 태스크는 액티비티만 포함하며, 서로 다른 애플리케이션 프로세스에 존재하는 액티비티들로 구성된다. 반면에 프로세스는 한 애플리케이션의 모든 실행 코드와 객체들을 포함한다.

액티비티를 참조하는 태스크는 액티비티가 존재하는 프로세스와 다를 수 있다.
CriminalIntent 애플리케이션에서 연락처 앱을 실행했을 경우 액티비티를 참조하는 태스크는 CriminalIntent 것이다. 그러나, 연락처 액티비티 인스턴스는 연락처 앱 프로세스의 메모리 영역에 생성된다.

## 동시 문서
롤리팝 이상 버전의 장치: 
CriminalIntent의 범죄 보고서 전송을 위해 앱을 선택할 때 우리가 선택한 앱의 액티비티가 CriminalIntent의 태스크가 아닌 자신의 별도 태스크로 추가된다.

롤리팝 이상 버전에서는 암시적 인텐트 선택기가 별개의 새로운 태스크로 액티비티를 생성한다.
해당 액티비티는 android.intent.action.SEND 또는 action.intent.action.SEND_MULTIPLE 액션을 갖고 론칭되는 액티비티다. 

롤리팝부터는 동시 문서(concurrent document)라는 새로운 개념을 사용한다.
동시 문서에서는 런타임 시에 앱의 태스크를 몇 개라도 동적으로 생성할 수 있다.
롤리팝 이전에는 사전 정의된 태스크들만 가질 수 있었으며, 그것들의 이름이 매니페스트에 정의되어야만 했다.

롤리팝 이상 버전의 장치에서 실행 중인 앱에서는 복수 개의 태스크를 시작시킬 수 있다.
 startActivity를 호출하기 전에 
Intent.FLAT_ACTIVITY_NEW_DOCUMENT  플래그를 추가하면 된다.
```xml
<application
    android:documentLaunchMode="intoExisting">

```
또한 AndroidManifest.xml에 추가한다. 

단, 이 방법을 사용하면 한 무서당 하나의 태스크만 생성된다. 
그러나 지정된 문서에 이미 태스크가 존재하더라도 항상 새로운 태스크가 생성되게 할 수 있다.

Intent.FLAT_ACTIVITY_NEW_DOCUMENT 플래그와 함께 Intent.FLAG_ACTIVITY_MULTIPLE_TASK 플래그를 추가하면 된다. 

#android/책