# 5. 분산 환경에서의 Git
## 분산 환경에서의 workflow
중앙 집중형 버전 관리 시스템에서 각 개발자는 중앙 저장소를 중심으로 하는 하나의 노드일 뿐.
Git에서는 각 개발자의 저장소가 하나의 노드이기도 하고 중앙 저장소 같은 역할도 한다.
모든 개발자는 다른 개발자의 저장소에 일한 내용을 전송하거나, 다른 개발자들이 참여할 수 있도록 자신이 운영하는 저장소 위치를 공개할 수도 있다.

### 중앙 집중식 workflow
중앙 저장소는 딱 하나 있고 변경 사항은 모두 중앙 저장소에 집중된다. 
![](ch5-%E1%84%87%E1%85%AE%E1%86%AB%E1%84%89%E1%85%A1%E1%86%AB%E1%84%92%E1%85%AA%E1%86%AB%E1%84%80%E1%85%A7%E1%86%BC/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-02-07%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%206.27.37.png)
중앙 저장소를 하나 만들고 개발자 모두에게 push 권한을 부여한다. 두 명이상의 개발자가 같은 부분을 수정했더라도,  Git은 한 개발자가 다른 개발자의 작업 내용을 덮어쓰지 않도록 해준다. (pull 받지 않고 push를 하지 못하게 함.)

### Integration-Manager Workflow
리모트 저장소를 여러 개 운영한다.
다른 개발자는 읽기만 가능하고 자신은 쓰기도 가능한 공개 저자소를 만드는 workflow

프로젝트를 대표하는 하나의 공식 저장소가 있다.
1. 프로젝트 integration-manager는 프로젝트 메인 저장소에 push 한다.
2. 프로젝트 기여자는 메인 저장소를 clone하고 수정한다.
3. 기여자는 자신의 저장소에 push하고 integration-manager가 접근할 수 있도록 공개해 놓는다.
4. 기여자는  integration-manager에게 변경사항을 적용해 줄 것을 요청한다.
5. integration-manager는 기여자의 저장소를 리모트 저장소로 등록하고 수정사항을 merge하여 테스트한다.
6. integration-manager는 merge한 사항을 메인 저장소에 push 한다.

* 장점
기여자와  integration-manager가 각자의 사정에 맞춰 프로젝트를 유지할 수 있다.
기여자는 자신의 저장소와 브랜치에서 수정 작업을 계속 할 수 있다.
관리자는 기여자가 push 해 놓은 커밋을 적절한 시점에 merge한다.

### Dictator and Lieutenants Workflow
저장소를 여러개 운영하는 방식을 변형한 구조.
수백명의 개발자가 참여하는 큰 프로젝트를 운영할 때 사용한다.
여러명의  integration-manager가 저장소에서 자신이 맡은 부분마을 담당하고 이들을 Lieutenants라고 부른다. 모든 Lieutenants는 최종 관리자 아래에 있으며 최종 관리자를 Dictator라고 부른다.
최종 관리자가 관리하는 저장소를 공식 저장소로 하며 모든 프로젝트 참여자는 이 공식 저장소를 기준으로 작업한다.

1. 개발자는 코드를 수정하고 master 브랜치 기준으로 자신의 토픽 브랜치를 Rebase한다. 여기서 master 브랜치는 Dictator의 브랜치를 말한다.
2. Lieutenants들은 개발자들의 수정사항을 자신이 관리하는 master 브랜치에 merge 한다.
3. Dictator는 Lieutenant의 master 브랜치를 자신의 master 브랜치로 merge한다.
4. Dictactor는 merge 한 자신의 master 브랜치를 push하며 다른 모든 개발자는 Dictator의 master 브랜치를 기준으로 Rebase한다.

## 프로젝트 기여하기
### 커밋 가이드라인
* 공백 문자를 깨끗하게 정리하고 커밋해야 한다.
Git은 공백문자를 검사해볼 수 있는 간단한 명령을 제공한다.
`$ git diff --check`
* 하나의 커밋에는 하나의 이슈만 담자
같은 파일의 다른 부분을 수정하는 경우 `$git add -patch`사용
* 커밋 메시지의 첫 라인에는 50자가 넘지 않는 아주 간략한 메시지를 적자.

