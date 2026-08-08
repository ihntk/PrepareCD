# AGENTS.md

JavaFX + Maven (Java 17, JPMS) desktop tool that prepares compressor technical documentation: order XLS/LUX parsing,
drawing copy/rename, CD structure, working with external file managers. UI is a dual-panel file manager; most logic
lives in a CLI-compatible singleton.

## Build & run

- `mvn clean package` — shaded fat jar at `target/PrepareCD-<version>.jar` (shade plugin, main class
  `com.af.igor.prepcd.PrepareCD`).
- `mvn clean javafx:run` — run the GUI. Depends on the module name `com.af.igor.prepcd.preparecd` (already wired in
  pom).
- Wrapper `./mvnw` (Maven 3.8.5). Builds need network: `-o` offline fails because some plugins are not cached.
- No lint/format step; compile is the only automated check. `mvn test` runs **zero** tests (see below).

## Tests (gotcha)

- `src/test/**` is not JUnit: `MainAppTest`, `prepareCDTest`, `MachineExcelParserTest` are plain classes with `main()`
  used as manual drivers. junit-jupiter is declared in pom but never used.
- `WORKMODE_SETTINGS.md` references a `WorkModeSettingsTest.java` that does not exist — ignore that claim.
- Verify with `mvn -q compile` or by running the app; manual testing is the norm.

## Architecture

- Two entry points: `com.af.igor.prepcd.PrepareCD` (JavaFX `Application`, loads `view/RootLayout.fxml` +
  `view/MainFrame.fxml`) and CLI `MainApp.main → run(args)` with flags `-n <machine>`, `-x` (xls), `-c` (cd), `-i` (
  installation), `-m` (machine files), `-g` (GUI), `-v`, `-t`.
- `MainApp` is a singleton facade holding nearly all business logic (copy/rename, XLS/LUX parsing, launching file
  managers, work modes); controllers delegate to it. `Machine` is the order model; `util/` has the Apache POI parsers (
  `ExcelParser`, `LuxParser`, `MachineExcelParser`), `FSHelper`, and enums `MachinesCode`, `BaseDrawingPaths`,
  `WorkMode`.
- Machine lookup: `MainApp.initializeMachine(name)` finds the order's LUX file (`xlsx/xltx/xls`) matching the machine
  name in the remote `H_MACHINES` dir (local dir in OFFLINE mode), then sets `Machine`. Machine types map to codes via
  the `MachinesCode` enum; base drawings via `BaseDrawingPaths`.
- Work modes (GENERAL/REMOTE/OFFLINE) and file-manager choice persist via Java Preferences API (Windows registry /
  `~/.java/.userPrefs` on Linux).

## Config (critical)

- All directory paths are read from `~/.PrepareCD/PrepareCD.conf` (Windows-style, e.g. `c:/Data/my_docs/...`) in the
  `MainApp` constructor. `src/main/resources/PrepareCD.conf` is only a template, not the live config. On Linux the app
  misbehaves unless that file has valid local paths.
- Logging goes to `LOGFILE`/`LOGERROR` from that config via `log/SimpleLogger`.

## Module system

- `module-info.java` declares module `com.af.igor.prepcd.preparecd`. New dependencies must be added to BOTH `pom.xml`
  and `module-info.java` (`requires`/`opens`). It requires `org.apache.commons.collections4`, which is only a transitive
  dep of `poi-ooxml` — keep it.

## Repo conventions

- `src/main/kotlin`, `src/test/kotlin`, `gradle/`, `.gradle/` are stale leftovers — no Kotlin and no Gradle; Maven is
  the only build. Don't add Kotlin sources.
- Version: `pom.xml` is the source of truth (0.9.6). README.md's "0.8.8.6" is stale. Commit messages follow
  `<what changed>. Version X.Y.Z`.
- `dependency-reduced-pom.xml` is shade-generated and untracked — don't commit it.
- Docs/comments are a mix of English, Ukrainian, and Russian; README.md is bilingual.
