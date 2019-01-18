# 2. Git의 기초
## Git 저장소 만들기
### 기존 디렉토리를 Git 저장소로 만들기
프로젝트의 디렉토리로 이동해서 `$ git init` 명령을 실행한다.
. git이라는 하위 디렉토리를 생성한다.
Git이 파일을 관리하게 하려면 저장소에 파일을 추가하고 커밋해야 한다.
```shell
$ git add <file>
$ git commit -m 'fisrt commit'
```

### 기존 저장소를 Clone 하기
`$ git clone <저장소 주소>`
clone 명령은 디렉토리를 만들고 그 안에 .git 디렉토리를 만든다.
저장소의 데이터를 모두 가져와서 자동으로 가장 최신 버전을 사용할 수 있게 만들어준다.
`$ git clone <저장소 주소> <지정하고 싶은 저장소 이름>`

### 수정하고 저장소에 저장하기
워킹 디렉토리의 모든 파일은 Tracked / Untracked로 나뉜다.
**Tracked**파일은 이미 커밋을 한번 했던 파일이다.
* Unmodified
* Modified

#### 파일의 상태 확인하기
`$ git status`

#### 파일을 새로 추적하기
`$ git add <파일명>`
Untracked -> Tracked로 바뀐다
commit에 추가될 Staged 상태가 된다.

#### Modified 상태의 파일을 Stage 하기
 git add 명령을 수행하고 staged 상태의 파일을 수정하면 unstaged 상태의 파일 추가된다. 꼭 git add를 한 번 더 해주자.

####  파일 상태를 짤막하게 확인하기
```shell
$ git status -s
M README -> Staged & Modified
MM Rakefile -> Staged & UnStaged 
A lib/git.rb -> Staged & New
?? LICENSE.txt -> UnTracked
```

#### 파일 무시하기
Git으로 관리할 필요가 없는 파일은 .gitignore에 추가하자
```shell
$ cat .gitignore
*.[oa].   -> 확장자가 .o 이거나 .a 인 파일을 무시
*~			-> ~로 끝나는 파일을 모두 무시
```

**규칙**
* 빈 라인이나, \#으로 시작하는 라인은 무시한다.
* 표준 Glob 패턴을 사용한다. (정규표현식을 단순하게 만든것)
* 슬래시(/)로 시작하면 하위 디렉토리에 적용되지 않는다.
* 디렉토리는 슬래스(/)를 끝에 사용하는 것으로 표현한다.
* 느낌표(!)로 시작하는 패턴의 파일은 무시하지 않는다.

#### Staged와 UnStaged 상태의 변경 내용을 보기
파일의 어떤 부분이 변경됐는지 알고 싶을 때.
`$ git diff`
워킹 디렉토리의 파일 내용과 Staging Area에 있는 파일 내용을 비교합니다.
_Unstaged 상태의 파일의 수정 내용만 보여줍니다._

Staging Area에 넣은 파일의 변경 부분을 보고 싶다면,
`$ git diff --staged`

#### 변경 사항 커밋하기
`$ git commit`
커밋 메시지가 자동으로 생성된다.
첫 라인은 비어있고 둘째 라인부터 git status 명령의 결과가 출력된다.
`$ git commit -v`
git diff 명령의 결과 또한 함께 출력된다.
`$ git commit -m 'message'`
커밋 메시지 지정.

```shell
$ git commit -m "Story 182: Fix benchmarks for speed" [master 463dc4f] Story 182: Fix benchmarks for speed 
 2 files changed, 2 insertions(+)
 create mode 100644 README
```
master 브랜치에 커밋했고 체크섬은 463dc4f이다
수정한 파일은 2개, 추가 된 라인 2.

#### Staging Area 생략하기
```shell
$ git commit -a -m 'message'
```
-a 옵션을 추가하면 Tracked 상태의 파일을 자동으로 Staging Area에 넣어줍니다.  git add 명령을 생략할 수 있습니다.

#### 파일 삭제하기
* Git 없이 파일 삭제하기
```shell
$ rm grit.gemspec
$ git status 
On branch master
  Changes not staged for commit:
    (use "git add/rm <file>..." to update what will be committed)
    (use "git checkout -- <file>..." to discard changes in working directory)
      deleted:    grit.gemspec
  no changes added to commit (use "git add" and/or "git commit -a")

```
삭제된 파일은 Unstaged 상태가 된다.

