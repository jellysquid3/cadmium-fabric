Cadmium
===

:warning: Here be dragons. This mod can only be used in a developer workspace, at least until a library replacement
mechanism is developed for production environments. You also shouldn't trust it with any worlds you particularly
care about until more extensive testing is done.

No guarantee is made that this project will receive any further development or that it will even be maintained.

### Preface

The release of Minecraft 1.13 brought the concept of "data fixers" to the game, enabling seamless and incremental
upgrades for old world data. The theory behind the implementation is cutting edge stuff, but in the real world, things
are less than ideal. 

During start-up, the game will build (and optimize) data fixers for every supported version schema the game can
upgrade from. This process accounts for a huge portion (>85%) of the time the game spends sitting at the loading screen,
and will often bring most systems to their knees while it does so due to how it maximizes for parallelization.

Unfortunately, this problem has only continued to escalate with recent releases of the game. Minecraft 1.16 introduces
many new data fixers, and as if insult to injury, seemingly no longer waits for them to be finished loading before the
loading screen vanishes, leading to excessive lag on the main menu while they continue to build.

This mod attempts to solve, or at least greatly improve, the problem of data fixers being computationally expensive to
compute. Ultimately the best solution would be to pre-compile the optimized data fixers, so that the client doesn't need
to spend its time constantly re-computing them... but alas that is left to Mojang.

#### Cloning the repository

This project makes use of [Git submodules](https://git-scm.com/book/en/v2/Git-Tools-Submodules). When cloning the
repository, you must be sure to clone the submodules.

If you're cloning the repository for the first time, use the following:

```
git clone --recurse-submodules https://github.com/jellysquid3/cadmium-fabric.git
```

Otherwise, to clone the submodules in an already cloned repository:

```
git submodule update --init --recursive
```

### License

Sodium is made available under the MIT license, a free and open-source license. For more information, please see the
[license file](https://github.com/jellysquid3/cadmium-fabric/blob/1.15.x/dev/LICENSE.txt).
