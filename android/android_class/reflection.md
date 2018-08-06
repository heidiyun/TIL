# Reflection
> 자신의 클래스를 통하여 필요한 정보를 얻어내는 기법.

1. class 정보를 얻는 방법.
```java
	class Sample {

	}

	// 1. class
	Class clazz = Sample.class

	// 2. instance
	Sample sampleObject = new Sample()
	Class clazz2 = sampleObject.getClass()

	// 3. String
    Class clazz3 = "com.example.user.chatfirebase.controller.Sample".getClass()
```

2. 기능
1) 다양한 정보를 얻을 수 있다.
Constructor, property, method...

2) 클래스의 인스턴스를 동적으로 생성할 수 있다.

```java
	//안드로이드의 startActivity를 보자.
	Intent intent = new Intent(this, MainActivity.class)
	startActivity(intent)

	//실행하고자 하는 Activity의 class정보를 얻어온 뒤, onCreate()함수를 부름으로써
	// Activity가 화면에 보여진다. 
```