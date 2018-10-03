# Shape Drawable
> xml로 쉽게 배경이미지를 만들 수 있다.  

## 장점
* 실제 비트맵을 사용하지 않아도 되므로 apk 용량을 줄여준다.
res_drawable_xxx.xml로 저장하면 bitmap drawable과 동일하게 사용할 수 있다.

* 쉽게 모양을 바꿀 수 있어 편리하다.

## 속성
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
android:shape="rectangle">
    <solid android:color="#FFFF" />
    <stroke android:width="4dp"
        android:color="#FFFF"
        android:dashWidth="1dp"
        android:dashGap="2dp" />
    <padding android:left="7dp"
        android:top="7dp"
        android:bottom="7dp"
        android:right="7dp" />
    <corners android:radius="4dp" />
</shape>
```

다른 속성을 지정해주기 전에 shape를 정의해주어야 한다.
1. rectangle : 사각형 (default)
2. oval : 타원
3. line : 선
4. ring : 고리

* solid : 단색으로 채워넣기.
* stroke : 테두리 그려넣기. 
dashWidth, dashGap은 점선을 그릴 때 사용한다.
* corners : 가장자리를 둥글게 처리한다.
		속성 값이 클수록 가장자리가 더욱 둥글게 표현된다.
* padding : 패딩 값을 넣어줍니다.

## 사용법
Layout xml 파일에서 사용합니다.
```xml
<TextView
    android:id="@+id/eventMessage"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:background="@drawable/rounded_rectangle" />
```

android:background 속성에 drawable 리소스를 참조하면 적용됩니다.
#android/basic