# typescript 시작하기
## typescript 사용하기
`$ npm install -g typescript`

	* -g 옵션은 현재 파일안에만 설치하는게 아니라 전체에 적용된다.
그래서 명령어를 사용할 수 있다. 

`$ npm init `
	*  package.json을 만든다. -> 현재 모듈의 의존성 및 명령어를 관리한다.


* npx : 내 패키지 내의 모듈에서 사용가능한 명령어 갖고옴
-g로 설치하지 않은 모듈에 대해서.

`$ tsc init`
	* tsconfig.json를 생성한다.

	* typescript 모듈과 root 모듈이 다를 경우에는 tsc에 필요한 경우에는 tsc 모듈에 설치해주어야 한다.

`$ npm install @types/lodash`
	* 모듈을 설치할 때 js 모듈일 경우 tsc에서 사용하려면 선언을 가지고 와야 한다.

### 작성
변환할 때는 $tsc 명령 실행 ->. transfileing을 하는 것.
그러면 변환되니 js 파일이 생성된다.
node는 module : umd 사용
실행될 때는 node [파일명]

## 


#web
