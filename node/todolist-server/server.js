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
const mongoose = require("mongoose");

mongoose.set('userNewUrlParser', true);
mongoose.set('useFindAndModify', false);
mongoose.set('useCreateIndex', true);


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
    logger,
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

// const userSchema = new mongoose.Schema({
//     name: String,
//     email: String,
// });

// const Person = mongoose.model('users', userSchema);

// const person = new Person({name: "hi"});

// person.save(function(err) {
//     if (err) 
//         console.log(err);
// });


const setupDatabase = function (config) {
    // const dbUri = "mongodb://hello:1234qwer@13.209.98.172:20000/hello";
    with (config.database) {
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


// function exec() {
//     Person.create({ name: "tom", email: "heidiyun.goo@gmail.com" }, (error1, newPerson) => {
//         if (error1) {
//             console.log("error1");
//             return handleError(error1);
//         }

//         Person.findById(newPerson._id, (error2, foundPerson) => {
//             if (error2) {
//                 console.log("error2");
//                 return handleError(error2);
//             }

//             // foundPerson.name = "yunjin";

//             foundPerson.save((error3, updatedPerson) => {
//                 if (error3) {
//                     console.log("error3");
//                     return handleError(error3);
//                 }

//                 console.log('successfully saved person');
//                 // process.exit(0);
//             });
//         });
//     });
// }





// deletePerson("5bc87d4e78d7335deccc1c58");

app.listen(3000);