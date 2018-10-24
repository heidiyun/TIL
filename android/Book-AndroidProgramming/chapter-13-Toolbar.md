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

public boolean onOptionsItemSelected(MenuItem item)
// 액션 항목 선택에 응답하는 메소드.
```

호스팅 액티비티가 운영체제로부터 onCreateOptionsMenu(…)를 호출 받았을 때  FragmentManager는 Fragmnet.onCreateOptionsMenu(Menu, MenuInflater)를 호출하는 책임을 갖는다. 
프래그먼트가 onCreateOptionsMenu(…) 호출을 받아야 한다는 것을 명시적으로 FragmentManager에게 알려주어야 한다.
```java
public void setHasOptionsMenu(boolean hasMenu)
```



#android/책