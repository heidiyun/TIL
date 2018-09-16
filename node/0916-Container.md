# 0916 - Container
## Container 
* Docker Engine위에 동작한다.
* 하나의 프로세스 처럼 동작하며 OS가 차지하는 경우가 없다.
* 기반 환경도 바꿀 수 있다.
* immutable해서 이미 있는 시스템이면 공유해서 사용한다.

### Mac Docker 
```java
brew cask install docker
// docker download
docker ps
// docker 설치 완료됐으면 해당 명령어를 쳐보자.
// 실행중인 container 목록을 보여준다.

/* ../hi-server/에 Dockerfile을 만들 것이다.  - sublime 이용*/
subl Dockerfile 
// sublime이 없으면 brew로 설치
// 내용은 github hi-server/Dockerfile 참조
// 설정을 완료했으면

docker images
// 현재 만들어진 이미지 리스트를 보여준다.

docker build -t heidiyun/hi-server . 
// . -> 현재 디렉토리에 이미지 만들기
// <docker id/project name> <dockerfile path>
// node 버전이 같은 것을 또 생성하 필요가 없이 공유해서 쓸 수 있다.

docker run --name hi-server -p 3000:3000 heidiyun/hi-server
// 호스트에 3000번 포트로 접근, container에 3000번 포트로 접근
// 실행이 되면 웹에 127.0.0.1:3000번을 입력해보자.

pm2는 포그라운드를 백그라운드 서비스로 전환해주는 역할을 하는데,
이와 비슷한 기능이 docker도 제공해준다.
현재 내 시스템과 상관없이 서버가 돌아가게 해준다.

docker run --name hi-server -p 3000:3000 -d heidiyun/hi-server

```

그 외 docker 명령어
```java
docker ps -a
// docker ps는 실행중인 container만 보여준다면 위 명령어는 생성된 container의 모든 리스트를 보여준다.
// conflicting을 방지하기 위해서 같은 이름의 컨테이너는 지워줘야 한다.

docker stop <container id | container name>
// container 중지
// docker ps 명령어로 보이지 않는다.
docker rm <container id | container name>
// container 삭제

```

### linux Docker
```java
sudo yum install -y docker
// Docker 설치

sudo service docker start 
// docker 시작하기
// 이 명령어가 성공했으면 docker ps 명렁어를 사용할 수 있다.

sudo suermod -aG docker $USER
// 계속 sudo를 치는 것이 불편하니 현재 유저를 docker 그룹에 추가해주자.
// aG (add Group)
// $USER :  현재 유저

// exit로 재접속
docker ps

```

#node