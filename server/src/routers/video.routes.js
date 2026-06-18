import express from "express";
import * as videoController from "../controllers/video.controller.js";

const router = express.Router();

router.get("/", videoController.listVideos);

export default router;