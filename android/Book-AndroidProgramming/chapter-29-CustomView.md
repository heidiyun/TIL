# 29. 커스텀 뷰와 터치 이벤트
View의 커스텀 서브 클래스인 BoxDrawingView를 작성하여 터치 이벤트를 처리하는 방법을 배울 것이다. 

## 커스텀 뷰 생성하기.
1. Simple CustomView
내부적 구성이 복잡할 수 있으나, 자식 뷰가 없어서 구조가 간단하다.
대부분 커스텀 렌더링을 수행한다.

2. Composite CustomView
다른 뷰 객체들로 구성된다.
일반적으로 이 뷰는 자식 뷰들을 관리하지만, 
자신은 커스텀 렌더링을 하지 않고 자식 뷰에게 위임한다.

**커스텀 뷰 생성단계**
1. 슈퍼 클래스를 선택한다.
Simple CustomView의 경우, View는 비어 있는 캔버스와 같다.
Composite CustomView의 경우 FrameLayout과 같이 적합한 레이아웃 클래스를 선택한다. 
2. 슈퍼 클래스의 서브 클래스를 만들고, 슈퍼 클래스의 생성자를 오버라이드 한다.
3. 서브 클래스에서 슈퍼 클래스의 주요 메소드를 오버라이드 한다.

### BoxDrawingView 생성하기.
```kotlin
// 주 생성자는 XML로부터 뷰를 인플레이트할 때 사용한다.

class BoxDrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    constructor(context: Context) : this(context, null)
    // 코드에서 뷰를 생성할 때 사용한다.
    
}
```

fragment_drag_and_draw.xml
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<kr.ac.ajou.heidi.draganddraw.BoxDrawingView
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".DragAndDrawActivity">

</kr.ac.ajou.heidi.draganddraw.BoxDrawingView>
```

## 터치 이벤트 처리하기
`public void setOnTouchListener(View.OnTouchListener listener)`
위의 View 메소드를 사용해서 터치 이벤트 리스너를 설정한다.
위의 메소드는 setOnClickListener(View.OnClickListener)와 같은 방법으로 동작한다.

BoxDrawingView는 View의 서브클래스 이므로 View의 메소드를 오버라이드하여 더 쉽게 처리할 수 있다.
`public boolean onTouchEvent(MotionEvent event)`
MotionEvent는 터치 이벤트를 나타내는 클래스로서 화면을 터치한 위치와 액션을 포함한다. 

* ACTION_DOWN : 사용자 손가락이 화면을 터치함
* ACTION_MOVE :  사용자가 손가락을 화면 위에서 움직임
* ACTION_UP : 사용자가 화면엣 손가락을 뗌
* ACTION_CANCEL : 부모 뷰가 터치 이벤트를 가로챔

```kotlin
override fun onTouchEvent(event: MotionEvent?): Boolean {
    val current = PointF(event?./x/?: 0f, event?./y/?: 0f)
    var action = ""
    when(event?./action/) {
        MotionEvent./ACTION_DOWN/-> action = "ACTION_DOWN"
        MotionEvent./ACTION_MOVE/-> action = "ACTION_MOVE"
        MotionEvent./ACTION_UP/-> action = "ACTION_UP"
        MotionEvent./ACTION_CANCEL/-> action = "ACTION_CANCEL"
    }

    Log.i(TAG, "$action at x= ${current.x} y= ${current.y}")
    return true
}
```

### 모션 이벤트 추적 기록하기
BoxDrawingView에서 화면에 박스를 그리는 기능을 담당할 것이다.

## onDraw(…) 내부에서 렌더링하기
애플리케이션이 실행될 때 모든 뷰들은 invalid 상태이기 때문에 화면에 어떤 것도 그릴 수 없다. 
이것을 해결하기 위해 안드로이드는 최상위 View의 draw() 메소드를 호출한다. 
모든 뷰 계층이 자신을 그리게 되면 최상위 View는 더이상  invalid 상태가 아니게 된다.

invalidate() 함수를 호출하게 되면 해당 뷰를 다시  invalid 상태로 만들어서 다시 자신을 그리려  onDraw를 호출하게 한다.

```kotlin
override fun onDraw(canvas: Canvas?) {
    canvas ?: return
    canvas.drawPaint(backgroundPaint)

    for (i in 0./until/(boxen.size)) {
        val box = boxen[i]
        val left = Math.min(box.origin.x, box.current.x)
        val right = Math.max(box.origin.x, box.current.x)
        val top = Math.min(box.origin.y , box.current.y)
        val bottom = Math.max(box.origin.y, box.current.y)

        canvas.drawRect(left, top, right, bottom, boxPaint)
    }
}
// boxen에 저장된 box 객체에 대해 박스의 두 점을 통해 사각형의 각 꼭짓점을 계산한다. 

```


#android/책