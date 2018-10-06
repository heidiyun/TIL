// require : java import와 동일한 개념이다.
// 대문자 - class
// 소문자 - 인스턴스

const Koa = require("koa");
const Router = require('koa-router');
const logger = require("./logger");
// ./logger.js import
// const logger = require('koa-logger');

const app = new Koa();
const router = new Router();


router.get("/", function (ctx){
    ctx.body = "HEllO, TODO Service"
});

// app에 router를 연결

// app.use(logger());
// 바로 middleware에 등록할 수 없다.
// interface는 명시적인 타입
// 동적언언ㄴ 암묵적 약속

const logHandler = require("./middlewares/logHandler");
app.use(logHandler({
    logger
    // logger.js에서 설정한 logger를 전달해준다.
}));

app.use(router.routes())
.use(router.allowedMethods());

app.listen(3000);
