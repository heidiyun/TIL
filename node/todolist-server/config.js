const dotenv = require("dotenv");

// 환경변수를 env파일로 부터 로드
dotenv.config();

console.log(process.env.DB_HOST);

const env = process.env.NODE_ENV || "development";
//앞에게 null이면 development로 하겠다.
console.log(env);
// undefined로 뜬다.
// 사용자가 설정해주어야 한다.

const configs = {
    base: {
        env,
        name: process.env.APP_NAME || "sample-api",
        host: process.env.APP_HOST || "0.0.0.0",
        port: process.env.APP_PORT || 3000,
    },
    production: {
        database: {
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            name: process.env.DB_NAME,
            user: process.env.DB_USER,
            password: process.env.DB_PASSWORD,
        },
    },
    development: {
        database: {
            host: process.env.DB_HOST_DEV,
            port: process.env.DB_PORT_DEV,
            name: process.env.DB_NAME,
            user: process.env.DB_USER,
            password: process.env.DB_PASSWORD,
        },
    }
};

//config를 밖으로 전달해주어야 한다. server.js

module.exports = {
    ...configs.base,
    ...configs[env],
};