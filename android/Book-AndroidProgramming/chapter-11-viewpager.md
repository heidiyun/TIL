# 11. ViewPager 사용하기.
UI에 ViewPager를 추가하면 사용자가 화면을 밀어서 페이지 이동을 할 수 있다.

## ViewPager와 PagerAdapter
RecyclerView가 Adapter를 필요로 하듯이,
ViewPager 또한 PagerAdapter를 필요로 한다.

## FragmentStatePagerAdapter vs FragmentPagerAdapter
> 프래그먼트가 더 이상 필요없어질 때 프래그먼트를 삭제하는 방법에 차이가 있다.  

### FragmentStatePagerAdapter
필요 없어진 프래그먼트는 소멸된다.
호스팅 액티비티의 FragmentManager로부터 프래그먼트를 완전히 삭제하기 위해 트랜잭션이 커밋된다. 
`fragmentManager.beginTransaction().remove(/*fragment*/).commit;`
프래그먼트의 Bundle 객체를 onSaveInstanceState(Bundle) 메소드로 보존한다. 
소멸된 프래그먼트를 다시 생성하면 보존했던 인스턴스 상태를 사용해서 새로운 프래그먼트가 복원된다.

#### 사용.
메모리가 FragmentPagerAdapter보다 절약된다.
프래그먼트의 수가 많고 유동적이며 데이터의 크기가 클때 권장한다.
각 프래그먼트 데이터를 메모리에 보존하는 것을 원치않을 때 권장한다.

### FragmentPagerAdapter
필요 없어진 프래그먼트는 소멸되지 않는다.
호스팅 액티비티의 FragmentManager로 해당 프래그먼트의 뷰를 삭제하기 위해 트랜잭션이 커밋된다.
`fragmentManager.beginTransaction().detach(/*fragment*/).commit`
프래그먼트 인스턴스는 FragmentManager에 남아있는다.

#### 사용.
프래그먼트를 메모리에 유지하면 되므로 컨트롤러 코드를 더 쉽게 관리할 수 있다. 
프래그먼트의 수가 적고 고정적이며 데이터의 크기가 작을 때 권장한다.
( 이 경우 하나의 액티비티에 2~3개의 프래그먼트만 필요로 하므로 메모리 부족이 생길 위험이 거의 없다.)

## ViewPager
옆으로 밀어주는 제스처를 지원해준다.
ex) TabLayout을 ViewPager없이 Fragment로만 구현을 하면 제스처를 처리해주는 로직을 직접 구현해주어야 한다.
PagerAdapter의 인터페이스를 직접 구현해야 할 때.
-> ViewPager가 프래그먼트가 아닌 일반적인 View객체를 호스팅할 때.

RecyclerView의 어댑터는 뷰를 생성하지 못하고 그것을 결정하는 것은 FragmentManager이다.
PagerAdapter를 사용하면 여기서 직접 뷰를 생성해준다.

## 코드에서 ViewPager 생성하기
**권장하지 않습니다.**
레이아웃 리소스 파일을 사용하는 것은 컨트롤러 객체와 뷰 객체를 구분해준다.
또한, 장치의 특성을 기준으로 적합한 버전의 XML 파일을 자동으로 선택해준다.
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ViewPager viewPager = new ViewPager(this);
	setContentView(viewPager);
}

```



#android/책