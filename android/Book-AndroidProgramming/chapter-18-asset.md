# 18. 에셋
## 리소스 형태가 아닌 에셋
안드로이드에서는 리소스 형태로 음원을 저장할 수 있다.
ex) res/rwa/79_long_stream.wav -> R.raw.79_long_scream
리소스 형태로 음원을 저장하면 서로 다른 종류의 음원을 다른 리소스와 동일한 방법으로 처리할 수 있다. 

리소스 시스템으로 서로 다른 종류의 음원을 처리할 때, 종류가 많으면 하나씩 관리하기가 번거롭다.
하나의 큰 폴더에 음원을 넣어서 애플리케이션에 포함시키는 것이 편하다.

에셋을 사용하면 사용자가 원하는 폴더 구조로 만들어 파일을 저자하고 사용할 수 있다.

### 에셋 가져오기
App -> New -> Folder -> Assets Folder 
assets 폴더가 생성된다.
assets 폴더에 필요한 리소스들을 넣어주면 된다.

### 사용하기
에셋은 AssetManager 클래스를 사용해서 접근할 수 있다.
AssetManager 인스턴스는 어떤 Context에서도 얻을 수 있다. 
```kotlin
val assetManager = context.getAssets()
// AssetManager 인스턴스 얻기

AssetManager.list(string)
// string : 폴더 경로
// 전달한 폴더 경로에 포함된 파일들의 이름을 반환한다.
```

## 에셋 파일 액세스하기
에셋 파일의 경로에 있는 파일을 열 때는 File 객체를 사용하면 안 되고 AssetManager를 사용해야 한다.
```kotlin
val assetPath = "sample_sounds/81_uehea.wav"
val soundData : InputStream = assetManager.open(assetPath)
```

InputStream으로 데이터를 읽을 수 있다.

일부 API에서는 InputStream 대신 FileDescriptor를 사용해야 한다.
이 경우에는 AssetManager.openFd(string)을 사용해서 에셋 파일을 읽는다.
```kotlin
val assetPath = "sample_sounds/81_uehea.wav"
// AssetFileDescriptor는 FileDescriptor와 다르다.
val assetFileDescriptor = assetManager.openFd(assetPath)

// assetFileDescriptor을 사용해서 FileDescriptor 얻는 방법
val filedescriptor = assetFileDescriptor.fileDescriptor()
```

#android/책