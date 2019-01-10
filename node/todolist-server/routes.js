const router = require('koa-joi-router');
const apiRouter = router();
const Joi = router.Joi;

const userController = require('./controllers/user');
const imageController = require('./controllers/image');
const todoController = require('./controllers/todo');

apiRouter.get("/", (ctx) => {
    // throw new Error("what the fuck");
    ctx.body = "HEllO, TODO Service"
});


apiRouter.route([
  ...userController,
  ...imageController,
  ...todoController
]);

// apiRouter.post("/users",{
//     body: {
//         email: Joi.string().required(),
//         name: Joi.string().required(),
//     }
// } ,userController.postUser);


module.exports = apiRouter;