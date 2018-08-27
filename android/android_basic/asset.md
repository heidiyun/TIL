# Asset
앱에서는 AndroidStudio 내에 저장된 파일에 대해 알 수 없다.
그래서 종종 AndroidStudio 에서 파일 읽기와 같은 작업을 수행할 때 알 수 없는 오류를 마주하는 경우가 많다.
앱에 필요한 파일일 경우 asset 폴더에 그 파일을 저장해야 앱에서 사용할 수 있다.

## Android Asset
파일을 APK(Android Package Kit)에 포함시켜 앱에서 사용할 수 있도록 만든 것.
즉, 앱이 릴리즈 되는 시점에, 앱이 사용할 수 있는 자산이라고 할 수 있다.

asset 폴더는 app_new_Folder 메뉴를 통해 생성할 수 있다.

aseet 폴더의 파일은 경로를 통해 직접 접근할 수 없어, AssetManager 클래스를 통해 접근해야 한다.

## Json 파일 읽어오기.
```
/*Fragment의 OnCreateView*/
val json = getAssetJsonDate(requireContext())
val parser = JsonParser()
val jsonObject = parser.parse(json) as JsonObject

/* 파일의 정보를 읽어오는 메소드*/
fun getAssetJsonDate(context: Context): String? {
    var json: String? = null
    try {
        val inputStream = context./assets/.open(“color.json”)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = /String/(buffer)
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }

    Log.e(“data”, json)
    return json
}

```
