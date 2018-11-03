# 17. 다중 패널 마스터-디테일 인터페이스
> 마스터-디테일 인터페이스  
> 리스트-디테일 인터페이스  
> 전체 목록과 현재 선택한 항목에 대한 세부 사항을 보여주도록 설계된 모델.  
![master-detail view example](https://user-images.githubusercontent.com/38517815/47952707-eabb8580-dfb6-11e8-9b4d-8fdee71abf64.png)

```java
@LayoutRes
// 서브 클래스에서 이 메서드를 오버라이드하여 구현할 때 반드시 적절한 레이아웃 리소스 ID를 반환해야 한다는 것을 안드로이드 스튜디오에 알려주는 거다.
protected int getLayoutResId() {
    return R.layout./activity_fragment/;
}
```

## 두 개의 프래그먼트 컨테이너를 갖는 레이아웃 생성하기.

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:divider="?android:attr/dividerHorizontal"
    android:showDividers="middle"
    android:orientation="horizontal"
    >

    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"
        />

    <FrameLayout
        android:id="@+id/detail_fragment_container"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="3"/>

</LinearLayout>

```

```java
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
```

### 앨리어스 리소스 사용하기.



#android/책