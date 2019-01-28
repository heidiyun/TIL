# 3. Git 브랜치
> 모든 코드를 복사해서 원래 코드와는 독립적으로 개발을 진행할 수 있게 해주는 것.  

## 브랜치란 무엇인가
Git은 commit이 이루어지면  commit object를 저장한다. (데이터 스냅샷의 포인터, 커밋메시지 등의 메타테이터 등등..) 
이전 커밋 포인터가 있어 현재 커밋이 무엇을 기준으로 바뀌었는지 알 수 있다. 
브랜치를 합친 Merge commit 같은 경우에는 이전 커밋 포인터가 여러개 있다.

* 예시
파일이 3개 있는 디렉토리가 하나 있다.
파일을 Staging Area에 저장하고 커밋해보자.
파일을  Stage하면 Git 저장소에 파일을 저장(Blob)하고 Staging Area에 해당 파일의 체크섬을 저장한다.
커밋하면 루트 디렉토리와 각 하위 디렉토리의 트리 개체를 체크섬과 함께 저장소에 저장한다.
그 다음 커밋 개체를 만들고 메타데이터와 루트 디렉토리 트리 개체를 가르키는 포인터 정보를 커밋 개체에 넣어 저장한다.
Git 저장소에는 다섯 개의 데이터 개체가 생긴다.
각 파일에 대한 Blob 세 개, 파일과 디렉토리 구조가 들어있는 트리 개체 하나, 메타데이터와 루트 트리를 가리키는 포인터가 담긴 커밋 개체
파일을 수정하고 커밋하면 이전 커밋이 무엇인지도 저장한다.

Git 브랜치는 커밋 사이를 이동할 수 있는 일종의 포인터 개념이다.
기본적으로 Git은  master 브랜치를 만든다.
최초로 커밋하면 Git은 master라는 이름의 브랜치를 만든다.
커밋을 만들 때 마다 브랜치가 자동으로 가장 마지막 커밋을 가리키게 한다.

### 새 브랜치 생성하기
 `$ git branch test1`
새로 만든 브랜치도 지금 작업하고 있던 마지막 커밋을 가리킨다.

**현재 작업중인 브랜치가 무엇인지 Git은 어떻게 알 수 있을까?**
Git은 HEAD라는 특수한 포인터를 가지고 있다.
이 포인터는 현재 작업중인 로컬 브랜치를 가르킨다.
브랜치를 새로 만들어도 Git의  HEAD 포인터가 master 브랜치를 가리키고 있다.

**git branch 명령은 새로운 브랜치를 생성하기만 할 뿐!!**

`$ git log --decorate`
—decorate 옵션을 사용하면 브랜치가 어떤 커밋을 가리키는지 확인할 수 있다.

### 브랜치 이동하기
>  `$ git checkout [브랜치이름]`  


`$ git checkout test1`
HEAD 포인터가 test1으로 이동한다.
```
$ git add ./
$ git commit
```
새로운 커밋은 test1 브랜치만 가리키게 된다.
이전의 master 브랜치는 이전 커밋을 가리키고 있다. 
`$ git checkout master`
HEAD 포인터가 master를 가리킨다.
master 브랜치는 이전 커밋을 가리키고 있으므로 워킹 디렉토리의 파일도 다시 그 시점으로 돌아가게 된다.
```shell
// 파일 수정 후
$ git commit 
```
이 커밋부터는 프로젝트의 히스토리가 분리된다.
test1의 커밋의 내용과 합치고 싶다면 merge하자!

**Git 브랜치는 커밋을 가리키는  체크섬 파일에 불과하기 때문에 생성, 수정, 삭제가 쉽다.**
다른 버전 관리 도구는 프로젝트를 통째로 복사해야하기 때문에 시간이 오래걸릴 수 있다.

## 브랜치와 Merge의 기초
### 브랜치의 기초
* 브랜치 생성과 checkout을 한번에
`$ git checkout -b [브랜치 이름]`
checkout 명령에 -b 옵션을 추가합니다.

