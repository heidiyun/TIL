# 1006 - Amazon instance with MongoDB
## amazon linux2 with MongoDB
### MongoDB 설치
```
$ sudo vi /etc/yum.repos.d/mongodb-org-4.0.repo
// yum 저장소에 mongodb-org4.0.repo 를 추가한다.

[mongodb-org-4.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/amazon/2013.03/mongodb-org/4.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-4.0.asc

$ sudo yum update 
// repo가 추가됐다고 뜬다.

$ sudo yum install -y mongodb-org
// mongodb-org 설치.
```

Yum (linux package manager)
linux는 os버전이 바뀌면 매니저의 저장소가 달라져서 최신의 버전으로 업데이트 해주지 않는다.

Application - Program + Resource
프로그램 : 실행 가능한 파일

의존하고 있는 파일들을 하나의 꾸러미로 묶어놓았다. - 압축(MAC- .DMG, linux(Redhat - .rpm)

_etc_ 리눅스의 모든 설정 파일

RDB : 확장성이 떨어진다.
3 tier에서 많이 쓰인다.

Database는 하나밖에 쓸 수 없어 성능이 저하될 수도 있다.
Shadding? :  scale up 과 같은 기능.

```
$ ulimit -a : 운영체제 설정 값
Open files : 하나의 프로세스가 열 수 있는 한계값
$ ulimit -n 2048 : 늘릴 수 있다. 값을

$ sudo vi /etc/security/limits.conf
*                soft    fsize           unlimited
*                hard    fsize           unlimited
*                soft    cpu             unlimited
*                hard    cpu             unlimited
*                soft    as              unlimited
*                hard    as              unlimited
*                soft    memlock         unlimited
*                hard    memlock         unlimited
*                soft    nofile          64000
*                hard    nofile          64000
*                soft    nproc           64000
*                hard    nproc           64000

$ sudo reboot

$ ulimit -a
// open files 값이 변경된다.
```

## MongoDB Connect
1. Local 컴퓨터에 MongoDB를 설치한다.
```
-----Mac------------------------------------------
$ brew search mongodb

$ brew cask install mongodb-compass
// 설치가 되었으면 mongodb를 띄운다.
name: ec2 ip6 주소를 입력
```

2.  Mongodb 보안그룹을 설정한다 
AWS EC2 보안그룹 추가.
MongoDB port 번호 - 0.0.0.0 
(Port 27017에 대해서 열어준다.  외부 컴퓨터에서 접속할 수 있도록.)

지금 vpc설정으로 내부에서 밖에 접속을 하지 못한다. 
포트 27017에 대해서만 외부 컴퓨터에서 접속할 수 있도록 허용해주는 것이다.

3. Amazon Instance에서 설정파일 수정하기.
```
-----ec2 -----------------------------------------
$ sudo systemctl start mongod
// 그래도 connect가 안돼
// 지금 mongodb 설정파일에서 bindIP가 localhost로 설정되어 있어서 그렇다.

$ sudo vi /etc/mongod.conf
// bindIP를 0.0.0.0로 고친다.
// yaml -> Json을 보기 편하게 해놓은 형식.

$ sudo systemctl restart mongod

mongoDB Connect 완료!
```

127.0.0.1 -> localhost

### 문제점
지금은 누구나 접속할 수 있다.
해야할 것.
1. bindIP 설정
2. port 번호 기본값이 아닌걸로 바꿔주기.
```
mongo disconnect
MongoDB 처음 설정할 시, port 번호를 부여한다. 27017(X)
보안그룹의 port 번호를 수정한다.

$ sudo vi /etc/mongod.conf
port 번호 바꾸기

```

4. DB에 접속할 때 Id, password로 인증하기.
슈퍼유저 생성- root 권한
```
$ sudo systemctl restart mongod

$ mongo
// 그냥 mongo 쓰면 27017로 접속

$ mongo --port 20000
> use admin
> db.createUser({
... user: "admin",
... pwd: "1234qwer",
... roles: ["root"]
... }
... )

// 슈퍼 유저.
// 모든 데이터베이스에 영향을 끼친다.

$ mongo --port 20000 -u admin -p 1234qwer --authenticationDatabase 'admin'
로 접속!하면 되는데

// 이렇게만 하면 설정이 안된다.
// $ mongo로 명령을 쳤을 때도 접속이 가능하다.
// 아이디 비밀번호 설정이 제대로 먹지 않은 것.
// MongoDB의 Connect할 때도 아이디, 비밀번호 인증을 받지 않는다.

// 설정파일에 들어가서 security를 활성화시켜 주어야 한다.
$ sudo vi /etc/mongod.conf
security:
  authorization: enabled
```

각 데이터베이스의 유저 생성
```
// 각 데이터베이스의 접근 권한을 분리해주어야 한다.

$ mongo --port 20000 -u admin -p 1234qwer --authenticationDatabase 'admin'
use hello
switched to db hello
> db.createUser({
... user: "hello",
... pwd: "1234qwer",
... roles: [ "dbOwner" ]
... }
... )

$ sudo systemctl restart mongod

```

MongoDB Connect
```
ID/Password 모드로 바꿔준다.
use시 사용한 이름,
비밀번호
사용자 이름을 차례로 적어 넣고 connect!
```

## Mac todolist server
```
$ mkdir todolist-server
$ npm init
$ npm install
// package json 만들어짐
$ npm i koa
$ code .
// server.js 파일 만들기
$ npm i lodash
$ npm i koa-router
// 요청이 들어왔을 때, 경로에 따라 다른 작업을 할 수 있게 해주는 koa-router
// npm install은 visual studio의 내부 터미널에서 해주어도 된다.

$ ctrl + d -> eof
	// npm 종료.
```

REPL
HotSpot

Node.js
-> Javascript를 브라우저가 아닌 네이티브(일반적인 운영체제에서 수행가능)에서 수행할 수 있도록 해주는 플랫폼이다.

```javascript
const Koa = require("koa");
const Router = require('koa-router');

const app = new Koa();
const router = new Router();

router.get("/", function (ctx){
    ctx.body = "HEllO, TODO Service"
});

// app에 router를 연결

app.use(router.routes())
.use(router.allowedMethods());

app.listen(3000);
// 항상 저장하자.
// npm start
// 여기까지 하면 로그를 받을 수 없다.
```

```
$ npm i koa-logger
```

```javascript

const Koa = require("koa");
const Router = require('koa-router');

const app = new Koa();
const router = new Router();
const logger = require('koa-logger');

router.get("/", function (ctx){
    ctx.body = "HEllO, TODO Service"
});

// app에 router를 연결

app.use(router.routes())
.use(router.allowedMethods());

app.use(logger());
app.listen(3000);
// 로그 안뜸
```

```javascript
// require : java import와 동일한 개념이다.
// 대문자 - class
// 소문자 - 인스턴스

const Koa = require("koa");
const Router = require('koa-router');

const app = new Koa();
const router = new Router();
const logger = require('koa-logger');

router.get("/", function (ctx){
    ctx.body = "HEllO, TODO Service"
});

// app에 router를 연결

app.use(logger());

app.use(router.routes())
.use(router.allowedMethods());

app.listen(3000);
// middleware 끼리 충돌나지 않도록 순서를 잘 지정해주는 것이 중요하다.
```

```
$ npm i bunyan
```

```
$ npm i nodemon -D
// 개발용 dependencies

$ npx nodemon server.js
// global로 설치하지 않고 devDependency로 설치했을 경우
// 바로 nodemon 명령어를 사용할 수 없다.
// 이때 nodemon 명령을 사용할 수 있도록 해주는 nodemon의 도구가 npx이다.

---------code-------------------
// in package.json
 "scripts": {
    "start": "nodemon server.js"
	}

// path를 알아서 잡아줘서 npx를 쓰지 않아도 된다.

$ npm remove koa-logger
```

파이프 연산(|): 앞에 있는 프로세스의 표준출력을 뒤에 있는 프로세스의 표준 입력으로 바꿔주기.

```
npm start | npx bunyan
"start": "nodemon server.js | bunyan",

$ npm start

$ npm run publish
// production
$ npm start
// development
```

.env 파일은 github에 올리지 않습니다.
 nodemodules는 .gitignore 파일에 등록합니다.
자세한 코드 설명은 [github/heidiyun/TIL/node/todolist-server](https://github.com/heidiyun/TIL/tree/master/node/todolist-server) 참고하세요.

#node