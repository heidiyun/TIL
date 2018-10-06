'use strict'
// 자바스크립트의 표준을 사용하겠다.

const bunyan = require("bunyan");

const name = "todolist-server";
const config = {
    name, // name: name 같은 이름의 필드를 초기화 할 때 사용하는 문법.
    streams: [
        {
        type: "stream",
        stream: process.stdout,
        level: "debug", // info
        }
    ]

};

// 분산 로거 (streams)
// 들어온 로그를 등록된 모든 객체에 로그를 퍼뜨려준다.

const options = {
    ...config,
    serializers: bunyan.stdSerializers,
};

// 특정한 변화를 수행하는 것. 직렬화
// 어떻게 파싱할 것인지 결정한다.

// spread 문법 ... depth를 평평하게 만들어준다. 

const logger = bunyan.createLogger(options);
module.exports = logger;