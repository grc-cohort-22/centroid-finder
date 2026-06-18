import express from "express";
import * as processController from "../controllers/process.controller.js";

const router = express.Router();

router.post("/:filename", processController.startProcess);

export default router;