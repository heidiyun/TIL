# Vue- Create
## SPA (Single Page Application)
각각 마다 index.html이 존재해서 매번 로드를 해야 하지만, 여기서는 하나의 파일로 관리해서 로드를 처음 시작시 한번 만 해주면 된다. 그리고 안드로이드 라이브러리와 같은 기능을 매 index.html마다 해주는 것이 아니라 한번에 관리 할 수 있다.
* PWA (Progressive Web App)
* 
## Node
* node에서 패키지 관리 - npm
```shell
$ npm init // package.json (의존성, 메타데이터, 명령어 관리)
$ npm run build // dist 한 파일을 묶어줌.
$ npm run serve // dev환경으로 실시간 변동 보여줌.
$ npm install -g @vue/cli // vue cli 설치 // vue 명령어 사용 가능
```

## Vue Create

![](Vue-%20Create/BC76E864-8CA5-4941-8484-27A5A5494F75.png)

* 0524-vue 폴더를 생성합니다.

![](Vue-%20Create/C966865F-836F-475E-8515-1A640DBCD756.png)

* babel : 구동환경을 맞춰주는 것. -> transpile : 예를 들어서 apk 버전마다 메소드가 다르듯이 메소드를 바꿔주는 것.
* lint : JS 코드 스타일 맞추는 것.
* router : 주소에 따라서 어떤 결과를 줄 것인지

![](Vue-%20Create/FC2172A2-9869-458C-AA68-999C87B9A222.png)

* Package.json 
	-> 의존성
	-> 메타데이터
	-> command 관리

```shell
* npm run build
-> dist 한 파일을 묶어줌.
* npm run serve
-> dev환경으로 실시간 변동 보여줌.
```

## git-page 
* gitignore 파일에서 dist 지우고 git에 올리기.
* git page에 연결해줄 때는 git repository 이름과 같아야 한다.
* 파일마다 save 걸어줘야 한다. 



#web