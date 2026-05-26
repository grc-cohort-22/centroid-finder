## 1: JavaCV

Pros:
- Popular choice, wrapper for FFmpeg / OpenCV
- Native performance (leverages use of codecs)
- Active development and support

Cons:
- Many native libraries used (large footprint for project)
- Harder to implement (steep learning curve)

## 2: JCodec

Pros:
- Pure Java implementation, no external dependencies
- Simple to implement

Cons:
- Slower performance
- Lower quality encoding

## 3: Video4J

Pros:
- High level wrapper for OpenCV
- Seems to be good for decoding videos into BufferedImage frames?
- Easy to implement

Cons:
- Not as popular as other solutions
- Dependant on external OpenCV installation for video decoding