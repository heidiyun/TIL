1. 프래그먼트 인자를 사용하는 이유를 설명


2. FragmentStatePagerAdapter와 FragmentPagerAdapter 의 차이점과 각 장점을 설명
> FragmentStatePagerAdapter는 필요없어진 프래그먼트를 소멸시킨다.
프래그먼트의 필요한 데이터를 bundle에 저장하여 새로 해당 프래그먼트를 생성할 시 복원할 수 있다.
프래그먼트 수가 많고, 데이터의 크기가 클 때 유용하다.
FragmentPagerAdapter를 사용할 때 보다 메모리를 절약할 수 있다.


> FragmentPagerAdapter는 필요없어진 프래그먼트의 뷰를 삭제한다.
그래서 메모리에 계속 해당 프래그먼트가 남아있어 FragmentManager의 프래그먼트 리스트에 저장되어 있다.
프래그먼트 수가 적고, 데이터의 크기가 작을 때 유용하다.
그리고 코드의 구현이 FragmentStatePager보다 간단하다.


1. 싱글톤 장,단점
> 장점 : 여러 클래스에서 접근할 수 있어 데이터를 공유하기가 편하다.
	  같은 인스턴스를 여러번 만들 때보다 메모리 낭비가 덜하다.

> 단점 : 싱글톤 인스턴스에대한 의존성이 높아져 코드의 단위 테스트가 힘들어진다.

2. FragmentStatePagerAdapter vs FragmentPagerAdapter 차이점
> FragmentStatePagerAdapter는 필요없어진 프래그먼트를 소멸시킨다.
프래그먼트의 필요한 데이터를 bundle에 저장하여 새로 해당 프래그먼트를 생성할 시 복원할 수 있다.
프래그먼트 수가 많고, 데이터의 크기가 클 때 유용하다.
FragmentPagerAdapter를 사용할 때 보다 메모리를 절약할 수 있다.


> FragmentPagerAdapter는 필요없어진 프래그먼트의 뷰를 삭제한다.
그래서 메모리에 계속 해당 프래그먼트가 남아있어 FragmentManager의 프래그먼트 리스트에 저장되어 있다.
프래그먼트 수가 적고, 데이터의 크기가 작을 때 유용하다.
그리고 코드의 구현이 FragmentStatePager보다 간단하다.

3. 싱글톤 사용방법
> 생성자를 private로 만든다.
> 생성한 인스턴스를 저장할 private 변수를 만든다.
> 외부 클래스에서 인스턴스를 반환받을 get함수를 만든다.
	* 이미 생성된 인스턴스가 있을 경우에는 기존의 인스턴스를 반환한다.
	* 생성된 인스턴스가 없을 경우에는 생성자를 호출하여 인스턴스를 새로 생성후 반환한다.

1. 프래그먼트가 자신의 호스팅 액티비티에 속한 인텐트에 직접 액세스하는 방법이 안 좋은 이유와 더 좋은 방법을 쓰시오
> 프래그먼트가 호스팅 액티비티에 의존성이 생겨 재사용하기가 힘들다.

2. 프래그먼트의 생명 주기를 설명하시오
onAttach -> onCreate -> onCreateView -> onStart -> onResume -> onPause -> onStop -> onDestroy -> onDetach

3. Activity Context , Application Context의 차이를 설명하시오
