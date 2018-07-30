# Tab Scroll
> DrawerLayout + CoordinatorLayout + AppBarLayout + TabLayout(ViewPager)

```xml
<?xml version="1.0" encoding="utf-8"?>

<android.support.v4.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/profileDrawerLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.design.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <android.support.design.widget.AppBarLayout
            android:id="@+id/profileBar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="60dp"
            app:layout_constraintRight_toLeftOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            tools:layout_editor_absoluteX="0dp">

            <android.support.v7.widget.Toolbar
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:layout_scrollFlags="scroll|enterAlways">

                <android.support.constraint.ConstraintLayout
                    android:layout_width="match_parent"
                    android:layout_height="match_parent">

                    <ImageView
                        android:id="@+id/ownerAvatarImage"
                        android:layout_width="104dp"
                        android:layout_height="105dp"
                        android:layout_marginBottom="8dp"
                        android:layout_marginStart="8dp"
                        android:layout_marginTop="8dp"
                        android:src="@drawable/ic_github"
                        app:layout_constraintBottom_toBottomOf="parent"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent"
                        app:layout_constraintVertical_bias="0.0" />

                    <TextView
                        android:id="@+id/nameText"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="16dp"
                        android:text="Goo"
                        android:textSize="25dp"
                        app:layout_constraintStart_toEndOf="@+id/ownerAvatarImage"
                        app:layout_constraintTop_toTopOf="parent" />

                    <TextView
                        android:id="@+id/IDText"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="18dp"
                        android:text="hyun"
                        android:textSize="20dp"
                        app:layout_constraintStart_toEndOf="@+id/ownerAvatarImage"
                        app:layout_constraintTop_toBottomOf="@+id/nameText" />

                    <TextView
                        android:id="@+id/emailText"
                        android:layout_width="200dp"
                        android:layout_height="19dp"
                        android:layout_marginBottom="8dp"
                        android:layout_marginStart="16dp"
                        android:text="heidi@gmail.com"
                        app:layout_constraintBottom_toBottomOf="parent"
                        app:layout_constraintStart_toEndOf="@+id/ownerAvatarImage"
                        app:layout_constraintTop_toBottomOf="@+id/IDText" />

                </android.support.constraint.ConstraintLayout>

            </android.support.v7.widget.Toolbar>

            <android.support.design.widget.TabLayout
                android:id="@+id/tabLayout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="?attr/colorPrimary"
                android:minHeight="?attr/actionBarSize"
                android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar" />

        </android.support.design.widget.AppBarLayout>

        <android.support.v4.view.ViewPager
            android:id="@+id/pager"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"/>

    </android.support.design.widget.CoordinatorLayout>

    <include
        layout="@layout/app_bar_navigation"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:layout_editor_absoluteX="127dp"
        tools:layout_editor_absoluteY="383dp" />

    <android.support.design.widget.NavigationView
        android:id="@+id/navView"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:fitsSystemWindows="true"
        app:headerLayout="@layout/nav_header_main"
        app:menu="@menu/activity_main_drawer" />

</android.support.v4.widget.DrawerLayout>
```

* Coordinator 안에 tab scroll을 구현할 모든 내용이 들어가야 한다.
* AppBarLayout 안에 스크롤시 숨겨지거나 올라가는 동작을 하는 bar layout이 들어가야 한다.
(Github Profile, TabLayout)
* NestedScrollView와 ViewPager가 같이 있을 시에는 잘 동작하지 않으므로 둘 중 하나만 써야 한다.
(NestedScrollView 안에 ViewPager를 넣을 경우 Fragment를 받아오지 못한다.)
* NavigationDrawer은 CoordinatorLayout 밖에 위치해야 한다. 
* app_bar_navigation이 맨 위로 올라가게 되면 AppBarLayout에 의해 감춰지고, 아래로 내려가면 AppBarLayout이 app_bar_navigation 안으로 숨겨진다. 