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
    Roads2DGenerator road2gen;
    road2gen.getConfig()
        .setWidth(50)
        .setHeight(25)
        .setDensity(0.9)
    ;

    // You can use the following parameter (nSeedRandom) to reproduce the results.
    // If the parameters are the same, then the result will be the same on any machine.
    road2gen.getConfig().setSeedInitRandom(std::time(0));
    // road2gen.getConfig().setSeedInitRandom(1686154273);

    // You can exclude some area
    road2gen.getConfig().setPresetExcludes(12, 10, 18, 15);

    if (!road2gen.generate()) {
        std::cerr
            << "FAILED. Could not generate. Try again or change input params and try again." << std::endl
            << "Init Seed: " << road2gen.getConfig().getSeedInitRandom() << std::endl
            << "Error Message: " << road2gen.getErrorMessage() << std::endl
            << std::endl;
        return 1;
    }
    // std::cout << "Init Seed: " << road2gen.getConfig().getSeedInitRandom() << std::endl;
    road2gen.printMap();

    // json examples
    // road2gen.exportLikeJsonPixelMapToFile("examples/road2dgen_example_pixelmap.json");

    // spline graph for unigine example
    // Roads2DGeneratorUnigineSplineGraph unigineSpl(road2gen.exportLikeGraph());
    // unigineSpl.modifyScale(10);
    // unigineSpl.modifyRandom(10.0, 10.0, 0.5);
    // unigineSpl.exportToSPLFile("examples/road2dgen_example_spline_graph_for_unigine.spl");

    return 0;
}
