import fs from 'fs';
import path from 'path';
import { Writable } from 'stream';
import { spawn } from 'child_process';
import { v4 as uuidv4 } from 'uuid';
import { jobStore } from '../store/jobStore.js';

export const startVideoJob = async ({ filename, targetColor, threshold }) => {
  const safeFilename = path.basename(filename);
  const videosDir = path.resolve(process.env.VIDEOS_DIR);
  const inputVideo = path.join(videosDir, safeFilename);
  const outputCsv = path.join(path.resolve(process.env.OUTPUT_DIR), `${safeFilename}.csv`);

  await fs.promises.access(inputVideo);

  const jobId = uuidv4();
  const jarPath = path.resolve(process.env.PROCESSOR_JAR);

  jobStore.set(jobId, { status: 'processing' });

  const child = spawn('java', ['-jar', jarPath, inputVideo, outputCsv, targetColor, threshold.toString()]);

  child.on('close', (code) => {
    if (code !== 0) {
      jobStore.set(jobId, {
        status: 'error',
        error: `Error processing video: process exited with code ${code}`,
      });
      return;
    }

    const baseName = path.parse(safeFilename).name;

    const resultPath = path.join(
    path.resolve(process.env.OUTPUT_DIR),
    `${baseName}_${jobId}.csv`
    );

    jobStore.set(jobId, { status: 'done', result: resultPath });
  });

  child.on('error', (err) => {
    jobStore.set(jobId, {
      status: 'error',
      error: `Error processing video: ${err.message}`,
    });
  });

  //child.stderr.on('data', () => {});

  child.stderr.pipe(
    new Writable({
      write(_chunk, _encoding, callback) {
        callback();
      }
    })
  );

  /*child.stderr.on('data', (data) => {
    console.log(`[${jobId}] stderr: ${data}`);
  });*/

  return jobId;
};