"use strict"

const bunyan = require("bunyan");
const _ = require("lodash");

const log = function ( {logger = null} = {}) {
    //  인자로 logger라는 것이 있으면 바로 쓸 수 있다.
    // logger가 인자로 전달되지 않으면 null이 기본값이 된다.
    if (_.isNil(logger)) {
        throw Error("Logger is required");
    }
    return function (ctx, next) {
      logger.info("hello");
      // debug, info, warn, error -> level
      next();
    };
}

// const log = function ( options = {}) {
//     const logger = options.logger;
//     return function (ctx, next) {
//       logger.info("hello");
//       // debug, info, warn, error -> level
//       next();
//     };
// }

// lodash : functional한 기능을 제공해준다.
// 검증된 라이브러리

// next();를 해주지 않으면 router로 보내지지 않아서 서버를 돌릴때 Not Found가 뜬다.
// 그러나 로그는 찍힌다.

// options로 조건을 설정할 수 있다.
// ctx : request response에 대한 정보
// next : 다음 미들웨어로 패스
// 특정 ip만 router에 전달하고 싶다.
// middleware는 자동적으로 호출된다. (함수)
// AOP

module.exports = log;