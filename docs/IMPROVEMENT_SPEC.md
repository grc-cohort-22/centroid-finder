# Centroid Finder — Improvement Spec

**Status:** Draft · 2026-08-08
**Goal:** Take a finished, working class project and make it read as professional
engineering work to someone who spends ninety seconds on it.

This is a **repackaging and polish** spec, not a rebuild. The code works, is tested,
and is deployed. What holds it back is presentation, code organization, and two real
defects in the build and deploy path.

---

## 1. Positioning

Centroid Finder's job in the portfolio is to demonstrate what the other projects
don't:

- **Java 17 + Spring Boot** — the enterprise stack, absent from PugetScope (TS/Node)
  and AtlasQuest (Kotlin)
- **Real algorithms** — DFS connected-component labeling, Euclidean color-distance
  binarization, threshold sensitivity. Actual CS, not CRUD.
- **Async job orchestration** — submit → poll → fetch, with a separate processor
  process for long-running video work. This is the strongest interview material in
  the repo and is currently near-invisible in the README.
- **An unsaturated domain** — video analysis and centroid tracking is the opposite
  of another ecommerce app.

Everything below serves that positioning. Work that doesn't is out of scope.

## 2. Success criteria

Done when:

1. The repo does not display "forked from grc-cohort-22/centroid-finder"
2. A reader understands what it does, sees it working, and grasps the architecture
   without scrolling past a `docker run` command
3. A Java reviewer opening `src/main/java/...` sees a package structure, not 43
   files in one directory
4. `git clone && docker build .` produces a running image with no host-side Maven build
5. A failing test blocks deployment (it currently does not)
6. The container image is under 400 MB (currently very likely over 1 GB)

---

## Workstream A — Repository presentation

Highest return per hour. Do this first.

### A1. Detach the fork

GitHub renders "forked from grc-cohort-22/centroid-finder" directly beneath the repo
name, above the description and README. It reads as *class assignment* before a
reviewer sees any of your work, and visually attributes 156 commits to someone else's
project.

- [ ] Request fork detachment via GitHub Support, **or** push the existing history to
      a fresh repo (`centroid-finder`) and archive the fork
- [ ] Keep history intact — the 15 upstream commits from April 2025 are the course
      skeleton and should remain, with credit (see A2)

### A2. Rewrite the README

Current structure is roughly 75% local setup instructions. Target structure:

1. **One-sentence description** of what it does
2. **The ensantina threshold GIF, high up.** It is the single best asset in the repo —
   a salamander resolving and dissolving as the threshold slides, which communicates
   the actual engineering problem better than paragraphs would
3. **Live demo link** + frontend screenshot
4. **Architecture** — expanded well past the current three lines. Cover the async job
   pipeline explicitly: submit-job → poll-status → fetch-CSV, with video processing
   handed to a separate JVM process. Include `docs/Architecture_Diagram.png` inline.
5. **The interesting problems** — threshold sensitivity and lighting variance (already
   written and good), plus job lifecycle handling and why video work runs out-of-process
6. **Infrastructure** — ECS Fargate, RDS, EFS, ALB, CloudFront, Terraform modules,
   GitHub Actions deploying via OIDC with no static AWS keys. This is currently
   undersold to the point of invisibility.
7. **Running it locally** — condensed. Prefer `docker compose up` over the current
   multi-line `docker run` invocations.
8. **Attribution, at the bottom:** two-person course project, built on a provided
   skeleton, with a specific statement of what you owned.

Two things to fix specifically:

- [ ] Move "This was built as a part of a software development course" out of
      paragraph two. Keep it — it's honest — but leading with it invites the reader
      to discount everything after.
- [ ] Be specific about ownership. Commit split is 80 (konradkelly) / 58 (Fkarau) /
      18 (instructor skeleton). Vague group projects get discounted wholesale;
      "I built the async job pipeline, the Spring Boot API, and the AWS deployment"
      does not.

### A3. Repo metadata

- [ ] Set the GitHub description (currently empty)
- [ ] Add topics: `java`, `spring-boot`, `computer-vision`, `image-processing`,
      `video-processing`, `aws`, `terraform`, `ecs-fargate`, `javacv`
- [ ] Link the frontend repo prominently in the README, and link back from it

### A4. Clean out `docs/`

