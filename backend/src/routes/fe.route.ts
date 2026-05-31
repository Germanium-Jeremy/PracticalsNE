import express from 'express'
import { controller_getAllExtinguishers, controller_addExtinguisher, controller_buyExtinguisher, controller_returnExtinguisher } from '../controllers/fe.controller.js';
import { authenticateToken } from '../middlewares/auth.middleware.js';
const Router = express.Router();

Router.get('/all', authenticateToken, controller_getAllExtinguishers);
Router.post('/add', authenticateToken, controller_addExtinguisher);
Router.post('/buy', authenticateToken, controller_buyExtinguisher);
Router.post('/return', authenticateToken, controller_returnExtinguisher);

export default Router;