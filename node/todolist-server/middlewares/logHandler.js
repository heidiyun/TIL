"use strict"

// mongoose
// 데이터베이스에 접속해서, 데이터를 저장하고, 로드하는 연산을 수행하는 클라이언트
// : Database Connector

const bunyan = require("bunyan");
const _ = require("lodash");
const humanize = require('humanize-number');

function time(start) {
  const delta = new Date - start;
  return humanize(delta < 10000
    ? delta + 'ms'
    : Math.round(delta / 1000) + 's');
}

const reqSerializer = (ctx = {}) => {
  return {
    method: ctx.method,
    path: ctx.path,
    url: ctx.url,
    headers: ctx.headers,
    protocol: ctx.protocol,
    ip: ctx.ip,
    query: ctx.query,
    body: ctx.request.body,
  };
}

const resSerializer = function (ctx = {}) {
  return {
    statusCode: ctx.status,
    responseTime: ctx.responseTime,
    type: ctx.type,
    headers: (ctx.response || {}).headers,
    body: ctx.body,
  };
}

const log = function ({ logger = null } = {}) {
  //  인자로 logger라는 것이 있으면 바로 쓸 수 있다.
  // logger가 인자로 전달되지 않으면 null이 기본값이 된다.
  if (_.isNil(logger)) {
    throw Error("Logger is required");
  }
  return async (ctx, next) => {
    logger.info("hello");
    ctx.log = logger;
    ctx.log.addSerializers({
      req: reqSerializer,
      res: resSerializer,
      err: bunyan.stdSerializers.err,
    });

    //requset logging
    ctx.log.info({
      req: ctx,
      event: "request",
    });

    // debug, info, warn, error -> level

    try {
      const startTime = new Date();
      await next();
      // router에 동작한 handler가 동작해서 response가 온다.
      ctx.responseTime = time(startTime);
      // response time은 사용자가 계산해주어야 한다.

      // response logging
      ctx.log.info({
        //   req: ctx,
        res: ctx,
        event: "response",
      });
    } catch (err) {
      ctx.log.error({
        err,
        event: "error",
      });
      throw err;
      // err를 밖으로 던져줘야 무슨 에러인지 나타난다.
    }
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