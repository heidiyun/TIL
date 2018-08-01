# Nested TabLayout

Fragment안에서 Adapter에 childFragmentManager을 넘긴다.
외부에서 childFragmentManager을 사용할 수 없다. 

```java
	/* Adapter */
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

	/* 바깥쪽 TabLayout의 Fragment */
	class Created : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.issue_tab_created, container, false)

        setupSubViewPager(view.pager_issue_created)
        view.tab_issue_created.setupWithViewPager(view.pager_issue_created)

        return view
    }

    private fun setupSubViewPager(viewPager: ViewPager) {
        val adapter = SectionsPageAdapter(childFragmentManager)
        adapter.addFragment(CreatedOpen(), "Open")
        adapter.addFragment(CreatedClosed(), "Closed")
        viewPager.adapter = adapter
    }

	}

```

안쪽 Fragment에서 보여줄 내용을 처리할 코드를 작성하면 된다. 