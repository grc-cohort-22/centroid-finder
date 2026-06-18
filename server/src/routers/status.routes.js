import express from 'express';
import * as statusController from '../controllers/status.controller.js';

const router = express.Router();    

router.get('/status/:jobId', statusController.getStatus);

export default router;