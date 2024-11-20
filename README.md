# Space Game

## Introduction:

This is a 2D grand strategy 4x game based on Paradox Interactive's grand strategy hit Stellaris, developed in Java using the libGdx framework, with the twist of allowing more in-depth planet development and planet invasion mechanics.

Take control of a civilization as it takes its first steps into the stars. Play any of many randomly generated/custom-built species as you explore the stars, settle alien worlds, and engage in epic 2D 
fleet battles and in-depth land invasions on a thousand worlds.

This is a link to the project road-map: https://github.com/JDSweet/SpaceGame/blob/master/RoadMap.md

## Controls:
1. In the galactic map, select the star system you want to visit.
2. You can click planets generated in each star system to view a window showing debug information.
3. If you click the "spawn ship" button in the bottom-right portion of the screen, the ability to spawn ships is enabled. Anywhere you click, an immobile ship will spawn.
4. In order to disable ship spawning mode, click "spawn ship" again.
5. If ship spawning mode is disabled, and there are currently ships spawned onto the map, you can click anywhere in the star system map, and the ships you spawned in will turn and fly towards that region.
6. They will stop about 10 world units away from their target destination.
------------------------------------------------------------------------------

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
