# Roads2DGenerator

Implemented simularity for c++, java.

2D map of roads generation algorithm

(collapse wave function???)

Forked and improved from https://github.com/sea-kg/roadmapgen2d

Example of generated structures (30x30 points):


![example1.png](https://github.com/sea-kg/Roads2DGenerator/blob/main/images/example1.png?raw=true)
![example2.png](https://github.com/sea-kg/Roads2DGenerator/blob/main/images/example2.png?raw=true)
![example3.png](https://github.com/sea-kg/Roads2DGenerator/blob/main/images/example3.png?raw=true)
![example4.png](https://github.com/sea-kg/Roads2DGenerator/blob/main/images/example4.png?raw=true)


## Usage

1. Copy `src/Roads2DGenerator.*` into your project

```cpp
#include "Roads2DGenerator.h"

...
Roads2DGenerator road2gen;

// configure
road2gen.getConfig()
    .setWidth(30)
    .setHeight(30)
    .setDensity(0.7)
;

// You can use the following parameter (nSeedRandom) to reproduce the results.
// If the parameters are the same, then the result will be the same on any machine.
road2gen.getConfig().setSeedInitRandom(std::time(0));
// road2gen.getConfig().setSeedInitRandom(1686154273);

road2gen.getConfig().setPresetExcludes(12, 10, 18, 15);

if (!road2gen.generate()) {
    std::cerr
        << "FAILED. Could not generate. Try again or change input params and try again." << std::endl
        << "Init Seed: " << road2gen.getConfig().getSeedInitRandom() << std::endl
        << "Error Message: " << road2gen.getErrorMessage() << std::endl
        << std::endl;
    return 1;
}

// use a table with true/false
std::vector<std::vector<bool>> vPixelMap = road2gen.exportToPixelMap();
for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
        if (vPixelMap[x][y]) {
            // has road
        } else {
            // no road
        }
    }
}

// or use a table with directional elements
std::vector<std::vector<std::string>> vTable = exportToTable();
for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
        std::string sRoad = vTable[x][y];
        if (sRoad == "cross") {
            // ╬
        } else if (sRoad == "horizontal") {
            // ═
        } else if (sRoad == "vertical") {
            // ║
        } else if (sRoad == "right-down") {
            // ╔
        } else if (sRoad == "left-down") {
            // ╗
        } else if (sRoad == "right-up") {
            // ╚
        } else if (sRoad == "left-up") {
            // ╝
        } else if (sRoad == "left-up-down") {
            // ╣
        } else if (sRoad == "right-up-down") {
            // ╠
        } else if (sRoad == "left-right-down") {
            // ╦
        } else if (sRoad == "left-right-up") {
            // ╩
        } else {
            // nope
        }
    }
}


// json examples
road2gen.exportLikeJsonPixelMapToFile("road2dgen_example_pixelmap.json");

```

## Used in games

### yourCityIsInvadedByAliens

Repo: https://github.com/sea5kg/yourCityIsInvadedByAliens

SDL 2D game

### The Guiding Thread

Description: https://itch.io/jam/sibgamejam-may-2025/rate/3530033

Gameplay: https://youtu.be/pl22Vp3yUuY?si=YqOClG28xD4-bAZ6

Unigine 3D game


## Projects with similar algorithms

### Tank_Game

https://github.com/nickolasddiaz/Tank_Game/

Generator: [core/src/main/java/io/github/nickolasddiaz/utils/TerrainGenerator.java](https://github.com/nickolasddiaz/Tank_Game/blob/master/core/src/main/java/io/github/nickolasddiaz/utils/TerrainGenerator.java)
