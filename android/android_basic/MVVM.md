# MVVM
> Model View ViewModel  
1. Model : 프로그램에서 사용되는 실제 데이터를 담고 있는 부분.
Network, DB, SharedPreference
2. View : View가 ViewModel을 관찰하면서 이벤트를 받아 상태를 갱신한다.
Activity, Fragment, Dialog, Toast, Snackbar, Menu 등의 UI 컴포넌트
3. View Model : 모델을 업데이트하고 업데이트 된 정보를 View가 수신할 수 있도록 이벤트를  보낸다.
View또는 Activity Context에 대한 참조를 가지면 안된다.

* ViewModel은 Model 객체를 가지고 있으나 View 객체는 가지고 있지 않습니다.  View는 ViewModel을 관찰하고 있으면서 model의 데이터가 변경되면 화면을 갱신하는 역할을 지닙니다.

* 이런 특성 때문에 ViewModel이 View에 종속되지 않으며, 한개의 View model에 여러개의 View가 대응될 수 있습니다.

## Data Binding
데이터 바인딩은 UI와 비즈니스 로직을 연결하는 프로세스다.
데이터의 값이 변경될 때마다 데이터에 연결된 UI의 요소에 변경 내용이 자동으로 반영되는 것을 말한다.

안드로이드에서는 Data Binding library 를 제공합니다.
레이아웃을 통해서 DataBinding을 구현하는 형태로 제공됩니다.

### 장점
* 데이터 바인딩을 사용하면  VIew와 ViewModel이 약한 결합을 가질 수 있다.
* 자바에서 코드를 작성할 경우, findViewById의 보일러플레이트를 줄일 수 있다.
Butterknife 라이브러리를 사용하지 않아도 됩니다.

```java
/* TextView가 레이아웃 파일에 4개가 있다. */
public class MainActivity: AppCompatActivity() {
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;

	@Override
	protected void onCreate(@Nullable Bundle 	savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textView1 = (TextView) findViewById(R.id.text_view_1);
		textView2 = (TextView) findViewById(R.id.text_view_2);
		textView3 = (TextView) findViewById(R.id.text_view_3);
		textView4 = (TextView) findViewById(R.id.text_view_4);

	}
}

/* ButterKnife library 사용 */

public class MainActivity: AppCompatActivity() {
	@BindView(R.id.text_view_1)
	TextView textView1;
	@BindView(R.id.text_view_2)
	TextView textView2;
	@BindView(R.id.text_view_3)
	TextView textView3;
	@BindView(R.id.text_view_4)
	TextView textView4;

	@Override
	protected void onCreate(@Nullable Bundle 	savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}
}

/* DataBidning */
public class MainActivity: AppCompatActivity() {
  	private	ActivityMainBinding binding;
	@Override
	protected void onCreate(@Nullable Bundle 	savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		binding.setModel(MyViewModel());
		// binding.text_view_1 바로 접근 가능!
	}
}
```

### One Way DataBinding
모델 객체에 대한 변경 내용은 뷰 객체에 반영되지만,
뷰 객체
의 변경 내용은 모델 객체에 반영되지 않는 모습과 같이 한쪽 방향으로만 변경 사항이 업데이트 되는 흐름을 말한다.

### Two Way DataBinding
모델 객체와 뷰 객체의 변경 사항이 서로에게 자동으로 반영되는 것과 같이 바인딩 되어 있는 두 개체의 변경사항이 상호 업데이트 되는 흐름을 말합니다. 

### 준비 환경
1. 데이터 바인딩 라이브러리는 support library로 Android2.1 이상에서 사용할 수 있습니다. 
2. Android Plugin for Gradle 1.5.0-alpha1 이상

```xml
android {
    ....
    dataBinding {
        enabled = true
    }
}

```


### 예제 (one-way-databinding)
> 이메일과 패스워드를 사용하여 가상 로그인.  
1. model class
```kotlin

class User(var email: String, var password: String) {

    fun isInputDataValid(): Boolean {
        return !TextUtils.isEmpty(email)
                && Patterns./EMAIL_ADDRESS/.matcher(email).matches()
                && password.length > 5
    }
}
```

생성자의 인자로 이메일과 패스워드를 받고, 이메일 형식과 패스워드를 확인하는 메소드를 가지고 있다.

2. ViewModel 
```kotlin
class LoginViewModel : BaseObservable() {
    val user = User("", "")

    fun afterEmailTextChanged(s: CharSequence) {
        user.email = s.toString()
    }

    fun afterPasswordTextChanged(s: CharSequence) {
        user.password = s.toString()
    }

    fun onLoginButtonClicked() {

        if (user.isInputDataValid()) {
            Log.i("LoginViewModel", user.email)
            Log.i("LoginViewModel", user.password)
        }

    }

}

```

모델 객체를 가지고 있다.
onLoginButtonClicked 메소드에서 model 객체의 정보도 바뀌었는지 확인한다.

3. View
**Activity**
```kotlin
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityUserBinding>(this, R.layout./activity_user/)
        binding./viewModel/= LoginViewModel()
    }
}

```

**xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
        xmlns:android="http://schemas.android.com/apk/res/android" xmlns:bind="http://schemas.android.com/apk/res-auto">

    <data>
        <variable name="viewModel" type="kr.ac.ajou.heidi.architecturesample.LoginViewModel"/>
    </data>

    <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent"
                  android:orientation="vertical">


        <EditText
                android:id="@+id/inEmail"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:afterTextChanged="@{(editable) -> viewModel.afterEmailTextChanged(editable)}"
                android:inputType="textEmailAddress"
        />


        <EditText
                android:id="@+id/inPassword"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:afterTextChanged="@{(editable) -> viewModel.afterPasswordTextChanged(editable)}"
                android:inputType="textPassword"
        />

        <Button android:layout_width="match_parent" android:layout_height="wrap_content"
                android:onClick="@{()->viewModel.onLoginButtonClicked()}"
                android:text="LOGIN"


        />


    </LinearLayout>
</layout>


```

### 예제(two-way-databinding)

```kotlin
class LoginViewModel : BaseObservable() {

    val user = User("hi@naver.com", "")


    @Bindable
    fun getEmail(): String {
        return user.email
    }

    fun setEmail(email: String) {
        user.email = email
        notifyPropertyChanged(BR./email/)
    }




    fun afterEmailTextChanged(s: CharSequence) {
        user.email = s.toString()


    }

    fun afterPasswordTextChanged(s: CharSequence) {
        user.password = s.toString()

    }

    fun onLoginButtonClicked() {

        if (user.isInputDataValid()) {
            Log.i("LoginViewModel", user.email)
            Log.i("LoginViewModel", user.password)
        }

    }

}

```

```xml
<EditText
        android:id="@+id/inEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@={viewModel.email}"
        android:inputType="textEmailAddress"
/>

```

- [ ] MVVM과 데이터 바인딩이 왜 필요한지.

- [ ] MVC/MVP와 비교해서 어떤 특징이 있는지.
- [x] MVVM의 개념.
- [x] 데이터 바인딩의 개념
- [x] 데이터 바인딩을 통한 MVVM의 구현.

- [x] file말고 model을 해서 할 수 있는 예제를 찾아보자. 
- [ ] androidx viewmodel 사용한 예제
- [ ] viewmodel을 쓸 경우와 BaseObserval를 쓸 경우의 차이점?

- [x] 데이터 바인딩 까지 오는 길 버터나이프.
#android