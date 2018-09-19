# 5. 두 번째 액티비티 만들기
## 새로운 액티비티 생성하기
방법 
1. Java/new/Java Class
2. 액티비티 생성 위저드 사용
	Java/new/Activity/Empty Activity
* activity class, activity layout, AndroidManifest에 activity 자동 등록

레이아웃 속성
1. `tools:text`
디자인 시에 지정된 값이 미리 보기에는 나타나지만 앱을 실행할 때는 나타나지 않는다.
2. preview에서 landscape로 설정하면 가로 방향에서 레이아웃이 어떻게 나타나는지 확인 할 수 있다.
	
## 액티비티 시작시키기
> public void startActivity(Intent: intent)  
> fun startActivity(intent: Intent)  
> 안드로이드 운영체제에 해당 액티비티를 실행시켜 줘! 요청  

startActivity(…)을 호출하면 ActivityManager의 컴포넌트로 전달이 된다. 
ActivityManager는 전달 받은 인텐트의 액티비티 인스턴스를 생성한다.
생성한 인스턴스의 onCreate(...)함수를 호출한다.

### 인텐트로 통신하기
> 인텐트는 컴포넌트가 운영체제와 통신하기 위해 사용하는 객체다.  

이 예제에서는 액티비티를 ActivityManager에 알려주기 위해 인텐트를 사용한다.
```java
/* java */
Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
startActivity(intent);

/* kotlin */
val intent = Intent(this@QuizActivity, CheatActivity::class.java)
startActivity(intent)
```

실행 될 Activity가 AndroidManifest에 선언되어 있지 않으면 ActivityNotFoundException이 발생한다.

> 명시적 인텐트는 애플리케이션 내부의 액티비티를 시작하기 위해 사용  
> 암시적 인텐트는 애플리케이션 외부의 액티비티를 시작하기 위해 사용  

## 액티비티 간의 데이터 전달
> Intent 객체의 extra를 사용해서 데이터를 전달한다.  

extra는 자료구조 Map과 비슷하게 key와 value 한 쌍으로 이루어진다.
```java

/* 데이터를 전달할 때 */
public Intent putExtra(String name, boolean value)
// 사용예
Intent intent = new Intent(context, CheatActivity.class);
intent.putExtra(name, value);

/* 데이터 받을 때 */
public boolean getBooleanExtra(String name, boolean defaultValue)
// 이 외에도 value의 type별로 메소드가 존재한다.

// 사용예
boolean answer = getIntent().getBooleanExtra(name, false);
```

* getIntent() 는 startActivity의 인자로 전달되었던 객체를 반환한다.

## 자식 액티비티로부터 결과 돌려받기
자식 액티비티로부터 결과를 받고싶을 때에는 startActivity(...) 대신, 
startActivityForResult(…)를 사용한다.
```java
public void startActivityForResult(Intent intent, int requestCode)
// requestCode는 사용자가 정의한 정수이다.
// 어떤 액티비티에서 오는 결과인지 확인할 때 사용한다.
```

**자식 액티비티에서 결과코드 주기**
`public final void setResult(int resultCode)`
resultCode는 일반적으로 사전에 정의된 상수를 사용한다. 
자식 액티비티가 어떻게 끝났는지에 따라 부모 액티비티에서 다른 액션을 취할 필요가 있을 때 유용하다.
1. Activity.RESULT_OK (정수 -1)
2. Activity.RESULT_CANCELED (정수 0)

꼭 setResult(…) 메소드를 호출하지 않아도 운영체제가 디폴트 값을 부모 액티비티에 반환해 준다. 
그때의 resultCode는 Activity.RESULT_CANCELED이다.

**자식 액티비티에서 데이터 주기**
```java
public final void setResult(int resultCode, Intent data)
// 사용예
/* Java */
Intent data = new Intent();
data.putExtra(key name, value);
setResult(RESULT_OK, data);

/* Kotlin */
val data = Intent()
data.putExtra(key name, value)
setResult(RESULT_OK, data)
```

**부모 액티비티에서 데이터 받기**
```java
protected void onActivityResult(int requestCode, int resultCode, Intent data)
```

## 안드로이드가 액티비티를 어떻게 알까.
애플리케이션을 실행하면 안드로이드 운영체제는 애플리케이션의 launcher activity를 실행시킨다. Launcher acitivity는 AndroidManifest 파일에서 설정해줄 수 있다.

```xml
// 앱의 메인 액티비티는 아래 두 항목을 포함해야 한다.
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
	// 다른 앱에서 해당 액티비티에 접근할 수 있는 권한
    <category android:name="android.intent.category.LAUNCHER" />
  // apk 파일을 설치하는 권한
	// 여러개 있으면 그 수 만큼 기기에 앱이 설치된다. 
</intent-filter>
```

사용자가 앱 아이콘을 선택하면, 안드로이드 운영체제가 launcher activity로 선언한 액티비티의 onCreate() 메소드를 호출합니다. 

모든 앱의 액티비티들이 back 스택을 공유한다. 
즉, ActivityManager가 안드로이드 운영체제에 있는 것이다. 
-> 운영체제 장치 전체에서 back 스택이 사용된다.

#android