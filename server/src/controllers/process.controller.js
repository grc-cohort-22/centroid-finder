import { startVideoJob } from "../services/process.service.js";

export const startProcess = async (req, res) => {
    try {
        const { filename } = req.params;
        const { targetColor, threshold } = req.query;

        if(!targetColor || !threshold) {
            return res.status(400).json({
                error: "Missing targetColor or threshold query parameter."
            });
        }

        const jobId = await startVideoJob({filename, targetColor, threshold});
        return res.status(202).json({jobId});
    } catch (err) {
        console.error(err);
        return res.status(500).json({error: "Error starting job"});
    }
};