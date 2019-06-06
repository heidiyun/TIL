# Vue - App.vue 분리하기
안드로이드의 Activity에 해당하는 Vue가 있다.
Vue는 Component로 Vue를 가진다.
즉, 하나의 Vue는 여러 개의 Vue를 import해서 가지고 있을 수 있다.

* App.vue
공통적인 특성을 정의해놓은 곳
한 번 정의하면 여러군데에서 쓰이는 것들. (Global Navigation Bar와 같은 것)

그러다보니 프로젝트가 커지게 되면 한 파일에 코드가 너무 많아져서 모듈화가 꺠진다.
App.vue에는 template, style, script 세 영역으로 나누어진다.
이것들을 각기 다른 파일로 나누자

* src 폴더 밑에 app 폴더를 만들자.
	* app.scss
	* app.ts
	* app.vue
![](Vue-App.vue%E1%84%87%E1%85%AE%E1%86%AB%E1%84%85%E1%85%B5%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5/565EDBC4-A61A-4CFA-98DB-606BAFBA1282.png)


index.ts는 시작점을 명시하지 않았을 때, 이 파일이 실행된다.

* 기존 App.vue
```javascript

<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Homasdfasdfe</router-link>|
      <router-link to="/about">About</router-link>
    </div>
    <router-view/>
  </div>
</template>

<style lang="scss">
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  padding: 30px;
  a {
    font-weight: bold;
    color: #2c3e50;
    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>

```

	
* app/app.vue
```javascript
<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Home</router-link>|
      <router-link to="/about">About</router-link>
    </div>
    <router-view/>
  </div>
</template>

<style lang="scss" scoped>
// scoped : 여기 안에서만 사용하것이다. 다른 vue에서는 사용x
@import './app.scss';
</style>
// ./app.scss  파일 속성 import

<script src="./app.ts">
</script>
// script소스는 ./app.ts에서 구현
```

* app/app.ts (script)
```ts
import { Vue, Component } from 'vue-property-decorator';

// 컴포넌트 사용 명시
@Component({})
export default class App extends Vue {
  public displayNum: number = 10;
  public clickName() {
    this.displayNum = 9;
  }
}

// Class MainActivity : AppCompatActivity와 같다. 
// Class App은 Vue이므로 Vue를 상속받는다. 
// Class App은 Vue안의 Vue. 즉, 컴포넌트이다.
```

* index.ts
```ts
import App from './app';

export default App;

```

## 


#web
