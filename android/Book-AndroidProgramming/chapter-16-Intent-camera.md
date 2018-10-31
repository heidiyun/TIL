# 16. 인텐트를 사용한 사진 찍기.
## 외부 스토리지
사진은 화면 외의 다른 저장소에 보관해야 한다.
풀 사이즈 사진은 용량이 커서 SQLite 데이터베이스에 넣을 수 없다.
-> 장치의 파일 시스템에 저장해야 한다.

보통 사진, SQLite 데이터베이스를 개인 스토리지에 저장한다.
![](chapter-16-Intent-camera/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202018-10-31%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%209.10.03.png)
![context의 기본 파일과 디렉토리 메소드](https://user-images.githubusercontent.com/38517815/47787203-716f2900-dd51-11e8-910d-6f574de78379.png)
위와 같은 메소드들은 개인 스토리지 영역에 파일을 저장하고 사용할 때 필요하다.

**다른 애플리케이션과 공유할 파일 또는 다른 앱에서 파일을 받는 경우에는 외부 스토리지에 저장해야 한다.**
1. 기본 외부 스토리지
안드로이드 장치에는 최소한 하나의 외부 스토리지 위치가 있다. 
-> 기본 외부 스토리지의 위치.
`Environment.getExternalStorageDirectory()에서 반환되는 폴더에 위치한다.`

2. 기타 외부 스토리지
기본 외부 스토리지 이외의 것들.

Context 클래스는 외부 스토리지를 얻는 메소드도 제공한다.
해당 메소드는 모두 공유 가능한 곳에 파일을 저장하므로 사용시에 주의하자.

외부 스토리지가 없을 수도 있기때문에 사용시에 외부 스토리지 확인 코드를 꼭 포함시키자.

## 카메라 인텐트 사용하기.
> 카메라 인텐트는 MediaStore에 정의되어 있다.   
MediaStore.ACTION_IMAGE_CAPTURE  액션을 갖는 암시적 인텐트를 요청한다.

### 외부 스토리지 권한
> 외부 스토리지에 읽거나 쓰려면 권한이 필요하다.   
> AndroidManifest <uses-permission>  

Context.getExternalFilesDir(String)에서는 앱에 국한된 폴더를 반환하므로 권한을 명시하지 않아도 된다. (안드로이드 4.4 이상 버전부터)
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="18" />
```

### 인텐트 요청하기.
MediaStore.ACTION_IMAGE_CAPTURE  액션은 기본적으로 작은 섬네일 사진을 줍니다. 

전체 해상도 사진을 받으려면 인텐트 엑스트라 데이터로 MediaStore.EXTRA_OUTPUT 상수와 사진을 저장할 위치를 가리키는 Uri를 전달하면 된다.

```java
final Intent captureImage = new Intent(MediaStore./ACTION_IMAGE_CAPTURE/);

boolean canTakePhoto = mPhotoFile != null &&
        captureImage.resolveActivity(packageManager) != null;
mPhotoButton.setEnabled(canTakePhoto);

if (canTakePhoto) {
    Uri uri = FileProvider./getUriForFile/(getContext(), "kr.ac.ajou.heidi.criminalintentj.fileprovider", mPhotoFile );
    captureImage.putExtra(MediaStore./EXTRA_OUTPUT/, uri);

}

```

## 비트맵의 크기 조정과 보여주기
카메라로 사진을 찍으면 파일 시스템의 파일로 저장됩니다.
이제, 사진 파일을 읽어서 로드한 후 사용자에게 보여주어야 한다.
이때 합당한 크기의 Bitmap객체로 로드해야 한다. 
Bitmap 객체를 얻을 때는 BitmapFactory 클래스를 사용하면 됩니다.
`Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath())`

Bitmap은 화소(pixel) 데이터를 저장하는 간단한 객체이다.
파일이 압축되어도 Bitmap 자체는 압축되지 않습니다.
-> 1600만 화소의 24비트 카메라 이미지는 5MB 크기의 JPG로 압축될 수 있지만, Bitmap 객체로 로드하면 48MB 크기로 커집니다.

```java
public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
    // 파일의 이미지 크기를 알아낸다.
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory./decodeFile/(path, options);

    float srcWidth = options.outWidth;
    float srcHeight = options.outHeight;

    int inSampleSize = 1;
    if (srcHeight > destHeight || srcWidth > destWidth) {
        if (srcWidth > srcHeight) {
            inSampleSize = Math./round/(srcHeight / destHeight);
        } else {
            inSampleSize = Math./round/(srcWidth / destWidth);
        }
    }

    options = new BitmapFactory.Options();
    options.inSampleSize = inSampleSize;

    return BitmapFactory./decodeFile/(path, options);
}
```

## 사용할 장치 기능 선언하기
애플리케이션에서 사용하는 기능을 장치가 지원하지 않을 때 사전에 설치가 안 되게 할 수 있다.
AndroidManifest.xml에 어떤 기능을 사용할 것이라고 선언한다.
 
CriminalIntentJ에서는 카메라 기능이 필요하다.
```xml
<uses-feature android:name="android.hardware.camera2.CameraDevice"
    android:required="false" />

```
android:required는 생략 가능한 속성이다.
이 속성은 해당 기능이 제공되지 않으면 애플리케이션이 제대로 작동하는지를 판단한다.
false -> 애플리케이션이 동작한다.
true ->  애플리케이션이 동작하지 않는다.

## 레이아웃 파일에 Include 사용
Include를 사용하면 시간과 복잡도를 줄일 수 있다.
Include는 컴파일 시 해당 레이아웃 파일의 코드로 치환된다.
즉, 코드가 복사 붙여넣기 한 것과 동일하게 작동하기 때문에 일부 속성을 덮어쓰기도 한다.
모든 반복되는 코드를 include로 처리하는 것은 권장하지 않는다.
잘 못 사용하면 뷰의 계층적 구조를 복잡하게 할 수 있다.