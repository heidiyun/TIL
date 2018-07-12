# Implicit Intent

intent : 수행하고자 하는 일을 안드로이드 운영체제에 알려주는 객체

Explicit Intent : 시작시킬 액티비티 클래스를 지정하는 것.

Implicit Intent : 원하는 작업을 안드로이드 운영체제에 알려줌.
				-> 작업이 가능한 액티비티를 안드로이드 운영체제가 찾아서 시작 시킴.
				( 작업 가능한 액티비티가 여러개이면, 사용자가 선택할 수 있다.)

**구성요소**
1. action
> Intent 클래스의 상수. (무슨 일을 할 것인지)

* 웹 URL : Intent.ACTION_VIEW
```xml
	<action android:name=android.intent.action.VIEW" />
```
* 정보 전송 : Intent.ACIONT_SEND

2. data
> MIME/URI
액션을 수행하기 위해 필요한 정보
```xml
	<data android:host="git" android:scheme="http" />
```

3. category (어디서, 언제, 어떻게 사용할 것인지)
* 인터넷 브라우저를 사용할 때 : 
```xml
<category android:name="android.intent.category.BROWSABLE" />
```
* 암시적 인텐트에 응답할 때 :
```xml
	<category android:name="android.intent.category.DEFAULT" />
```
모든 암시적 인텐트에 자동 추가됨.

* 액티비티가 최상위 수준의 앱으로 런처에 보여할 때
```xml
	<category android:name="android.intent.category.LAUNCHER" />
```