* Git 으로 파일 삭제하기
```shell
$ git rm <파일명>
// Tracked 상태의 파일에만 이 명령을 쓸 수 있습니다.
// 워킹 디렉토리에 있는 파일을 삭제하기 때문에 실제 폴더에서도 지워집니다.

$ git rm grit.gemspec
$ git status
On branch master Changes to be committed: 
  (use "git reset HEAD <file>..." to unstage)
    deleted:    grit.gemspec

```
삭제된 파일은 Staged 상태가 됩니다.

삭제된 파일을 커밋하면 Git은 이 파일을 더이상 추적하지 않습니다.
이미 파일을 수정했거나(Modified) Staging Area에 add 했다면 -f 옵션으로 강제 삭제해야 합니다.

	* Staging Area에서만 삭제하고 워킹 디렉토리에 있는 파일은 지우지 않고 남겨두고 싶을때는 아래 명령을 사용하자. (.gitignore에 파일을 미쳐 넣지 못했을 때)
```shell
$ git rm --cached <파일명>
```

	* 여러개의 파일/디렉토리를 한번에 삭제하기
	file-glob 패턴 사용하기.
```shell
$ git rm log/\*.log
log 디렉토리에 있는 .log 파일을 모두 삭제한다.
$ git rm \*~
~로 끝나는 모든 파일을 삭제한다.
```

#### 파일 이름 변경하기
Git은 다른 VCS 시스템과 달리 파일 이름의 벼경이나 파일의 이동을 명시적으로 관리하지 않습니다.
파일 이름이 변경되어도 별도의 정보를 저장하지 않습니다.

```shell
$ git mv <원래 파일명> <바꾸자하는 파일명>
$ git status
// 자동으로 바꾼 파일명으로 등록되어 있다.

// git mv 명령은 아래 명령과 같습니다.
$ mv README.md README
$ git rm README.md
$ git add README
```

#### 커밋 히스토리 조회하기
```shell
$ git log
$ git log -p
// 각 커밋의 diff 결과를 보여준다.
$ git log -2
// 최근 2개의 커밋만 보여준다.
$ git log -2 -p
$ git log -stat
// 각 커밋의 통계 정보를 보여줍니다.
// 얼마나 많은 파일이 변경되었는지, 얼마나 많은 라인이 추가 되었는지.
$ git log --pretty=oneline
// short, full, fuller
// 로그 결과를 지정한 포맷으로 보여준다.
$ git log --pretty=format:"<아래 표 지정된 형식>"
$ git log --pretty=format:"<아래 표 지정된 형식>" --graph
```

