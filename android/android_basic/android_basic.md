# (실무에 바로 적용하는 안드로이드 프로그래밍)
# 1. 처음 만드는 안드로이드 애플리케이션
## 앱 기본사항
1. 액티비티 : 안드로이드 SDK 클래스인 Activity의 개개체로, 화면을 통해서 			  사용자가 작업할 수 있게 해준다. 
			 서브 클래스를 통해서 앱에서 필요한 기능을 구현할 수 있다.
			 액티비티는 하나의 레이아웃으로 구성된다.

2. 레이아웃 : 사용자 인터페이스 객체드로가 그것들의 화면 위치를 정의한다. 
			 XML로 작서된 정의들로 구성되며, 각 정의는 버튼처럼 화면에 나타나는 객체를 생성하는데 사용된다.

## 안드로이드 스튜디오 구성
1. 프로젝트 뷰 : 프로젝트를 구성하는 디렉토리와 파일을 컴퓨터 파일 시스템에 				  저장된 형태로 볼 수 있다.  
				-> 모든 디렉토리와 파일을 보여주므로 필요한 파일을 찾기가 불편하다.
   안드로이드 뷰 : 주로 사용하는 파일이나 디렉토리만 그룹으로 분류하여 				   보여준다.
   				  -> 보여주지 않는 파일과 디렉토리가 있으므로 그 때는 프로젝트 뷰를 사용한다.
2. activity.xml 
	* design tab : 실제 장치에서 나타나는 레이아웃의 모습을 그래픽 형태로 				보여준다.
					이곳에서 직접 변경하면 자동으로 XML 소스로 생성된다.

	* text tab : 그래픽 형태의 레이아웃을 XML소스로 보여준다. 직접 XML을 
				 작성하여 레이아웃에 추가하거나 변경할 수 있다. 

## 사용자 인터페이스 레이아웃 만들기
위젯 
* 사용자 인터페이스를 만드는 구성요소로, 
  텍스트나 그래픽을 보여줄 수 있고, 화면에 다른 위젯을 배치할 수 있다.
  ex) 버튼, 체크 상자.
* 모든 위젯은 View 클래스의 인스턴스이거나, View의 서브클래스(TextView, Button)의 인스턴스이다.
* activity.xml.에 정의되야 한다.

### 뷰 계층구조
> 위젯들은 View 객체들의 계층구조에 존재한다.
루트 요소의 안드로이드 리소스 XML 네임스페이스를 http://schemas.android.com/apk/res/android로 지정해야 한다.

layout은 ViewGroup(View의 서브 클래스)로부터 상속받는다.
 -> ViewGroup은 다른 위젯들을 포함하고 배열하는 위젯이다.

### 위젯 속성

1. android:layout_width, android:layout_height
* match_parent : 자신의 부모만큼의 크기가 된다.
* wrap_content : 자신이 갖는 콘텐츠에 필요한 크기가 된다.

2. android:padding 
지정된 만큼의 공간을 위젯 크기에 추가한다.

3. android:orientation
자신의 자식들이 수직 또는 수평으로 나타낼지 결정한다.

4. android:text
뷰에 보여줄 텍스트를 나타낸다.
	-> 리터럴 문자열 자체를 나타내는 것이 아니다.
	   문자열 리소스에 대한 참조이다.
	   * 문자열 리소스란?
	   		문자열 파일이라고 하는 별도의 XML 파일에 정의된 문자열이다. 

### 문자열 리소스 생성하기
* 모든 프로젝트에는 strings.xml 디폴트 문자열 파일이 포함된다.
app/res/values/strings.xml

* 이름을 달리한 여러개의 문자열 파일을 만들 수 있다.

```xml
<string name="question_text">
        콘스탄티노플은 터키의 가장 큰 도시이다.
    </string>
// question_text를 참조하면 런타임시에 해당 문자열을 받게 된다.
```

## 레이아웃 XML에서 뷰 객체로

```xml
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

* AppCompatActivity : Activity 클래스의 서브 클래스며, 과거 안드로이드 버전과의
호환성을 지원하기 위해 제공된다.
* onCreate() : 액티비티 서브 클래스의 인스턴스가 생성될 때 자동으로 호출된다.
			   액티비티의 사용자 인터페이스 레이아웃(화면)을 보여주고 처리해야 한다.
* setContentView : 레이아웃을 인플레이트(레이아웃을 뷰 객체로 생성하는 것)하여 화면에 나타낸다.

### 리소스와 리소스 ID
> 리소스 : 애플리케이션의 일부이며, 코드가 아닌 이미지 파일이나 오디오 파일 및 XML파일 같은 것들.

* 리소스는 app/res 밑의 서브 디렉토리에 존재한다.
* 코드에서 리소스를 사용하려면 '리소스 ID'를 정해야 한다.

```xml
<Button
            android:id="@+id/true_button"
            android:text="@string/true_button"
            
             />
             // + 표시는 생성을 의미한다.
             // 문자열을 참조만 한다. (+ 없음)
```

### 위젯의 참조 얻기

```java
	public View findViewById(int id)

	fun findViewById(id: Int): View
