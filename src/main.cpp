#include <filesystem>
#include <string>

#include <cstdlib>
#include <iostream>
#include <fstream>
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
        .setDensity(0.4)
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

    {
        // example export like svg
        int cell_size = 10;
        std::string sFilepath = "roads.svg";
        std::ofstream fw(sFilepath, std::ofstream::out);
        if (!fw.is_open()) {
            std::cerr << "Could not open file " << sFilepath << std::endl;
            return false;
        }
        fw
            << "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
            << "<svg\n"
            << "   width=\"" << road2gen.getConfig().getWidth() * cell_size << "\"\n"
            << "   height=\"" << road2gen.getConfig().getHeight() * cell_size << "\"\n"
            << "   version=\"1.1\"\n"
            << "   xmlns=\"http://www.w3.org/2000/svg\"\n"
            << "   xmlns:svg=\"http://www.w3.org/2000/svg\">\n"
        ;

        std::vector<std::vector<bool>> pixelMap = road2gen.exportLikePixelMap();
        for (int x = 0; x < pixelMap.size(); x++) {
            std::vector<bool> column = pixelMap[x];
            for (int y = 0; y < column.size(); y++) {
                if (column[y]) {
                    fw
                        << "  <rect\n"
                        << "    style=\"opacity:1.0;fill:#ffffff;fill-rule:evenodd;stroke-width:1.0;stroke-linecap:round;stroke-linejoin:round;stroke-dashoffset:19.02\"\n"
                        << "    width=\"" << cell_size << "\"\n"
                        << "    height=\"" << cell_size << "\"\n"
                        << "    x=\"" << x * cell_size << "\"\n"
                        << "    y=\"" << y * cell_size << "\" />\n"
                    ;
                }
            }
        }

        fw
            << "</svg>\n"
        ;
        fw.close();
    }


    return 0;
}
