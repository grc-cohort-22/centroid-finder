import express from 'express';
import dotenv from 'dotenv';
import path from 'path';
import videoRoutes from './routers/video.routes.js';
import processRoutes from './routers/process.routes.js';
import thumbnailRoutes from './routers/thumbnail.routes.js'
import statusRoutes from './routers/status.routes.js';

//configure Express.js app
const app = express();
dotenv.config();

//view engine
app.set("view engine", "ejs");
app.set("views", "src/views");

//static directories
app.use(express.static('public'));

//middleware
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const videoFolder = path.resolve(process.env.VIDEOS_DIR);

//routers
app.use("/videos", express.static(videoFolder));
app.use("/api/videos", videoRoutes);
app.use("/process", processRoutes);
app.use("/thumbnail", thumbnailRoutes);
app.use('/process', statusRoutes);


export default app;