Nine files read as working scratch rather than documentation: `ideas.md`, `notes.md`,
`plan.md`, `polish.md`, `options.md`, `goals.md`, `priorityImprovements.md`,
`humanImprovements.md`, `robotImprovements.md`.

- [ ] Move to a `working-notes` branch or delete
- [ ] Keep and polish: `aws-deployment-spec.md`, `backend-deployment.md`,
      `frontend-deployment.md`, `video-frame-reader-factory.md`,
      `Architecture_Diagram.png`, `server_diagram.png`, and the media assets

**Effort: 4–6 hours.**

---

## Workstream B — Package structure

All 43 classes sit in the single flat package `io.github.konradkelly.centroidfinder` —
controllers, DTOs, domain types, config, JPA entities, and JavaCV adapters in one
directory. A Java reviewer notices this immediately, and it is the one thing in the
repo that reads as inexperienced.

Proposed layout:

```
io.github.konradkelly.centroidfinder
├── image/     ColorDistanceFinder, EuclideanColorDistance, ImageBinarizer,
│              DistanceImageBinarizer, BinaryGroupFinder, DfsBinaryGroupFinder,
│              ImageGroupFinder, BinarizingImageGroupFinder, Group, Coordinate
├── video/     VideoFrameReader, JavaCvVideoFrameReader, FrameSample,
│              FrameCentroidAnalyzer, VideoCentroidPipeline, TimestampedCentroidResult,
│              CsvResultWriter, VideoProcessingConfig, ThumbnailService,
│              VideoCatalogService
├── job/       JobEntity, JobRepository, JobService, JobStatus, JobStatusMapper,
│              JobStore, JobProcessLauncher, DefaultJobProcessLauncher, JobProcessResult
├── web/       VideoController, ApiExceptionHandler, ErrorResponseDto,
│              JobStatusResponseDto, StartJobResponseDto, NotFoundException,
│              ValidationException, ServerException
├── config/    AsyncConfiguration, StaticFileConfig, ServerPathsProperties
└── (root)     ServerApplication, VideoProcessorApp, CliArgumentParser
```

The `image/` package is a nice side effect: it isolates the pure-algorithm code with
no Spring or JavaCV dependency, which makes the "this part is real CS" story visually
obvious in the directory tree.

- [ ] Move classes (IDE refactor — imports update automatically)
- [ ] Mirror the structure under `src/test/java`
- [ ] Update `exec.mainClass` and the Spring Boot plugin `mainClass` in `pom.xml` if
      the entrypoint packages change
- [ ] Verify `mvn clean package` and both test profiles still pass

**Effort: 1–2 hours.** Mechanical, but do it on its own branch with a green CI run.

---

## Workstream C — Build and container

### C1. Container image size — the biggest technical win available

`pom.xml` depends on `javacv-platform` 1.5.8, which bundles FFmpeg and OpenCV native
binaries for **every** supported platform: Windows, macOS, and Linux across x86, x86_64,
and ARM. The assembly plugin then packages all of it into
`centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar`, and the Dockerfile copies
that into a Linux-only container.

The image is almost certainly north of 1 GB, and the overwhelming majority is native
libraries for operating systems it will never run on.

- [ ] Replace `javacv-platform` with `javacv` plus explicit `linux-x86_64`-classified
      `ffmpeg` and `opencv` artifacts (or set the `javacpp.platform` property to
      `linux-x86_64`)
- [ ] Measure before and after with `docker images`
- [ ] **Put the number in the README.** "Cut the container image from 1.4 GB to 280 MB
      by restricting JavaCPP native bundles to the target platform" is a concrete,
      quantified engineering result, and those are rare in portfolio projects.

### C2. Multi-stage Dockerfile

The Dockerfile copies pre-built jars out of `target/`, so `docker build .` fails on a
clean clone unless the user runs Maven first. That breaks the most common thing a
reviewer tries.

- [ ] Stage 1: `maven:3.9-eclipse-temurin-17` → `mvn package`
- [ ] Stage 2: `eclipse-temurin:17-jre-jammy` → copy only the two jars
- [ ] Add a non-root `USER`
- [ ] Add `HEALTHCHECK` against the Actuator health endpoint (the
      `spring-boot-starter-actuator` dependency is already present)
- [ ] Layer-cache dependencies: copy `pom.xml` and run `mvn dependency:go-offline`
      before copying `src/`

### C3. Small build fixes

