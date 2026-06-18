for each api call:

GET /api/videos
    simply return the database of videos we have in a json. The video database itself will be within a json.

GET /thumbnail/{filename}
    can be done purely in javascript
    we can use node.js ffmpeg to grab the frame and output it as an image 
    https://www.perplexity.ai/search/c7b3a98c-de07-49f0-a121-b7ed7294d05f

POST /process/{filename}
    can be done using exec approach in express, this uses child processes.
    https://www.perplexity.ai/search/7b15a54e-0f90-4b60-96da-fbdebf2787eb

GET /process/{jobId}/status
    when we start the jobs we can wait for a return before we mark a status as complete.
    until that status is marked complete, we can have this call check for a series of true/false and will return based off that data.
    5 responses are shown, so these can all be implemented with their own conditions.