### 비공개 소규모 팀
이런 환경에서는 보통 subversion같은 중앙 집중형 버전 관리 시스템에서서 사용하던 방식을 사용한다.
특징은 서버가 아닌 클라이언트쪽에서 merge 한다는 점이다.
즉, 저장소에 새로운 커밋이 올라와있는데 클라이언트가 새로운 커밋을 push하려면 새로운 커밋을 자신의 저장소에 merge한 다음 push 할 수 있다.

### 비공개 대규모 팀
integration-manager workflow를 선택하는게 좋다.
대규모 팀을 작은 팀으로 나누어 작은팀의 결과물을 integration-manager가 merge하고 공유 저장소의 master 브랜치에 업데이트 한다.

```shell
$ git push -u origin featureB:featureBee
```
featureB 브랜치의 작업 내용을 featureBee로  push 한다.

### 공개 프로젝트 fork
공개 팀을 운영할 때에는 모든 개발자가 프로젝트의 공유 저장소에 직접적으로 쓰기 권한을 가지지 않는다. 프로젝트의 관리자는 몇 가지 일을 더 해야 한다. 

* Git 호스팅에서 Fork를 통해 프로젝트에 기여하는 법
1. 메인 저장소를 clone 한다.
2. 토픽 브랜치를 만들고 기여한다.
```shell
$ git clone (url)
$ git checkout -b featureA
$ git commit
```
프로젝트 웹사이트로 가서 Fork 버튼을 누르면 원래 프로젝트 저장소에서 갈라져 나온 쓰기 권한이 있는 저장소가 하나 만들어진다.
그러면 로컬에서 수정한 커밋을 원래 저장소에 push 할 수 있다.
그 저장소를 로컬 저장소의 리모트 저장소로 등록한다.
```shell
$ git remote add myfork (url)
```
이제 등록한 리모트 저장소에 push 한다.  (로컬 저장소의 master 브랜치에 merge한 후 push 하는 것보다 간단.)
관리자가 토픽 브랜치를 프로젝트에 포함시키고 싶지 않을 때 토픽 브랜치를 merge 하기 이전 상태로 master 브랜치를 되돌릴 필요가 없다.

Fork 한 저장소에 push 하고 나면 프로젝트 관리자에게 이 사실을 알려야 한다. 이것을 **Pull Request**라고 한다. 
* 수동으로 이메일 만들기
```shell
$ git request-pull origin/master myfork
```

프로젝트 관리자가 사람들의 수정사항을 merge하고 나서 나의 브랜치를 merge 하려고 할 때 충돌이 날 수 있다.
그러면 내 브랜치를 origin/master에 rebase 해서 충돌을 해결하고 다시 pull request를 보낸다.
```shell
$ git checkout featureA
$ git rebase origin/master
$ git push -f myfork featureA
```
이미 공개된 저장소의 push한 브랜치를 rebase했기 때문에 -f 옵션을 주고 강제로 기존 서버에 있던 featureA 브랜치 내용을 덮어 써야 한다.

* featureB브랜치의 수정을 새로운 브랜치에서.
```shell
$ git checkout -b featureBv2 origin/master
$ git merge --squash featureB
$ git commit
$ git push myfork featureBv2
```
—squash 옵션은 현재 브랜치에 merge할 때 해당 브랜치의 커밋을 모두 하나의 커밋으로 합쳐서 merge 한다. 다른 브랜치에서 수정한 사항을 전부 가져오는 것은 같으나 merge 커밋이 만들어지지 않아 부모가 하나이다. 

### 대규모 공개 프로젝트와 이메일을 통한 관리
오래된 대규모 프로젝트는 대부분 메일링리스트를 통해서 patch를 받아들인다.

토픽 브랜치를 만들어 수정하는 작업은 비슷하나 patch를 제출하는 방식이 다르다.
**프로젝트를 fork 하여 push 하는 것이 아니라 커밋 내용을 메일로 만들어 개발자 메일링리스트에 제출한다.**
장점 : 수신한 이메일에 들어있는 patch를 바로 적용할 수 있다.

```shell
$ git format-patch -M origin/master
```
메일링리스트에 보낼 mbox 형식의 파일을 생성한다.
결과로 생성한 파일이름을 보여준다. (.patch)
각 커밋은 하나씩 메일 메시지로 생성된다.
* 커밋 메시지의 첫 번째 라인 - 제목
* merge 메시지 내용과 patch 자체 - 메일 메시지 본문

