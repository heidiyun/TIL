const mongoose = require("mongoose");
const User = require("../models/User");
const _ = require("lodash");
const Joi = require("koa-joi-router").Joi;
const bcrypt = require("bcrypt");
const { ClientError,
NotFoundError} = require("../error");

const jwt = require("jsonwebtoken");
const {
    jwtSecret
} = require("../config");
console.log(jwtSecret);

// Validator : Joi

// const postUser = (ctx) => {
//     // console.log(ctx.request.body);
//     ctx.body = "ok";
// };

// const userSchema = new mongoose.Schema({
//     name: String,
//     email: String,
//     password: String,
// });

const postUser = {
    path: "/users",
    method: "POST",
    validate: {
        body: {
            email: Joi.string().email().required(),
            name: Joi.string().required(),
            password: Joi.string().regex(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{6,}$/).required(),
            // name: Joi.string().default("Unnamed")
            // name을 입력하지 않아도 될 때 default값으로 들어간다.
        },
        type: "json",
    },
    async handler(ctx) {
        ctx.body = await User.signUp(ctx.request.body);
        // const user = new User(ctx.request.body);
        
        // const {
        //     eamil,
        //     password,
        // } = ctx.request.body

        // const data = (await user.save()).toObject();

        // ctx.body = await User.signUp(ctx.request.body);

        // const user = new User(ctx.request.body);

      /*  const {
            email,
            password
        } = ctx.request.body

        const exist = await User.findOne({
            email
        }).exec();

        if (!_.isNil(exist)) {
            throw new ClientError("Already exist email");
        }

        const passwordHash = await bcrypt.hash(password, 10);

        const user = new User({
            ...ctx.request.body,
            password: passwordHash,
        });
    
        const data = (await user.save()).toObject();
        delete data.password;

        ctx.body = data;

        // ctx.body = await exec(ctx.request.body);
        // ctx.status = 201;
        */
    }

    //함수 형식
};

const getUser = {
    path: "/user",
    method: "GET",          
    validate : {
        headers: Joi.object( { 
            'access_token': Joi.any().valid('application/json').required(),
        }).unknown()
    },
    async handler(ctx) {
        console.log(ctx.request.body);
       const user = await User.getUser(ctx.request.body);
    //    ctx.body = user;
    //     // ctx.body = await getPerson();
    //     ctx.status = 201;
    }
};

const updateUser = {
    path: "/users",
    method: "PUT",
    validate : {
        body : {
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

    console.log('await', result);
    Person.create({ name: body.name, email: body.email }, (error1, newPerson) => {
        if (error1) {
            console.log("error1");
            return handleError(error1);
        }

        Person.findById(newPerson._id, (error2, foundPerson) => {
            if (error2) {
                console.log("error2");
                return handleError(error2);
            }

            // foundPerson.name = "yunjin";

            foundPerson.save((error3, updatedPerson) => {
                if (error3) {
                    console.log("error3");
                    return handleError(error3);
                }

                console.log('successfully saved person' + updatedPerson);
                return Promise.resolve(updatedPerson);
                // return updatedPerson;
                // process.exit(0);
            });
        });
    });
}

function handleError(error) {
    console.error('Error' + error + error.stack);
    // process.exit(2);
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
    validate : {
        body : {
            email: Joi.string().email().required(),
            password: Joi.string().required(),
        },
        type: "json"
    },

    async handler (ctx) {
       const token = await User.signIn(ctx.request.body);

       
       ctx.body = {
           token,
       };
    }
    
}

module.exports = [
    postUser,
    getUser,
    updateUser,
    deleteUser,
    signIn
];