![](ch2-Git%E1%84%80%E1%85%B5%E1%84%8E%E1%85%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-18%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%203.05.26.png)
![image](https://user-images.githubusercontent.com/38517815/51368412-9e520180-1b32-11e9-8816-9f6f26003fa7.png)
저자는 작업을 수행한 원작자이고, 커밋터는 마지막으로 이 작업을 커밋한 사람이다.

![](ch2-Git%E1%84%80%E1%85%B5%E1%84%8E%E1%85%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-18%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%203.08.20.png)
![](ch2-Git%E1%84%80%E1%85%B5%E1%84%8E%E1%85%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-18%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%203.09.18.png)
![image](https://user-images.githubusercontent.com/38517815/51368638-af4f4280-1b33-11e9-9f7e-f82fa90bef8b.png)
![image](https://user-images.githubusercontent.com/38517815/51368650-ba09d780-1b33-11e9-89e6-8c908a99afc6.png)

#### 조회 제한조건
```shell
$ git log --since=2.weeks
// 2주치 커밋만 보여줌.
$ git log -S<키워드>
// 키워드에 만족하는 커밋만 보여줌
```
![](ch2-Git%E1%84%80%E1%85%B5%E1%84%8E%E1%85%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-18%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%203.15.57.png)
![image](https://user-images.githubusercontent.com/38517815/51368743-00f7cd00-1b34-11e9-9395-658ddd1f131f.png)

#### 되돌리기
한 번 되돌리기를 수행하면 복구할 수 없다.
```shell
$ git commit 
$ git add forgotten_file
$ git commit --amend
// 앞서 한 커밋과 뒤에 한 커밋은 동일하다.
```

####  파일 상태를 Unstaged로 변경하기
```shell
$ git reset HEAD <파일명>
```

#### Modified 파일 되돌리기
```shell
$ git checkout -- <파일명>
```

### 리모트 저장소
리모트 저장소를 관리할 줄 알아야 다른 사람과 협업할 수 있다.
#### 리모트 저장소 확인하기
```shell
$ git remote
$ git remote -v 
// 단축이름과 URL 출력
```
#### 리모트 저장소 추가하기
```shell
$ git remote add <단축이름> <url>
```
#### 리모트 저장소를 pull 하거나 fetch 하기
* 리모트 저장소에서 데이터 가져오기
```shell
$ git fetch <remote-name>
```
로컬에는 없지만 리모트 저장소에 있는 데이터를 모두 가져온다. 그러면 리모트 저장소의 모든 브랜치를 로컬에서 접근할 수 있어서 언제든지 Merge를 하거나 내용을 살펴볼 수 있다.

* 저장소를 clone 하면 자동으로 리모트 저장소를 ‘origin’ 이름으로 추가한다. 
git fetch origin을 실행하면 clone한 이후에 수정된 것을 모두 가져온다. 
* git fetch는 리모트 저장소의 모든 데이터를 로컬로 가져오지만, 자동으로 merge하지는 않는다. 
* git pull 명령을 이용하면 데이터를 가져오고 자동으로 로컬 브랜칭돠 merge 한다.

#### 리모트 저장소에 push하기
```shell
$ git push <리모트 저장소 이름> <브랜치 이름>
$ git push origin master
// mater 브랜치를 origin 서버에 push 한다.
```

#### 리모트 저장소 살펴보기
```shell
$ git remote show <단축이름>
```

#### 리모트 저장소 이름 바꾸기
```shell
$ git remote rename <기존단축이름> <새로운단축이름>
```
#### 리모트 저장소 삭제하기
```shell
$ git remote rm <단축이름>
```

### 태그
#### 태그 조회하기
```shell
$ git tag
// 알파벳 순서대로 보여줌
$ git tag -l <키워드>
```

#### 태그 붙이기
* Annotated 태그
Git  데이터베이스에 태그를 만든 사람의 이름, 이메일, 태그 날짜, 태그 메시지를 저장한다.
GPG서명도 할 수 있다.
```shell
$ git tag -a <태그이름> -m <태그 메시지>
```
* Lightweight 태그
브랜치와 비슷하지만 가리키는 지점이 최신 커밋으로 이동하지는 않는다. 특정 커밋에 대한 포인터
```shell
$ git tag <태그이름>
```

#### 태그 보기
```shell
$ git show <태그이름>
```

#### 나중에 태그하기
예전 커밋에 태그 붙이기
```shell
$ git tag -a <태그이름> <커밋 해시>
```

#### 태그 공유하기
git push 명령은 자동으로 리모트 서버에 태그를 전송하지 않는다.
태그를 만들었으면 서버에 별로도 push 해야 한다.
```shell
$ git push <서버이름> <태그이름>

$ git push <서버이름> --tags
// 서버에 없는 모든 태그를 푸시한다.
```

#### 태그를 checkout 하기
 태그는 브랜치와 달리 가리키는 커밋을 바꿀 수 없어서 checkout해서 쓸 수 없다.
태그가 가리키는 특정 커밋 기반의 브랜치를 만들어 작업하려면 새로운 브랜치를 생성해야 한다.
```shell
$ git checkout -b <브랜치이름> <태그이름>
```

### Git Alias
git  명령을 전부 입력하는게 번거롭다면 git config를 이용하여 각 명령의 alias를 쉽게 만들어보자.
```shell
$ git config --global alias.co checkout
$ git config --global alias.ci commit

/* 사용 */
$ git ci -m "commit message'
```

이미 있는 명령어를 새로운 명령으로 만들어보자.
```shell
$ git config --global alias.unstage 'reset HEAD <파일명>'

/* 사용 */
$ git unstage <파일명>
```

외부 명령어 사용하기
느낌표(!) 를 앞에 추가하자
```shell
$ git config --global alias.visual '!gitk'
```



#progit