-M 옵션은 이름이 변경된 파일이 있는지 확인해준다.

메일링리스트에 메일을 보내기 전에 각 Patch 파일을 손으로 고칠 수 있다. 
‘ ---‘ 라인과 diff - -git으로 시작한느 라인 사이에 내용을 추가하면 개발자는 읽을 수 있지만, 나중에  patch에 적용되지는 않는다.

Git에는  Patch 메일을 그대로 보낼 수 있는 도구가 있다. IMAP 프로토콜로 보낸다.
* Gmail을 사용하여 Patch 메일을 전송하는 방법
1. ~/.gitconfig 파일에서 이메일 부분을 설정한다.
```git
[imap]
     folder = "[Gmail]/Drafts"
     host = imaps://imap.gmail.com
     user = user@gmail.com
     pass = p4ssw0rd
     port = 993
     sslverify = false
```
IMAP 서버가 SSL을 사용하지 않으면 마지막 두 라인은 필요 없고 host에서 imaps: 대신 imap: 로 한다.
git imap-send 명령으로 Patch 파일을 IMAP 서버의 Draft 폴더에 이메일로 보낼 수 있다.

SMTP 서버를 이용해서 Patch를 보낼 수도 있다.
먼저  SMTP 서버를 설정해야 한다. ~/.gitconfig 파일의 sendemail 섹션을 고친다.
```shell
[sendemail]
     smtpencryption = tls
     smtpserver = smtp.gmail.com
     smtpuser = user@gmail.com
     smtpserverport = 587
```
git send-email 명령으로 패치를 보낼 수 있다.

## 프로젝트 관리하기
프로젝트 운영하기
1. format-patch 명령으로 생성한 patch를 이메일로 받아서 프로젝트에 patch 적용하기.
2. 프로젝트의 다른 리모트 저장소로부터 변경 내용을 merge 하기

### 토픽 브랜치에서 일하기
메인 브랜치에 통합하기 전에 임시로 토픽 브랜치를 하나 만들고 거기에 통합해보고 다시 메인 브랜치에 통합하는 것이 좋다.
master 브랜치에서 새 토픽 브랜치 만든다.
`$ git checout -b <토픽 브랜치 이름> master`
새로 생성한 토픽 브랜치에 patch를 적용해보고 적용한 내용을 다시 Long-Running 브랜치로 merge한다.

### 이메일로 받은 patch 적용하기
* patch를 적용하는 방법
1. git apply
2. git am

#### Apply 명령 사용
git diff 명령으로 만든 Patch 파일을 적용할 때에는 git apply 명령을 사용한다.
`$ git apply <패치이름>.patch`

Patch 파일 내용에 따라 현재 디렉토리의 파일들을 변경한다.
위 명령은 `patch -p1` 명령과 거의 같지만, 훨씬 더 꼼꼼하게 비교한다. git diff로 생성한 patch파일에 파일을 추가하거나, 파일을 삭제하고, 파일의 이름을 변경하는 내용이 들어 있으면 그대로 적용된다.(patch 명령으로는 이런 일들을 수행할 수 없음)

git apply는 **모두 적용, 아니면 모두 취소**모델을 사용해서 patch를 적용하는데 실패하면 patch를 적용하기 이전 상태로 전부 되돌려 놓는다.
(patch 명령은 중간에 실패하면 그 상태로 중단된다.)

git apply는 자동으로  커밋해주지 않는다.

실제로 patch를 적용해보기 전에 patch가 잘 적용되는지 시험해보기
`$ git apply --check <패치이름>.patch`

#### AM 명령을 사용하는 방법
git format-patch 명령을 사용하면 관리자의 작업이 훨씬 쉬워진다. format-patch 명령으로 만든 patch 파일은 기여자의 정보와 커밋 정보가 포함되어 있기 때문이다.

**format-patch** 명령으로 생성한 patch 파일은 git am 명령으로 적용한다.
git am은 메일 여러 통이 들어있는 mbox 파일을 읽어서 patch 한다.
mbox 파일은 간단한 텍스트 파일이다.
```
From 330090432754092d704da8e76ca5c05c198e71a8 Mon Sep 17 00:00:00
From: Jessica Smith <jessica@example.com>
Date: Sun, 6 Apr 2008 10:17:23 -0700
Subject: [PATCH 1/2] add limit to log function
Limit log functionality to the first 20

```

