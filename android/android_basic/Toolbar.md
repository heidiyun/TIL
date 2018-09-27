# Toolbar
Toolbar 컴포넌트는 안드로이드 5.0(롤리팝)에서 새로 추가됨.
롤리팝 이전에는 actionbar를 toolbar 역할을 수행하였다.
-> AppCompat 라이브러리를 사용하면 안드로이드 API 레벨 7(안드로이드 2.1)까지 toolbar를 사용할 수 있다.

[앱 바 설정  |  Android Developers](https://developer.android.com/training/appbar/setting-up?hl=ko)

## Toolbar 사용하기.
1. v7 AppCompat support library를 gradle에 추가.
2. activity가 AppCompatActivity 상속
3. AndroidManifest.xml 

```xml
<application
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"
    />
```

 res/style.xml
```xml
    <style name="AppTheme" parent="Theme.AppCompat.DayNight.NoActionBar">
    
```

4. 레이아웃 파일 
```xml
<android.support.v7.widget.Toolbar
    xmlns:android="http://schemas.android.com/apk/res/android"
	  android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    >
</android.support.v7.widget.Toolbar>
// 기본 설정
```

5. Activity class
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
	  setSupportActionBar(toolbar)
}
```

### toolbar 속성
1. Toolbar 색상을 바꾸고 싶을 때
`android:background=“@color/colorPrimary”`
2. Toolbar title color를 바꾸고 싶을 때
`app:titleTextColor=“@color/whiteColor”>`


## AppCompat 라이브러리 사용하기
1. 모든 액티비티가 AppCompatActivity를 상속한다. (자동)
2. AppCompat 라이브러리를 추가한다. (자동)
3. AppCompat의 테마 중 하나를 사용한다. (Manifest)

## 메뉴
메뉴는 액션 항목(action item)으로 구성된다.
액션 항목의 명칭은 문자열 리소스로 만들어야 한다. (res/strings.xml)
메뉴의 리소스를 xml파일로 생성한다. (res/menu)
 -> showAsAction 
		1. ifRoom : 공간이 있다면 보여주고, 없다면 오버플로 메뉴로 들어간다.
		2. withText : 텍스트를 함께 보여준다.
		3. always : 항상 툴바에 보여준다.
		4. never : 오버플로 메뉴로 들어간다.

## 메소드

```kotlin
fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
//  액티비티에 대한 옵션 메뉴 지정 
//  onCreate에서 setHasOptionsMenu(true)를 추가해주어야 한다.

fun onOptionsItemSelected(item: MenuItem): Boolean
// 메뉴 항목을 선택할 때 불린다.
// 각 메뉴가 해야할 일을 정의한다.
 
```

## Up 버튼 활성화 (계층적 내비게이션)
Back 버튼을 사용하는 것은 일시적 내비게이션으로, 직전에 있던 화면으로 이동한다.
Up 버튼을 활용한 계층적 내비게이션은 지정한 화면이 앱 계층의 위로 이동한다.
	-> 일시적 내비게이션과 달리 새로운 인텐트가 생성되어 startActivity() 메소드가 실행된다.

AndroidManifest.xml 파일에서 up 버튼을 사용할 activity의 속성에 아래와 같이 추가한다.
```
<activity
    android:name=“.ui.RepoActivity”
    android:parentActivityName=“.ui.ProfileActivity”>

    <meta-data
        android:name=“android.support.PARENT_ACTIVITY”
        android:value=“.ui.ProfileActivity” />
</activity>

```

그리고, 
Activity의 onCreate 함수에 아래와 같은 코드를 추가한다.

```
setSupportActionBar(toolbar)
supportActionBar!!.title= null
supportActionBar?.setDisplayHomeAsUpEnabled(true)

```


# ToolBar vs ActionBar
1. 툴바에는 제일 왼쪽에 아이콘이 나타나지 않는다.
2. 툴바는 액션바보다 제약이 적다.
	* 액션바는 항상 화면 위에 나타나야 한다. 
	(사용자가 원하는 곳에 툴바가 위치할 수 있다.)
	* 액션 바의 크기는 정해져있어 변경될 수 없다.
	* 한 화면에 하나의 액션바만 존재할 수 있다. 
	(한 화면에 여러개의 툴바 생성 가능 
	-> 한 화면에 여러개의 프래그먼트를 쓸 경우 유용)


#android
