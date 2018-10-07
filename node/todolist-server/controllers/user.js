const mongoose = require("mongoose");
const User = require("../models/User");
const _ = require("lodash");
const Joi = require("koa-joi-router").Joi;

// Validator : Joi

// const postUser = (ctx) => {
//     // console.log(ctx.request.body);
//     ctx.body = "ok";
// };


// module.exports.postUser = postUser;
const postUser = {
    path: "/users",
    method: "POST",
    validate: {
        body :{
        email: Joi.string().email().required(),
        name: Joi.string().required(),
        // name: Joi.string().default("Unnamed")
        // name을 입력하지 않아도 될 때 default값으로 들어간다.
    },
    type: "json",
    },
    async handler(ctx) {
        ctx.body = "ok";
    }
    //함수 형식
};

module.exports = [
    postUser
];