import fs from 'fs';
import { getResultsPath, parseCSV } from '../services/results.service.js';

export const getResults = async (req, res) => {
  try {
    const { filename } = req.params;
    const { filePath, safeFilename } = await getResultsPath(filename);

    const raw = await fs.promises.readFile(filePath, 'utf-8');
    const data = parseCSV(raw);

    res.status(200).json({
      filename: safeFilename,
      rows: data,
      downloadUrl: `/results/${safeFilename}/download`,
    });
  } catch (err) {
    if (err.code === 'ENOENT') {
      return res.status(404).json({ error: 'Results file not found' });
    }
    console.error(err);
    return res.status(500).json({ error: 'Error fetching results' });
  }
};

export const downloadResults = async (req, res) => {
  try {
    const { filename } = req.params;
    const { filePath, safeFilename } = await getResultsPath(filename);

    res.setHeader('Content-Type', 'text/csv');
    res.setHeader('Content-Disposition', `attachment; filename="${safeFilename}"`);

    fs.createReadStream(filePath).pipe(res);
  } catch (err) {
    if (err.code === 'ENOENT') {
      return res.status(404).json({ error: 'Results file not found' });
    }
    console.error(err);
    return res.status(500).json({ error: 'Error downloading results' });
  }
};