# Glide
> 이미지 로딩 라이브러리
> 웹 상의 이미지를 로드하여 보여주기 위해 사용한다.

1. build.gradle dependencies에 추가하기.
```
	apply plugin: 'kotlin-kapt'

	implementation 'com.github.bumptech.glide:glide:4.7.1'
    kapt 'com.github.bumptech.glide:compiler:4.7.1'

```

2. 이미지 가져오기.
```java
	GlideApp.with(this).load("https://avatars0.githubusercontent.com/u/3")
	.into(image) // 가져온 이미지를 보여줄 이미지 뷰.
```

* with(context: Context)
* load : 웹 상에서의 이미지 경로 URL or 안드로이스 리소스 ID or 로컬 파일 or URI
