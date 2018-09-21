# strings.xml
```xml
<TextView
    android:id="@+id/apiLevelTextView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

apiLevelTextView의  text를 런타임에 코드로 설정해주고 싶다.
현재 장치의 API 레벨을 보여주고자 한다.
예) API 레벨 XX

```kotlin
/* kotlin */
apiLevelTextView.text = 
	"API 레벨" + Build.VERSION.SDK_INT.toString
```

위와 같이 작성해주면 될 거 같은데 다음과 같은 오류가 발생한다.
**Do not concatenate text displayed with setText. Use resource string with placeholders.**

이 경우에는 다음과 같이 해결하자.

```xml
<!--res/string.xml-->
<string name="api_level">API 레벨 %d</string>
```

```kotlin
apiLevelTextView./text/= 
	getString(R.string./api_level/, Build.VERSION./SDK_INT/)
```

java의 string format을 그대로 따른다.
[java string format](http://micropai.tistory.com/48)

#android