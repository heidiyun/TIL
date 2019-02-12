const _ = require("lodash");
const User = require("../models/User");
const Joi = require("koa-joi-router").Joi;
const { ClientError,
    NotFoundError } = require("../error");

const jwt = require("jsonwebtoken");

const {
    jwtSecret
} = require("../config");

const getTodolist = {
    path: "/todo",
    method: "GET",
    async handler(ctx) {
        const header = ctx.request.header.authorization;
        const token = header.split(" ")[1]
        
        let decoded
        try {
            decoded = jwt.verify(token, jwtSecret);
        } catch (err) {
            throw new ClientError("Invalid token");
        }

    const info = await User.findById(decoded.data.user.id, function (err, user) {
            if (err) {
                throw new NotFoundError("User not found");
            }

            return user.todolist;
        });

        console.log(info);
        ctx.body = info.todolist;

    }
}

const addTodo = {
    path: "/todo",
    method: "POST",
    validate: {
        body: {
            subject: Joi.string().required(),
            solved: Joi.boolean().required(),
        },
        type: "json",
    },
    async handler(ctx) {
        const header = ctx.request.header.authorization;
        const token = header.split(" ")[1]
        const body = ctx.request.body

        let decoded
        try {
            decoded = jwt.verify(token, jwtSecret);
        } catch (err) {
            throw new ClientError("Invalid token");
        }

        User.findById(decoded.data.user.id, function (err, user) {
            if (err) {
                throw new NotFoundError("User not found");
            }
            user.todolist.push({ subject: body.subject, solved: body.solved });
            user.save()
        });

        ctx.body = "ok"

    }
}
const removeTodo = {
    path:"/todo",
    method:"PUT",
    validate: {
         body : {
            _id:Joi.string().required()
         },
         type: "json"
    },
    async handler(ctx) {
        const header = ctx.request.header.authorization;
        const token = header.split(" ")[1];

        let decoded;
        try {
            decoded = jwt.verify(token, jwtSecret);
        } catch (err) {
            throw new ClientError("Invalid token");
        }

        User.findById(decoded.data.user.id, function (err, user) {
            if (err) {
                throw new NotFoundError("User not found");
            }
            user.todolist.pull({ _id: ctx.request.body._id });
            user.save()

        });

        ctx.body = "ok";
    }
}

const updateTodo = {
    path: "/update/todo",
    method: "PUT",
    validate: {
        body: Joi.object({
            subject: Joi.string(),
            solved: Joi.boolean(),
            // name: Joi.string().default("Unnamed")
            // name을 입력하지 않아도 될 때 default값으로 들어간다.
        }).or("subject", "sovled").required(),
        type: "json",
    },
    async handler(ctx) {
        const header = ctx.request.header.authorization;
        const token = header.split(" ")[1];

        let decoded;
        try {
            decoded = jwt.verify(token, jwtSecret);
        } catch (err) {
            throw new ClientError("Invalid token");
        }

        const data = ctx.request.body

        const object = {
            ...data
        }

        User.update("5be7035046a68497031e3c64", object,function (err, user) {
            if (err) {
                throw new NotFoundError("User not found");
            }
            console.log(user);

        });





    }
};



module.exports = [
    addTodo,
    getTodolist,
    removeTodo,
    
];
