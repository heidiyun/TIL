# Todolist-server
>  코드는 [todolist-server](https://github.com/heidiyun/TIL/tree/master/node/todolist-server)를 참고하세요.  

## Javascript
> Script 언어  

**특징**
Compile 단계가 존재하지 않는다.
Runtime 이라고 불리는 스크립트 해석기가 있습니다.  (인터프리터)
	* 프로그래밍 언어의 소스코드를 바로 실행하는 컴퓨터 프로그램/환경
	* 원시 코드를 기계어로 번역하는 컴파일러와 대비됩니다.
인터프리터가 코드를 실시간으로 해석해서 수행합니다.

**인터프리터 기능**
다음 3가지 중에 적어도 한가지 기능을 가져야 한다.
1. 소스 코드를 직접 실행한다.
2. 소스 코드를 효율적인 다른 중간 코드로 변환하고, 변환한 것을 바로 실행한다.
3. 인터프리터 시스템의 일부인 컴파일러가 만든, 미리 컴파일된 저장 코드의 실행을 호출한다.

## Node.js
> Javascript를 브라우져가 아닌 네이티브(Windows, Linux, Mac)에서 수행할 수 있도록 해주는 플랫폼.  


## 암호화
1. 단방향 암호화 
비밀번호, 주민번호를 저장할 경우 많이 사용한다.
이미 암호화 된 것을 풀 수 없다.
Hash
* todolist-server에서는 비밀번호를 저장할 때 단방향 암호화를 사용하며, bcrypt 모듈을 사용합니다.

2. 양방향 암호화(RSA)
인증서 기반 암호화
* 공개키 : 암호화 할 때 사용한다.
* 비공개키 : 암호를 풀 때 사용한다.

## JWT (JSON Web Token)
사용자의 정보를 token으로 변환한것
그래서 token을 받으면 사용자 정보로 변환할 수 있다.

## Memory
> 접근 속도가 빠른 순  
1. CPU Cache
2. Memory (RAM)
3. SSD (HHD)  하드디스크
4. Network Storage(Database) - MySQL, MongoDB
Memory DB(Redis, Memcached) - Session Store

## Image
이미지를 업로드 할 때,
이미지 파일 - storage
이미지 파일의 메타 정보 - database

```
$ vi .envrc 
	AWS_ACCESS_KEY_ID=
	AWS_SECRET_ACCESS_KEY=
-> IAM access key & scret access key 추가하기.

$ direnv allow

envrc. gitignore에 추가하기.
$ brew install aws
$ aws s3 ls

$ npm i aws-sdk
------------------------------------------------------------
코드를 실행했을 때 
missing credential in config 
에러가 뜬다면,

$ aws configure
설정해준다.

----POSTMAN-------
POST -> 127.0.0.1:3000/images
		-> Header : Authorization
		-> Result body : signedUrl

PUT -> signedUrl
	  -> body : 업로드 할 이미지

$ aws s3 ls storagename /
으로 file올라왔는지 확인하기.

GET -> 127.0.0.1:3000/images/:filename
	  -> result body : redirect url
or

주소창 : 127.0.0.1:3000/images/:filename
```


#node