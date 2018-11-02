# 런타임에 권한 요청
targetSdkVersion 23 이상 또는 장치가 Android 6.0(API 레벨 23, 마시멜로우)부터 사용자는 앱이 설치될 때가 아니라 앱이 실행되는 중에 앱에 권한을 부여해야 합니다.

사용자가 앱을 설치하거나 업데이트할 때 권한을 부여할 필요가 없어서 앱 설치 과정이 간소화됩니다.
또한 사용자가 앱의 기능을 더 세부적으로 제어할 수 있습니다.

**권한 종류**
정상 권한 - 사용자 개인정보를 위험에 빠뜨리지 않는다.	
		  다른 앱의 작업에 위험 요소를 제공하지 않는다.
		  앱의 매니페스트에 정상 권한을 나열하는 경우, 시스템은 자동		  으로 권한을 부여한다.
위험 권한 - 사용자 기밀 데이터에 대한 액세스
		  다른 앱의 작업에 영향을 미칠 수 있다.
		  사용자는 앱에 대한 명시적 승인을 제공해야 한다.


**권한 선언**
* Android 5.1 이하/ targetSdkVersion 22 이하 
매니페스트에 위험 권한을 선언하는 경우, 사용자는 앱을 설치할 때 권한을 승인해야 하며, 권한을 승인하지 않을 경우 시스템이 앱을 설치하지 않는다.

* Android 6.0 이상 / targetSdkVersion 23 이상
매니페스트에 권한을 나열해야 하며, 앱이 실행되는 중에 필요한 위험 권한을 요청해야 합니다. 사용자가 권한을 승인하지 않아도 제한된 기능으로 앱이 계속 실행될 수 있습니다.
장치가 Android 6.0이상이라면 애플리케이션의 targetSdkVersion이 더 낮더라도 사용자가 앱의 권한을 취소할 수 있다.

AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

```kotlin

 val permissionCheck =
        ContextCompat.checkSelfPermission(/applicationContext/, Manifest.permission./READ_EXTERNAL_STORAGE/)
// 필요한 동작에 대한 권한을 갖고 있는지 확인한다.
// 권한이 있다면 PackageManger.PERMISSION_GRANTED(0)이 반환된다.
// 권한이 없다면 PackageManger.PERMISSION_DENIED(-1)이 반환된다.

    if (permissionCheck != PackageManager./PERMISSION_GRANTED/) {
        ActivityCompat.requestPermissions(this, /arrayOf/(Manifest.permission./READ_EXTERNAL_STORAGE/), 1)
    }
// requestPermissions 메소드로 필요한 권한을 제공합니다.
// 파라미터 : activity, permissions, requestCode

    if (permissionCheck == PackageManager./PERMISSION_GRANTED/) {
       // 권한을 가지게 되면 해야할 일 정의.
}


```


[참고문헌](https://developer.android.com/training/permissions/requesting?hl=ko)

#node