import fs from "fs";
import path from "path";

export const getAvailableVideos = async () => {
    const videosDir = path.resolve(process.env.VIDEOS_DIR);
    const files = await fs.promises.readdir(videosDir);
    return files.filter(file => /\.(mp4|mov|avi|mkv)$/i.test(file));
};