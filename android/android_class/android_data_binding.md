# Data Binding 
> 뷰의 값을 읽어오거나 쓰는 코드를 없앨 수 있는 방법  

## 흐름
* CLI 프로그래밍
Linux
* GUI 프로그래밍
Windows - Win32 API
JAVA - Swing, Java Scene Builder

	* Windows - MFC (Microsoft Foundation Class) - C++
	Microsoft Windows 운영체제 환경에서 작동하는 GUI 프로그램을 C++을 사용하여 개발할 수 있도록 WIn32 API의 핸들과 C언어 함수들을 C++ 언어의 클래스화 한 라이브러리.
	-> GUI Wizard의 도입.
	-> GUI 컴포넌트를 코드로 옮겨주는 기능.

눈에 보이는 화면을 코드를 통해서 작성하는 것이 아니라, Markup Language를 통해서 작성하는 형태로 변경되었다. (HTML, XML)
(View와 Business logic을 분리)
C# -> WinForm, WPF(Windows Presentation Foundation)
Java -> Android(xml)

* Web의 데이터 바인딩
React.js, React Native(앱) -> Facebook에서 만듬. 
	* 인스타, 페이스북 앱은 React Native를 사용하여 만듬.
Vue.js ,Vue Native (앱)
JSP/Servlet, ASP.NET -> 서버에서 페이지(사용자 인터페이스)를 만들어서 넘겨준다. 다운받아서 쓸 수 있음.

## Android Data Binding 
> androidx lifecycle   

1. setContentView (레이아웃 인플레이트)
기존
``` kotlin
class Sample {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}
}
```
데이터바인딩
```kotlin
class Sample {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding: ActivityMainBinding = DataBindingUtil.setContentView(
        this, R.layout./activity_main/)
// 타입은 레이아웃 파일의 이름을 따라간다.
	}
}
```

2. layout xml
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">

<data/>
</layout>
```
 루트 속성이 layout으로 바뀐다. 
<data/>에는 데이터 바인딩을 할 속성을 선언한다.

3. View component naming rules
```xml
<Button
    android:id="@+id/viewmodel_activity_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
/>
```

```kotlin
/*기존*/
viewmodel_activity_button.setOnClickListener {
}

/*데이터바인딩*/
binding.viewmodelActivityButton.setOnClickListener {
}
```

4. data class
```kotlin
/* 기존 */
data class User(var firstName: String, var lastName: String) 
/* 데이터바인딩 */
data class User(var firstName: ObservableField<String>, var lastName: ObservableField<String>)
```

#android/basic

