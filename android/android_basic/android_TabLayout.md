
# Tablayout
> 안드로이드에서 tablayout 사용하기.

1. build.gradle에 dependency추가하기
	```
	implementation 'com.android.support:design:27.1.1'

	```

2. res/layout/activity_profile_tab.xml 생성하기.

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:orientation="vertical"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context=".ui.ProfileActivity">
	    
	    <android.support.design.widget.TabLayout
	        android:id="@+id/tabLayout"
	        android:layout_marginTop="181dp"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:background="?attr/colorPrimary"
	        android:minHeight="?attr/actionBarSize"
	        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar" />

	    <android.support.v4.view.ViewPager
	        android:id="@+id/pager"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent" />

	</LinearLayout>
	```
	* LinearLayout을 사용하여 수직 형태로 뷰가 나열되도록 설정.
	TabLayout을 사용하면 탭에 대한 애니메이션, 디자인 등을 쉽게 설정할 수 있다.
	ViewPager는 탭에서 보여주는 화면이다. 
	

3. 각 탭에서 보여줄 layout파일 만들기. (res/layout/profile_tab_???.xml)
> 이 코드에서는 총 5개의 tab을 만든다. (overview, repo, starred, following, follower)

* following tab 예시.
	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<android.support.constraint.ConstraintLayout 	
		xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools">

	    <android.support.v7.widget.RecyclerView
	        android:id="@+id/followingView"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:layout_marginBottom="8dp"
	        android:layout_marginEnd="8dp"
	        android:layout_marginStart="8dp"
	        app:layout_constraintBottom_toBottomOf="parent"
	        app:layout_constraintEnd_toEndOf="parent"
	        app:layout_constraintStart_toStartOf="parent"
	        app:layout_constraintTop_toTopOf="parent"
	        tools:listitem="@layout/item_following" />

	</android.support.constraint.ConstraintLayout>
	```


4. 각 탭과 연결되는 Fragment를 생성한다.

	```java
		class FollowingTab: Fragment() {
			    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

	        val view = inflater.inflate(R.layout.profile_tab_follow, container, false)

	        // 각 fragment에서 처리할 코드 작성.

	        return view
		}
	```
	

5. ViewPager Adater를 생성한다.

	```java
		/* 자바로 작성한 이전의 방식 */

	import android.support.v4.app.Fragment;
	import android.support.v4.app.FragmentManager;
	import android.support.v4.app.FragmentStatePagerAdapter;

	public class TabPagerAdapter extends FragmentStatePagerAdapter {

	    private int tabCount;

	    public TabPagerAdapter(FragmentManager fm, int tabCount) {
	        super(fm);
	        this.tabCount = tabCount;
	    }

	    @Override
	    public Fragment getItem(int position) {
	        switch (position) {
	            case 0:
	                OverviewTab overviewTab = new OverviewTab();
	                return overviewTab;
	            case 1:
	                RepositoryTab repositoryTab = new RepositoryTab();
	                return repositoryTab;
	            case 2:
	                StarTab starTab = new StarTab();
	                return starTab;
	            case 3:
	                FollowingTab followingTab = new FollowingTab();
	                return followingTab;
	            case 4:
	                FollowerTab followerTab = new FollowerTab();
	                return followerTab;
	            default:
	                return null;
	        }
	    }

	    @Override
	    public int getCount() {
	        return tabCount;
	    }
	}
	```
	탭이 변할때마다 position을 받아 Fragment를 전환해주는 역할을 한다.

	* 단점
		* FragmentManager에 등록된 Fragment를 최대한 영구적으로 가지고 있도록 구현되어 있어, view가 보이지 않는 상황에도 Fragment가 메모리를 차지하고 있다.
		=> fragment의 개수가 많아지게 되면 성능에 악영향을 끼칠 수도 있다.

	```java

	import android.support.v4.app.Fragment
	import android.support.v4.app.FragmentManager
	import android.support.v4.app.FragmentPagerAdapter

	class SectionsPageAdapter(fn: FragmentManager) : FragmentPagerAdapter(fn) {
	    val fragmentList : ArrayList<Fragment> = arrayListOf()
	    val fragmentTitleList : ArrayList<String> = arrayListOf()

	    fun addFragment(fragment: Fragment, title: String) {
	        fragmentList.add(fragment)
	        fragmentTitleList.add(title)
	    }

	    override fun getPageTitle(position: Int): CharSequence? {
	        return fragmentTitleList.get(position)
	    }

	    override fun getItem(position: Int): Fragment {
	        return fragmentList.get(position)
	    }

	    override fun getCount(): Int {
	        return fragmentList.size
	    }

	}
	```
	ListView와 유사하게 동작한다. view가 보이지 않으면 saved state 만을 보관하고 파괴한다. 

	* 단점 : Fragment들을 동적으로 생성/파괴하므로 탭 전환시 overhead가 발생할 수 있다.


5. Adapter 연결하기.

	```java
	        tabLayout.addTab(tabLayout.newTab().setText("Overview"))
	        tabLayout.addTab(tabLayout.newTab().setText("Repository"))
	        tabLayout.addTab(tabLayout.newTab().setText("Stars"))
	        tabLayout.addTab(tabLayout.newTab().setText("Following"))
	        tabLayout.addTab(tabLayout.newTab().setText("Followers"))
	        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL)
	        // TabLayout에 탭 제목 설정하고 추가하기.

	        val pagerAdapter = TabPagerAdapter(supportFragmentManager, tabLayout.tabCount)


	        pager.adapter = pagerAdapter
	        // 위에 생성한 pagerAdatper와 ViewPager연결하기.

	        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
	        // 탭의 선택 상태가 변경될 떄 호출되는 리스너.

	        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
	            override fun onTabReselected(tab: TabLayout.Tab) {
	                pager.setCurrentItem(tab.position)
	            }

	            override fun onTabUnselected(tab: TabLayout.Tab?) {
	            }

	            override fun onTabSelected(tab: TabLayout.Tab?) {
	            }

	        })
	        // ViewPager에서 페이지의 상태가 변경될 떄 페이지 변경 이벤트를 
	        // TabLayout에 전달하여 탭의 선택 상태를 동기화 해주는 역할.

	```
	위의 코드를 아래 코드로 대체하여 간결하게 작성할 수 있다.

	```java
		private fun setUpViewPager(viewPager: ViewPager) {
			val adapter = SectionPageAdapter(supportFragmentManager)
			adapter.addFragment(OverviewTab(), "Overview")
	        adapter.addFragment(RepositoryTab(), "Repository")
	        adapter.addFragment(StarTab(), "Star")
	        adapter.addFragment(FollowerTab(), "Follower")
	        adapter.addFragment(FollowingTab(), "Following")
	        viewPager.adapter = adapter
		}

		 setupViewPager(pager) 
		 //ViewPager에 Adapter연결하기.

	     tabLayout.setupWithViewPager(pager)
	     // tabLayout에 viewPager 연결하기.

	```