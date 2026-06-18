/* eslint-disable consistent-return */
import path from 'path';
import fs from 'fs';
import { extractFirstFrame } from '../services/thumbnail.service.js';

export const getThumbnail = async (req, res) => {
  const { filename } = req.params;

  const videoPath = path.join(process.env.VIDEOS_DIR, path.basename(filename));

  if (!fs.existsSync(videoPath)) {
    return res.status(404).json({ error: 'Video file not found' });
  }

  try {
    const frameBuffer = await extractFirstFrame(videoPath);
    res.setHeader('Content-Type', 'image/jpeg');
    res.send(frameBuffer);
  } catch (err) {
    console.error('Thumbnail error:', err);
    res.status(500).json({ error: 'Error generating thumbnail' });
  }
};