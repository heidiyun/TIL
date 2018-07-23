# RecyclerView
> ListView와 다르게 View Holder를 강제한다.

1. build.gradle의 dependencies에 추가하기.
```
    implementation 'com.android.support:recyclerview-v7:27.1.1'
```

2. ListView를 썼던 곳을 RecyclerView로 바꿔주기
```xml
	 <android.support.v7.widget.RecyclerView
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
        app:layout_constraintTop_toTopOf="parent"
        tools:listitem="@layout/item_repo"
        // 현재 코드에서 repository를 보여주기위해 cardView를 사용하였는데, preview에서 카드뷰의 형태를 그대로 보기위해 쓰는 코드.
         />
```

3. Activity.kt에 ViewHolder 만들기.

```java
	 class RepoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
    )

```

4. RecyclerView Adapter만들기.
```java
	class SearchListAdapter: RecyclerView.Adapter<RepoViewHolder>() {
        var items: List<GithubRepo> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
            return RepoViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return items.count()
        }

        override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
            val item = items[position]
         //   holder.itemView.repoNameText.text = item.fullName
         //   holder.itemView.repoOwnerText.text = item.owner.login

         //위의 코드와 아래 코드는 동일하다.
         // with은 수신객체지정하여 쓸 수 있게 해준다. 
         // 여기서 수신객체는 holder.itemView
            with(holder.itemView) {
                repoNameText.text = item.fullName
                repoOwnerText.text = item.owner.login

                Glide.with(this).load(item.owner.avatarUrl).into(ownerAvatarImage)
                // owner의 Image받아오기.
            }
        }
    }
```

5. onCreate에서 LayoutManager 설정해주기.

```java
     searchListView.layoutManager = LinearLayoutManager(this)
     //RecyclerView를 사용할때는 LayoutManager를 설정해주어야 한다. (반드시!!)
     // 아이템 배치 방법.
```

* LinearLayoutManager : 가로/ 세로로 아이템을 배치
 	* GridLayoutManager : 한 줄에 한개 이상의 아이템을 배치 할 수 있으나, 크기는 첫번째 아이템의 크기와 동일하게 고정된다.
 	* StaggeredGridLayoutManager : 아이템 크기를 다양하게 적용할 수 있다.
 	* CustomLayoutManager : 3개의 레이아웃 매니저를 상속받아 커스텀 할 수 있다.
