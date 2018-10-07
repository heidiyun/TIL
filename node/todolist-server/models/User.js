const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const User = new Schema({
    name: {
        type: String,
    },
    email: {
        type: String,
        unique: true,
        //동일한 이메일이 들어오면 오류를 반환해준다.
    },
});

module.exports = mongoose.model("User", User);