* 브랜치를 옮길 때 충돌나면 브랜치를 변경할 수 없으므로 commit!
더 자세한 것은 stashing, cleaning에서 다룰 것이다.

* 현재 작업 브랜치에 다른 브랜치의 작업 내용 합치기
`$ git merge [브랜치 이름]`
	* fast-forward : 단순히 최신의 커밋을 가리키는 것으로 merge 하는 방법

* 브랜치 삭제하기
`$ git branch -d [브랜치 이름]`

### Merge의 기초
커밋 히스토리가 분리되었을 때 merge 작업을 수행하게 되면
`Merge made by the 'recursive' strategty` 문구가 뜬다.

각 브랜치를 가리키는 커밋 두 개와 공통 커밋 하나를 사용하여 3-way-Merge를 수행한다.
단순히 브랜치 포인터를 최신 커밋으로 옮기는 게 아니라 merge 결과를 별도의 커밋으로 만들고 현재 작업 브랜치가 그 커밋을 가리키도록 한다.

![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%204.55.07.png)

Git은 Merge 하는데 필요한 최적의 공통 커밋을 자동으로 찾는다.
CSV나 Subversion 같은 버전 관리 시스템은 개발자가 직접 공통 커밋찾아서 Merge 해야 한다.

### 충돌의 기초
* 3-way-Merge가 실패하는 경우
두 브랜치에서 같은 파일의 한 부분을 동시에 수정하고 Merge 하면 실패한다.
**git status 명령을 사용하여 Merge 충돌이 어떤 파일에서 일어났는지 알 수 있다.**
충돌이 일어난 파일은 unmerged 상태로 표시된다.

* Merge 도구
`$ git mergetool`
Merge 도구를 종료하면 Git은 Merge가 잘 됐는지 물어본다.
잘 되었다고 입력하면  자동으로 git add가 수행되어 해당 파일이  Staging Area에 저장된다.
그리고 git commit을 진행하면 된다.!

### 브랜치 관리
* 브랜치 목록보기
`$ git branch`
‘*’ 기호가 붙어있는 브랜치는 현재 작업 브랜치를 나타낸다.
* 각 브랜치 별 마지막 커밋 보기
`$ git branch -v`
* merge / nomerge branch
`$ git branch --merged`
merge가 진행된 브랜치 목록을 보여준다.
삭제한 브랜치도 목록에 나온다.
`$ git branch --no-merged`
merge 하지 않은 브랜치 목록을 보여준다.
**아직 merge하지 않은 커밋을 담고 있는 브랜치는  삭제할 수 없다.**
* merge하지 않은 브랜치 강제 삭제
`$ git branch -D [브랜치 이름]`

## 브랜치 Workflow
### Long-Running 브랜치
> 프로젝트의 규모가 크고 복잡한 프로젝트일수록 유용하다.  
배포했거나 배포할 코드(안정 상태의 코드)만 merge해서 가지고 있는 안정 브랜치
안정화되지 않고 개발 중에 있어 계속 커밋 히스토리를 만들고 있는 개발 브랜치 
개발 브랜치가 안정화가 되면 안정 브랜치에 merge 시킨다.


### 토픽 브랜치
> 프로젝트 크기에 상관없이 유용하다.  
한 가지 주제나 작업을 위한 짧은 호흡의 브랜치 (예: 오류 수정)

## 리모트 브랜치
리모트 Refs는 리모트 저장소에 있는 포인터인 레퍼런스다.
리모트 저장소에 있는 브랜치, 태그 등을 의미한다.
* 리모트 Refs 목록 보기
`$ git ls-remote`
* 리모트 브랜치 목록 보기
`$ git remote show`
`$ git retmoe`
* 리모트 트래킹 브랜치
리모트 브랜치를 추적하는 브랜치
이 브랜치는 로컬에 있지만 움직일 수 없다.
리모트 서버에 연결할 때마다 리모트 브랜치에 따라서 자동으로 움직인다.
리모트 저장소에 마지막으로 연결했던 순간에 브랜치가 무슨 커밋을 가리키고 있었는지 나타내는 일종의 북마크라고 생각하면 쉽다.
* 리모트 브랜치 이름
(remote 저장소 이름)/(branch 이름)
origin/master

