# Priority Improvements

## Refactoring Code

1. Add package declarations and organize Java classes into packages instead of using the default package. This would make the project easier to maintain, test, and package.

2. Extract shared logic and use interfaces for reusable parts like image binarizing, group finding, and color distance. This would make the code easier to test and extend later.

I reviwed the code and found that reusable interfaces were already implemented.

## Adding Tests

1. Add more unit tests for edge cases such as no groups found, one-pixel groups, full-image masks, and invalid input files.

2. Add an integration test that runs the full video processor on a small sample video and checks that a CSV file is created with expected centroid values.

Added additional unit tests for VideoProcessorOptions.

New tests validate:

Missing command-line arguments
Invalid argument handling
Correct parsing of valid arguments

- Added edge case test for no groups found.
- Added edge case test for a full-image mask.
- Verified all tests pass successfully.

Verification:

Ran mvn test
All 36 tests passed successfully
BUILD SUCCESS

## Improving Error Handling

1. Validate command-line arguments early in `VideoProcessorOptions`, including missing arguments, invalid color values, invalid threshold values, and missing input files.
The improvements:
This adds validation for:

✅ Missing arguments
✅ Missing input file
✅ Invalid hex color
✅ Invalid threshold value
✅ Negative threshold
Replace catch (Exception e) in VideoProcessorApp
Improve VideoProcessor error handling
✅ Validate VIDEO_DIR, OUTPUT_DIR, and PROCESSOR_JAR in the server
✅ Confirm the server still runs

Added input-file validation to VideoProcessorOptions.

Updated unit tests to use temporary files instead of relying on sampleInput/sample.mp4. This makes the tests independent of project files and more reliable on different machines.

2. Improve server error responses so the Express API clearly returns useful messages when the video file, JAR path, or output folder is missing.

## Writing Documentation

1. Improve the README with clear build and run instructions for both the Java processor and the Express server.

2. Add Javadocs/comments for the new video-processing classes, especially `VideoProcessorApp`, `VideoProcessorOptions`, `VideoProcessor`, `VideoFrameReader`, `CsvWriter`, and `LargestGroupSelector`.

## Improving Performance

1. Replace recursive DFS with iterative DFS or BFS to avoid stack overflow on large video frames.

2. Reuse image/array buffers between video frames to reduce memory allocations and improve processing speed.

## Hardening Security

1. Validate and sanitize filenames in the Express server so users cannot request files outside the video directory.

2. Avoid exposing sensitive local paths in API responses. Return public result URLs instead of full filesystem paths.

## Bug Fixes

1. Fix any file path issues between the `server` and `processor` folders so the Express server can reliably find videos, the JAR, and output CSV files.

2. Make sure generated files like `.jar`, `.mp4`, `target/`, and output CSV files are ignored and not committed to GitHub.

## Other

1. Add GitHub Actions so tests run automatically when code is pushed.

2. Add logging so video processing and server errors are easier to debug.
