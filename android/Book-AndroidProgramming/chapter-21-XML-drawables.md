# XML drawables
> 화면에 그려지는 것.  

## 균일한 버튼 만들기
현재 RecyclerView에서는 화면 크기와 무관하게 항상 세 개의 열로 보여준다.
만약 화면에 여분의 공간이 있다면 RecyclerView에서 화면의 크기에 맞게 세 개의 열을 확장시켜 보여줄 것이다.
이때 버튼의 크기는 확장되는 것을 원하지 않기 때문에 FrameLayout으로 Button을 감싸주었다. 
이 경우, FrameLayout 만 확장되고 Button은 확장되지 않는다. 
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="8dp">

    <Button
        android:id="@+id/list_item_sound_button"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="center"
        tools:text="Sound name" />
</FrameLayout>
```

## 형태 Drawable
> xml drawable은 화면의 밀도와는 무관하다.  

 원형 drawable 만들기.
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="@color/dark_blue" />
</shape>
```

## 상태 리스트 drawable
> 어떤 상태를 기준으로 다른 drawable을 참조하는 drawable이다.  
버튼이 눌러진 상태와 눌러지지 않은 상태 각각 다른 drawable 리소스를 참조하도록 해준다.

```xml
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/button_beat_box_pressed"
        android:state_pressed="true" />
    <item android:drawable="@drawable/button_beat_box_normal"/>
</selector>
```

## 레이어 리스트 drawable
> 두 개의 XML drawable을 하나로 결합할 수 있다.  

버튼이 눌러진 상태일 때 버튼 주위에 진한 붉은색의 고리를 추가할 것이다.
```xml
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape
            android:shape="oval">
            <solid
                android:color="@color/red" />
        </shape>
    </item>

    <item>
        <shape
            android:shape="oval">
            <stroke
                android:width="4dp"
                android:color="@color/dark_red" />
        </shape>
    </item>
</layer-list>
```

## XML drawable을 사용해야 하는 이유
XML drawable은 유연성이 좋다.
여러 곳에 적용할 수 있으며, 쉽게 변경이 가능하다.

XML drawable은 화면 밀도와 무관하여 화면 밀도에 따라 따로 정의할 필요가 없다.

## 9-patch 이미지
이미지 파일을 버튼의 배경으로 사용할 경우, 버튼의 너비가 배경 이미지 너비보다 크면 이미지가 확장 될 것이다.

 확장할 부분과 확장하지 않을 부분을 안드로이드가 알 수 있도록 9-patch 이미지 파일을 사용하자.
9-patch는 이미지를 3x3 격자로 분할한다. 그리고 격자의 모서리는 그대로 있고, 옆쪽은 한 방향으로만 확장되며, 중앙은 양방향으로 확장된다.

두가지 점만 제외하고 PNG 이미지와 동일하다.
확장자명 : .9.png
이미지 테두리 주위에 한 픽셀을 더 갖고 있다. (경계 픽셀)
-> 중앙을 나타낼 때 검게 그려지며, 테두리를 나타낼 때 투명하게 그려진다.

## Mipmap 이미지
애플리케이션에서 이미지가 필요할 때는 동일한 이미지를 서로 다른 크기로 생성하여 리소스 수식자가 붙은 폴더에 추가하면 적절한 이미지를 안드로이드가 판단하고 찾아준다.

**단점**
구글 플레이 스토어에 업로드하는 APK파일에는 화면 밀도마다 우리 프로젝트의 drawable 디렉토리에 저장했던 모든 이미지가 포함된다. 사용하지 않는 것도 포함해서.

이런 것을 해소하기 위해 각 화면 밀도마다 별도의 APK 파일을 생성할 수 있다.
단 런처 아이콘은 예외다.

Mipmap은 런처 아이콘 이미지를 위한 디렉토리이다. 장치의 밀도와 관계 없이 홈 스크린에 가장 적합한 해상도의 아이콘을 보여준다.

런처 앱은 홈 화면에 보이는 앱 아이콘의 크기와 갯수를 스스로 정할 수 있다. 즉, 장치의 런처마다 제공해주어야 하는 아이콘 이미지가 다르다. 
즉, 런처에서 보여주는 아이콘의 크기에 따라 최적의 이미지를 전달한다.



#android/책