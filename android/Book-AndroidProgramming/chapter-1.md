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

```
* text tab : 그래픽 형태의 레이아웃을 XML소스로 보여준다. 직접 XML을 
			 작성하여 레이아웃에 추가하거나 변경할 수 있다. 
```

## 사용자 인터페이스 레이아웃 만들기
위젯
* 사용자 인터페이스를 만드는 구성요소로,
텍스트나 그래픽을 보여줄 수 있고, 화면에 다른 위젯을 배치할 수 있다.
ex) 버튼, 체크 상자.
* 모든 위젯은 View 클래스의 인스턴스이거나, View의 서브클래스(TextView, Button)의 인스턴스이다.
* activity.xml.에 정의되야 한다.

### 뷰 계층구조
> 위젯들은 View 객체들의 계층구조에 존재한다.  
> 루트 요소의 안드로이드 리소스 XML 네임스페이스를 http://schemas.android.com/apk/res/android로 지정해야 한다.  
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
> (시작 후 이벤트 발생을 기다린다.)  

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

## 에뮬레이터에서 실행하기
> 안드로이드 애플리케이션을 실행하려면 실제 장치나 안드로이드 가상 장치(AVD) 중 하나가 필요하다.  
* AVD (Android Virtual Device) 
: 안드로이드 SDK에 포함되어 함께 배포된다. 
  각 실제 장치와 동일한 런타임 환경으로 실행될 수 있도록 에뮬레이터로 생성되어야 한다.
에뮬레이터는 컴퓨터에서 안드로이드 운영체제가 따로 실행되게 해주는 것과 같다. 

* 에뮬레이터 생성 : 에뮬레이션할 안드로이드 장치와 시스템 이미지를 선택한다. 
( 시스템 미지에는 리눅스 커널과 안드로이드 프레임워크가 포함된다.)

* 에뮬레이터 실행시 “Intel HAXM is required to run this AVD. VT-x is disabled in BIOS”와 같이 HAXM관련 에러가 발생할 경우
	1. HAXM을 올바르게 설치한다. (인텔 HAXM을 사용하여 에뮬레이터 성능 향상시키기 절 참조)
	2. 컴퓨터에서 사용 중인 백신 프로그램에서 VT 를 사용하고 있는지 확인.
	사용 중이라면 해제하고 재부팅.
	3. AVD 생성시 ABI를 armeabi-v7a로 선택하여 재생성.
	
* 안드로이드 프레임워크 6.0 이상 버전의 에뮬레이터를 사용하여 얻을 수 있는 장점
	1. SMP(Symmetric Multi-Processor)를 지원하여 성능이 향상되었다.
	-> 두 개 이상의 동일한 CPU 코어가 주 메모리에 대한 액세스를 공유하는 설계
	-> 이전 버전은 UP(Uni-Processor)로 구성.  (단일 프로세서)
		 : 여러개의 CPU가 있지만 하나만 사용하여 애플리케이션을 실행하였다.
	즉, 멀티코어 CPU 환경에서 안드로이드 애플리케이션을 더 빠르게 실행할 수 있게 되었다.
	2. 안드로이드 스튜디오에서 ADB(Android Debug Bridge)를 통해 에뮬레이터로 APK를 전송하는 시간이 빨라짐. 
 	용량이 큰 애플리케이션의 경우 유용하다.
	3. 에뮬레이터에 여러 사용자 인터페이스가 추가되어 더 편리하게 애플리케이션을 테스트할 수 있다. (볼륨 조정, 화면 방향 전환, 화면 캡처 등..)

## 안드로이드 앱 빌드 절차
안드로이드 도구들이 리소스 파일, 코드 파일 그리고 AndroidManifest파일을 하나의 .apk파일로 만들어준다.
.apk 파일은 실제 장치나 에뮬레이터에서 실행될 수 있게 디버그 키를 가지고 있다.

ART가 AOT를 수행하여 .dex 바이트 코드를 CPU가 필요로 하는 기계어로 일괄 변환한다. : ELF(Executable and Linkable Format) 
	-> 실행 속도가 더 빠르고, 배터리 수명도 향상된다.
aapt(Android Asset Packaging Tool)가 레이아웃 파일의 리소스를 컴파일 하여 .apk파일에 통합된다.
Activity의 onCreate()에서 setContentView()가 호출되면 LayoutInflater 클래스를 사용하여 레이아웃 파일에 정의된 각 View의 인스턴스를 생성한다.

###   CLI로 gradle사용하기.
터미널/프로젝트 디렉토리
`./gradlew tasks`
`./gradlew installDebug`
