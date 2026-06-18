/* eslint-disable no-empty-function */
/* eslint-disable consistent-return */
import { spawn } from 'child_process';
import { buffer } from 'node:stream/consumers';
import ffmpegPath from 'ffmpeg-static';


export const extractFirstFrame = async (videoPath) => {
if (!process.env.VIDEOS_DIR) {
  throw new Error('VIDEOS_DIR is not defined in environment variables');
}
  const ffmpeg = spawn(ffmpegPath, [
    '-i', videoPath,
    '-vframes', '1',
    '-f', 'image2pipe',
    '-vcodec', 'mjpeg',
    'pipe:1',
  ]);

  ffmpeg.stderr.on('data', () => {});

  const frameBuffer = await buffer(ffmpeg.stdout);

  await new Promise((resolve, reject) => {
    ffmpeg.on('close', (code) => {
      if (code !== 0) {
        return reject(new Error(`ffmpeg exited with code ${code}`));
      }
      resolve();
    });

    ffmpeg.on('error', reject);
  });

  return frameBuffer;
};