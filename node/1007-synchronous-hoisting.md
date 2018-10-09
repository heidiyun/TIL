# 1007 class - 동기/비동기,  Hoisting
> Node.js: 비동기 기반의 프레임워크  

## 동기(Synchronous) vs 비동기(Asynchronous)
foo() 함수는 A라는 값을 반환한다.
### 동기(Synchronous)
 A = foo()   
foo()라는 함수의 반환값을 특정 변수에 저장할 수 있다.
그리고 foo()함수의 연산이 끝날 때 까지 CPU는 다른일을 처리하지 않고 대기상태이다.
만약 foo()라는 함수의 연산이 끝나지않으면 CPU는 무한정 대기상태에 빠지게 된다.
비동기 방식보다 코드가 간단하고 직관적이다.

### 비동기(Asynchronous)
foo() 함수의 반환 시점을 알 수 없다.
foo() 함수 호출 후 CPU는 다른 이벤트를 처리한다.  (자원을 효율적으로 사용할 수 있다.)
단, 코드의 복잡도는 동기 방식보다 올라가게 됩니다.
언제 반환 될지 모르니 코드의 흐름을 제어하는 것이 포인트.
그래서 비동기 방식에서는 Callback을 사용합니다.
Callback을 사용하여 함수의 연산이 끝났을 때 호출해야 할 함수를 지정합니다.

```java
   afoo(onComplete(a) {
		//…
     });

// onComplete가 호출되는 시점이 afoo()함수의 연산이 끝나는 시점이다.
```

Callback문 안에 계속 다음에 해야 할 일을 정의해야 할 경우, Callback문을 중첩하면서 보일러 플레이트가 발생하고 Callback hell에 빠지게 될 수 있으므로 주의해야 합니다.
 (가독성도 떨어지게 됩니다.)
```javascript
const fs = require("fs");
fs.readFile('server.js', function (err, data) {
	if (err) throw err;
	console.log("data :" + data);
	
	fs.readFile('logger.js', function (err, data) {
		if (err) throw err;
		consle.log("data :" + data);
	});
});

```

그래서 Javascript에서는 Promise라는 개념을 도입했습니다.
```javascript
const fs = require("fs");

const readFile = function(path) {
    return new Promise(function(resolve, reject) {
        fs.readFile(path, (err, data) => {
            if (err) {
                rejcet(err);
            }
            resolve(data);
        });
    });
}

readFile("server.js").then((data) => {
    console.log("data : " + data);
});
```

**장점**
1.  여러개의 job을 묶어서 flow control 을 할 수 있고 error 처리를 묶을 수 있다.
2. 가독성이 올라갔다.

promise로 묶어 놓은 비동기 함수를 동기적으로 반환값을 받을 수 있는 키워드 
**Async function , await**
```javascript
const fs = require("fs");

const readFile = function(path) {
	return new Promise((resolve, reject) => {
		fs.readFile(path, (err, data) => {
			if (err) {
				reject(err);
			}

			resolve(data);
		});
	});
}

async function foo() {
    try {
    const data1 = await readFile("logger.js");
    console.log("data1: " + data1);
    const data2 = await readFile("server.js");
    console.log("data2: " + data2);
    } catch (err) {
        console.error(err);
    }
}

foo();

//------------------------------------------------
const result = await Promise.all(readFile("index.js"), readFile("server.js"));
	const [ data1, data2 ] = result;
```

## Javascript hoisting
>  변수 및 함수 선언이 물리적으로 작성한 코드의 상단으로 옮겨지는 것으로 가르치지만, 실제로는 그렇지 않습니다. 변수 및 함수 선언은 컴파일 단계에서 메모리에 저장되지만, 코드에서 입력한 위치와 정확히 일치한 곳에 있습니다.  

코드를 선언하기 전에 함수 또는 변수를 사용할 수 있습니다.
정확한 오류를 잡아낼 수 없다는 단점이 있습니다.
또한, 사용자가 의도치 않은 동작이 발생할 수도 있습니다.

### Method hoisting
```javascript
function foo() {
	console.log("foo!");
}

foo();

>> foo!
--------------------------------------------------

foo();

function foo() {
	console.log("foo!");
}

>> foo!
```
위의 두 코드 모두 올바르게 동작합니다.

### Variable hoisting
변수는 선언하기 전에 초기화하여 사용될 수 있으나,
초기화 없이는 사용할 수 없습니다.

변수를 선언한 뒤 나중에 초기화시켜 사용한다면, 그 값은 undifined로 지정됩니다.

```javascript
num = 6;
num + 7;
var num;

// num을 사용하는 시점보다 뒤에 선언해도 에러가 나지 않습니다.
-------------------------------------------------
var x = 1;
console.log(x + " " + y);
var y = 2;

>> 1 undefined

-------------------------------------------------

var x = 1;
var y;
console.log(x + " " + y);
y = 2;

>> 1 undefined

// 위의 코드와 같은 방식으로 동작합니다.
```

Javascript 에서 함수를 선언하는 세가지 방법

```javascript
/*
function setupDatabase() {

}
*/
// 위와 같은 함수의 선언 방식은 호이스팅이 발생하여 사용자가 의도치 않은 코드의 사용이 발생할 수 있습니다.
// 권장하지 않습니다.

/*
const setupDatabase = function () {

};
*/

const setupDatabse = () => {

};

```
 
#node