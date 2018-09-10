# Android SearchView 사용하기.
## searchable한  activity 사용하기.
1. res_menu_activity_search.xml 추가하기.
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/menuItemSearch"
        android:title="@string/search_hint"
        app:actionViewClass="android.support.v7.widget.SearchView"
        app:showAsAction="ifRoom" />
</menu>
```

2. SearchView를 사용할 MainActivity에 추가하기.
```java
	 override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fragment_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
// 위 코드까지만 작성해도 searchView는 작동한다.
// apply안의 코드는 seachView로 사용할 수 있는 속성 또는 메소드를 정의할 때 쓰인다.
        (menu?.findItem(R.id.menuItemSearch)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName)) 
// 검색을 완료했을 때, searchable속성을 가진 activity를 띄우고 싶다면 위와 같은 코드를 작성해야 한다.
        }
        return true
    }
```
SearchView에서 동작하는 메소드는 apply안에서 동작한다.

3. java/ SearchActivity.kt 추가하기.
```java
	
class SearchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)

            val githubUser = provideUserApi(this)
            val call = githubUser.getUsers(query)
            call.enqueue({ response ->
                val result = response.body()
                result?.let {
                	println("userInfo: {it.items")
                }
            }, {
                println("error!!!!! ${it.localizedMessage}")
            })
        }
    }

}
```

4. AndroidManifest.xml에 추가하기.
```xml
		<activity
            android:name=".MainActivity"
            android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <meta-data
                android:name="android.app.default_searchable"
                android:value=".ui.SearchActivity" />
        </activity>

        <activity android:name=".ui.SearchActivity">
            <intent-filter>
                <action android:name="android.intent.action.SEARCH" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
            <meta-data
                android:name="android.app.searchable"
                android:resource="@xml/searchable" />
        </activity>
```


## Fragment를 이용한 실시간 검색기능.
> searchView를 클릭했을 때, 보여지고있는 activity위를 fragment로 전환하고 검색의 결과를 실시간으로  recyclerView를 사용하여 fragment위에 띄울 것이다.  

1. Fragment layout 파일, kotlin파일 생성하기.
res/fragment_searchview.xml
fragment/searchView

2. SearchView의 여러 기능을 override 한다.
```java
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        /menuInflater/.inflate(R.menu./fragment_search/, menu)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id./menuItemSearch/)?./actionView/as SearchView)./apply/*{*

setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
// queryText가 제출되었을 때, 행동을 정의한다.

                override fun onQueryTextChange(newText: String?): Boolean {
                    /println/("newText $newText")
                    fragment.setApi(newText, 1)

                    return true
                }
            })
// queryText가 바뀔 때마다 반응하는 메소드로, 검색 단어가 바뀔 때바다 api로 단어를 보내는 역할을 수행하도록 하였다. 

            /queryHint/= "Search User" 

            setOnSearchClickListener *{*
/supportFragmentManager/.beginTransaction()
                        .add(R.id./contents/, fragment)
                        .commit()
            *}*
// searchView를 클릭했을 경우, fragment를 추가하도록 정의했다.

searchView = this
setOnCloseListener{
supportFragmentManager/.beginTransaction().remove(fragment).commit()
false
}
// searchView가 닫힐 때, frgament 파괴.
*}*
        return true
    }

```

3. Fragment에서 할 일 정의하기.
```java
 val listAdapter = ListAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout./fragment_example/, container, false)
        val layoutManager = LinearLayoutManager(requireContext()./applicationContext/)

        view.searchView./adapter/= listAdapter
        view.searchView./layoutManager/= layoutManager

        view.searchView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = view.searchView./layoutManager/./childCount/
val totalItemCount = view.searchView./layoutManager/./itemCount/
val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + pastVisibleItems >=
                        totalItemCount) {
                    setApi(query, ++page)
						// 스크롤이 recyclerView의 최하단에 위치하게 되면 다음 페이지의 이벤트를 받는다.  
                }
            }
        })
        return view
    }


// setApi onQueryTextChange에서 계속해서 불러주는 함수.
    fun setApi(query: String?, page: Int) {
        val searchApi = /provideUserApi/(/activity/!!./applicationContext/)
        if (page > pageLimit) return
        val call = searchApi.getUsers(query, page)
        this.query = query
        this.page = page
        call./enqueue/(*{*response *->*
/println/("query $page")
            /println/("response 들어옴.")
            val result = response.body()
            result?./let/*{*
pageLimit = (*it*.totalCount / 30) + 1
                if (page == 1)
                    listAdapter.items.clear()
                listAdapter.items.addAll(*it*.items)
// 다음 페이지의 결과들을 추가한다.
                listAdapter.notifyDataSetChanged()
            *}*
*        }*, *{*

*        }*)

    }



```

4. backpressed눌렀을 때, searchView 닫힘과 함께 fragment 파괴하기.
```java
 override fun onBackPressed() {

            fragment./isVisible/-> {
                /println/("fragment visible")

                searchView./isIconified/= true
//	펼쳐진 searchView를 아이콘화 시키기.
                searchView.clearFocus()
// 입력된 queryText 날리기.

            }
            else -> {
                /println/("super")
                super.onBackPressed()
            }
        }

    }

```

#android
