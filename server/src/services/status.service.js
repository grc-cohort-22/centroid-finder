import { jobStore } from '../store/jobStore.js';

export const getJobStatus = async (jobId) => {
  const job = await jobStore.get(jobId);

  if (!job) return null;
  
  return job;
};