const mongoose = require("mongoose");
const User = require("../models/User");
const _ = require("lodash");
const Joi = require("koa-joi-router").Joi;

// Validator : Joi

// const postUser = (ctx) => {
//     // console.log(ctx.request.body);
//     ctx.body = "ok";
// };

const userSchema = new mongoose.Schema({
    name: String,
    email: String,
});

const Person = mongoose.model('users', userSchema);

// module.exports.postUser = postUser;
const postUser = {
    path: "/users",
    method: "POST",
    validate: {
        body: {
            email: Joi.string().email().required(),
            name: Joi.string().required(),
            // name: Joi.string().default("Unnamed")
            // name을 입력하지 않아도 될 때 default값으로 들어간다.
        },
        type: "json",
    },
    async handler(ctx) {
        ctx.body = await exec(ctx.request.body);
        ctx.status = 201;
    }

    //함수 형식
};

const getUser = {
    path: "/users",
    method: "GET",
    async handler(ctx) {
        ctx.body = await getPerson();
        ctx.status = 201;
    }
};

const updateUser = {
    path: "/users/update",
    method: "POST",
    validate : {
        body : {
            _id: Joi.string().required(),
            name: Joi.string().required(),
            email: Joi.string().email().required(),
        },
        type: "json"
    },
    async handler(ctx) {
        ctx.body = await updatePerson(ctx.request.body);
        ctx.status = 201;
    }
};

const deleteUser = {
    path: "/users",
    method: "DELETE",
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
        const person = await Person.create({ name: body.name, email: body.email });
        // const foundPerson = await Person.findById(person._id);
        const updatePerson = await person.save();
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
    const people = await Person.find();
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

module.exports = [
    postUser,
    getUser,
    updateUser,
    deleteUser
];