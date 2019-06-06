# 기본 문법 - 변수, Class, Dictionary
## 변수 선언
1. let : 변수 (코틀린의 var)
2. const :  한 번 초기화되면 바꿀 수 없음. (코틀린의 val)

## class
1. JavaScript
원래 js에서는 class가 없다. (지금은 있긴 있음.)
```javascript
function ClassTest() {
	
}

const a = new ClassTest();
```

2. TypeScript
```ts
class ClassTest {
	private name : String;
	public constructor() {
		this.name = "yun"
	}
}
```
*  필드를 선언했는데 constructor에서 초기화를 안해주면 에러난다.
	*  그냥 null이 아니다라고 명시시켜주고 싶으면 이름뒤에 (!)붙여주기
```ts
class ClassTest {
	private name!: String;
	public constructor() {
	}
}
// error가 안난다.
```
	* nullable을 표시할 때는 (?) 붙여주기.

### Class Import
calc.ts 파일이 있다.
```ts
export class Calc {
	public num: number = 10;
    public constructor() {
    
    }
}

export class Button {
	
}
// export가 없으면 다른 파일에서 import 할 수 없음.

// index.ts
import {Calc} from './calc';
// calc 파일의 모든 것이 import 되어서 선택해야 한다는 뜻.
import {Calc, Button} from './calc';

const c = new Calc();
c.num

import * as Calc from './calc'
Calc.Calc
Calc.Button


export default class Calc {
	
}
import Calc form './calc'
// {} 표기 안해도 됨. class Calc가 import된다.


```

* Util처럼 class의 Object 생성하지 않아도 되는 경우.
```ts
//JSON : JavaScript Object Notation
export default {
	send: () => {},
	host: 'www.naver.com'
}

```
## Dictionary 
```ts
const dic : {[key:string] : number} = {yun : 22};

```

#web