# 25. 검색
## SearchView 사용하기.
res/menu 디렉토리에 SearchView를 정의할 xml 파일을 만든다.
```xml
<item
        android:id="@+id/menu_item_search"
        android:title="@string/search"
		app:actionViewClass="androidx.appcompat.widget.SearchView"
        app:showAsAction="ifRoom"/>

```
androidx로 바뀌면서 기존 지원라이브러리의 SearchView를  
“androidx.appcompat.widget.SearchView”로 대체한다.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
}

override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu./fragment_photo_gallery/, menu)
}
```
onCreateOptionsMenu(…)를 사용하여 만들어놓은 menu xml 파일을 인플레이트하고, setHasOptionsMenu(…)를 호출하여 프래그먼트가 안드로이드 운영체제로부터 메뉴의 콜백 메소드를 호출 받을 수 있도록 한다. 

```kotlin
override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu./fragment_photo_gallery/, menu)

    val searchItem = menu?.findItem(R.id./menu_item_search/)
    val searchView = searchItem?./actionView/as SearchView

    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean { 
// 서치뷰의 텍스트를 입력한 후 검색을 요청할 때 호출되는 메소드
            updateItems()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
// 서치뷰의 텍스트가 바뀔 때마다 호출되는 메소드.
            return false
        }
    })
}
```

## 공유 프레퍼런스를 사용한 간단한 데이터 보존
앱이 다시 시작되거나 사용자가 장치의 전원을 껐다 켜도 보존해야 할 데이터가 있을 때 SharedPreferences 클래스를 사용한다.

 SharedPreferences의 인스턴스는 키와 값의 한쌍으로 데이터를 저장한다.
키 : 문자열 값 : 여러 데이터 타입을 제공.

```kotlin
class QueryPreferences {
    companion object {
        const val PREF_SEARCH_QUERY = "searchQuery"
        
        fun getStoredQuery(context: Context): String? =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(PREF_SEARCH_QUERY, null)
// 저장된 데이터를 읽어올 때.
        
        fun setStoredQuery(context: Context, query: String) = 
                PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_QUERY, query)
                    .apply()
    }
// 데이터를 쓸 때.
}

```


#android/책