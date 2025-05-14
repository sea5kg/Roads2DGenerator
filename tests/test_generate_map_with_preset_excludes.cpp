#include "Roads2DGenerator.h"

#include <vector>
#include <iostream>

int main() {
  Roads2DGenerator road2gen;
  road2gen.getConfig()
      .setWidth(50)
      .setHeight(25)
      .setDensity(0.9)
      .setPresetExcludes(20, 5, 30, 15)
      // .setSeedInitRandom(1747235954)
  ;

  if (!road2gen.generate()) {
    return 1;
  }
  road2gen.printMap();

  const std::vector<std::vector<bool>> &pixelmap = road2gen.exportLikePixelMap();

  std::string sGot = "";
  bool bGood = true;
  for (int y = 5; y <= 15; y++) {
    for (int x = 20; x <= 30; x++) {
      if (pixelmap[x][y] == true) {
        sGot += "1";
        bGood = false;
      } else {
        sGot += "0";
      }
    }
    sGot += "\n";
  }

  if (!bGood) {
    std::cerr
      << "Expected empty zone " << std::endl
      << ", but got " << sGot
      << std::endl;
    return 1;
  }

  return 0;
}
