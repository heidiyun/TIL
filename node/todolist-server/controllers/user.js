const User = require("../models/User");
const _ = require("lodash");
const Joi = require("koa-joi-router").Joi;
const bcrypt = require("bcrypt");
const { ClientError,
    NotFoundError } = require("../error");

const jwt = require("jsonwebtoken");
const {
    jwtSecret
} = require("../config");
console.log(jwtSecret);

const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{6,}$/;

const postUser = {
    path: "/users",
    method: "POST",
    validate: {
        body: {
            email: Joi.string().email().required(),
            name: Joi.string().required(),
            password: Joi.string().regex(passwordRegex).required(),
            // name: Joi.string().default("Unnamed")
            // name을 입력하지 않아도 될 때 default값으로 들어간다.
        },
        type: "json",
    },
    async handler(ctx) {
        ctx.body = await User.signUp(ctx.request.body);

    }
};

const putUser = {
    path: "/update",
    method: "PUT",
    validate: {
        body: Joi.object({
            name: Joi.string(),
            email: Joi.string().email(),
            password: Joi.string().regex(passwordRegex),
        }).or("email", "name", "password").required(),
        type: "json"
    },
    async handler(ctx) {
        // if (_.isNil(ctx.user)) {
        //     throw new ClientError("Unauthorization");
        // }

        // const {
        //     password = null
        // } = ctx.request.body;
        // const data = ctx.request.body;

        // if (!_.isNill(password)) {
        //     const passwordHash = await User.generatePassword(password);
        //     data.password = passwordHash
        // }

        const header = ctx.request.header.authorization;
        const token = header.split(" ")[1];
        ctx.body = await User.putUser(ctx.request.body, token);
    }
};

const getUser = {
    path:"/user",
    method: "GET",
    async handler(ctx) {
        
        if (_.isNil(ctx.user)) {
            throw new ClientError("Unauthorization");
        }

        ctx.body = ctx.user;
    }
};

const updateUser = {
    path: "/users",
    method: "PUT",
    validate: {
        body: {
            _id: Joi.string(),
            name: Joi.string(),
            email: Joi.string().email(),
        },
        type: "json"
    },
    async handler(ctx) {
        ctx.body = await updatePerson(ctx.request.body);
        ctx.status = 201;
    }
};

const deleteUser = {
    path: "/users/delete",
    method: "POST",
    validate: {
        body: {
            _id: Joi.string().required()
        },
        type: "json"
    },
    async handler(ctx) {
        ctx.body = await deletePerson(ctx.request.body);
        ctx.status = 201
    }
};


async function exec(body) {

    try {
        const person = await Person.create({ name: body.name, email: body.email, password: body.password });
        const foundPerson = await Person.findById(person._id);
        const updatePerson = await foundPerson.save();
        return updatePerson;
    } catch (e) {
        return e;
    }
}

async function getPerson() {
    const people = await User.find();
    console.log("people" + people);
    return people;
}

async function updatePerson(body) {
    const person = await Person.findById(body._id);
    if (!person) return

    person.name = body.name;
    person.email = body.email;

    const result = await person.save();
    return result;

}

async function deletePerson(id) {
    const result = await Person.deleteOne({ _id: id });
    return result;
    console.log(result);
}

const signIn = {
    path: "/auth/login",
    method: "POST",
    validate: {
        body: {
            email: Joi.string().email().required(),
            password: Joi.string().required(),
        },
        type: "json"
    },

    async handler(ctx) {
        const access_token = await User.signIn(ctx.request.body);

        ctx.body = {
            access_token,
        };
    }

}

module.exports = [
    postUser,
    putUser,
    getUser,
    signIn
];