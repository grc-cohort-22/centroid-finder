import express from 'express';
import * as resultsController from '../controllers/results.controller.js';

const router = express.Router();

router.get('/:filename', resultsController.getResults);
router.get('/:filename/download', resultsController.downloadResults);

export default router;