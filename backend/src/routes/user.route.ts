import express from 'express'
import { controller_getAUser, controller_getUsers } from '../controllers/user.controller.js';
import { authenticateToken } from '../middlewares/auth.middleware.js';
import { authorizeRole } from '../middlewares/role.middleware.js';
const Router = express.Router();

Router.get('/allUsers', authenticateToken, controller_getUsers);
Router.get('/getUser', authenticateToken, authorizeRole('admin'), controller_getAUser);

export default Router;