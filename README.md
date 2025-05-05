# Roads2DGenerator

Algorithm for map generation of roads in 2D

(collapse wave function???)

Forked and improved from https://github.com/sea-kg/roadmapgen2d

Example of generated structs (30x30 points):



![example1.png](https://github.com/sea-kg/Roads2DGenerator/blob/main/images/example1.png?raw=true)
![example2.png](https://github.com/sea-kg/Roads2DGenerator/blob/main/images/example2.png?raw=true)
![example3.png](https://github.com/sea-kg/Roads2DGenerator/blob/main/images/example3.png?raw=true)
![example4.png](https://github.com/sea-kg/Roads2DGenerator/blob/main/images/example4.png?raw=true)


## Usage

1. Copy `src/Roads2DGenerator.*` to your project

```cpp
#include "Roads2DGenerator.h"

...

int width = 30;
int height = 30;
Roads2DGenerator road2gen(width, height);
road2gen.generate(0.7);

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

```

## Used in games

### yourCityIsInvadedByAliens

Repo: https://github.com/sea5kg/yourCityIsInvadedByAliens

SDL 2D game

### The Guiding Thread

Description: https://itch.io/jam/sibgamejam-may-2025/rate/3530033

GamePlay: https://youtu.be/pl22Vp3yUuY?si=YqOClG28xD4-bAZ6
