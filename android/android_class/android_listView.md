# Android ListView 적용하기.
> GithubApp 특정 단어를 포함한 repository 불러오기.

1. ListView layout file을 만든다.
 res/layout/activity_search.xml
```xml
	<ListView
        android:id="@+id/searchListView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginBottom="8dp"
        android:layout_marginEnd="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

2. ListView안의 item의 layout file을 만든다.
res/layout/item_repo.xml
```xml
	 <TextView
        android:id="@+id/repoNameText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:text="im/java-lecture"
        app:layout_constraintStart_toEndOf="@+id/imageView2"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/repoOwnerText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:text="skei"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toEndOf="@+id/imageView2"
        app:layout_constraintTop_toBottomOf="@+id/repoNameText" />

    <ImageView
        android:id="@+id/imageView2"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_marginBottom="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:src="@drawable/ic_github"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

```

3. Activity.kt파일을 생성한다. 
> Github에서 data를 받아, 
activity_search.xml, item_repo를 inflate 할 것이다.

```java
	 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val githubApi = provideGithubApi(this)
        val call = githubApi.searchRepository("TIL")
        call.enqueue({
            response ->
          val statusCode = response.code()
            if (statusCode == 200) {
                val result = response.body()
                result?. let {
                	// 받아온 정보를 listView의 Adapter의 item에 저장.
                }
            } else{
                toast("error - $statusCode")
            }
        }, {
                t ->
            toast(t.localizedMessage)
        })
    }
```

4. 받아오는 정보를 ListView의 형태로 넣어주기 위해 Adapter를 만들어준다.
> 여기 코드에서는 Github repository의 정보를 List<GihubRepo>의 형태로 받아온다. 


```java
	 class SearchListAdapter(val context: Context) : BaseAdapter() {
        var items: List<GithubRepo> = emptyList()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        	// parameter는 변경불가능한 상태이다.

            val item = items[position]

                val view = LayoutInflater.from(context).inflate(R.layout.item_repo, null)
                view.repoNameText.text = item.fullName
                view.repoOwnerText.text = item.owner.login
                return view
           
        }

        override fun getItem(position: Int): Any {
            return items[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
            // listView에서 ROW 순서
            // list에서의 index와 비슷한 역할.
        }

        override fun getCount(): Int {
            return items.count()
        }

    }

```
* LayoutInflater : xml로 되어 있는 layout을 kotlin의 View로 변환한다.

5. ListView에 Adapter를 연결해준다.

```java
	lateinit var listAdapter: SearchListAdapter
	// 늦은 초기화 실제로 Adapter에 접근할 때 생성한다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        listAdapter = SearchListAdapter(this)
        searchListView.adapter = listAdapter
        // xml로 생성해둔 ListView에 Adapter를 연결한다.

        val githubApi = provideGithubApi(this)
        val call = githubApi.searchRepository("TIL")
        call.enqueue({
            response ->
          val statusCode = response.code()
            if (statusCode == 200) {
                val result = response.body()
                result?. let {
                    listAdapter.items = it.items
                    listAdapter.notifyDataSetChanged()
                    // 핵심!!!! : 변경사항을 listView에 알려주어야 한다.
                    // 알려주지않으면 바뀐 정보가 반영되지 않는다.
                }
            } else{
                toast("error - $statusCode")
            }
        }, {
                t ->
            toast(t.localizedMessage)
        })
    }
```