# 13. Toolbar
> 툴바 컴포넌트는 안드로이드 5.0(Lollipop)에서 추가되었다.  

## AppCompat 라이브러리 사용
1. 모든 액티비티를 AppCompatActivity를 상속하도록 하자.
2. AppCompat 라이브러리를 추가한다.
3. AppCompat의 테마 중 하나를 사용한다.
	* 테마는 AndroidManifest.xml에서 설정할 수 있다.
	* 테마는 res_values_styles.xml에 정의되어 있다. 
	* 앱 테마는 프로젝트 생성 시, 선택한 템플릿과 minimumSDK 레벨에 따라 여러 버전이 있다.
	서로 다른 안드로이드 버전에 맞게 구성된 리소스 수식자이다.
	* **AppCompat 라이브러리를 사용하면 모든 안드로이드 버전에 일관된 형태를 제공하기 때문에 테마를 변경할 필요가 없다.**

## 메뉴
툴바의 제일 오른쪽에 메뉴를 둘 수 있다.
메뉴는 액션 항목 (action item)으로 구성된다.
	* 현재 화면과 관련된 액션/ 앱 전체의 액션을 수행할 수 있다.

### 앱의 네임스페이스
app과 같이 특이한 네임스페이스는 AppCompat 라이브러리와 관련해서 필요하다.

### 안드로이드 에셋 구성하기
1. 시스템 아이콘을 참조한다.
```xml
@android:drawable/ic_menu_xx 
```

2. 사용자가 직접 구성한다.
	* 각 화면 밀도별로 하나씩 만들어야 한다.
	* [참고 문헌](http://developer.android.com/design/style/iconography.html)
3. 앱의 요구에 맞는 시스템 아이콘을 찾아서 프로젝트 리소스로 복사한다.
	* 시스템 아이콘은 설치된 안드로이드 SDK 디렉토리에 있다. 
4. 안드로이드 스튜디오의 에셋 구성 도구를 이용하자. 
res_new_Image Asset

### 메뉴 생성하기
코드에서 메뉴는 Activity 클래스의 callback 메소드에 의해 관리된다. 
	* callback method : 특정 상황에서 시스템이 호출하는 것.

```java
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
// 메뉴를 생성하는 메소드.
```

호스팅 액티비티가 운영체제로부터 onCreateOptionsMenu(…)를 호출 받았을 때  FragmentManager는 Fragmnet.onCreateOptionsMenu(Menu, MenuInflater)를 호출하는 책임을 갖는다. 
프래그먼트가 onCreateOptionsMenu(…) 호출을 받아야 한다는 것을 명시적으로 FragmentManager에게 알려주어야 한다.
```java
public void setHasOptionsMenu(boolean hasMenu)
```

### 메뉴 선택에 응답하기

```java
public boolean onOptionsItemSelected(MenuItem item)
// 액션 항목 선택에 응답하는 메소드.
```

어떤 액션 항목이 선택되었는지는 MenuItem의 ID를 통해 확인할 수 있다. 
(menu 파일의 resource ID와 일치한다.)

## 계층적 내비게이션 활성화하기
Back button -> 일시적 내비게이션 (직전의 화면으로 이동함)
Up button -> 계층적 내비게이션 (현재 화면 위로 이동한다.)

```xml
<activity android:name=".controller.CrimePagerActivity"    android:parentActivityName=".controller.CrimeListActivity"/>
// up button을 클릭하였을 때, 이동하는 액티비티를 지정한다.
// up button 활성화.
```

### 계층적 내비게이션의 동작 방법
CriminalIntentJ에서 up button을 누르면 다음과 같은 인텐트가 생성된다.
```java
Intent intent = new Intent(this, CrimekListActivity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
startActivity(intent);
finish();
```

**FLAG_ACTIVITY_CLEAR_TOP**
액티비티 스택에  사용하고자하는 기존 액티비티 인스턴스가 있다면 해당 액티비티 인스턴스가 스택의 가장 위에 올라올 수 있도록 모든 다른 액티비티를 스택에서 꺼낸다.

0 -> 1 -> 2 -> 3 
Up button으로 3 액티비티에서 1 액티비티를 시작시키면

0 -> 1 -> 2 -> 3 -> 1 이 아니라, 
0 -> 1 이 된다.

## 툴바 vs 액션바
([TIL/Toolbar.md at master · heidiyun/TIL · GitHub](https://github.com/heidiyun/TIL/blob/master/android/android_basic/Toolbar.md)
참고하세요.

#android/책