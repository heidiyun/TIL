# 33. Material Design
> 안드로이드 5.0 롤리팝에서 새로 소개된 다자인 스타일  
* material은  metaphor다. 
앱의 구성 요소가 실제 사물이나 소재처럼 동작해야 한다.
* 선명하고 생생하며 의도가 반영되어야 한다. 
* 움직임이 의미를 부여한다
: 사용자의 액션에 대한 응답으로 앱이 생동감 있게 움직여야 한다.

새로운 위젯 
1. Floating Action Button
2. SnackBar
새로운 애니메이션 도구
1. State List Animator
2. Animated State List Drawable
3. Circular reveal Animation
4. Shared Element Transition

## Material Surface
> 사물이나 소재의 표면  
1dp 두께의 얇은 종이 소재로 생각.
그림이나 텍스트 등을 보여줄 수 있다.
실제 종이처럼 동작하기 때문에 다른 material suface를 통과하여 이동할 수 없다. 
대신 3차원 공간에서 서로 다른 surface 주위를 이동하며, 위로 솟아 오르거나 밑으로 가라앉을 수 있다.

### Elevation과 Z Value
인터페이스에 입체감을 주려면 컴포넌트에 그림자 효과를 주는 것이 좋다.
각 뷰에 elevation을 지정하여 안드로이드가 그림자를 그리도록 해야 한다.

안드로이드 롤리팝에서는 레이아웃에 z- 축의 개념을 도입했다.
뷰가 3차원 공간에 위치할 곳을 지정할 수 있다.
elevation은 뷰에 지정하는 좌표와 같은 것이며, 그 위치로부터 뷰를 이동시킬 수 있다.
`android:elevation="2dp"` 와 같이 뷰 속성을 지정할 수 있다.

롤리팝 이전의 버전에서는 elevation 속성이  무시된다.

elevation을 변경할 때는 translationZ와 Z 속성을 사용한다.
Z 속성의 값은 항상 elevation 값에 translationZ 값을 더한 것이다.

### 상태 리스트 애니메이터 (State List Animator)
상태 리스트 애니메이터는 상태 리스트 drawable과 다르게 동작한다.
하나의 drawable을 다른 것으로 변경하는 대신에 해당 뷰를 특정 상태로 애니메이션 한다.

* 버튼을 누를 때 솟아오르는 애니메이션
res/animator
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:state_pressed="true">
        <objectAnimator android:propertyName="translationZ"
                        android:duration="100"
                        android:valueTo="6dp"
                        android:valueType="floatType"/>
    </item>

    <item android:state_pressed="false">
        <objectAnimator android:propertyName="translationZ"
                        android:duration="100"
                        android:valueTo="0dp"
                        android:valueType="floatType"/>
    </item>
    
</selector>
```
속성 애니메이션을 사용할 때는 위와 같은 방법이 좋다.

장면과 전환을 사용하는 프레임 애니메이션을 사용할 때는 애니메이트 상태 리스트 drawable을 사용해야 한다.
각 상태 간의 프레임 애니메이션 전환도 정의할 수 있다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<animated-selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/pressed"
        android:drawable="@drawable/ic_button_beat_box_pressed"
        android:state_pressed="true" />

    <item
        android:id="@+id/released"
        android:drawable="@drawable/ic_button_beat_box_default" />


    <transition
        android:fromId="@id/released"
        android:toId="@id/pressed" >
        <animation-list>
            <item android:duration="10" android:drawable="@drawable/button_frame_1"/>
            <item android:duration="10" android:drawable="@drawable/button_frame_2"/>
            <item android:duration="10" android:drawable="@drawable/button_frame_3"/>
        </animation-list>
    </transition>

</animated-selector>


```

## 애니메이션 도구
### 원형 노출
> 잉크가 번지는 것처럼 보이도록 하는 것.  

``` kotlin
ViewAnimationUtils.createCircularReveal(view: View, centerX: Int, centerY: Int, startRadius: Float, endRadius: Float)

override fun onClick(clickSource: View) {
	  val clickCoords = IntArray(2)
   
    clickSource.getLocationOnScreen(clickCoords)
    clickCoords[0] += clickSource./width// 2
    clickCoords[1] += clickSource./height// 2
	// 사용자가 터치한 뷰의 화면 좌표 알아내기.
}
```

 애니메이션 실행
```kotlin
fun performRevealAnimation(view: View, screenCenterX: Int, screenCenterY: Int) {
	val animatingViewCoords = IntArray(2)
	val centerX = screenCenterX - animatingViewCoords[0]
	val centerY = screenCenterY - animatingViewCoords[1]

	val size = Point()
	/activity/?./windowManager/?./defaultDisplay/?.getSize(size)
	val maxRadius = size.y
	
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, maxRadius).start()
	}
}
```

### 공유 요소 전환
동일한 요소에 대한 것을 또 다른 뷰에서 보여줄 때 두 화면 간의 전환 처리 애니메이션

Activity가 AppCompatActivity를 상속받지 않을 때, 전환을 사용할 수 있게 하는 법
1. onCreate(…)에 코드 추가
```kotlin
override fun onCreate(savedInstanceState : Bundle) {
	window?.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
	super.onCreate(savedInstanceState)
}
```
2. style에서 설정하기
```xml
<resources>
	<style name="TransparentTheme"
		parent="@android:style/Theme.Translucent.NoTitleBar">
		<item name="android:windowActivityTransitions">true</itme>
</resources>

```

```kotlin
ViewCompat.setTransitionName(sourceView, "image")
// 뷰에 전환 이름 설정하기.
val options = ActivityOptionsCompat.makeSceneTransitionAnimation(*it*, view.fragmentBeatBoxRecyclerView, "image")
/activity/?.startActivity(intent, options.toBundle())
```

## 뷰 컴포넌트
### CardView
다른 뷰들을 담는 컨테이너 역할을 한다.
사용하려면 app 모듈에 의존성을 추가해야 한다.

### Floating Action Button
Com.android.support:design 의존성 추가.
CoordinatorLayout을 사용하면 다른 컴포넌트의 움직임에 따라 버튼의 위치를 자동으로 변경해준다.
### Snackbar
화면 제일 밑에 나타나는 컴포넌트
Toast와 유사한 역할을 한다.
Toast와는 달리 사용자로부터 이벤트를 받을 수 있는 버튼을 둘 수 있다.
```kotlin
Snackbar.make(view, "스낵바", Snackbar.LENGTH_SHORT).show()

Snackbar.make(view, "스낵바", Snackbar.LENGTH_SHORT).setAction("YES") {finish()}.show()
// 버튼있는 스낵바
}
```
#android/책