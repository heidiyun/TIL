# 20. 스타일과 테마
## 색상 리소스
res_values_colors.xml
```xml
<resources>
    <color name="red">#F44336</color>
    <color name="dark_red">#C3352B</color>
    <color name="gray">#607D8B</color>
    <color name="soothing_blue">#0083BF</color>
    <color name="dark_blue">#005A8A</color>
</resources>
```

## 스타일
> 속성들의 집합을 한 곳에서 정의한 후 필요한 위젯에 적용할 수 있다.  

res_values_styles.xml
```xml
<resources>
    <style name="BeatBoxButton">
       
		 <item name="android:background">@color/dark_blue</item>
    </style>
</resources>

```

```xml
<Button xmlns:android="http://schemas.android.com/apk/res/android"
    style="@style/BeatBoxButton"
/>
```

### 스타일 상속
> 스타일은 상속도 지원한다.  
상속방법
1. 스타일 이름 앞에 상속하는 스타일의 이름을 붙인다.
```xml
<style name="BeatBoxButton.Strong">
    <item name="android:textStyle">bold</item>
</style>
```

2. parent를 지정한다.
```xml
<style name="StyleBeatBoxButton" parent="@style/BeatBoxButton">
    <item name="android:textStyle">bold</item>
</style>
```

### 스타일 단점
스타일이 필요한 위젯에 각각 적용해야 한다.

## 테마
> 스타일과 마찬가지로 속성들의 집합을 한 곳에서 정의할 수 있다.  
* 스타일과 달리 속성들이 자동으로 앱 전체에 적용된다. 
	* 테마는 매니페스트에 선언될 수 있다.
* 스타일의 참조도 저장할 수 있다.

colorPrimary : 툴바의 배경색
colorPrimaryDark : 화면 제일 위에 나타나는 상태 바의 색상
(롤리팝 이전에는 테마와 무관하게 상태 바가 검은색이 된다.) 
colorAccent :  colorPrimary 색과 대조를 이루어야 하며, EditText와 같은 일부 위젯에 색을 넣는데 사용된다.

## 테마 속성 오버라이드하기
### 필요한 테마 찾기
res_values_styles.xml -> Theme.AppCompat Command + 마우스클릭
프로젝트의 모든 테마가 정의되어 있다. 
```xml
<style name="Platform.AppCompat" parent="android:Theme.Holo">
```
 테마 이름 맨 앞에 android 네임스페이스 
-> 안드로이드 운영체제에 존재하는 테마.

```xml
<style name="AppTheme" parent="Theme.AppCompat">
    <!-- Customize your theme here. -->
    <item name="colorPrimary">@color/red</item>
    <item name="colorPrimaryDark">@color/dark_red</item>
    <item name="colorAccent">@color/gray</item>

    <item name="android:colorBackground">@color/background_holo_dark</item>

</style>
```

android:Theme.Holo에서 찾은. Background 속성을 오버라이드 한다.
colorBackground 속성을 오버라이드할 때 맨 앞에 android 네임스페이스를 붙여야 한다는 것에 유의하자. 

## 버튼 속성 변경하기.
res_layout_list_item_sound.xml에 style 속성을 설정하여 BeatBox의 버튼들의 스타일을 바꿔주었다. 
하지만 여러 프래그먼트에 걸쳐 많은 버튼들이 있는 더 복잡한 레이아웃에서 각 버튼마다 style을 지정해주는 것은 번거롭다.
그래서 스타일을 테마에 정의하고 우리 앱의 모든 버튼에 한번에 적용하는 것이 좋다.

```xml
<resources>
    <style name="AppTheme" parent="Theme.AppCompat">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/red</item>
        <item name="colorPrimaryDark">@color/dark_red</item>
        <item name="colorAccent">@color/gray</item>

        <item name="android:colorBackground">@color/soothing_blue</item>
        <item name="buttonStyle">@style/BeatBoxButton</item>

    </style>

    <style name="BeatBoxButton" parent="android:style/Widget.Holo.Button">
        <item name="android:background">@color/dark_blue</item>
    </style>

</resources>

```

BeatBoxButton 스타일에서 android:style/Widget.Holo.Button을 부모로 지정하여 모든 속성을 상속받는다.

AppTheme에서  buttonStyle 속성을 오버라이드 하여 BeatBoxButton으로 교체하였다. 

여기까지 했을 때 버튼을 누르면 그 모습이 변경되지 않는다.
버튼이 눌러진 상태에 관한 스타일 변경이 없다.

## 스타일 상속 추가로 알아보기.
테마 이름 앞에 android:가 붙어있지 않은 경우 테마들이 같은 패키지에 있는 것이다.
다른 패키지의 부모를 참조할 때는 명시적인 parent 속성이 사용된다. 

## 테마 속성 사용하기
xml에서 색상과 같은 실제 값을 참조할 때는 @ 표기법을 사용한다.
`  <item name="colorPrimaryDark">@color/dark_red</item>`

테마의 리소스를 참조할 때는 ? 표기법을 사용한다.
`android:background="?attr/colorAccent"`
? 는 테마 속성이 참조하는 리소스의 사용을 나타낸다.

**코드에서 테마 속성 사용**
```kotlin
val theme = /activity/?./theme/?: return view
val attrsToFetch: IntArray = /intArrayOf/(R.attr./colorAccent/)
val typedArray = theme.obtainStyledAttributes(R.style./AppTheme/, attrsToFetch)
val accentColor  = typedArray.getInt(0,0)
```

#android/책