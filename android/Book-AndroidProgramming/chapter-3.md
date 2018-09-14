# 3. 액티비티 생명주기
> 액티비티의 모든 인스턴스는 실행(running), 일시 중지(paused), 중단(stopped) 상태로 이루어진 생명주기(lifecycle)을 갖는다.  
> 액티비티에 상태 변경을 알려주는 메소드들은 안드로이드 런타임이 자동 호출한다.  

##  생명주기 별 호출되는 메소드

```java
class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.*O*)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.*activity_main*)

        Log.i("MainActivity", "onCreate()")

    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart()")

    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume()")

    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause()")

    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "onStop()")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity", "onDestroy()")

    }
}

```

* Activity가 실행될 때
아래와 같은 순서로 메소드가 호출된다.
/MainActivity: onCreate()
/MainActivity: onStart()
/MainActivity: onResume()

* Activity가 전환될 때
아래와 같은 순서로 메소드가 호출된다.
/MainActivity:onPause() - 아직 Activity가 화면에 보일 때
/MainActivity:onStop()  - Activity가 화면에서 보이지않을 때
—보통 위 두개의 메소드만 호출된다.--
/MainActivity:onDestroy() - Activity가 소멸할 때
메모리에서 소멸시킨다.
(보통 앱이 실행을 끝났을 때 호출된다.)

## 장치 회전과 액티비티 생명주기
장치의 화면을 회전시키면 Activity 인스턴스가 소멸되었다가 새로 생성된다.
onPause-> onStop -> onDestroy  -> onCreate -> onStart -> onResume

* 결함 
QuizApp에서 QuizActivity가 새로 생성될 때마다 currentindex가 0으로 초기화되어 사용자는 첫 번째 문제부터 다시 시작하게 된다. 

### 장치 구성과 대체 리소스
장치 구성 : 각 장치의 현재 상태를 나타내는 특성들의 집합
		  ex) 화면 방향, 화면 밀도, 화면 크기 등.

애플리케이션에는 서로 다른 장치 구성들에 맞추기 위해 대체 리소스를 제공한다. 
(다른 장치 구성에 맞추기위해 같은 리소스를 각 특성마다 다르게 제공한다.)

화면 방향과 같은 요소는 런타임 시에 변경될 수 있고, 화면 밀도와 같은 요소는 런타임 시에 변경될 수 없다.

즉, 화면의 방향이 전환될 때 인스턴스가 새로 생성되는 것은 새로운 구성에 가장 잘 맞는 리소스들을 가질 수 있도록 하기 위해서이다.

-> 화면 구성이 변경되면 현재 액티비티의 인스턴스를 소멸시키고 새로운 인스턴스를 생성한다.

#### 가로 방향 레이아웃 생성하기.
```
res 디렉토리에 Android resource directory를 생성한다.
Resource type : layout
Available qualifiers : orientation -> landscape
Directory name : layout-land
```
-land 접미사는 구성 수식자 중 하나다. 
res 서브 디렉토리의 구성 수식자는 현재 장치 구성에 가장 잘 맞는 리소스들을 안드로이드가 식별하는 방법이다.
**즉, 화면 방향이 가로일 때는 res/layout-land 리소스를 사용하고, 화면 방향이 세로일 때는 res/layout 리소스를 사용한다.**

```xml
/*res/layout-land/activity_quiz.xml*/
<FrameLayout ...>
// 특정 방법으로 자식 view들을 배열하지 않는다.
// 자식 view들은 android:layout_gravity 속성에 따라 배열된다.
</FrameLayout>
```

## 장치 회전 시 데이터 저장하기
`fun onSaveInstanceState(outstate: Bundle)`
메소드를 사용하여 데이터를 저장할 수 있다.
이 메소드는 onPause, onStop, onDestroy가 호출되기 전에 시스템에서 호출한다.
이 메소드의 슈퍼메소드에서는 Bundle 객체로 데이터를 저장하게 한다. 
즉, onCreate함수에서 bundle에 저장된 데이터를 읽어올 수 있다.

```java
class QuizActivity: AppCompatActivity() {
	companion object {
		const val KEY_INDEX = "index"
	}

	private var currentIndex = 0

	override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.*activity_quiz*)

    if(savedInstanceState != null) {
        currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
    }

	// KEY_INDEX에 저장된 값을 currentIndex에 넣어준다.
}	

override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState)
    Log.i("QuizActivity", "onSaveInstanceState")
    outState?.putInt(KEY_INDEX, currentIndex)
	}
// KEY_INDEX에 currentIndex를 저장한다.
}
```

* 필요성
일정 시간 장치가 사용되지 않거나, 메모리가 부족할 때 액티비티가 소멸할 수도 있다. 단, 실행중인 액티비티는 절대 소멸시키지 않고, paused or stopped 상태의 액티비티는 해당될 수 있다. 이때, onSaveInstanceState(...)함수를 호출한다.  
이때 Bundle객체는 안드로이드 운영체제에 의해 액티비티 레코드에 저장된다. 
**즉, 액티비티 레코드를 이용하여 액티비티를 되살릴 수 있는 것이다.**

액티비티 레코드는 애플리케이션, 장치가 종료될 때 폐기된다.  또는 오랫동안 사용하지 않을시 폐기될 수 있다.


#android