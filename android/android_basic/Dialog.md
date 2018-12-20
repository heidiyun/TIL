# Dialog
## 기본
```kotlin
/activity/?./alert/(message) *{*
positiveButton(getString(R.string./yes_ko/)) *{*
		// 네를 선택했을 때 할 일
    *}*
negativeButton(getString(R.string./no_ko/)) *{*
		// 아니오를 선택했을 때 할 일
    *}*
*}*?.show()
```

## 리스트 
```kotlin
val array = /arrayOf/<CharSequence>("오름차순", "내림차순")
val alertBuilder = AlertDialog.Builder(*it*./context/)
alertBuilder
    .setItems(array) *{*_, which *->*
when (which) {
            0 -> {
				// 오름차순 메뉴를 선택해서 할 일
            }
            1 -> {
				// 내림차순 메뉴를 선택해서 할 일
            }
        }
    *}*

alertBuilder.create().show()


```
#android/basic