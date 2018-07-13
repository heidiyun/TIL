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