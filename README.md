# Centroid Finder

**Tracks an object through a video by colour.** Give it a target colour and a
distance threshold; it binarises every frame, finds connected regions of matching
pixels, and writes the centroid of the largest one to a timestamped CSV — one row
per second of footage.

Java 17 · Spring Boot · JavaCV/FFmpeg · PostgreSQL · ECS Fargate via Terraform

![Threshold sweep over an ensantina salamander](docs/ensantina-thresholds.gif)

*The same frame at increasing distance thresholds. Too tight and the animal breaks
into fragments; too loose and the background floods in. Finding the value that holds
an object together without swallowing the scene is the actual problem.*

---

## How it works

Three stages, each independently testable:

**Binarise.** Every pixel is scored by Euclidean distance from the target colour in
RGB space. Anything inside the threshold becomes white, everything else black. The
threshold runs 0–442, because 442 is the distance between opposite corners of the
RGB cube — the largest possible distance between two colours.

**Find groups.** A depth-first search labels connected regions of white pixels. The
search is **iterative**, using an explicit `ArrayDeque` rather than recursion — a
large connected region in a high-resolution frame would otherwise be an easy way to
overflow the stack.

**Locate.** Each group's centroid is the mean position of its pixels. Groups are
ordered by size, and the largest is written out as that frame's result. Frames with
no match emit `-1,-1` rather than being skipped, so the CSV stays aligned to the
video's timeline.

Binarisation reads the frame with a single bulk `getRGB(0, 0, w, h, …)` into an
`int[]` rather than calling `getRGB(x, y)` per pixel. Per-pixel access goes through
the `BufferedImage` colour model on every call, and this runs for every pixel of
every frame. On a 478-second clip (11,477 frames) that change alone took a full
run from **4m48s to 3m06s — 35% faster**, with byte-for-byte identical output.

![Web UI: original frame, binary mask with centroid overlay, and a completed job](docs/preview-ensantina.png)

*The preview page: source frame, the binary mask the algorithm actually sees, the
computed centroid as an orange dot, and the finished job offering its CSV.*

---

## Architecture

```
POST /process/{filename}          →  job accepted, UUID returned immediately
   │
   ├─ Spring @Async thread pool (4 core / 8 max, queue 100)
   │     │
   │     └─ ProcessBuilder → separate JVM  ──►  video-processing CLI
   │                                              (JavaCV/FFmpeg frame decode)
   │
   └─ JobStore (PostgreSQL)  ←  PROCESSING → DONE | ERROR

GET  /process/{jobId}/status      →  poll until DONE, then fetch the CSV
```

**Video work runs in its own JVM, not just its own thread.** Each job is launched
with `ProcessBuilder` against the CLI jar. FFmpeg decoding happens in native code
reached through JNI, where a malformed file can take down the whole JVM rather than
raising a catchable exception. A crashed subprocess costs one job; the API stays up.
It also makes the timeout enforceable — `JOB_TIMEOUT` is a real wall clock, and a
process that overruns is destroyed rather than left to leak a thread.

The tradeoff is honest: process startup costs a JVM launch per job, and stdout has to
be drained on a separate thread or the subprocess blocks once its output buffer
fills. For minutes-long video decodes, that overhead is noise against the isolation
it buys.

**The API never blocks on processing.** Submitting returns a UUID immediately; status
lives in Postgres, so it survives a restart of the API. Statuses are `PROCESSING`,
`DONE`, `ERROR` — an errored job carries its diagnostic message rather than
disappearing.

---

## API

| Method | Route | Description |
|---|---|---|
| `GET` | `/api/videos` | List filenames in `VIDEOS_DIR` |
| `GET` | `/thumbnail/{filename}` | First frame as JPEG |
| `POST` | `/process/{filename}?targetColor=FF0000&threshold=100` | Queue a job, returns `{ "jobId": "..." }` |
| `GET` | `/process/{jobId}/status` | Current status and result location |
| `GET` | `/videos/{filename}` | Fetch the source video |
| `GET` | `/results/{filename}` | Download the result CSV |

