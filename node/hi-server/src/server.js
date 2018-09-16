const Koa = require("koa");   //import npm i koa
const Router = require("koa-router"); // npm i koa-router
const logger = require("koa-logger"); // npm i koa-loagger

const app = new Koa(); // Koa 객체 생성
const router = new Router();

// app.use(async ctx => {
//     ctx.body = "Hello, Koa"
// }); // 비동기 함수 async

router.get("/", async ctx => {
    ctx.body = {
        name: "Hello REST API Server",
        version: "0.0.3",
        links: {
            user_url:"https://api.heidiyun.xyz/users"

        }
    };
});
// 경로에 따라서 동작이 달라져야 하는 것을 설정한다. ->   router

router.get("/users", async ctx => {
    ctx.body = [
        {
            name: "Alice",
            age: 42
        },
        {
            name: "Tom",
            age: 12
        }
    ]
});

app.use(logger())
app.use(router.routes()).use(router.allowedMethods());
app.listen(3000); 
// 코드 정렬 control + shift + f