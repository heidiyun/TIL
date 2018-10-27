const jwt = require("jsonwebtoken");
const {
    jwtSecret
} = require("../config");
const _ = require("lodash");
const User = require("../models/User");


const authHandler = function () {
    return async (ctx, next) => {
        
        try {
        const token = ctx.request.header.authorization;
        if (_.isNil(token)) {
            return next();
        }

        const jwtToken = token.split(" ")[1];
        console.log("jwtToken"+ jwtToken);

        if (_.isNil(jwtToken)) {
            return next();
        }

        const userId = jwt.verify(jwtToken, jwtSecret).data.user.id;
        const user = await User.findById(userId);
        ctx.user = user;

    } catch (err) {
        return next();
    }
        return next();

    };
}

module.exports = authHandler;