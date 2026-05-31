import express from 'express'
import { controller_loginUser, controller_registerUser } from '../controllers/auth.controller.js';
const Router = express.Router();

Router.post('/register', controller_registerUser);
Router.post('/login', controller_loginUser);

export default Router;