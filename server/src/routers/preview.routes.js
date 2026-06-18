import express from 'express';
import * as previewController from '../controllers/preview.controller.js';

const router = express.Router();

router.get('/:filename', previewController.streamVideo);

export default router;