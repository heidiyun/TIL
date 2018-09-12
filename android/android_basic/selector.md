
# Selector 사용하기
위젯의 이벤트 효과를 커스텀 할 때 유용하다.

1. 요소

* </selector/>
가장 최상위에 위치해야 하는 요소.
하나 이상의 <item> 요소를 포함한다.
```
xmlns:android 
// 필수 항목
// XML네임스페이스를 정의한다. 
// "http://schemas.android.com/apk/res/android"

android:constantSize
// Boolean
// drawable의 상태가 변경되어도 크기가 동일하게 유지하는 경우(true)
// 상태에 따라 크기가 달라지는 경우(false)
// 기본 값 :  True

android:variablePadding
// Boolean
// 상태에 따라 패딩이 변경되어야 하는 경우(true)
// 상태에 상관없이 패딩이 동일해야 하는 경우(false)
// 기본 값 : False
```
	 
* </item/>
</selector/>의 하위 요소
특정 상태 동안 사용할 속성을 정의한다.
```xml
android:drawable
// 필수 항목
// drawable resource에 대한 참조
android:state_pressed
// 객체의 눌림 상태 
// ex. 버튼을 터치하거나 클릭할 때
android:state_focused
// 객체의 포커스 상태
// 사용자가 텍스트 입력을 선택할 때
android:state_selected
// 방향 컨트롤을 사용하여 탐색할 때 객체의 선택 상태
android:state_checkable
// 객체의 선택 가능 상태
android:state_checked
// 객체의 선택 상태 
android:state_enabled
// 객체의 활성화 상태 
//  ex. 터치/클릭 이벤트를 수신 가능한 경우
android:state_window_focused
// 애플리케이션 창에서의 포커스 상태
// 애플리케이션 창의 가장 위쪽에 있어 이벤트를 받을 수 있는 상태 (true)
android:state_activated
// 객체의 영구적 선택 상태
// 객체가 영구적인 선택 항목으로 활성화되었을 때 (true)
android:state_hovered
// 커서가 객체를 가르키는 상태

// 속성 값은 true or false.
```

2. 사용 예

res/drawable/button.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@color/buttonText" android:state_selected="true" />

    <item android:drawable="@color/tabSelectedBackground" android:state_selected="false" />
</selector>
```
res/layout/xxx.xml
```java
<Button
    android:layout_height="wrap_content"
    android:layout_width="wrap_content"
    android:background="@drawable/button" />
```