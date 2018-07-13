# Android Recycler View
> Android 5.0부터 제공되는 Recycler View 
ListView의 장/단점을 보완한다.

## ListView
> 같은 형태의 뷰들이 나열되어 있는 리스트 구조.
> adapter를 통해 데이터를 뿌려주는 형식.

* 구조
1. data : 리스트 하나의 뷰들을 구성함.
2. adapter : 데이터를 리스트에 넣어줌.

1. xml 코드 
```xml
<LinearLayout
 xmlns:android=”http://schemas.android.com/apk/res/android"
 android:layout_width=”match_parent”
 android:layout_height=”match_parent”>
<ListView
 android:id=”@+id/listview”
 android:layout_height=”match_parent”
 android:layout_width=”match_parent”
 android:divider=”#ffcc22"
 android:dividerHeight=”1.5dp”
 android:choiceMode=”singleChoice”
 />
</LinearLayout>

<LinearLayout
 xmlns:android=”http://schemas.android.com/apk/res/android"
 android:orientation=”horizontal”
 android:layout_width=”match_parent”
 android:layout_height=”match_parent”>
 <ImageView
 android:id=”@+id/imageview”
 android:layout_width=”60dp”
 android:layout_height=”60dp”/>
 <TextView
 android:id=”@+id/textview”
 android:layout_width=”wrap_content”
 android:layout_height=”match_parent”
 android:gravity=”center”/>
</LinearLayout>
```

2. code

데이터를 처리하는 클래스
```java
	class ListViewitem(val icon: Int, val name: String) 
```
MainActivity.kt
```java
	val listView: ListView = findViewById(R.id.listview)

	val data: ArrayList<ListViewitem> = ArrayList<ListViewitem>()
	val lion = ListViewitem(R.drawable.lion, "Lion")
	val tiger = ListViewitem(R.drawable.tiger, "Tiger")
	val dog = ListViewitem(R.drawable.dog, "Dog")
	val cat = ListViewitem(R.drawable.cat, "cat")

	data.add(lion)
	data.add(tiger)
	data.add(dog)
	data.add(cat)

	val adapter = ListviewAdapter(this, R.layout.listviewitem, data)
	listView.setAdapter(adapter)
```

Adapter만들기
```java
	class ListviewAdapter(val context: Context, 
		val layout: Int,
		 val data: ArrayList<Listviewitem>) : BaseAdapter() {
		init {
			val inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
		}	

		override fun getCount(): Int {
		 	return data.size()
		 }
		override fun getItem(position: Int): String { 
			return data.get(position).getName()
		}
		override fun getItemId(position: Int): Long {
			return position
		}
		override fun getView(position: Int, convertView: View, parent: ViewGroup) {
			if (convertView==null) = convertView = inflater.inflate(layout, parent, flase)

			val listviewitem = data.get(position)

			val icon: ImageView = convertView.findViewById(R.id.imageview)
			icon.setImageResource(listviewitem.getIcon())

			val name: TextView = convertView.findViewById(R.id.textview)
			name.setText(listviewitem.getName())

			return convertView

		}

	}
```

## recyclerView
* Android Lollipop과 함께나옴.
  SupportLibrary에 포함되어 Android Version7 이상에서 사용가능

listView 의 getView메소드는 UI Thread에서 수행되므로 특별한 작업 수행시 비동기 처리가 필요하다.

장점 : 간단하게 리스트를 만들 수 있다.
단점 : 아이템에 애니메이션 효과를 적용하기가 힘들다.
		리스트에 한 개 이상의 View가 필요한 경우가 있지만 커스텀하기 쉽지 않다.
	   ViewHolder 패턴을 강제적으로 사용하지 않아 findViewById가 매번 호출될 수 있어 성능에 악영향을 끼칠 수 있다.

**특징**
1. RecyclerView는 LayoutManager를 통해서 View에 그리는 방법을 정의한다.

* RecylerView Adapter 
> Data의 ViewHolder의 정의에 따라서 UI가 선택된다.
 1. ViewHolder의 적용으로 View의 재사용을 가능하게 한다.
 2. LayoutManager의 추가로 아이템의 배치 방법을 정의할 수 있다.
 	* LinearLayoutManager : 가로/ 세로로 아이템을 배치
 	* GridLayoutManager : 한 줄에 한개 이상의 아이템을 배치 할 수 있으나, 크기는 첫번째 아이템의 크기와 동일하게 고정된다.
 	* StaggeredGridLayoutManager : 아이템 크기를 다양하게 적용할 수 있다.
 	* CustomLayoutManager : 3개의 레이아웃 매니저를 상속받아 커스텀 할 수 있다.

2. ViewHolder 패턴을 강제하여 View의 재사용을 강제한다. (성능 개선)


```
dependencies {
    compile 'com.android.support:recyclerview-v7:24.2.1'
}
```

https://thdev.tech/androiddev/2016/11/01/Android-RecyclerView-intro.html