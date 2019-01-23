# Chapter 9,10,11 문제
1. 싱글톤 장,단점

2. FragmentStatePagerAdapter vs FragmentPagerAdapter 차이점

3. 싱글톤 사용방법

4.  프래그먼트 인자를 사용하는 이유 

5.  프래그먼트가 자신의 호스팅 액티비티에 속한 인텐트에 직접 액세스하는 방법이 안 좋은 이유와 더 좋은 방법을 쓰시오

6. 프래그먼트의 생명 주기를 설명하시오

7. Activity Context , Application Context의 차이를 설명하시오


- - - -
1. 어느 클래스에서나 자유롭게 접근할 수 있어서 편리하다.
공통 인스턴스를 여러개 만들어야 될 때 편리하다.
데이터를 공유하기 편하다.
메모리 낭비를 줄일 수 있다.

코드의 단위 테스트를 어렵게 한다.
싱글톤 인스턴스에 대한 의존성이 높아져서 앱의 유지 보수가 힘들어질 수 있다.

2. FragmentStatePagerAdapter는 필요없어진 프래그먼트가 소멸된다.
소멸 시, 필요한 데이터를 번들에 저장하여 해당 프래그먼트를 다시 생성할 시 복원할 수 있다.
메모리가 FragmentPagerAdapter보다 절약되며, 데이터가 많고 프래그먼트 수가 많고 유동적일 때 유용하다.

FragmentPagerAdapter는 필요 없어진 프래그먼트가 소멸되는 것이 아니라, 프래그먼트의 뷰를 삭제한다.
프래그먼트 인스턴스는 fragmentManager에 저장되어 있다.
프래그먼트의 수가 적고 데이터의 크기가 작을 때 유용하며, 코드가 더욱 간결하다.

3. 사용방법
* 생성자를 private로 만든다.
* 인스턴스를 반환하는 get() 메소드를 static으로 만든다.
생성된 인스턴스가 있으면 그것을 반환하고
없으면 생성자를 호출하여 새로 생성한 뒤 반환한다.
* 생성된 인스턴스를 저장할 변수를 private로 만든다.

4. 	메모리의 회수 시점이 불확실하기 때문에 프래그먼트가 생성될 때 인스턴스 변수를 설정하는 것은 신뢰할 수 있는 방법이 아니다. 

5. 프래그먼트의 캡슐화가 어려워진다.  이보다 좋은 방법은 프래그먼트 인자를 통해 전달하면 된다. 

6. 프래그먼트 생성 -> onAttach() -> onCreate() -> onCreateView -> onActivityCreated() -> onStart() -> onResume() -> 프래그먼트 detach,replace() -> onPause() -> onStop() -> onDestroyView() -> onDestroy() -> onDetach()

7. 액티비티 컨텍스트는 액티비티의 정보를 가지고 있으므로 액티비티의 정보, 뷰 등을 수정할 때 사용하는 것이 좋다.  -> 라이프 사이클과 관련된 일을 하는 경우

어플리케이션 Context는 어플리케이션 전체에 대한 컨텍스트이므로 서비스 등 어플리케이션 전반적으로 일어나는 일에 사용하는 것이 좋다. -> 라이프 사이클과 관련 없는 일을 하는 경우

#android/책