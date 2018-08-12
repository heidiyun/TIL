# Rx
Rx(Reactive Extension)
-> Functional Programming
-> Declarative Programming 
	(로직을 통해서 작성하는 것이 아니라, 데이터에 쿼리를 전해주는것)
-> Data Binding

RxJava/Kotlin
: Callback 방식과는 달리 발생하는 이벤트를 이벤트 스트림에 전달하고,
이벤트 스트림을 관찰하다가 원하는 이벤트가 감지되면 이에 따른 동작을 수행하는 패턴을 사용한다.

* 장점 : 비동기 이벤트를 컬렉션을 처리하는 개념으로 일반화해서 처리할 수 있다.

	* 비동기
	: 결과의 완료시점을 알 수 없다.
	-> Callback을 통해 처리한다.
	-> Callback이 무수히 많아지면 흐름을 제어하기 힘든 Callback Hell이 만들어진다.
	-> 코드의 가독성이 현저하게 떨어지게 된다.

* 요소
	1. Observable
		: 이벤트를 만들어내는 주체로, 이벤트 스트림을 통해 만든 이벤트를 내보낸다.
		  여러개의 이벤트를 만들 수 있고, 만들지 않을 수도 있다.

	2. Observer
		: Observable 에서 만들어진 이벤트에 반응하며, 이벤트를 받았을 때 수행할 작업을 정의한다.
		**Observer가 Observable을 구독(subscribe)한다고 한다.**
	
	3. Operator
		: 연산자는 이벤트 스트림을 통해 저달되는 이벤트를 변환한다.
		  이벤트가 가지고 있는 값을 다른 형태로 변환하는 것도 가능(map, filter)
		  특정 조건을 만족하는 이벤트 스트림을 흘려보내거나, 개수를 변경하는 작업 가능(filter, first)

	4. Scheduler
		: 해당 작업을 수행할 스레드를 지정한다.
		UI - main thread
		IO/ Worker /New Thread
		observerOn 메소드를 사용해서 지정하며, 이 메소드를 호출한 직후에 오는 연산자나 옵저버에서 수행되는 작업의 스레드가 변경된다.

	5. Disposable
		: Observer가 Observable을 구독할 때 생성되는 객체.
		  Observable에서 만드는 이벤트 스트림과 이에 필요한 리소스를 관리한다.
		  CompositeDisposable을 사용하면 여러개의 Disposable 객체를 하나의 객체에서 관리할 수 있다.

