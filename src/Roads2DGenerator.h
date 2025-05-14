/*
MIT License

Copyright (c) 2021-2023 Evgenii Sopov (mrseakg@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

// original source-code: https://github.com/sea-kg/Roads2DGenerator

#ifndef __ROADS_2D_GENERATOR_H__
#define __ROADS_2D_GENERATOR_H__

#include <string>
#include <vector>
#include <map>

class Roads2DGeneratorSafeLoop {
    public:
        Roads2DGeneratorSafeLoop(int nMaxLoop);
        void doIncrement();
        bool isOverMax();
        int getLoopNumber();
    private:
        int m_nMaxLoop;
        int m_nCurrentLoop;
};

class Roads2DGeneratorPseudoRandom {
    public:
        Roads2DGeneratorPseudoRandom();
        void setInitSeed(unsigned int nSeed);
        unsigned int getNextRandom();
        unsigned int getInitSeed();
        unsigned int getSeed();
    private:
        unsigned int m_nSeed;
        unsigned int m_nInitSeed;
};

class Roads2DGeneratorPoint {
    public:
        Roads2DGeneratorPoint();
        Roads2DGeneratorPoint(int x, int y);
        int getX() const;
        int getY() const;
    private:
        int m_nX;
        int m_nY;
};

class Roads2DGeneratorGraph {
    public:
        Roads2DGeneratorGraph();
        int findOrAddPointGetIndex(Roads2DGeneratorPoint point);
        void addConnection(int index1, int index2);
        const std::vector<Roads2DGeneratorPoint> &getPoints() const;
        const std::vector<std::pair<int,int>> &getConnections() const;

    private:
        std::vector<Roads2DGeneratorPoint> m_vPoints;
        std::vector<std::pair<int,int>> m_vConnections;
};

class Roads2DGeneratorConnectedComponent {
    public:
        Roads2DGeneratorConnectedComponent();
        bool hasPoint(Roads2DGeneratorPoint point);
        void addPoint(Roads2DGeneratorPoint point);
        const std::vector<Roads2DGeneratorPoint> &getPoints() const;
        bool hasIntersection(const Roads2DGeneratorConnectedComponent &component);
        void merge(const Roads2DGeneratorConnectedComponent &component);

    private:
        std::vector<Roads2DGeneratorPoint> m_vPoints;
};

class Roads2DGeneratorConnectedComponents {
    public:
        Roads2DGeneratorConnectedComponents();
        void addConnectedPoints(Roads2DGeneratorPoint point1, Roads2DGeneratorPoint point2);
        std::vector<Roads2DGeneratorConnectedComponent> getComponents();

    private:
        void mergeComponents();

        std::vector<Roads2DGeneratorConnectedComponent> m_vComponents;
};

class Roads2DGeneratorConfig {
    public:
        Roads2DGeneratorConfig();
        Roads2DGeneratorConfig &resetConfig();
        Roads2DGeneratorConfig &setWidth(int val);
        int getWidth() const;
        Roads2DGeneratorConfig &setHeight(int val);
        int getHeight() const;
        Roads2DGeneratorConfig &setDensity(float nDensity);
        float getDensity() const;
        int getMaxInitPoints() const;
        Roads2DGeneratorConfig &setSeedInitRandom(int val);
        int getSeedInitRandom() const;
        Roads2DGeneratorConfig &setMaxAllowInitPointsTries(int val, bool bSetByUser = true);
        bool isSetByUserMaxAllowInitPointsTries() const;
        int getMaxAllowInitPointsTries() const;
        Roads2DGeneratorConfig &setMaxAllowMoveDiagonalTailsTries(int val, bool bSetByUser = true);
        bool isSetAsUserMaxAllowMoveDiagonalTailsTries() const;
        int getMaxAllowMoveDiagonalTailsTries() const;
        Roads2DGeneratorConfig &setMaxAllowConnectUnunionRoadsTries(int val, bool bSetByUser = true);
        bool isSetByUserAllowConnectUnunionRoadsTries() const;
        int getMaxAllowConnectUnunionRoadsTries() const;
        Roads2DGeneratorConfig &setMaxAllowRemoveAllShortCiclesLoopTries(int val, bool bSetByUser = true);
        bool isSetAsUserMaxAllowRemoveAllShortCiclesLoopTries() const;
        int getMaxAllowRemoveAllShortCiclesLoopTries() const;

        Roads2DGeneratorConfig &setPresetExcludes(int x_start, int y_start, int x_end, int y_end);
        const std::map<std::pair<int,int>, bool> &getPresets();

    private:
        int m_nWidth;
        int m_nHeight;
        float m_nDensity;
        int m_nSeedInitRandom;
        int m_nMaxAllowInitPointsTries;
        bool m_bSetByUserMaxAllowInitPointsTries;
        int m_nMaxAllowMoveDiagonalTailsTries;
        bool m_bSetByUserMaxAllowMoveDiagonalTailsTries;
        int m_nMaxAllowConnectUnunionRoadsTries;
        bool m_bSetByUserMaxAllowConnectUnunionRoadsTries;
        int m_nMaxAllowRemoveAllShortCiclesLoopTries;
        bool m_bSetByUserMaxAllowRemoveAllShortCiclesLoopTries;
        std::map<std::pair<int,int>, bool> m_presets;
};

class Roads2DGenerator {
    public:
        Roads2DGenerator();
        const std::string &getErrorMessage();
        Roads2DGeneratorConfig &getConfig();
        bool generate(const Roads2DGeneratorConfig &);
        bool generate();
        void printMap();
        std::vector<std::vector<std::string>> exportLikeTable();
        std::vector<std::vector<bool>> exportLikePixelMap();
        Roads2DGeneratorGraph exportLikeGraph();
        std::string exportLikeJsonPixelMap();
        bool exportLikeJsonPixelMapToFile(const std::string &sFilepath);

    private:
        void resetMap();
        void initPresets();
        bool isBorder(int x, int y);
        bool isPreset(int x, int y);
        bool isAllowed(int x, int y);
        bool isRame(int x, int y);
        bool isSinglePoint(int x, int y);
        bool isEqual(std::vector<Roads2DGeneratorPoint> vLeft, std::vector<Roads2DGeneratorPoint> vRight);
        bool tryChangeToTrue(int x, int y);
        bool tryChangeToFalse(int x, int y);
        bool randomInitPoints();
        int moveDiagonalTails();
        bool moveDiagonalTailsLoop();
        bool checkAndRandomMove(int x, int y);
        int getAroundCount(int x, int y);
        std::vector<Roads2DGeneratorPoint> findSinglePoints();
        int drawLineByY(int x0, int x1, int y);
        int drawLineByX(int y0, int y1, int x);
        int connectPoints(Roads2DGeneratorPoint p0, Roads2DGeneratorPoint p1);
        void removeSinglePoints();
        void removeRames();
        bool canConnectClosePoints(int x, int y);
        void connectAllClosePoints();
        int removeAllShortCicles();
        bool removeAllShortCiclesLoop();
        bool isDeadlockPoint(int x, int y);
        std::vector<Roads2DGeneratorPoint> findDeadlockPoints();
        Roads2DGeneratorPoint findShortPointFrom(Roads2DGeneratorPoint p0, std::vector<Roads2DGeneratorPoint> points);
        void tryConnectDeadlocksLoop();
        void removeDeadlocksLoop();
        bool connectUnunionRoads();
        std::string getRoadPart(int x, int y);
        std::vector<Roads2DGeneratorConnectedComponent> findConnectedComponents();

        std::vector<std::vector<bool>> m_vPixelMap;

        Roads2DGeneratorPseudoRandom m_random;
        Roads2DGeneratorConfig m_config;
        std::string m_sErrorMessage;
        std::vector<int> m_cachePresets;
};


#endif // __ROADS_2D_GENERATOR_H__
