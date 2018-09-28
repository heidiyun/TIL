# xml <include>
같은 기능을 가지는 Layout을 여러 Activity에서 사용하는 경우가 있다.
Activity 마다 같은 형식의 Layout을 만들어 주는 것보다,
**include** 속성을 이용하면 하나의 레이아웃을 작성한 후 여러 액티비티에서 공유할 수 있다.

예) 애플리케이션에서 공통으로 쓰는 toolbar 
```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.Toolbar xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:background="@color/colorPrimary"
    app:titleTextColor="@color/whiteColor">

</android.support.v7.widget.Toolbar>

```

Activity layout 파일.
```xml
<include layout="@layout/app_bar" />
```
c언어의 ‘#include’와 비슷한 역할로,
include한 레이아웃 파일의 코드를 치환한다.

## 주의할 점
```xml
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".controller.ProfileActivity">

    <include
        layout="@layout/app_bar"
        android:id="@+id/include"
        />

</android.support.constraint.ConstraintLayout>
```

Include 한 레이아웃에 constraint로 연결하면 자동으로 id가 생성된다.
원래 toolbar의 id는 ‘toolbar'

이 때, 액티비티 클래스에서 다음과 같은 코드를 입력해보자.
```kotlin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout./activity_profile/)
        setSupportActionBar(toolbar)
        /supportActionBar/?./title/= null
        toolbar./title/="Profile"
    }
}

```
빌드결과, toolbar에서 NullPointerException이 발생한다.

그 이유는,  Toolbar의 원래 id를 새로 생성된 id가 덮어쓰면서 제대로 작동할 수 없기 때문이다.
새로 생성된 id는 View의 객체로서,  Toolbar를 제대로 가리킬 수 없다.

보통의 경우, 
Toolbar를 AppbarLayout으로 잡거나 FrameLayout으로 위치를 지정해준 후, 컨텐츠의 부분만 ConstraintLayout으로 잡는다.


#android