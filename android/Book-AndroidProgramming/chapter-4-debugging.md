# 4. 안드로이드 앱의 디버깅
## 오류를 찾는 방법
1. 스택 기록 로깅
* Log.e : error
* Log.w : warning
* Log.i : information
* Log.d : debugging
* Log.v : developing
* parameter 
	* TAG : String, message: String
	* TAG: String, message: String, exception: Throwable
	* TAG로  Logcat에서 필터링 할 수 있다.
	* 생성된 exception을 인자로 받을 수 있고, 새로 exception을 생성할 수 있다.

	장점 
	 코드 여러 곳에서 발생하는 스택 기록을 하나의 로그에서 볼 수 있다.
	 TAG로 검색하여 사용자가 원하는 로그 기록만 분류하여 볼 수 있다.

2. 중단점 사용하기. 
: 특정 코드에 중단점을 찍고 애플리케이션을 디버깅하게 되면 중단점이 찍힌 코드가 실행될 때 애플리케이션이 중지한다. 
코드가 사용하는 변수와 객체 등의 정보를 볼 수 있다.
Resume Program 버튼을 누르면 중단점 이후의 코드를 실행시킬 수 있다.

	장점   
	새로운 로그를 작성하기 위해서 앱을 다시 디버깅해야 하는 번거로움이 없다.  
 	앱을 실행하면서 중단점을 설정할 수 있어 편리하며 코드가 사용하는 리소스에 대한 정보를 한 눈에 파악할 수 있다.

3. 예외 중단점 사용하기. 
: 사용자가 지정한 예외한 발생하는 코드 부분에서 앱 실행이 멈춘다. 
ex)Run -> View Breakpoints -> add -> Java Exception Breakpoints -> RuntimeException 

	주의   
	중단점을 이용하지 않을 때에는 반드시 해제시켜주어야 사용자가 의도치 않은 중단을 방지할 수 있다.

4. Lint 사용하기. 
: 정적 분석기로써 앱을 실행하기 전 컴파일 타임에 코드의 오류를 찾는 것이다.  
  컴파일러가 알 수 없는 문제를 사전에 찾을 수 있다.   
  -> 해당 라인을 빨간색 줄로 표현해준다.  
Analyze -> Inspect Code (검사 범위 설정) -> Inspection Results 창에서 확인할 수 있다.  

## R 클래스 관련 문제
1. res의 xml 파일이 제대로 작성되었는지 확인한다.
만약, R.java 파일이 최신 버전이 아니라면 어디서 리소스를 참조하든 에러가 생긴다.
2. 프로젝트를 새로 빌드한다.
Build -> Clean Project
Build -> Rebuild Project
3. gradle을 실행하여 프로젝트를 동기화한다.
Tools -> Android -> Sync Project with Gradle Files
현재 프로젝트 설정에 맞춰 다시 빌드 해준다.
5. 안드로이드 Lint를 실행한다.


#android