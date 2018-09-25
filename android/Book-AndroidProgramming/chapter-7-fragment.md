# 7. UI 프래그먼트와 프래그먼트 매니저
> CriminalIntent 애플리케이션으로 실습 합니다.  

## Fragment
액티비티에서 하나 이상의 fragment로 UI를 관리하면 안드로이드 기본 원칙(하나의 액티비티가 하나의 뷰와 결속된다.)을 피해서 유연성 있는 설계를 할 수 있다.
그러나, fragment로 구조를 설계할 경우 코드가 더 복잡해지고 많아진다는 단점이 있다.

fragment는 액티비티의 작업 수행을 대체할 수 있는 컨트롤러 객체이다.
(*작업 : 사용자 인터페이스 관리)

액티비티와 마찬가지로 하나의 뷰를 가진다. 
액티비의 뷰는 fragment의 뷰들이 추가될 레이아웃을 가질 수 있다.
액티비티 뷰는 fragment 생애 주기 동안 같이 존재한다. 

UI Fragment :  사용자 인터페이스 관리 담당

### Support Library
Fragment는 API level 11부터 추가 되었다.
API level 4까지 동작하도록 Support Library가 지원해준다. 

File_Project Structure_Dependencies에서  추가할 수 있다.
App/dependency에도 자동으로 추가된다.
dependency 문자열 - groupId:artifactId:version 형식을 따른다.
(com.android.support:support-v4:28.0.0)

## UI Fragment 호스팅
**필수**
* 액티비티 레이아웃에 프래그먼트의 뷰가 들어갈 위치를 정의해야 한다.
* 프래그먼트 인스턴스 생명주기를 관리해야 한다.

### Fragment 생명주기
![](chapter-7-fragment/B3E0A208-F375-4096-B8BD-63BBB4742845.png)
![fragment lifecycle](https://t1.daumcdn.net/cfile/tistory/214A7A335728843327)

onAttach(), onCreate(), onCreateView() 는 setContentView()에서 모두 호출된다.

프래그먼트는 액티비티 내부에서 처리되기 때문에 안드로이드 운영체제는 액티비티가 사용 중인 프래그먼트를 알지 못한다. 

### 호스팅 방법
#### 프래그먼트를 액티비티의 레이아웃에 정적으로 추가한다.
장점 : 구현이 간단하다
단점 : 프래그먼트와 프래그먼트 뷰가 액티비티 뷰에 고정되어 유연하게 동작할 수 없다.
	  즉, 액티비티 생애 동안 프래그먼트를 교체할 수 없다.

#### 프래그먼트를 액티비티의 코드에 동적으로 추가한다.
장점 : 런타임 시에 프래그먼트를 제어할 수 있다.
단점 : 구현이 복잡하다.

## UI Fragment 생성하기
> (액티비티 생성 시와 동일한 방법)  
1. 레이아웃 파일에 위젯들을 정의하여 사용자 인터페이스를 구성한다.
2. 클래스를 생성하고 그것의 뷰를 우리가 정의한 레이아웃으로 설정한다.
3. 레이아웃으로부터 인플레이트된 위젯들을 코드에 연결한다.

### Fragment 생명주기 메소드 구현하기.
**Fragment.onCreate(…)에서는 프래그먼트의 뷰를 인플레이트하지 않는다.**
onCreateView(…)에서 프래그먼트 뷰를 인플레이트 한다.

```java
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout./fragment_crime/, container, false);
    return view;
}
// 레이아웃 리소스 아이디를 인자로 전달하여 LayoutInlfater.inflate(...)를 호출함으로써 프래그먼트의 뷰를 명시적으로 인플레이트 한다.
// 두 번째 인자는 위젯들을 올바르게 구성하기 위해 필요한 뷰의 부모이다.
// 세 번째 인자는 인플레이트된 뷰를 뷰의 부모에게 추가할 것인지 LayoutInflater에 알려준다.
```

## UI Fragment를 FragmentManager에 추가하기.
FragmentManager는 프래그먼트를 관리하고 프래그먼트 뷰를 액티비티 뷰 계층에 추가하는 책임을 갖는다.

```java

/*  허니콤 버전 이후 API level 4 이후 */
import android.support.v4.app.FragmentManager;
public class CrimeActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout./activity_crime/);

        FragmentManager fragmentManager = getSupportFragmentManager();
    }
}


/* 허니콤 버전 이전 API level 4 이전 */
public class CrimeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout./activity_crime/);

        FragmentManager fragmentManager = getFragmentManager();

    }
}
```

### Fragment Transaction
> 프래그먼트를 사용해서 런타임 시에 화면을 구성 또는 재구성하는 방법.  

FragmentManager는 fragment transaction의 back 스택을 유지, 관리한다.
Transaction은 fragment list 에 add, remove, attach, detach, replace 하는데 사용된다.
```java

   FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment = fragmentManager.findFragmentById(R.id./fragment_container/);
    
    if (fragment == null) {
        fragment = new CrimeFragment();
        fragmentManager.beginTransaction()
		  // FragmentTransaction 인스턴스를 생성하여 반환한다.
                .add(R.id./fragment_container/, fragment)
                .commit();
    }
```


#android/책