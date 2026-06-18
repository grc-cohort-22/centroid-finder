import fs from 'fs';
import path from 'path';

export const getVideoPath = async (filename) => {
  const safeFilename = path.basename(filename);
  const videoPath = path.join(path.resolve(process.env.VIDEOS_DIR), safeFilename);

  await fs.promises.access(videoPath, fs.constants.R_OK);

  return { videoPath, safeFilename };
};