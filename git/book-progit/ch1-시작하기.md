# 1. 시작하기
## 버전 관리(VCS - Version Control System)
파일 변화를 각각의 버전이라고 한다면, 내가 필요할 때 특정 버전을 다시 꺼내올 수 있도록 하는 것이 버전관리이다.

**장점**
* 파일을 이전 상태로 되돌릴 수 있다.
* 시간에 따른 수정 사항을 비교해 볼 수 있다.
* 문제가 발생하면 추적할 수 있다.
* 파일을 잃어버리거나 잘 못 수정할 경우 복구가 쉽다.
### 로컬 버전 관리
Local VCS는 간단한 데이터베이스를 사용해서 파일의 변경 정보를 관리한다.

![](ch1-%E1%84%89%E1%85%B5%E1%84%8C%E1%85%A1%E1%86%A8%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5/page24image1752992.png) 
![image](https://user-images.githubusercontent.com/38517815/51294337-ce77a280-1a55-11e9-9bde-27277f8a52d3.png)
많이 사용되는 VCS 도구 중에 RCS가 있다.
Mac OS X 운영체제에서도 개발 도구를 설치하면 RCS가 함께 설치된다.
RCS는 기본적으로 Patch Set(파일에서 변경되는 부분) 을 관리한다. 
일련의 Patch Set을 적용해서 모든 파일을 특정 시점으로 되돌릴 수 있다. 

### 중앙집중식 버전 관리 (CVCS)
2명이상이 함께 작업을 할 경우 버전 관리를 위해 중앙집중식 버전 관리가 개발되었다.
파일을 관리하는 서버가 별도로 있고 클라이언트가 중앙 서버에서 파일을 받아서 사용(check out)한다.
![](ch1-%E1%84%89%E1%85%B5%E1%84%8C%E1%85%A1%E1%86%A8%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-17%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%201.13.44.png)
![image](https://user-images.githubusercontent.com/38517815/51300444-42727480-1a6f-11e9-89b0-ce918e97f8df.png)
**장점**
클라이언트 모두가 서로 무엇을 하고 있는지 볼 수 있다.
모든 클라이언트의 개인 로컬 데이터베이스를 관리하는 것보다 편리하다.
**단점**
중앙 서버가 다운 되면 백업할 수 없고, 다른 사람들과 협업할 수 없다.
중앙 데이터베이스가 있는 하드디스크에 문제가 생기면 프로젝트의 모든 히스토리가 삭제된다. 

### 분산 버전 관리 시스템 (DVCS)
클라이언트가 마지막 버전의 파일만 받아서 사용하는 것이 아니라, 저장소 전부를 복제합니다. 서버에 문제가 생기면 복제한 저장소로 작업을 계속 할 수 있습니다.

![](ch1-%E1%84%89%E1%85%B5%E1%84%8C%E1%85%A1%E1%86%A8%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-17%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%203.47.28.png)
![image](https://user-images.githubusercontent.com/38517815/51300467-51f1bd80-1a6f-11e9-87b5-b48e4b2aa8c4.png)
또한 대부분의 DVCS 환경에서는 리모트 저장소가 존재합니다.
사람들은 동시에 다양한 그룹과 다양한 방법으로 협업할 수 있습니다.
**리모트 저장소**
인터넷이나 네트워크 어딘가에 있는 저장소를 뜻한다.
저장소는 여러개가 있을 수 있습니다.
각각의 권한이 다를 수 있습니다.

## 짧게 보는 Git 역사
Linux 커널은 규모가 큰 오픈소스 프로젝트이다.
이 프로젝트는 Patch와 단순 압축파일로만 관리되었다.
2002년부터 BitKeeper라고 불리는 상용 DVCS를 사용하기 시작했지만, 무료 사용이 제고되면서 프로젝트 커뮤니티에서 자체 도구를 만들기 시작했다. 이때 탄생한것이 Git이다.

**Git의 목표**
1. 빠른 속도
2. 단순한 구조
3. 비선형적인 개발(수천 개의 동시 다발적인 브랜치)
4. 완벽한 분산
5. Linux 커널 같은 대형 프로젝트에 유용할 것(속도, 데이터 크기)

## Git 기초
### 데이터를 다루는 방법의 차이
Subversion과 같은 VCS와 Git의 가장 큰 차이점은 데이터를 다루는 방법에 있다.
Subversion, Perforce와 같은 것들은 각 파일의 변화를 시간순으로 관리하면서 모든 파일들을 새로 저장한다.
Git dms 파일이 달라지지 않았으면 파일을 새로 저장하지 않고, 이전 상태의 파일에 대한 링크만 저장한다.
**Git은 데이터를 스냅샷의 스트림처럼 취급한다.**
즉, 스크린샷을 찍는 것처럼 현재 파일의 변경상태에만 집중한다.

### 거의 대부분의 명령을 로컬에서 실행.
거의 모든 명령이 로컬 파일과 데이터만 사용하기 때문에 네트워크에 있는 다른 컴퓨터는 필요 없다. 즉, 네트워크의 속도에 영향을 덜 받는다.
프로젝트의 모든 히스토리가 로컬 디스크에 있기 때문에 명령을 빠르게 실행한다.
(프로젝트의 히스토리를 조회할 때 서버 없이 조회한다
즉, 파일을 비교하기 위해 리모트에 있는 서버에 접근해서 예전 버전을 가져올 필요가 없다.)

### Git의 무결성
Git은 체크섬으로 데이터를 관리한다.
SHA-1 해시를 사용하여 체크섬을 만든다. (40자 길이의  16진수 문자열)
Git은 파일을 이름으로 저장하지 않고 해당 파일의 해시로 저장한다.

### Git은 데이터를 추가할 뿐.
Git을 사용하면 Git 데이터베이스에 데이터가 추가된다.
되돌리거나 데이터를 삭제할 방법이 없다.
 커밋하고 나면 데이터를 잃어버리기 어렵다.

### 세 가지 상태
Git은 파일을 Committed, Modified, Staged 세 가지 상태로 관리한다.
1. Committed  : 데이터가 로컬 데이터베이스에 안전하게 저장됐다.
2. Modified : 수정한 파일을 아직 로컬 데이터베이스에 커밋하지 않았다.
3. Staged : 현재 수정한 파일을 곧 커밋할 것이다.

Git 프로젝트의 세 가지 단계
![](ch1-%E1%84%89%E1%85%B5%E1%84%8C%E1%85%A1%E1%86%A8%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-17%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%204.46.19.png)
![image](https://user-images.githubusercontent.com/38517815/51303030-76ea2e80-1a77-11e9-8774-b2f4ec81a74e.png)
1. .git directory : 프로젝트의 메타데이터와 객체 데이터베이스를 저장한다.
			: git clone을 하면 이 디렉토리가 만들어진다.
2. Working Directory : 프로젝트의 특정 버전을 checkout한 것이다.
3. Staging Area는 git 디렉토리에 있다. 단순한 파일이고 곧 커밋할 파일에 대한 정보를 저장한다.

**git이 하는 일**
1. 워킹 디렉토리에서 파일을 수정한다. -> Modified
2. Staging Area에 파일로 커밋할 스냅샷을 만든다. -> Staged
3. Staging Area에 있는 파일들을 커밋해서 Git 디렉토리에 영구적인 스냅샷으로 저장한다. -> Committed

### Git 설치
#### Linux에 설치
리눅스의 각 배포판에서 사용하는 패키지 관리 도구를 사용하여 설치한다.
* Fedora
`$ sudo yum install git`

* Ubuntu (debian)
`$ sudo apt-get install git`

다른 배포판은 http://git-scm.com/download/linux에서 확인하세요.

#### Mac에 설치
/http://git-scm.com/download/mac/
http://mac.github.com/ 에서 내려받기

#### Windows에 설치
[GitHub Desktop | Simple collaboration from your desktop](http://windows.github.com)
/http://git-scm.com/download/win/ 에서 내려받기

#### 소스코드로 설치하기
가장 최신 버전이 필요하다면 소스코드로 설치하는 것이 좋다.
curl, lib, openssl, expat, libiconv를 필요로 한다.

* yum이나 apt-get
```
$ sudo yum install curl-devel expat-devel gettext-devel \
        openssl-devel zlib-devel
$ sudo apt-get install libcurl4-gnutls-dev libexpat1-dev \ gettext libz-dev libssl-dev

/* 의존 패키지 */
$ sudo yum install asciidoc xmlto docbook2x
$ sudo apt-get install asciidoc xmlto docbook2x

/* 최신 패키지 가져오기 Kernel.org */
$ tar -zxf git-1.9.1.tar.gz
$ cd git-1.9.1
$ make configure
$ ./configure --prefix=/usr
$ make all doc info
$ sudo make install install-doc install-html install-info

```

### Git 최초 설정
git config라는 도구로 설정 내용을 확인하고 변경할 수 있다.
1. _etc_gitconfig 파일 : 시스템의 모든 사용자와 모든 저장소에 적용되는 설정
`$ git config --system`
2. ~_. gitconfig, ~_.config_git_config 파일 : 특정 사용자에게만 적용되는 설정
`$ git config --global`
3. .git/config 파일 : 이 파일은 git 디렉토리에 있고 특정 저장소에만 적용된다.

각 설정의 운선순위는 1 > 2 > 3 순이다.

#### 사용자 정보
사용자 이름과 이메일 주소를 설정한다.
한 번 커밋한 후에는 정보를 변경할 수 없다.
```
$ git config --global user.name "HeidiYun"
$ git config --global user.email "heidiyun.goo@gmail.com
```
프로젝트마다 다른 이름과 이메일 주소를 사용하고 싶다면 --global 옵션을 빼고 명령을 실행한다.

#### 편집기
Git에서 사용할 텍스트 편집기를 고른다.
기본 편집기 - vi, vim
`$ git config --global core.editor emacs`

#### 설정 확인
`$ git config --list`
특정 정보에 대해서 알고 싶을 때
`$ git config <key>`
`$ git config user.name`

#### 도움말 보기
```
$ git help <verb>
$ git <verb< --help
$ man git-<verb>

ex
$ git help config
```




#progit