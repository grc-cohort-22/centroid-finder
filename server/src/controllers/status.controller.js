/* eslint-disable consistent-return */
import { getJobStatus } from '../services/status.service.js';

export const getStatus = async (req, res) => {
  const { jobId } = req.params;

  try {
    const job = await getJobStatus(jobId);
      

    if (!job) {
      return res.status(404).json({ error: 'Job ID not found' });
    }

    if (job.status === 'processing') {
      return res.status(200).json({ status: 'processing' });
    }

    if (job.status === 'done') {
      return res.status(200).json({ status: 'done', result: job.result });
    }

    if (job.status === 'error') {
      return res.status(200).json({ status: 'error', error: job.error });
    }

  } catch (err) {
    console.error(err);
    return res.status(500).json({ error: 'Error fetching job status' });
  }
};