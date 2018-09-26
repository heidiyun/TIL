# 8. 레이아웃과 위젯으로 사용자 인터페이스 생성하기
## XML 레이아웃 속성
### 스타일(style)
위젯이 어떻게 보이고 동작하는지를 나타내는 속성을 지정하는 XML리소스.
예) 보통보다 더 큰 크기의 텍스트 위젯을 구성하는 스타일 리소스
```xml
<style name="BigTextStyle">
	<item name="android:textSize">20sp</item>
	<item name="android:padding">3dp</item>
</style>
```

스타일 리소스를 사용하면 사용자가 원하는 스타일을 만들어 낼 수 있다.
res/values 에 파일로 추가하면 레이아웃 파일에서 만든 스타일을 지정할 수 있다.

### 테마(theme)
> 스타일의 모음  

안드로이드에서는 애플리케이션이 사용할 수 있는 테마를 제공한다.
테마 속성 참조를 사용하면 앱의 테마 스타일을 위젯에 적용할 수 있다.
```xml
style="?android:listSeparatorTextViewStyle"
// 안드로이드 런타임 리소스 매니저에게 앱의 테마에가서 
// listSeparatorTextViewStyle이라는 이름의 속성을 찾아라. 
// 이 속성은 다른 스타일 리소스를 가리킨다. 
// 그 리소스의 값을 가져다가 여기에 넣어라.
```

### 화면 픽셀 밀도: dp, sp
* dp(density-independent pixel) 
밀도 독립적 픽셀
크기를 픽셀 값으로 지정하지 않을 때 사용된다.
장치 화면이 고밀도일 때는 많은 수의 픽셀을 갖는 화면을 채우기 위해 dp가 확장된다.
하나의 dp는 항상 장치 화면의 1/160이다.
화면 밀도와 무관하게 일정한 크기를 갖는다.
즉, 어떤 해상도의 장치에서 보든 같은 크기로 보이도록 해준다.
*  sp(scale-independent pixel)
크기 독립적 픽셀
사용자의 폰트 크기 선택도 고려한 dp이다. 
주로 화면에 나타나는 텍스트 크기를 설정하기 위해 사용된다.
* pt, mm ,in
포인트(1/72인치),  밀리미터, 인치로 크기를 지정할 수 있는 크기 단위다.
모든 장치에 잘 맞게 구성될 수 없어서 사용을 권장하진 않는다.

### 안드로이드 디자인 지침
http://developer.android.com/design/index.html
가능한 이 지침을 따라야 한다.

### 레이아웃 매개변수
속성 중 이름이 layout_으로 시작하는 것을 가르키며, 위젯의 부모에 지시하는 것이다.

### Margin vs Padding
Margin : 레이아웃 매개변수로, 위젯들 가느이 간격을 결정한다.
		위젯이 margin을 알 수 없다.

Padding : 위젯이 갖고 있는 콘텐츠보다 자신이 얼마나 더 커야 하는지를 나타낸다.

## 그래픽 레이아웃 도구 사용하기.
![create landscape variation](https://user-images.githubusercontent.com/38517815/46059920-2daa6200-c19c-11e8-8f24-2410db1a7115.png)
Create landscape variation 메뉴를 실행시키면 가로 방향 레이아웃이 생성된다.
res/layout-land 디렉토리가 생성되고, 기존 세로 방향 레이아웃 파일이 이 디렉토리에 복사된다.

### android:layout_weight 속성
이 속성은 LinearLayout에 자식들을 배치한느 방법을 알려준다.
자식뷰들의 layout_width를 보고, 남는 공간을 layout_weight에 따라 할당한다.

자식뷰들의 layout_width와 상관없이 배치하고 싶다면 layout_width의 값을 0dp로 할당한다. 그러면 layout_weight의 값만 기준으로 삼아 배치된다.

### 위젯 ID와 복수 레이아웃
세로와 가로 레이아웃이 다른 위젯을 갖는 경우라면 위젯을 엑세스하기 전에 해당 레이아웃에 존재하는지 확인 작업을 거쳐야 한다.

```java
Button landscapeOnlyButton = findViewById(R.id.landscapeOnlyButton);
if (landscapeOnlyButton != null) {
	// 할 일
}
```



#android/책