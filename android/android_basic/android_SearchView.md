# Android SearchView 사용하기.

1. res/menu/activity_search.xml 추가하기.
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
        (menu?.findItem(R.id.menuItemSearch)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
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