![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%207.17.54.png)

로컬과 서버의 커밋 히스토리는 독립적이기 때문에 같이 협업하는 사람이 리모트 저장소에 push를 해도 origin/master 포인터는 그대로다.
리모트 서버로부터 저장소 정보를 동기화하려면 `$ git fetch origin`명령을 사용한다.
우선 ‘origin’ 서버의 주소 정보를 찾아서, 현재 로컬 저장소가 갖고 있지 않은 새로운 정보가 있으면 내려받고 로컬 저장소에 업데이트 한다.
그리고 origin/master 포인터의 위치를 최신 커밋으로 이동시킨다.

![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%207.20.50.png)

#### 리모트 저장소 추가하여 사용하기
`$ git remote add <단축이름> <url>`
git fetch 명령을 사용하여  새로 추가한 서버의 데이터를 내려받는다. 

![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%207.24.05.png)

### push 하기
로컬 브랜치를 서버로 전송하려면 쓰기 권한이 있는 리모트 저장소에 push 해야 한다.
`$ git push <remote 저장소> <branch 이름>`

```shell
$ git push origin serverfix
// Git은 serverfix라는 이름을 refs/heads/serverfix:refs/heads/serverfix로 확장한다.
// serverfix라는 로컬 브랜치를 서버로 push하는데 리모트의 serverfix 브랜치로 업데이트한다는 뜻이다.

$ git push origin serverfix:serverfix
// 위와 같은 명령
// 로컬의 serverfix 브랜치를 리모트 저장소의 serverfix 브랜치로 push해줘.
// 로컬 브랜치의 이름과 리모트 서버의 브랜치 이름이 다르 때 필요하다.
// $ git push origin serverfix:anothername
```

누군가 저장소를 Fetch하고 나서 서버에 있는 serverfix브랜치에 접근할 때 origin/serverfix라는 이름으로 접근할 수 있다.

**Fetch 명령으로 리모트 트래킹 브랜치를 내려받는다고 해서 로컬 저장소에 수정할 수 있는 브랜치가 새로 생기는 것이 아니다. 그저 origin/serverfix 브랜치 포인터가 생기는 것이다.**

새로 받은 브랜치의 내용을 merge 하려면 `$ git merge origin/serverfix`명령을 사용한다.
merge하지 않고 리모트 트래킹 브랜치에서 시작하는 새 브랜치를 만들려면 `$ git checkout -b serverfix origin/serverfix`명령을 사용한다. 
: origin/serverfix에서 시작하고 수정할 수 있는 serverfix라는 로컬 브랜치가 만들어진다.

### 브랜치 추적
리모트 트래킹 브랜치를 로컬 브랜치로 checkout하면 자동으로 **트래킹 브랜치(Upstream branch)**가 만들어진다. 
트래킹 브랜치는 리모트 브랜치와 직접적인 연결고리가 있는 로컬 브랜치이다.
트래킹 브랜치에서 git pull 명령을 사용하면 리모트 저장소로부터 데이터를 내려받아 연결된 리모트 브랜치와 자동으로 merge 한다.

* 트래킹 브랜치 만들기
origin이 아닌 다른 리모트 가능
master 브랜치 아닌 다른 브랜치 추적 가능
```shell
$ git checkout --track origin/serverfix
// 리모트 브랜치이름과 같은 이름으로 브랜치 자동 생성
$ git checkout -b sf origin/serverfix
// 리모트 브랜치와 다른 이름으로 브랜치를 만들때 사용
$ git branch -u origin/serverfix
$ git branch --set-upstream-to origin/serverfix
// 이미 로컬에 존재하는 브랜치가 리모트의 특정 브랜치 추적하게 하기.
```

  추적 브랜치를 설정했다면 추적 브랜치를 @{upstream}이나 @{u}로 대체하여 사용가능하다.
