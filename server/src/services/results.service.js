import fs from 'fs';
import path from 'path';


export const getResultsPath = async (filename) => {
  const safeFilename = path.basename(filename);
  const filePath = path.join(path.resolve(process.env.OUTPUT_DIR), safeFilename);

  await fs.promises.access(filePath, fs.constants.R_OK);

  return { filePath, safeFilename };
};

export const parseCSV = (raw) => {
  const [headerLine, ...rows] = raw.trim().split('\n');
  const headers = headerLine.split(',').map((h) => h.trim());

  return rows.map((row) =>
    Object.fromEntries(
      row.split(',').map((val, i) => [headers[i], val.trim()])
    )
  );
};