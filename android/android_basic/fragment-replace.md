# Fragment Replace
Add a new listener for changes to the fragment back stack.

Activity에서 supportFragmentManager로 Fragment를 추가하는 경우에는 onPause(), onResume() 메소드가 잘 호출된다.
그러나 ViewPager와 함께 동작하는 경우에는 ViewPager와 바로 붙어있는 Fragment의 생명주기 메소드가 적절히 호출되지 않는 문제가 발생한다.

ViewPager는 좌우 페이지를 미리 그려놓고 준비한다는 특징이 있습니다.
그래서 ViewPager의 fragment를 갈아치워도 pause가 불리지 않는 것입니다.
생명주기 메소드가 적절히 호출되지 않습니다.
setUserVisibleHint() 함수를 사용할 시, fragment를 전환하거나 새롭게 추가할 때는 잘 작동하는 것 처럼보이나 backstack으로 다시 돌아올때는 함수가 실행되지 않습니다.

#android/basic