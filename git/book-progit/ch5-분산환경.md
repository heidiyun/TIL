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















#progit