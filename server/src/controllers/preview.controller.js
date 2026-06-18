/* eslint-disable consistent-return */
import fs from 'fs';
import path from 'path';
import { getVideoPath } from '../services/preview.service.js';

export const streamVideo = async (req, res) => {
  try {
    const { filename } = req.params;
    const { videoPath, safeFilename } = await getVideoPath(filename);

    const stat = fs.statSync(videoPath);
    const fileSize = stat.size;
    const ext = path.extname(safeFilename).toLowerCase();

    const mimeTypes = {
      '.mp4': 'video/mp4',
      '.mov': 'video/quicktime',
      '.avi': 'video/x-msvideo',
      '.webm': 'video/webm',
      '.mkv': 'video/x-matroska',
    };

    const contentType = mimeTypes[ext] ?? 'video/mp4';
    const range = req.headers.range;

    if (range) {
      const [startStr, endStr] = range.replace(/bytes=/, '').split('-');
      const start = parseInt(startStr, 10);
      const end = endStr ? parseInt(endStr, 10) : fileSize - 1;
      const chunkSize = end - start + 1;

      res.writeHead(206, {
        'Content-Range': `bytes ${start}-${end}/${fileSize}`,
        'Accept-Ranges': 'bytes',
        'Content-Length': chunkSize,
        'Content-Type': contentType,
      });

      fs.createReadStream(videoPath, { start, end }).pipe(res);
    } else {
      res.writeHead(200, {
        'Content-Length': fileSize,
        'Accept-Ranges': 'bytes',
        'Content-Type': contentType,
      });

      fs.createReadStream(videoPath).pipe(res);
    }
  } catch (err) {
    if (err.code === 'ENOENT') {
      return res.status(404).json({ error: 'Video file not found' });
    }
    console.error(err);
    return res.status(500).json({ error: 'Error streaming video' });
  }
};