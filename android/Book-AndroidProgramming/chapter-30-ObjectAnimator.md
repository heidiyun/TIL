# 30. 속성 애니메이션
## 간단한 속성 애니메이션
```kotlin
private fun startAnimation(view: View) {
        val sunYStart = view.sun./top/.toFloat()
        val sunYEnd = view.sky./bottom/.toFloat()

        val heightAnimator = ObjectAnimator
                .ofFloat(view.sun, "y", sunYStart, sunYEnd)
                .setDuration(3000)
        heightAnimator.start()
}
```

해의 꼭대기부터 하늘의 밑바닥까지 가라앉는 애니메이션 효과를 줄것이다.

ObjectAnimator는 ValueAnimator의 서브 클래스로, 타켓 객체의 속성에 애니메이션을 적용할 수 있도록 지원합니다. 
아래와 같이 전달한 속성 값을 다르게 전달하여 setter 메소드를 반복적으로 호출합니다.
```kotlin
view.sun.y = 0
view.sun.y = 0.02
view.sun.y = 0.04
...
...
view.sun.y = 1

// 시작과 끝 지점 사이의 값을 찾는 일 -> interpolation
```

ofFloat 메소드는 Float 값으로 애니메이션을 적용합니다.
setDuration 메소드는 애니메이션의 길이를 지정합니다. 즉, 빠르기를 설정합니다.

### 뷰 변형 속성

ObjectAnimator 만으로는 애니메이션 효과를 다양하게 줄 수 없다.
따라서 transformation propert(변형 속성)와 함계 동작한다.

뷰는 자신의 부모 뷰를 기준으로 지정된 위치와 크기를 갖는다.
여기에 추가적인 속성을 설정한 후 뷰를 움직일 수 있는데 이것을 변형 속성이라고 한다.
1. 회전 속성 - rotation, pivotX, pivotY
2. 크기 속성 - scaleX, scaleY
3. 이동 속성 - translationX, translationY

### 인터폴레이터(interpolator) 사용해기
태양의 움직이는 속도에 가속도가 붙으면 더 생동감 있을 것.
-> AccelerateInterpolator 
-> A 지점에서 B 지점으로 애니메이션이 진행되는 방법을 변경할 수 있게 해준다.

```kotlin
/* SunsetFragment- startAnimation */
heightAnimator./interpolator/= AccelerateInterpolator()
// 추가
```

### 색상 값 산출하기
하늘 색에 변화주기

```kotlin
/* SunsetFragment- startAnimation */
val sunsetSkyAnimator = ObjectAnimator
        .ofInt(view.sky, "backgroundColor",
                ContextCompat.getColor(view./context/, R.color./blueSky/),
                ContextCompat.getColor(view./context/, R.color./sunsetSky/))

sunsetSkyAnimator.start()

// 추가
```

위와 같은 코드를 추가하면 파란색과 주황색 사이의 색상 값이 서서히 바뀌지 않고 빠르게 깜빡거리며 변한다.
이유는 색을 나타내는 정수 값이 단순한 숫자가 아니기 때문이다.
ObjectAnimator에서 시작과 끝 사이의 값을 찾는 방법이 불충분하다고 생각될 때는 TypeEvaluator의 서브 클래스를 사용하여 해결할 수 있다. TypeEvaluator는 ObjectAnimator에 시작과 끝 사이의 값을 알려주는 객체다.
이때 전체 값의 1/4씩을 산출해서 전달한다.
안드로이드는 TypeEvaluator의 서브 클래스인 ArgbEvaluator를 제공한다.

```kotlin
sunsetSkyAnimator.setEvaluator(ArgbEvaluator())
// 추가
```

하늘의 색이 네 번에 걸쳐 변경된다.

## 여러 애니메이터를 함께 사용하기
AnimatorListener를 사용하면 앞서 시작된 애니메이션이 완료되었음을 통보 받을 수 있다.
첫 번째 일몰 애니메이션이 끝날 때까지 기다리는 리스너를 작성하여 일몰이 끝나면 밤하늘 애니메이션이 시작되도록 하면 된다.
이 작업을 완료하f려면 여러 리스너가 필요한데, AnimatorSet을 사용하면 훨씬 쉽게 처리할 수 있다.

```kotlin
private fun startAnimation(view: View) {

        val sunYStart = view.sun./top/.toFloat()
        val sunYEnd = view.sky./bottom/.toFloat()

        val heightAnimator = ObjectAnimator
                .ofFloat(view.sun, "y", sunYStart, sunYEnd)
                .setDuration(3000)
        heightAnimator./interpolator/= AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
                .ofInt(view.sky, "backgroundColor",
                        ContextCompat.getColor(view./context/, R.color./blueSky/),
                        ContextCompat.getColor(view./context/, R.color./sunsetSky/))
                .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
                .ofInt(view.sky, "backgroundColor",
                        ContextCompat.getColor(view./context/, R.color./sunsetSky/),
                        ContextCompat.getColor(view./context/, R.color./nightSky/)
                )
                .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

//        heightAnimator.start()
//        sunsetSkyAnimator.start()

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator)
        animatorSet.start()

    }
```

play를 호출하면 AnimatorSet.Builder 인스턴스가 반환되므로 연속적으로 메소드를 호출할 수 있다.
play의 인자로 전달되는 Animator는 연속 호출되는 메소드의 주체가 된다.
heightAnimator를 sunsetSkyAnimator와 함께 동작시키고, heightAnimator를 nightSkyAnimator 시작 전에 동작시켜라. 

## 다른 애니메이션 API
android.view.animation 패키지에는 기존의 애니메이션 도구가 있고,
android.animation 패키지에는 허니콤에서 소개된 새로운 애니메이션 도구가 있다.

### 전환
안드로이드 4.4에서는 새로운 전환 프레임워크가 소개되었다.
이것을 사용하면 뷰 계층 구조간의 전환 애니메이션을 사용할 수 있다.
장면 - layout xml 파일
전환 - animation xml 파일
전환 프레임워크는 장면과 장면 간의 전환을 정의할 수 있다.


#android/책