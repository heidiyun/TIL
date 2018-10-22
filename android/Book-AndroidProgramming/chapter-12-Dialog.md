# 12. Dialog
## 목적
1. 사용자의 주의를 끌고 입력을 받을 때 사용한다.
2. 중요한 정보를 보여줄 때 유용하다.

AppCompat 라이브러리를 사용하면 최신 다이어로그 스타일을 가져와 사용할 수 있다.

## DialogFragment 생성하기.
AlertDialog를 사용할 때는 Fragment의 서브 클래스인 DialogFragment의 인스턴스에 포함되어 같이 동작하는 것이 좋다.

* DialogFragment : Dialog를 보여주는 프래그먼트로, 프래그먼트를 호스팅하는 액티비티 창 위로 대화상자가 나타납니다.
* DialogFragment 없이 AlertDialog를 보여주는 것도 가능하지만, 권장하지 않습니다.
Dialog가 FragmentManager에 의해 관리되면 여러가지 장점이 있다.
	* 사용자가 Back 버튼을 누르거나 화면을 돌릴 때 등 수명 주기 이벤트를 올바르게 처리하도록 보장할 수 있습니다.
	* 대화상자의 UI를 재사용할 수 있습니다.

* DialogFragment는 Android3.0(API 레벨 11)에 추가되었습니다.

DialogFragment를 FragmentManager에 추가하려면 다음 메소드를 호출하자.
```java
public void show(FragmentManager manager, String tag)
// transaticon이 자동으로 생성되고 커밋된다.
public void show(FragmentTransaction transaction, String tag)
// 사용자가 transaction을 생성하고 커밋해야 한다.
```

## 대화상자의 콘텐트 설정하기.
```java
public AlertDialog.Builder setView(View view)
```
  위의 메소드의 인자로 전달된 view객체를 대화상자의 내용으로 띄울 것이다.

```java
/* DatePickerFragment */
@Override
public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

    View v = LayoutInflater./from/(getActivity()).inflate(R.layout./dialog_date/, null);

    return new AlertDialog.Builder(getActivity())
            .setView(v)
            .setTitle(R.string./date_picker_title/)
            .setPositiveButton(android.R.string./ok/, null)
            .create();
}
```

inflate한 view 객체를 AlertDialog의 setView의 인자로 넘겨주면 Dialog에 적용된다.

## 프래그먼트 간에 데이터 전달하기
> 프래그먼트 인자의 생성과 설정은 프래그먼트 생성자를 대체하는 newInstance() 메소드에서 처리한다.  

```java
public static DatePickerFragment newInstance(Date date) {
    Bundle args = new Bundle();
    args.putSerializable(/ARG_DATE/, date);

    DatePickerFragment fragment = new DatePickerFragment();
    fragment.setArguments(args);
    return fragment;
}
```

### 데이터 반환하기
#### 목표 프래그먼트 설정하기.
```java
public void setTargetFragment(Fragment fragment, int requestCode)
```
 FragmentManager는 목표 프래그먼트와 요청 코드를 추적 관리한다.
목표로 설정했던 프래그먼트의 getTargetFragment(), getTargetRequestCode() 메소드를 호출하면 그 정보를 알 수 있다.






#android/책