```shell
$ git merge origin/master
$ git merge @{u}
```

	* 추적 브랜치가 어떻게 설정되어있는지 확인하기
	`$ git branch -vv`
	로컬 브랜치 목록과 로컬 브랜치가 추적하고 있는 리모트 브랜치를함게 보여준다.
	ahead : 로컬 브랜치에서 서버로 보내지 않은 커밋
	behind : 리모트 브랜치에서 로컬 브랜치로 merge 하지 않은 커밋

### pull 하기
fetch 명령과 merge 명령을 수행해준다.

### 리모트 브랜치 삭제
`$ git push <리모트저장소> --delete <리모트 브랜치 이름>`
`$ git push origin --delete serverfix`

## Rebase 하기
### Rebase 기초
커밋 히스토리가 분리된 두 개의 브랜치 합치기 
1. merge
2. rebase
```shell
$ git checkout <브랜치 이름>
$ git rebase <rebase할 브랜치 이름>
```

![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%208.00.37.png)
```shell
$ git checkout experiment
$ git rebase master
```
c3에서 변경된 사항을 patch로 만들고 이를 다시 c4에 적용한다. (rebase)

두 브랜치가 나뉘기 전인 공통 커밋으로 이동하고 나서 그 커밋부터 지금 checkout한 브랜치가 가리키는 커밋까지 diff를 차례로 만들어 어딘가에 임시로 저장한다. rebase 할 브랜치가 합칠 브랜치가 가리키는 커밋을 가리키게 하고 아까 저장해 놓았던 변경사항을 차례대로 적용한다.

![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%208.03.14.png)

그리고 나서 master 브랜치를 Fast-forward 시킨다.
```shell
$ git checkout master
$ git merge experiment
```

![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%208.04.13.png)

**rebase가 merge 보다 좀 더 깨끗한 히스토리를 만든다.**
히스토리가 분리되어 있어도 rebase를 거치면 하나의 히스토리만 남는다.

rebase와 merge의 최종 결과물은 같다.
rebase는 브랜치의 변경사항을 순서대로 다른 브래치에 적용하며 합치고, merge는 두 브랜치의 최종결과만을 가지고 합친다.

### Rebase 활용
![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%208.08.05.png)

master 브랜치에 client 브랜치만 합치고 싶을때.
`$ git rebase --onto master server client`
위 명령은 client 브랜치를 checkout 하고 server와 client의 공통조상 이후의 patch를 만들어 master에 적용한다. 
![](ch3-Git-branch/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-28%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%208.09.25.png)

```shell
$ git checkout master
$ git merge client
```

server 브랜치를 합치고 싶으면.
```shell
$ git reabse <basetbranch> <topic-branch>
$ git rebase msater server
```
 토픽 브랜치를 checkout 하고 베이스 브랜치에 rebase 한다.

### Rebase 위험성
**이미 공개 저장소에 push한 커밋을 rebase 하지 말자**
공개 저장소에 push한 커밋을 내가 pull하여 사용하고 있다고 생각하자.
push한 동료가 rebase로 커밋을 수정한 후 해당 push를 덮어 썼다고 생각해보자. 내가 그 코드를  pull 받았다고 생각하면, 이미 동료는 rebase로 없앤 코드를 나는 의존하고 있다. 커밋 히스토리가 꼬이게 되는 것이다.

#### Rebase 한 것을 다시  Rebase 하기
위와 같은 상황이 발생했을 때 유용한 기능이다.
덮어쓴 내용이 무엇인지 알아야 한다.
커밋 체크섬 외외도 Git은 커밋에 patch 할 내용으로 체크섬을 한번 더 구한다. ‘patch-id’
덮어쓴 커밋을 받아서 그 커밋을 기준으로  rebase 하면 된다.
```shell
$ git rebase teamone/master
$ git pull --rebase

$ git fetch
$ git rebase teamone/master

// git pull 명령에 자동으로 --rebase옵션 적용
$ git config --global pull.rebase true
```


#progit