사용하는 메일 클라이언트가 여러 메일을 하나의 mbox 파일로 저장할 수 있다면 메일 여러 개를 한 번에 patch 할 수 있다.

이메일로 받은 것이 아니라 format-patch 명령으로 만든 이슈 트래킹 시스템 같은데 올라온 파일이라면 먼저 내려받고서 git am 명령으로 patch 한다.

`$git am <패치 이름>.patch`
patch가 성공하면 자동으로 새로운 커밋이 하나 만들어진다.
이메일의 from과 date에서 저자 정보가, 이메일의 제목과 메시지에서 커밋 메시지가 추출돼 사용된다.
```
   $ git log --pretty=fuller -1
   commit 6c5e70b984a60b3cecd395edd5b48a7575bf58e0
   Author:     Jessica Smith <jessica@example.com>
	 AuthorDate: Sun Apr 6 10:17:23 2008 -0700
   Commit:     Scott Chacon <schacon@gmail.com>
   CommitDate: Thu Apr 9 09:19:06 2009 -0700
      add limit to log function
      Limit log functionality to the first 20
```
Commit 부분의 커밋 정보는 누가 언제 Patch를 했는지 알려 준다.
Author 정보는 실제로 누가 언제 Patch 파일을 만들었는지 알려 준다.

Patch에 실패하면 Patch를 중단하고 사용자에게 어떻게 처리할 지 물어온다.  충돌이 일어난 파일에는 충돌 표시를 해 놓는다.
충돌한 파일을 열어서 충돌 부분을 수정하고 나서 Staging Area에 추가하고 `$ git am --resolved ` 명령을 입력한다.

* 3-way-patch
`$ git am -3 <패치이름>.patch`

* 대화형 방식 사용하기
하나의 mbox 파일에 들어 있는 여러 patch를 적용할 때
각 patch를 적용할 때마다 묻는다.
`$ git am -3 -i mbox`

### 리모트 브랜치로 부터 통합하기
프로젝트 기여자가 저장소의 url과 변경 내용을 메일로 보내왔다면 URL을 리모트 저장소로 등록하고 merge 할 수 있다.

heidi씨가 ruby-client 브랜치에 기능을 만들었다고 메일을 보내왔다.
이 리모트 브랜치를 등록하고 checkout 해서 테스트한다.
```shell
$ git remote add heidi git://github.com/heidi/myproject.git
$ git fetch heidi
$ git checkout -b rubyclient heidi/ruby-client
```
이 후에 heidi씨가 또 다른 기능을 추가한 브랜치를 보내오면 이미 저장소를 등록했기 때문에 간단히 fetch 하고 checkout할 수 있다.

다른 개발자들과 함께 개발할 때는 이 방식이 가장 사용하기 좋다.
리모트 저장소로 등록하면 커밋의 히스토리도 알 수 있다.
merge 할 때 어디서부터 커밋이 가랄졌는지 알 수 있기 때문에  -3 옵션을 주지 않아도 자동으로 3-way-merge가 적용된다.

리모트 저장소로 등록하지 않아도 merge 할 수 있다.
`$ git pusll https://github.com/heidiyun/project`

#### 무슨 내용인지 확인하기.
기여물이 포함된 토픽 브랜치가 있으니 이제 그 기여물을 merge 할지 말지 결정해야 한다.
메인 브랜치에 merge할 때 필요한 명령어를 살펴보자.

* 지금 작업하는 브랜치에서 master 브랜치에 속하지 않는 커밋만 살펴 본다.
`$ git log contrib --not master`

master 브랜치와 토픽 브랜치의 공통 조상인 커밋을 찾아서 토픽 브랜치가 현재 가리키는 커밋과 비교해야 한다.
```shell
$ git merge-base contrib master
> 36c7dba2c95e6bbb78dfa822519ecfec6e1ca649 
$ git diff 36c7db
```
위의 명령을 짧게 줄이면
`$ git diff master...contrib`

#### 기여물 통합하기

* merge 하는 workflow
바로 master 브랜치에 merge 하는 것이 가자 ㅇ간단하다.
master 브랜치가 안전한 코드라고 가정한다.
토픽 브랜치를 검증하고 master 브랜치로 merge 할 때마다 토픽 브랜치를 삭제한다.

