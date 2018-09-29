# 0916 - Container
## Container 
* Docker Engine위에 동작한다.
* 하나의 프로세스 처럼 동작하며 OS가 차지하는 경우가 없다.
* 기반 환경도 바꿀 수 있다.
* immutable해서 이미 있는 시스템이면 공유해서 사용한다.

### Mac Docker 
```java
$ brew cask install docker
// docker download
$ docker ps
// docker 설치 완료됐으면 해당 명령어를 쳐보자.
// 실행중인 container 목록을 보여준다.

/* ../hi-server/에 Dockerfile을 만들 것이다.  - sublime 이용*/
$ subl Dockerfile 
// sublime이 없으면 brew로 설치
// 내용은 github hi-server/Dockerfile 참조
// 설정을 완료했으면

$ docker images
// 현재 만들어진 이미지 리스트를 보여준다.

$ docker build -t heidiyun/hi-server . 
// . -> 현재 디렉토리에 이미지 만들기
// <docker id/project name> <dockerfile path>
// node 버전이 같은 것을 또 생성하 필요가 없이 공유해서 쓸 수 있다.

$ docker run --name hi-server -p 3000:3000 heidiyun/hi-server
// 호스트에 3000번 포트로 접근, container에 3000번 포트로 접근
// 실행이 되면 웹에 127.0.0.1:3000번을 입력해보자.

pm2는 포그라운드를 백그라운드 서비스로 전환해주는 역할을 하는데,
이와 비슷한 기능이 docker도 제공해준다.
현재 내 시스템과 상관없이 서버가 돌아가게 해준다.

$ docker run --name hi-server -p 3000:3000 -d heidiyun/hi-server

```

그 외 docker 명령어
```java
$ docker images
// 생성된 이미지 목록
$ docker ps -a
// docker ps는 실행중인 container만 보여준다면 위 명령어는 생성된 container의 모든 리스트를 보여준다.
// conflicting을 방지하기 위해서 같은 이름의 컨테이너는 지워줘야 한다.

$ docker stop <container id | container name>
// container 중지
// docker ps 명령어로 보이지 않는다.
$ docker rm <container id | container name>
// container 삭제

$ docker rm <container name>
// 생성된 컨테이너 삭제 명령어

$ docker rmi <image name>
// 생성된 이미지 삭제 명령어

$ docker system prune -af
// 이미지 모두 삭제
```

### linux Docker
```java
$ sudo yum update -y
$ sudo reboot

$ sudo yum install -y docker
// Docker 설치

$ daemon: Background Process
-> android: Service
Service : 자신만의 액티비티를 갖고있지 않은 프로세스

$ sudo systemctl start docker
// $ sudo service docker start 
// docker 시작하기
// 이 명령어가 성공했으면 docker ps 명렁어를 사용할 수 있다.

$ sudo usermod -aG docker $USER
// 계속 sudo를 치는 것이 불편하니 현재 유저를 docker 그룹에 추가해주자.
// aG (add Group)
// $USER :  현재 유저

$ sudo systemctl enable docker.service
// 재부팅했을 때, docker daemon을 시작하는 명령을 실행하지 않아도 바로 docker 명령을 실행할 수 있다.
// Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.

// exit로 재접속
$ docker ps
```

### Docker Hub 사용하기
사용자는 하나의 private repository를 생성할 수 있다.

```java
docker search <image name>
// Docker Hub에서 image를 찾는 명령어

docker pull <image name>:<tag>
// Docker Hub에서 이미지를 받아오는 명령

docker login
docker push <Docker Hub id>/<image name>:<tag>
// 생성한 이미지를 Docker Hub에 올리는 명령어
```


### AWS ECR (Docker Container) 사용하기
#### AWS CLI 설치
1. 파이프 설치
```java
// 1. 설치 스크립트 다운로드
$ curl -O https://bootstrap.pypa.io/get-pip.py
// 2. 파이썬으로 스크립트 실행
$ python get-pip.py --user
// 3. ~/.bash_profile PATH 변수에 실행 파일 경로 추가하기
$ ehco $SHELL >> 의 결과값 : ~/.local/bin
export PATH=~/.local/bin:$PATH
// 4. pip가 올바르게 설치되었는지 확인하기
$ pip --version
```