```
위의 메소드를 호출하면 View 객체로 인플레이트된 위젯의 참조를 얻을 수 있다.

```java
  	val mTrueButton: Button = findViewById(R.id.true_button)
    val mFalseButton: Button = findViewById(R.id.false_button)
```

그러나 최근 안드로이드의 문법으로는 findViewById 메소드를 사용하지 않고 바로 이름으로 참조를 얻을 수 있다.

```java
	val mTrueButton: Button = true_button
	val mFalseButton: Button = false_button
	// 타입은 생략 가능하다.
```

또는, 변수를 생성하지 않고 리소스 ID를 바로 쓸 수 있다.

### 리스너 설정하기
> 안드로이드 애플리케이션은 이벤트 기반으로 구동된다.
(시작 후 이벤트 발생을 기다린다.)

> 이벤트에 응답하기 위해 생성하는 객체를 '리스너'라 한다.

```java
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTrueButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View) {

            }
        })
    }

 /* 람다로 대체 */
       mTrueButton.setOnClickListener { }
 
```

### 토스트 만들기
> 화면에 정보를 띄워주지만, 어떤 입력이나 액션도 요구하지 않는 짤막한 메시지.

```java
	public static Toast makeText(Context context, int resId, int duration)
	// context : Activit의 인스턴스
	// resId : 토스트가 보여주는 문자열 리소스 ID
	// duration : 얼마 동안 화면에 띄워줄 것인지.
```

```java
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        true_button.setOnClickListener {
            Toast.makeText(this,
                    R.string.correct_toast,
                    Toast.LENGTH_SHORT).show()
        }
    }
```
# Android Architecture Pattern
> 화면에 보여주는 로직과 실제 데이터가 처리 되는 로직을 분리.

==MVC(Model - View - Controller)==

**정의**
1. Model : 프로그램에서 사용되는 실제 데이터 및 데이터 조작 로직을 처리하는 부분
2. View : 텍스트, 체크 박스 와 같은 사용자 인터페이스 요소
3. Controller : 사용자의 입력을 받고 처리하는 부분
				데이터와 비즈니스 로직 사이의 상호동작을 관리.

**하는 일**
1. Model : 모델의 상태에 변화가 있을 때 컨트롤러와 뷰에 이를 통보한다. 
2. View : 사용자가 볼 결과물을 생성하기 위해 모델로부터 정보를 얻어 온다.
3. Controller : 모델에 명령을 보냄으로써 모델의 상태를 변경할 수 있다. 
				관련된 뷰에 명령을 보냄으로써 모델의 표시 방법을 바꿀 수 있다.

**처리 순서**
1. Controller로 사용자 입력이 들어옴.
2. Controller는 Model의 데이터 업데이트 및 로드 함
3. Model은 해당 데이터를 보여줄 View를 선택해서 화면에 보여줌.

**장점**

**단점**
1. 컨트롤러가 안드로이드 API에 깊게 종속되어 유닛 테스트가 어렵다.
2. 컨트롤러가 뷰에 단단히 결합되어, 뷰를 변경하면 컨트롤러로 돌아가서 변경해야 한다.
3. 많은 코드가 컨트롤러로 모이게 되면 컨트롤러의 크기가 커지고 문제 발생 확률이 높아진다.
4. View와 Model이 서로 의존적이다.

==MVP (Model - View - Presenter)== 

**정의**
Model과 View의 역할은 같다.
Presenter : View에서 요청한 정보를 Model로 부터 가공해서 View로 전달하는 부분

**MVC와의 차이점**
1. View에서 사용자 입력을 받는다. 
2. Model과 View는 각각 Presenter과 상호 동작을 한다.
   Model과 View는 서로를 알지 못한다.

**처리 순서**
1. View로 사용자 입력이 들어옴.
2. View는 Presenter에 작업 요청을 함.
3. Presenter에서 필요한 데이터를 Model에 요청 함.
4. Model은 Presenter에 필요한 데이터를 응답함
5. Presenter는 View에 데이터를 전달함
6. View는 Presenter로부터 받은 데이터를 화면에 보여줌.

**단점**
View와 Presenter가 강한 의존성을 가지고 된다.

==MVVM(Model + View + ViewModel)==

**정의**
VieModel : View를 표현하기 위해 만들어진 View를 위한 Model

**장점**
Command와 Data Binding으로 View와 ViewModel의 강한 의존성이 사라진다.

**처리 순서**
1. View에 입력이 들어오면 Command 패턴으로 ViewModel에 명령을 함.
2. ViewModel은 필요한 데이터를 Model에 요청 함
3. Model은 ViewModel에 필요한 데이터를 응답 함
4. ViewModel은 응답 받은 데이터를 가공해서 저장 함.
5. View는 ViewModel과 Data Binding으로 인해 자동으로 갱신 됨.

* Command 패턴
> 요청을 객체의 형태로 캡슐화하여 사용자가 보낸 요청을 나중에 이용할 수 있도록 
필요한 정보를 저장 또는 로깅, 취소할 수 있게하는 패턴이다. 

* data binding 
> 바인딩 설정이 올바르고 데이터가 적절한 알림을 제공하는 경우에, 
데이터의 값이 변경되면 데이터에 바인딩된 요소에 변경 내용이 자동으로 반영된다.