const router = require('koa-joi-router');
const apiRouter = router();
const Joi = router.Joi;

const userController = require('./controllers/user');

apiRouter.get("/", (ctx) => {
    // throw new Error("what the fuck");
    ctx.body = "HEllO, TODO Service"
});



apiRouter.route([
  ...userController
]);

// apiRouter.post("/users",{
//     body: {
//         email: Joi.string().required(),
//         name: Joi.string().required(),
//     }
// } ,userController.postUser);

module.exports = apiRouter;