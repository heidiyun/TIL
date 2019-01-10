const _ = require("lodash");
const bcrypt = require("bcrypt");
const saltRounds = 10;
const { ClientError } = require("../error");

const mongoose = require("mongoose");
const Schema = mongoose.Schema;


const jwt = require("jsonwebtoken");
const {
    jwtSecret
} = require("../config");

const User = new Schema({
    name: {
        type: String,
    },
    email: {
        type: String,
        unique: true,
        //동일한 이메일이 들어오면 오류를 반환해준다.
    },

    password: {
        type: String,
    },

    todolist: [
        {
            subject: {
                type: String
            },

            solved: {
                type: Boolean
            },
        },
        
    ]
});


User.statics.findOneByEmail = async function (email) {
    // return User.findOne({
    //    email 
    // }).exec(); static method 내부에서는 User가 아닌 this라고 써야됨.

    return this.findOne({
        email
    }).exec();
}

User.statics.findOneById = async function (_id) {

    return this.findOne({
        _id
    }).exec();
}

User.statics.signUp = async function (data) {

    const {
        email,
        password,
    } = data;

    const exist = await this.findOneByEmail(email);
    if (!_.isNil(exist)) {
        throw new ClientError("already exist email");
    }

    const passwordHash = await bcrypt.hash(password, saltRounds);

    const user = new this({
        ...data,
        password: passwordHash,
    });

    await user.save();
    const ret = user.toObject();
    delete ret.password;

    return ret;
}

User.methods.verifyPassword = async function (password) {
    return bcrypt.compare(password, this.password);
    // 메소드로 만들었기 때문에 this는 user객체로 들어간다.
}
// 위의 메소드는 user 객체에 대해서 쓸 수 있는 메소드이다.

// 아래 메소드는 User 모델에 대해서 쓸 수 있는 메소드이다.
User.statics.signIn = async function (data) {
    const {
        email,
        password,
    } = data;

    const user = await this.findOneByEmail(email);
    if (_.isNil(user)) {
        throw new NotFoundError("User not found");
    }

    const verified = await user.verifyPassword(password);
    if (!verified) {
        throw new ClientError("Invalid password");
    }

    const access_token = await user.generateAccessToken();

    return access_token;
}

User.methods.generateAccessToken = async function () {
    const accessToken = jwt.sign({
        data: {
            user: {
                id: this._id,
            },
        }
    }, jwtSecret, {
            expiresIn: '1h'
        });

    return accessToken;
}

User.statics.generatePassword = function (password) {
    return bcrypt.hash(password, saltRounds);
}


User.statics.findOneByAccessToken = async function (access_token) {
    return this.findOne({
        access_token
    }).exec();
}

User.statics.getUser = async function (token) {
    const decoded = jwt.verify(token, jwtSecret);
    const user = await this.findOneById(decoded.data.user.id);

    return user;
}

User.statics.putUser = async function (data, token) {

    const {
        password
    } = data;

    let newPassword;
    if (!_.isNil(password)) {
        newPassword = await bcrypt.hash(password, saltRounds);
    }

    const object = {
        ...data,
        password: newPassword
    }

    let decoded;
    try {
        decoded = jwt.verify(token, jwtSecret);
    } catch (err) {
        throw new ClientError("Invalid token");
    }

    return this.findByIdAndUpdate(decoded.data.user.id, object, function (err, user) {
        if (err) {
            throw new NotFoundError("User not found");
        }
    });

}

module.exports = mongoose.model("User", User);