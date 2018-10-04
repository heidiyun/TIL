# 10. 프래그먼트 인자 사용하기
## 프래그먼트로부터 액티비티 시작하기.
Fragment.startActivity(Intent) 메소드를 호출한다.
```java
Intent intent = new Intent(getActivity(), CrimeActivity.class);
// 명시적 인텐트 생성
startActivity(intent);
```

## Extra 쓰기
```java
Intent intent = new Intent(context, CrimeActivity.class);
intent.putExtra("extra", crimeID);
startActivity(intent);
```

## Extra 읽기
```java
@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UUID crimeId = (UUID) getActivity().getIntent()
            .getSerializableExtra(CrimeActivity./EXTRA_CRIME_ID/);

    mCrime = CrimeLab./get/(getActivity()).getCrime(crimeId);
}

```

getIntent()는 CrimeActivity를 시작시키는데 사용되었던 intent를 반환한다.
해당 Intent의 getSerializableExtra(String)을 호출하여 Crime 객체의 UUID를 가져온다.

### Fragment에서 Activity의 Intent를 직접 액세스하는 방법의 단점
> 프래그먼트의 캡슐화를 어렵게 한다.  

CriminalIntentJ에서 CrimeFragment는 더 이상 재사용 가능한 클래스가 아니다. crimeId의 이름을 정의하고 있는 intent를 갖는 액티비티에 의해 호스팅 되어야 하기 때문이다.
즉, 다른 액티비티와 함께 사용될 수 없다.

## 프래그먼트 인자 (Bundle)
모든 프래그먼트 인스턴스는 Bundle 객체를 가질 수 있다.

Activity의 intent extra와 동일하게 키와 값이 한 쌍으로 된 데이터를 갖는다.

###  인자를 프래그먼트에 첨부하기
Fragment.setArguments(Bundle) 메소드를 호출한다.
**단, 프래그먼트가 생성된 후, 프래그먼트 액티비티에 추가되기 전에 프래그먼트에 인자를 첨부해야 한다.**

newInstance() 이름의 static 메소드를 Fragment 클래스에 추가하는 것이 좋다. 
Fragment instance와 bundle 인스턴스를 생헝하고 bundle instance를 프래그먼트 인자로 첨부한다.

###  인자 가져오기
```java
UUID crimeId = (UUID) getArguments().getSerializable("crime_id");
```

## 프래그먼트로부터 결과 받기
```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
}

```

단,  Fragment는 setResult(…) 메소드는 갖지 않기 떄문에 아래와 같이 호스팅 액티비티에 결과 값의 반환을 알려주면 된다.

```java
public class CrimeFragment extends Fragment {
	public void returnResult() {
		getActivity().setResult(Activity.RESULT_OK, null);	
	}
}
```



#android/책
