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
> alias resource : 또 다른 리소스를 참조하는 리소스.  
> res_values_refs.xml  

태블릿과 폰 어떤 장치를 사용할 것인지에 따라서 보여주는 레이아웃이 달라야 한다.
리소스 수식자(qualifier)를 사용하여 가로-세로 방향의 레이아웃을 다르게 보여주는 것과 같은 방법으로 할 수 있다. 그러나 이 방법을 사용하게 되면 보여주려는 레이아웃 내용 전체를 갖고 있어야 하므로 많은 중복이 발생한다.
activity_masterdetail.xml <- activity_fragment.xml
res_layoutsw600dp_activity_masterdetail.xml <- activity_twopane.xml

위의 방식을 대체하여 앨리어스 리소스를 사용할 수 있다.
CriminalIntentJ 생성할 앨리어스 리소스는 activity_fragment.xml, activity_twopane.xml 레이아웃 파일을 참조할 것이다.

```xml
<resources>
    <item name="activity_masterdetail" type="layout">@layout/activity_fragment</item>
</resources>
```
*res_values_에 있지만 type으로 layout을 가지므로 리소스 ID는 R.layout 내부 클래스에 정의된다.

### 안드로이드 리소스
안드로이드에서는 리소스 ID로 리소스를 참조한다.
모든 리소스 ID는 빌드 도구인 aapt(android asset package tool)에 의해 자동 생성되는 R.java 파일에 정의된다.

R.java 파일에는 R 클래스가 정의되어 있으며, 각 리소스 타입 (string, layout, drawable…)이 R 클래스의 내부 클래스로 정의된다.
이 내부 클래스 안에는 고유한 값을 갖는 static int 상수로 리소스들이 정의된다.

### 태블릿 대체 리소스 생성하기
CriminalIntentJ에서  activity_masterdetail이 res_values_에 있으므로 디폴트 앨리어스로 설정된다.

화면이 더 큰 장치일 경우 activity_masterdetail 앨리어스가 activity_twopane.xml을 참조하도록 대체 리소스를 생성할 것이다.

res_values_ -> new resource file -> file name : “refs”, available qualifiers -> Smallest Screen Width -> 600

```xml
<resources>
    <item name="activity_masterdetail" type="layout">@layout/activity_twopane</item>
</resources>
```

* 지정된 하면 크기보다 작은 장치에서는 activity_fragment.xml을 사용한다.
* 지정된 화면 크기 이상의 장치에서는 activity_twopane.xml을 사용한다.

## 프래그먼트 콜백 인터페이스
프래그먼트 독립성을 유지하기 위해 프래그먼트에 콜백 인터페이스를 정의하여 호스팅 액티비티에 프래그먼트를 호출하는 일을 위임한다.

위의 기능을 수행하기 위해서 콜백 인터페이스를 정의할 것이다.
콜백 인터페이스를 사용하면 어떤 액티비티가 호스팅하는지 프래그먼트가 알 필요 없이 자신을 호스팅하는 액티비티의 메소드들을 호출할 수 있다.

```java
class CrimeListFragment extends Fragment {
	private Callbacks mCallbacks;

	public interface Callbacks {
    	void onCrimeSelected(Crime crime);
	}

	@Override
	public void onAttach(Context context) {
    	super.onAttach(context);
    	mCallbacks = (Callbacks) getActivity();
	}

	@Override
	public void onDetach() {
    	super.onDetach();
    	mCallbacks = null;
	}

}

// 프래그먼트를 호스팅하는 액티비티 객체의 타입을 콜백 인터페이스로 캐스팅한 후 멤버 변수에 저장한다.
```

CrimeListFragment가 호스팅 액티비티의 메소드를 호출할 수 있게 되었다.

호스팅 액티비티를 CrimeListFragment.Callbacks 타입으로 캐스팅했다는 것은,
호스팅 액티비티가 반드시 CrimeListFragment.Callbacks 인터페이스를 구현해야 한다는 것이다.

호스팅 액티비티 클래스가 해당 인터페이스의 서브 타입이 되어야만 액티비티의 객체가 인터페이스의 타입이 될 수 있다.

```java
public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks {
	
	@Override
	public void onCrimeSelected(Crime crime) {
		// 폰 인터페이스라면 새로운 CrimePagerActivity를 시작시킨다.
		// 만일 태블릿 인터페이스를 사용 중이라면 CrimeFragment를 detail_fragment_container에 넣는다.
	}	

}
```

어떤 인터페이스가 인플레이트되어 뷰로 동작 중인지 판단하기 위해서는 레이아웃 ID로 확인할 수 있다. 
이보다는 레이아웃이 detail_fragment_container를 갖고 잇는지 여부를 확인하는 것이 더 좋다.

* 레이아웃 파일 이름이 변경되어도 문제 없다.
* 레이아웃이 어떤 파일로부터 인플레이트되는지 신경 쓰지 않아도 된다. 

```java
@Override
public void onCrimeSelected(Crime crime) {
    if (findViewById(R.id./detail_fragment_container/) == null) {
        Intent intent = CrimePagerActivity./newIntent/(this, crime.getmId());
        startActivity(intent);
    } else {
        Fragment newDetail = CrimeFragment./newInstance/(crime.getmId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id./detail_fragment_container/, newDetail)
                .commit();
    }
}

```

위의 메소드를 기존의 CrimePagerActivity를 시작시키는 곳에서 호출해주면 된다

현재는 범죄를 추가할 때와 범죄를 볼 때만 updateUI()를 호출하여 범죄 디테일 화면에서 수정한 결과 값이 list에 반영되지 않는다.

CrimeFragment에서 콜백 인터페이스를 사용하여 위의 문제점을 해결해보자.

## 장치 크기 결정
안드로이드 3.2 이전 버전에서는 화면 크기 수식자가 사용되었다.
Small -> 320x426dp
Normal -> 320x470dp
Large -> 480x640dp
Large -> 720x960dp

안드로이드 3.2 버전 이상에서는 새로운 화면 크기 수식자가 나왔다.

wXXXdp -> 사용 가능한 너비 : 너비가 XXX dp와 같거나 크다.
hXXXdp -> 사용 가능한 높이 : 높이가 XXX dp와 같거나 크다.
swXXXdp -> 최소 너비 : 너비 또는 높이 중 더 작은 쪽이 XXX dp와 같거나 크다.

#android/책