* 대규모 merge workflow
git을 개발하는 프로젝트는 Long-Running 브랜치를 4개 운영한다.
master, next, pu, maint 이다.
maint는 마지막으로 릴리즈한 버전을 지원하는 브랜치다.
기여자가 새로운 기능을 제안하면 관리자는 자신의 저장소에 토픽 브랜치를 만들어 관리한다.
안정화되면 next로 merge하고 저장소에 push 한다.
토픽 브랜치가 좀 더 개선돼야 하면 next가 아니라 pu에 merge 한다.
그 후에 안정되면 next로 옮긴다.
토픽 브랜치가  master 브랜치로 merge되면 저장소에서 삭제한다.

* rebase와 cherry-pick workflow
히스토리를 평평하게 관리하려고 merge보다 rebase 나 cherry-pick을 더 선호하기도 한다.

토픽 브랜치에서 작업을 마친 후 master에 통합할 때 master 브랜치를 기반으로 rebase한다. 그리고 master 브랜치를 fast-forward 시킨다.

한 브랜치에서 다른 브랜치로 작업한 내용을 옮기는 또 다른 방식으로cherry-pick 이 있다.
Git의 Cherry-pick은 커밋 하나만 reabse 하는 것이다. 커밋 하나로 patch  내용을 만들어 현재 브랜치에 적용하는 것이다.
토픽 브랜치에 있는 커밋중에서 하나만 고르거나 토픽 브랜치에 커밋이 하나밖에 없을 때  rebase보다 유용하다.
`git cherry-pick`

* Rerere (reuse recorded resolution,  충돌 해결방법 재사용)
수시로 merge나 rebase를 한다거나 오랫동안 유지되는 토픽브랜치를 
쓰는 사람에게 유용하다.
수작업으로 충돌 해결하던 것을 쉽게 해준다.
rerere 기능이 활성화 되어있으면 merge가 성공할 때마다 그 이전과 이후 상태를 저장해둔다. 나중에 충돌이 발생하면 비슷한 상황에서 merge가 성공한 적이 있었는지 찾아보고 해결이 가능하다면 자동으로 해결한다.
rerere 기능 설정하기
`$ git config --globa rerere.enabled true`

### 릴리즈 버전에 태그 달기
`$ git tag -s v1.4 -m 'my signed 1.5 tag'`
 태그에 서명하면 서명에 사용된 PGP 공개키도 배포해야 한다.
Git 개발 프로젝트는 관리자의 PGP 공개키를 Blob 형식으로 Git 저장소에 배포한다.

`$ gpg --list-keys` 어떤 PGP 공개키를 포함할지 확인한다.
`$ gpg -a --export <ID> | git hash-object -w --stdin`
git hash-object 명령으로 공개키를 바로 git 저장소에 넣을 수 있다.  git 저장소 안에 Blob 형식으로 공개키를 저장해주고 그 Blob의 SHA-1 값을 반환한다.

이 해쉬 값으로 PGP 공개키를 가리키는 태그를 만든다.
`$ git tag -a maintainer-pgp-pub <해쉬값>`

`$ git show <tag>` 서명된 태그 확인하기

### 빌드 넘버 만들기
`$ git describe master v1.6.2-rc1-20-g8c5b85c `
가장 가까운 태그의 이름과 태그에서 얼마나 더 커밋이 쌓였는지 해당 커밋의 sha-1 값을 조금 가져다가 이름을 만든다.

git describe 명령은 -a 나 -s 옵션을 주고 만든 Annotated 태그가 필요하다.
릴리즈 태그는 git describe 명령으로 만든다.

### 릴리즈 준비하기
먼저 Git을 사용하지 않는 사람을 위해 소스코드 스냅샷을 압축한다.
`$ git archive master --prefix='project/'|gzip > 'git decribe master'.tar.gz`
압축한 스냅샷 파일은 웹사이트나 이메일로 사람들에게 배포할 수 있다.

### shortlog 보기
이메일로 프로젝트 변경 사항을 사람들에게 알려야 할 때, git shortlog 명령으로 지난 릴리즈 이후의 변경 사항 목록을 쉽게 얻어올 수 있다.
`$ git shortlog --no-mergest master --not v1.0.1`
v1.0.1 이후의 커밋을 요약해준다.

















#progit