- [ ] **Java version inconsistency:** `pom.xml` and the Dockerfile target 17, but the
      README's `docker run` example uses `eclipse-temurin:25-jre`. Pick one — Java 21
      LTS is the reasonable target — and make all three agree.
- [ ] Bump `javacv` from 1.5.8 (current is 1.5.11)
- [ ] Drop the `<excludes>**/*Test.java</excludes>` from `maven-compiler-plugin`. Test
      sources are compiled by `testCompile`, not `compile`, so this exclusion on the
      main compile does nothing and reads as confusion.
- [ ] Move off `1.0-SNAPSHOT` — tag a `1.0.0` release
- [ ] Add JaCoCo and publish a coverage number

**Effort: 3–4 hours**, dominated by getting the JavaCPP classifiers right.

---

## Workstream D — CI/CD correctness

**This section contains the only actual defect in the repo. Fix it first.**

### D1. Deploy is not gated on tests

`run-tests.yml` and `deploy.yml` both trigger on `push: branches: [main]`, as two
independent workflows. Nothing connects them. `deploy.yml` then runs
`mvn -B package -DskipTests`.

A commit that breaks every test in the project deploys to production anyway.

Fix options, in order of preference:

1. Merge into one workflow with `needs: [unit-tests, integration-tests]` on the deploy job
2. Keep them separate and use `workflow_run` so deploy triggers only on CI success
3. At minimum, run tests inside `deploy.yml` before the build step

PugetScope already solves this with a reusable `_ci.yml` (`on: workflow_call`) that both
`ci.yml` and `deploy.yml` invoke, with `deploy` declaring `needs: ci`. Port that pattern
here for consistency across repos.

### D2. Supply chain and safety

The pipeline is otherwise in good shape — OIDC with `id-token: write` and no static
AWS keys, SHA-tagged images, a `deploy-prod` concurrency group, and
`wait-for-service-stability`. Worth adding:

- [ ] Trivy scan on the built image, failing on HIGH/CRITICAL
- [ ] Dependabot for Maven, GitHub Actions, and Docker base images
- [ ] `gitleaks` in CI (the repo is currently clean — `terraform.tfvars` and
      `tfstate` are correctly untracked — and it should stay that way)
- [ ] ECS deployment circuit breaker with `rollback: true`, so a bad deploy reverts
      itself instead of hanging until the stability timeout

**Effort: 1–2 hours.**

---

## Workstream E — Optional substance

Only after A–D. Each is genuinely resume-bearing; none is required.

- **Benchmark the pipeline.** Frames per second across resolutions, single- vs
  multi-threaded frame analysis. Publish a table. Quantified performance work is
  uncommon in portfolios and reads as senior.
- **Parallelize frame processing.** The per-frame binarize-and-find work is
  embarrassingly parallel. A `parallelStream()` or a bounded executor plus a
  before/after benchmark is a strong, contained addition.
- **Replace the DFS with union-find**, or make the algorithm pluggable, and compare.
  The current `DfsBinaryGroupFinder` is recursive — worth checking whether it
  stack-overflows on a large connected region, since that's a real bug if so and a
  good story either way.
- **Observability.** Actuator is already a dependency; expose Micrometer/Prometheus
  metrics for job duration, queue depth, and failure rate.
- **A short demo video** — upload a clip, watch the job run, download the CSV, plot
  the track.

---

## Out of scope

- Rewriting the algorithm core. It works and it's tested.
- Migrating off Spring Boot, or off ECS.
- Merging the frontend repo into this one. Link them well instead.
- Removing your partner's contributions or obscuring the group-project origin.
  Specific, honest attribution is more credible than vagueness.

---

## Sequencing

| Order | Workstream | Effort | Why |
|---|---|---|---|
| 1 | **D1** — gate deploy on tests | 30 min | A real defect; fix before anything else |
| 2 | **A** — presentation | 4–6 h | Highest return per hour |
| 3 | **B** — package structure | 1–2 h | Mechanical, big perceptual payoff |
| 4 | **C** — build and container | 3–4 h | Produces a quantified result for the README |
| 5 | **D2** — supply chain | 1–2 h | Rounds out the DevOps story |
| 6 | **E** — substance | open | Only if the earlier work is done |

**Core (1–5): roughly two focused days.**

Do each on its own branch with a green CI run before merging. The PR history is itself
part of what a reviewer sees.
