import { Router } from 'express';
import { getThumbnail } from '../controllers/thumbnail.controller.js';

const router = Router();

router.get('/:filename', getThumbnail);

export default router;