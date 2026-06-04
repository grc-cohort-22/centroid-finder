// Express creates the web server and API routes
const express = require("express")

// fs lets us read folders and check if files exist
const fs = require("fs")

// path helps us safely create file paths
const path = require("path")

// spawn lets Node start another program, like our Java JAR
const { spawn } = require("child_process")

// uuid creates a unique jobId for each processing request
const { v4: uuidv4 } = require("uuid")

// dotenv loads variables from the .env file
require("dotenv").config({ path: "../.env" })

const app = express()

const PORT = process.env.PORT || 3000

// These paths come from .env
const videoDir = process.env.VIDEO_DIR
const outputDir = process.env.OUTPUT_DIR
const jarPath = process.env.PROCESSOR_JAR

if (!videoDir) {
  throw new Error("VIDEO_DIR is missing from .env")
}

if (!outputDir) {
  throw new Error("OUTPUT_DIR is missing from .env")
}

if (!jarPath) {
  throw new Error("PROCESSOR_JAR is missing from .env")
}

if (!fs.existsSync(videoDir)) {
  throw new Error(`Video directory not found: ${videoDir}`)
}

if (!fs.existsSync(outputDir)) {
  throw new Error(`Output directory not found: ${outputDir}`)
}

if (!fs.existsSync(jarPath)) {
  throw new Error(`Processor JAR not found: ${jarPath}`)
}

// This stores job status while the server is running
// Later this could be replaced with a database or filesystem storage
const jobs = {}

app.use(express.json())

// Publicly host video files
// Example: http://localhost:3000/videos/example.mp4
app.use("/videos", express.static(videoDir))

// GET /thumbnail/:filename
// Creates a thumbnail image from the video
app.get("/thumbnail/:filename", (req, res) => {
  const filename = req.params.filename
  const inputPath = path.join(videoDir, filename)

  console.log("thumbnail request:", inputPath)

  if (!fs.existsSync(inputPath)) {
    return res.status(404).json({
      error: "Video file not found"
    })
  }

  res.setHeader("Content-Type", "image/png")

  const ffmpeg = spawn("ffmpeg", [
    "-ss", "00:00:01",
    "-i", inputPath,
    "-vframes", "1",
    "-f", "image2pipe",
    "-vcodec", "png",
    "-"
  ])

  ffmpeg.stdout.pipe(res)

  ffmpeg.stderr.on("data", (data) => {
    console.log("ffmpeg:", data.toString())
  })

  ffmpeg.on("error", (error) => {
    console.log("ffmpeg error:", error.message)

    if (!res.headersSent) {
      res.status(500).json({
        error: error.message
      })
    }
  })

  ffmpeg.on("close", (code) => {
    console.log("ffmpeg exited with code:", code)
  })
})

// Publicly host CSV result files
// Example: http://localhost:3000/results/job-id.csv
app.use("/results", express.static(outputDir))

// Simple test route
app.get("/", (req, res) => {
 res.json({
  message: "Salamander server running"
 })
})

// GET /api/videos
// Returns a list of available mp4 videos
app.get("/api/videos", (req, res) => {
 fs.readdir(videoDir, (err, files) => {
  if (err) {
   return res.status(500).json({
    error: "Could not read video directory"
   })
  }

  const videos = files.filter((file) => {
   return file.toLowerCase().endsWith(".mp4")
  })

  res.json(videos)
 })
})

// POST /process/:filename?targetColor=HEX&threshold=NUMBER
// Starts a Java video processing job in the background
app.post("/process/:filename", (req, res) => {
 const filename = req.params.filename
 const targetColor = req.query.targetColor
 const threshold = req.query.threshold

 if (!targetColor || !threshold) {
  return res.status(400).json({
   error: "targetColor and threshold are required"
  })
 }

 const inputPath = path.join(videoDir, filename)

 if (!fs.existsSync(inputPath)) {
  return res.status(404).json({
   error: "Video file not found"
  })
 }

 const jobId = uuidv4()
 const outputFileName = `${jobId}.csv`
 const outputCsv = path.join(outputDir, outputFileName)

 jobs[jobId] = {
  jobId: jobId,
  status: "processing",
  filename: filename,
  resultUrl: null,
  error: null
 }

 const javaProcess = spawn("java", [
  "-jar",
  jarPath,
  inputPath,
  outputCsv,
  targetColor,
  threshold
 ], {
  // detached lets the Java process run independently
  detached: true,

  // ignore means Express does not wait to read Java output
  stdio: "ignore"
 })

 javaProcess.on("error", (error) => {
  jobs[jobId].status = "error"
  jobs[jobId].error = error.message
 })

 // unref lets Node continue without waiting for the Java process
 javaProcess.unref()

 res.json({
  jobId: jobId,
  status: "processing"
 })
})

// GET /process/:jobId/status
// Checks if the CSV file exists yet
app.get("/process/:jobId/status", (req, res) => {
 const jobId = req.params.jobId
 const job = jobs[jobId]

 if (!job) {
  return res.status(404).json({
   error: "Job not found"
  })
 }

 const outputFileName = `${jobId}.csv`
 const outputCsv = path.join(outputDir, outputFileName)

 if (job.status === "processing" && fs.existsSync(outputCsv)) {
  job.status = "done"
  job.resultUrl = `/results/${outputFileName}`
 }

 res.json(job)
})

if (require.main === module) {

  app.listen(PORT, () => {
    console.log(
      `Server listening on port ${PORT}`
    );
  });

}

module.exports = app;