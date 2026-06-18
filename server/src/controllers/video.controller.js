import { getAvailableVideos } from "../services/video.service.js";

export const listVideos = async (req, res) => {
    try {
        const videos = await getAvailableVideos();
        res.status(200).json(videos);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Error reading video directory" });
    }
};