import express from 'express'
import { allUsers, returnASingleUser } from '../controllers/user.controller.js';
import { authenticateToken } from '../middlewares/auth.middleware.js';
import { authorizeRole } from '../middlewares/role.middleware.js';
const Router = express.Router();

Router.get('/allUsers', authenticateToken, allUsers);
Router.get('/getUser', authenticateToken, authorizeRole('admin'), returnASingleUser);

export default Router;