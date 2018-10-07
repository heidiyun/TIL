// require : java import와 동일한 개념이다.
// 대문자 - class
// 소문자 - 인스턴스

const Koa = require("koa");
// const Router = require('koa-router');
const logger = require("./logger");
// ./logger.js import
// const logger = require('koa-logger');
const config = require("./config");
const bodyParser = require("koa-bodyparser");

console.log(config);

const app = new Koa();
// const router = new Router();
const router = require("./routes");

// app에 router를 연결

// app.use(logger());
// 바로 middleware에 등록할 수 없다.
// interface는 명시적인 타입
// 동적언어는 암묵적 약속

const logHandler = require("./middlewares/logHandler");
app.use(logHandler({
    logger
    // logger.js에서 설정한 logger를 전달해준다.
}));

app.use(async (ctx, next) => {
    try {
        await next();
    } catch (err) {
        ctx.staus = err.status || 500;
        ctx.body = {
            message: err.message,
            body: ctx.request.body,
            query: ctx.request.query
        }
        ctx.app.emit("error", err, ctx);
    }
});

app.on("error", (err, ctx) => {
    if (ctx == null) {
        logger.error({
            err,
            event: "error",
        });
    }
})

// app.on("error", (err, ctx) => {
//     if (ctx == null) {
//         logger.error({
//             err,
//             event: "error"
//         });
//     }

//     if (err.isJoi) {
//         ctx.status = 400;
        
//         // ctx.body = JSON({
//         //     message: err.message
//         // });
//     }
// })

// app.use(bodyParser());
app.use(router.middleware());
// app.use(router.routes())
// .use(router.allowedMethods());

//JavaScript 호이스팅 : 실행코드보다 아래 정의된 변수나 함수가 호출되는 경우. 
// var는 호이스팅 함.
// let은 호이스팅 하지 않음.

// 권장하지 않음.
/*
function setupDatabase() {

};
*/

/*
const setupDatabase = function() {

};
*/

/*
const setupDatabase = () => {

};
*/

const setupDatabase = function(config) {
    const mongoose = require("mongoose");
    // const dbUri = "mongodb://hello:1234qwer@13.209.98.172:20000/hello";
    with(config.database) {
    const dbUri = `mongodb://${user}:${password}@${host}:${port}/${name}`;
    
    console.log(dbUri);
    mongoose.connect(dbUri, {
        useNewUrlParser: true,
    });
    }

    const db = mongoose.connection;
    db.on("open", () => {
        console.log("Database connection successful");
    });

    db.on("connected", () => {
        console.log("MongoDB connected");
    });

    db.on("error", () => {
        console.log("Error in MongoDb:" + error);
    });

    db.on("disconnected", () => {
        console.log("MongoDB disconnected");
    });
};

setupDatabase(config);


app.listen(3000);