```json
// while running
{ "status": "processing" }

// on success
{ "status": "done", "result": "results/video_FF0000_100.csv" }

// on failure
{ "status": "error", "error": "Job timed out after 10m" }
```

Output CSV — one row per decoded frame, timestamped in seconds, with `-1,-1`
where nothing matched:

```
timestamp,x,y
0,345,211
0.041708,344,211
0.083416,344,210
0.125125,-1,-1
```

---

## Running it

Requires Java 17+, Maven, and Docker.

```bash
mvn clean package
```

That produces three artifacts in `target/`, and picking the wrong one is the
easiest way to lose ten minutes:

| Jar | What it is |
|---|---|
| `…-boot.jar` | The Spring Boot server. This is the one you run. |
| `…-jar-with-dependencies.jar` | The standalone video-processing CLI, also what the server spawns per job. |
| `…-SNAPSHOT.jar` | Plain classes, no manifest and no dependencies. Not runnable on its own. |

**With PostgreSQL:**

```bash
docker compose up -d
```

```bash
java -jar target/centroid-finder-1.0-SNAPSHOT-boot.jar
```

**Without PostgreSQL** — in-memory H2, nothing else to start:

```bash
java -Dspring.profiles.active=test -jar target/centroid-finder-1.0-SNAPSHOT-boot.jar
```

Either way the server comes up on `http://localhost:8080`.

**As a CLI, no server involved:**

```bash
java -jar target/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar input.mp4 output.csv FF0000 100
```

Arguments are `inputPath outputCsv targetColor(hex) threshold(0-442)`.

### Configuration

| Variable | Default | Purpose |
|---|---|---|
| `VIDEOS_DIR` | `./videos` | Where source videos are read from |
| `RESULTS_DIR` | `./results` | Where result CSVs are written |
| `JOB_TIMEOUT` | `10m` | Wall-clock limit before a job subprocess is destroyed |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/centroid_finder` | |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | |

---

## Tests

163 JUnit 5 tests across 15 classes, split into two profiles.

```bash
mvn test
```

```bash
mvn -P integration test
```

Unit tests cover the algorithm in isolation — colour distance, binarisation,
connected-group discovery, CSV formatting, CLI argument parsing. Integration tests
exercise the job repository against a real PostgreSQL and the controller through the
Spring context. Both run on every push and pull request, and gate the deploy
workflow (`.github/workflows/_ci.yml`).

---

## Deployment

Infrastructure is Terraform, under `terraform/`: ECS Fargate for the API, RDS
PostgreSQL, EFS for video and result storage, an ALB, ECR, and GitHub OIDC roles so
CI deploys without long-lived AWS keys. See [`terraform/README.md`](terraform/README.md).

The React frontend is a separate repository —
[Salamander-Labs](https://github.com/konradkelly/Salamander-Labs) — deployed to
S3 + CloudFront using `frontend-infra-template/`.

The AWS environment is not kept running continuously; an idle Fargate service plus
RDS is real money for a demo. `.github/workflows/deploy.yml` is therefore manual
trigger only, and documents how to bring the environment back up.

Design notes live in [`docs/`](docs/): the
[AWS deployment spec](docs/aws-deployment-spec.md), the
[backend](docs/backend-deployment.md) and [frontend](docs/frontend-deployment.md)
deployment guides, and a note on the
[video frame reader factory](docs/video-frame-reader-factory.md).

---

## Notes

The hard part was never the depth-first search. It was that the correct threshold
depends on lighting, on the animal's own colour variation, and on what else happens
to be in frame — and there is no value that works for every video. The GIF at the
top is the honest version of that: a parameter sweep, not a solved problem.

Built with [Fredrick Karau](https://github.com/Fkarau), starting from a course
skeleton. Sample footage is of an ensantina salamander.
