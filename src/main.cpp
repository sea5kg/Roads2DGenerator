#include <filesystem>
#include <string>

#include <cstdlib>
#include <iostream>
#include <ctime>

#include "Roads2DGenerator.h"
#include "Roads2DGeneratorUnigineSplineGraph.h"

#ifdef _WIN32
int wmain(int argc, wchar_t *argv[])
#else
int main(int argc, char *argv[])
#endif
{
    int width = 30;
    int height = 30;
    Roads2DGenerator road2gen(width, height);

    // You can use the following parameter (nSeedRandom) to reproduce the results.
    // If the parameters are the same, then the result will be the same on any machine.
    unsigned int nSeedRandom = std::time(0);
    // unsigned int nSeedRandom = 1686154273;

    road2gen.generate(0.7, nSeedRandom);
    road2gen.printMap();
    road2gen.getSeedRandom();

    // std::cout << "1" << std::endl;
    Roads2DGeneratorUnigineSplineGraph unigineSpl(road2gen.exportToGraph());
    unigineSpl.modifyScale(10);
    unigineSpl.modifyRandom(10.0, 10.0, 0.5);

    unigineSpl.exportToSPLFile("test.spl");
    return 0;
}