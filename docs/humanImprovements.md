## refactoring code

- What improvements can you make to the design/architecture of your code?
> Splitting up chunks of code into smaller, more reusable parts.
- How can you split up large methods or classes into smaller components?
> By spreading out portions of code that can be used elsewhere.
- Are there unused files/methods that can be removed?
> All files are used, certain methods could be removed on a case-by-case basis. (depends on if code can be reused)
- Where would additional Java interfaces be appropriate?
> For reusablility purposes (probably the video processing classes)
- How can you make things simpler, more-usable, and easier to maintain?
> As stated before, splitting up code into smaller chunks that can be reused.
- Other refactoring improvements?
> None that come to mind, may change as improvements are made.

## adding tests

- What portions of your code are untested / only lightly tested?
> ExtractVideoData and VideoSummaryApp don't have tests, ProcessFrame could use more tests.
- Where would be the highest priority places to add new tests?
> ExtractVideoData is the highest priority for adding tests.
- Other testing improvements?
> Proper tests for server, though that could be tackled by manual testing with Postman / Insomnia?

## improving error handling

- What parts of your code are brittle?
> Parts where try/catch blocks are used, as potential errors can cause issues later down the line
- Where could you better be using exceptions?
> Where data is passed around, being sure to catch potential issues
- Where can you better add input validation to check invalid input?
> Same answer as last.
- How can you better be resolving/logging/surfacing errors? Hint: almost any place you're using "throws Exception" or "catch(Exception e)" should likely be improved to specify the specific types of exceptions that might be thrown or caught.
> Hint provided is a good suggestion.
- Other error handling improvements?
> None that come to mind, may change as improvements are made.

## writing documentation

- What portions of your code are missing Javadoc/JSdoc for the methods/classes?
> Mainly the video processing portions on the processor side.
- What documentation could be made clearer or improved?
> Same as above, video processing classes in processor.
- Are there sections of dead code that are commented out?
> Most, if not all of the code is used somewhere, so no.
- Where would be the most important places to add documentation to make your code easier to read?
> Specific functions to improve understanding of what the code does.
- Other documentation improvements?
> Adding comments to help explain what specific functions do.

## improving performance (optional)

- What parts of your code / tests run particularly slowly?
> Probably the video processing itself? It does takes some time to process larger videos, could potentially be improved
- What speed improvements would most make running / maintaining your code better?
> Not sure about speed improvements, but using smaller, less packed classes could improve speed
- Other performance improvements?
> None that come to mind, may change as improvements are made.

## hardening security (optional)

- What packages / images are out of date / have security issues?
> None.
- Where could you have better input validation in your code to prevent malicious use?
> When inputting the path, color, and threshold values, could be improved.
- Other security improvements
> Adding regular expressions to parts where user input is involved.

## bug fixes (optional)

- What bugs do you know exist?
> No major bugs present, though there may be some that we haven't come across.
- What parts of the code do you think might be causing them?
> Same as last answer.
- Other bug fix improvements?
> Same as last answer.

## other

- Any other improvements in general you could make?
> Nothing else that comes to mind, may change as improvements are made.