2. Pip을 사용하여 AWS CLI 설치
```
// 설치 | 업그레이드 명령어
$ pip install awscli --upgrade --user
// 설치 확인
$ aws --version
```

#### Amazon ECR에 리포지토리 만들기
AWS 접속 후, 콘솔에 로그인하여 ECR 서비스로 이동한다.
리포지토리를 생성한다.

#### Amazon ECR 레스트리에 대해 Docker 인증하기
```
$ (aws ecr get-login --no-include-email --region ap-northeast-2)
// 생성한 리포지토리의 푸시명령 메뉴를 클릭하면 볼 수 있다.
// 위의 명령어의 결과값으로 나오는 명령을 복사하여 입력한다.
// 결과 출력은 Amazon ECR 레지스트리에 대해 Docker 클라이언트를 인증하는 데 사용하는 docker login 명령입니다.
// 지정된 레지스트리에 대해서 12시간 동안 유효한 인증 토큰을 제공합니다.
```

#### Access Key 만들기
AWS에서 내 계정 메뉴를 누르고 내 보안 자격 증명 메뉴에 들어간다.
액세스 키 항목을 선택하여 액세스 키를 생성하고 .csv 파일을 다운로드한다.
```java
$ vi ~/.aws/credentials
// 할당받은 access_key_id 와 secret_access_key 를 입력한다.
$ vi ~/.aws/config
// 사용하고자 하는 region을 입력한다.
```

#### Image Push 하기.
Dockerfile을 만들었다면,
```
$ docker build -t hello-server .
// 현재 디렉토리에 hello-server의 이름을 가진 이미지를 생성한다.

$ docker tag hello-server:latest 578412696765.dkr.ecr.ap-northeast-2.amazonaws.com/hello-server:latest
// 태그를 달아줍니다. 
// docker tag <image name>:<tag> <repo url>:<tag>

$ docker push 578412696765.dkr.ecr.ap-northeast-2.amazonaws.com/hello-server:latest
// 리포지토리에 푸시합니다. 
// docker push <repo url>:<tag>
```

#### Image Pull 하기.
```
$ docker pull <repo url>:<tag>
// docker pull 578412696765.dkr.ecr.ap-northeast-2.amazonaws.com/hello-server:latest
```

## Docker swarm
Node - Instance
1. Manager node
2. Worker node

수평확장 — docker swarm
컴퓨터를 여러개 만든다.

수직확장
컴퓨터의 사양을 올린다.

```
$ docker swarm leave --force
// docker swarm init 정보를 날린다.

/* Manager node에서 해야할 일 */
$ docker swarm init
여기서 나온 코드를 worker node에 복사해준다.
worker node에서 해야할 일은 끝.

--manager--
docker service ls

$ docker service create --name <docker service name> -p 3000:3000 <docker hub id>/<docker image name>

// 서비스가 정상적으로 돌아가면 docker service ls명령을 쳤을때 목록에 등록이 되어있어야 한다.

$ docker service rm <container name>
// 실행중인 docker service 지우기.

$ curl 127.x.x.x:3000 
// 위 명령을 쳤을 때 응답이 온다면 서버가 열린것이다.

$ docker service scale <docker service name>=<number>
// container를 4개로 늘렸다.
// docker service ls로 확인하면
// REPLICAS 4개로 늘어나 있다.
// docker node ls


```


## docker image update
만약 쓰고 있던 docker hub image의 내용을 바꾸고 새로 image를 빌드하고 docker hub에 image를 push했다.
실행중인 container를 지우고 다시 create하지 말고 image를 업데이트 하고싶을 때. 

``` 
---mac terminal hi-server--
1. $ code .
	으로  version 바꾸기
2. $ docker build -t <docker id/project name> <dockerfile path>
	이미지 다시 빌드.
3. docker tag <docker id/project name> <imagefile name>:<tag>
	이미지를 명확히 구분해주기 위해서 tag를 달아준다.
4.docker push <Docker Hub id>/<image name>:<tag>
	빌드 한 이미지를 docker hub에 올려준다.

--- ec2 instance --
5. docker service update --image docker push <Docker Hub id>/<image name>:<tag> <docker service name>
	이미지를 실행중인 docker service에 업데이트 해준다.


$ docker service logs hello-server -f
// 로그 실시간으로 보기.
$ docker system prune -af
// 로그 지우기.
	
```

#node