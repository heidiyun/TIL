# SwipeRefreshLayout
> SwipeRefreshLayout을 사용하면 pull to refresh (당겨서 새로고침)을 쉽게 구현할 수 있다.  

## 의존성
android.support.v4.widget.SwipeRefreshLayout
androidx.swiperefreshlayout.widget.SwipeRefreshLayout

## 사용
새로고침 하고 싶은 부분을 SwipeRefreshLayout으로 감싸줍니다.
```xml
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    android:id="@+id/swipeLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:layout_constraintTop_toBottomOf="@id/toolbar">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/mainRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

```

본인은 새로고침 시, RecyclerView의 내용을 다시 받아오고 싶었다.

새로고침 시에 처리해주고싶은 내용은, SwipeRefreshLayout에 OnRefreshListener를 달아줘서 처리해준다.
코틀린의 람다 문법을 사용하였다.

```kotlin
view.swipeLayout.setOnRefreshListener *{*
	// 새로고침 시에 처리할 코드.
view.swipeLayout./isRefreshing/= false
*}*

```

새로고침 코드가 끝나고,  isRefreshing을 false로 지정해주지 않으면 로딩 아이콘이 무한정 돌아가므로 꼭! 코드를 넣어주자!

#android/basic