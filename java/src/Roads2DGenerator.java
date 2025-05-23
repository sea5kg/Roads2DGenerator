/*
MIT License

Copyright (c) 2021-2025 Evgenii Sopov (mrseakg@gmail.com)

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

// original source-code: https://github.com/sea5kg/Roads2DGenerator

package src;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.io.FileWriter;
import java.io.IOException;

public class Roads2DGenerator {

    // Roads2DGeneratorSafeLoop
    public class Roads2DGeneratorSafeLoop {
        private int m_nMaxLoop;
        private int m_nCurrentLoop;

        public Roads2DGeneratorSafeLoop(int nMaxLoop) {
            this.m_nMaxLoop = nMaxLoop;
            this.m_nCurrentLoop = 0;
        }

        public void doIncrement() {
            m_nCurrentLoop++;
        }

        public boolean isOverMax() {
            return m_nCurrentLoop >= m_nMaxLoop;
        }

        public int getLoopNumber() {
            return m_nCurrentLoop;
        }
    }

    // Roads2DGeneratorPseudoRandom
    public class Roads2DGeneratorPseudoRandom {
        private int m_nSeed;
        private int m_nInitSeed;

        public Roads2DGeneratorPseudoRandom() {
            m_nSeed = 0;
            m_nInitSeed = 0;
        }

        public void setInitSeed(int nSeed) {
            this.m_nInitSeed = nSeed;
            this.m_nSeed = nSeed;
        }

        public int getNextRandom() {
            // m_nSeed = std::sin(m_nSeed + 1) * float(m_nSeed + 1103515245) + 123;
            float num = m_nSeed + 1103515245;
            m_nSeed = (int)((Math.sin(m_nSeed + 1) * num + 123.0f));
            m_nSeed = m_nSeed & 0x0FFFFFFF;
            return m_nSeed;
            // return m_nSeed;
        }

        public int getInitSeed() {
            return m_nInitSeed;
        }

        public int getSeed() {
            return m_nSeed;
        }
    }

    // Roads2DGeneratorPoint
    public class Roads2DGeneratorPoint {
        private int m_nX;
        private int m_nY;

        public Roads2DGeneratorPoint() {
            this.m_nX = 0;
            this.m_nY = 0;
        }

        public Roads2DGeneratorPoint(int x, int y) {
            this.m_nX = x;
            this.m_nY = y;
        }

        public int getX() {
            return m_nX;
        }

        public int getY() {
            return m_nY;
        }

        @Override
        public String toString() {
            return "Point[" + m_nX + ", " + m_nY + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Roads2DGeneratorPoint that = (Roads2DGeneratorPoint) o;
            return m_nX == that.m_nX && m_nY == that.m_nY;
        }

        @Override
        public int hashCode() {
            return 100000 * m_nX + m_nY;
        }

    }

    // Roads2DGeneratorGraph
    public class Roads2DGeneratorGraph {
        private final List<Roads2DGeneratorPoint> m_vPoints;
        private final List<Roads2DGeneratorPair<Integer, Integer>> m_vConnections;

        public Roads2DGeneratorGraph() {
            this.m_vPoints = new ArrayList<>();
            this.m_vConnections = new ArrayList<>();
        }

        public int findOrAddPointGetIndex(Roads2DGeneratorPoint point) {
            for (int i = 0; i < m_vPoints.size(); i++) {
                if (m_vPoints.get(i).equals(point)) {
                    return i;
                }
            }
            m_vPoints.add(point);
            return m_vPoints.size() - 1;
        }

        public void addConnection(Integer index1, Integer index2) {
            Integer _min = Math.min(index1, index2);
            Integer _max = Math.max(index1, index2);
            for (Roads2DGeneratorPair connection : m_vConnections) {
                if (connection.first == _min && connection.second == _max) {
                    return;
                }
            }
            m_vConnections.add(new Roads2DGeneratorPair<>(_min, _max));
        }

        public List<Roads2DGeneratorPoint> getPoints() {
            return new ArrayList<>(m_vPoints); // return copy
        }

        public List<Roads2DGeneratorPair> getConnections() {
            return new ArrayList<>(m_vConnections); // return copy
        }
    }

    // Roads2DGeneratorConnectedComponent
    public class Roads2DGeneratorConnectedComponent {
        private final List<Roads2DGeneratorPoint> m_vPoints;

        public Roads2DGeneratorConnectedComponent() {
            this.m_vPoints = new ArrayList<>();
        }

        public boolean hasPoint(Roads2DGeneratorPoint point) {
            return m_vPoints.contains(point);
        }

        public void addPoint(Roads2DGeneratorPoint point) {
            if (!hasPoint(point)) {
                m_vPoints.add(point);
            }
        }

        public List<Roads2DGeneratorPoint> getPoints() {
            return new ArrayList<>(m_vPoints); // copy
        }

        public boolean hasIntersection(Roads2DGeneratorConnectedComponent component) {
            for (Roads2DGeneratorPoint point : m_vPoints) {
                if (component.hasPoint(point)) {
                    return true;
                }
            }
            return false;
        }

        public void merge(Roads2DGeneratorConnectedComponent component) {
            for (Roads2DGeneratorPoint point : component.getPoints()) {
                this.addPoint(point);
            }
        }
    }

    // Roads2DGeneratorConnectedComponents
    public class Roads2DGeneratorConnectedComponents {
        private List<Roads2DGeneratorConnectedComponent> m_vComponents;

        public Roads2DGeneratorConnectedComponents() {
            this.m_vComponents = new ArrayList<>();
        }

        public void addConnectedPoints(Roads2DGeneratorPoint p1, Roads2DGeneratorPoint p2) {
            boolean bAdded = false;

            for (Roads2DGeneratorConnectedComponent component : m_vComponents) {
                if (component.hasPoint(p1)) {
                    component.addPoint(p2);
                    bAdded = true;
                }
                if (component.hasPoint(p2)) {
                    component.addPoint(p1);
                    bAdded = true;
                }
            }
            if (!bAdded) {
                Roads2DGeneratorConnectedComponent newComponent = new Roads2DGeneratorConnectedComponent();
                newComponent.addPoint(p1);
                newComponent.addPoint(p2);
                m_vComponents.add(newComponent);
            }
            mergeComponents();
        }

        public List<Roads2DGeneratorConnectedComponent> getComponents() {
            return new ArrayList<>(m_vComponents);
        }

        private void mergeComponents() {
            List<Roads2DGeneratorConnectedComponent> mergedComponents = new ArrayList<>(this.m_vComponents);
            boolean bMerged = true;

            while (bMerged) {
                bMerged = false;
                for (int i = mergedComponents.size() - 1; i >= 1; i--) {
                    for (int t = i - 1; t >= 0; t--) {
                        if (mergedComponents.get(i).hasIntersection(mergedComponents.get(t))) {
                            mergedComponents.get(i).merge(mergedComponents.get(t));
                            mergedComponents.remove(t);
                            bMerged = true;
                            break;
                        }
                    }
                    if (bMerged) {
                        break;
                    }
                }
            }
            this.m_vComponents = new ArrayList<>(mergedComponents);
        }
    }

    // Roads2DGeneratorPair - simular for std::pair
    public class Roads2DGeneratorPair<A, B> {
        public final A first;
        public final B second;

        public Roads2DGeneratorPair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Roads2DGeneratorPair)) return false;
            Roads2DGeneratorPair<?, ?> pair = (Roads2DGeneratorPair<?, ?>) o;
            return first.equals(pair.first) && second.equals(pair.second);
        }

        @Override
        public int hashCode() {
            return 100000 * (int)first + (int)second;
        }
    }

    // Roads2DGeneratorConfig
    public class Roads2DGeneratorConfig {
        private int m_nWidth;
        private int m_nHeight;
        private float m_nDensity;
        private int m_nSeedInitRandom;
        private int m_nMaxAllowInitPointsTries;
        private boolean m_bSetByUserMaxAllowInitPointsTries;
        private int m_nMaxAllowMoveDiagonalTailsTries;
        private boolean m_bSetByUserMaxAllowMoveDiagonalTailsTries;
        private int m_nMaxAllowConnectUnunionRoadsTries;
        private boolean m_bSetByUserMaxAllowConnectUnunionRoadsTries;
        private int m_nMaxAllowRemoveAllShortCiclesLoopTries;
        private boolean m_bSetByUserMaxAllowRemoveAllShortCiclesLoopTries;
        private final Map<Roads2DGeneratorPair<Integer, Integer>, Boolean> m_presets = new HashMap<>();


        public Roads2DGeneratorConfig() {
            resetConfig();
        }

        public Roads2DGeneratorConfig resetConfig() {
            m_nWidth = 0;
            m_nHeight = 0;
            m_nDensity = 0.0f;
            m_nSeedInitRandom = 0;
            m_nMaxAllowInitPointsTries = 0;
            m_bSetByUserMaxAllowInitPointsTries = false;
            m_nMaxAllowMoveDiagonalTailsTries = 0;
            m_bSetByUserMaxAllowMoveDiagonalTailsTries = false;
            m_nMaxAllowConnectUnunionRoadsTries = 0;
            m_bSetByUserMaxAllowConnectUnunionRoadsTries = false;
            m_nMaxAllowRemoveAllShortCiclesLoopTries = 0;
            m_bSetByUserMaxAllowRemoveAllShortCiclesLoopTries = false;
            m_presets.clear();
            return this;
        }

        // Width methods
        public Roads2DGeneratorConfig setWidth(int val) {
            this.m_nWidth = val;
            return this;
        }

        public int getWidth() {
            return m_nWidth;
        }

        // Height methods
        public Roads2DGeneratorConfig setHeight(int val) {
            this.m_nHeight = val;
            return this;
        }

        public int getHeight() {
            return m_nHeight;
        }

        public Roads2DGeneratorConfig setDensity(float nDensity) {
            // Apply the same validation as in C++ version
            if (nDensity > 1.0f) {
                nDensity = 1.0f;
            }
            if (nDensity < 0.0f) {
                nDensity = 0.0f;
            }
            this.m_nDensity = nDensity;
            return this;
        }

        public float getDensity() {
            return m_nDensity;
        }

        public int getMaxInitPoints() {
            float num = m_nWidth * m_nHeight;
            return (int) (num * m_nDensity);
        }

        public Roads2DGeneratorConfig setSeedInitRandom(int val) {
            this.m_nSeedInitRandom = val;
            return this;
        }

        public int getSeedInitRandom() {
            return m_nSeedInitRandom;
        }

        public Roads2DGeneratorConfig setMaxAllowInitPointsTries(int val, boolean bSetByUser) {
            this.m_nMaxAllowInitPointsTries = val;
            if (bSetByUser) {
                this.m_bSetByUserMaxAllowInitPointsTries = bSetByUser;
            }
            return this;
        }

        public Roads2DGeneratorConfig setMaxAllowInitPointsTries(int val) {
            setMaxAllowInitPointsTries(val, true);
            return this;
        }

        public boolean isSetByUserMaxAllowInitPointsTries() {
            return m_bSetByUserMaxAllowInitPointsTries;
        }

        public int getMaxAllowInitPointsTries() {
            return m_nMaxAllowInitPointsTries;
        }

        public Roads2DGeneratorConfig setMaxAllowMoveDiagonalTailsTries(int val, boolean bSetByUser) {
            this.m_nMaxAllowMoveDiagonalTailsTries = val;
            if (bSetByUser) {
                this.m_bSetByUserMaxAllowMoveDiagonalTailsTries = bSetByUser;
            }
            return this;
        }

        public Roads2DGeneratorConfig setMaxAllowMoveDiagonalTailsTries(int val) {
            setMaxAllowMoveDiagonalTailsTries(val, true);
            return this;
        }

        public boolean isSetByUserMaxAllowMoveDiagonalTailsTries() {
            return m_bSetByUserMaxAllowMoveDiagonalTailsTries;
        }

        public int getMaxAllowMoveDiagonalTailsTries() {
            return m_nMaxAllowMoveDiagonalTailsTries;
        }

        public Roads2DGeneratorConfig setMaxAllowConnectUnunionRoadsTries(int val, boolean bSetByUser) {
            this.m_nMaxAllowConnectUnunionRoadsTries = val;
            if (bSetByUser) {
                this.m_bSetByUserMaxAllowConnectUnunionRoadsTries = bSetByUser;
            }
            return this;
        }

        public Roads2DGeneratorConfig setMaxAllowConnectUnunionRoadsTries(int val) {
            setMaxAllowConnectUnunionRoadsTries(val, true);
            return this;
        }

        public boolean isSetByUserAllowConnectUnunionRoadsTries() {
            return m_bSetByUserMaxAllowConnectUnunionRoadsTries;
        }

        public int getMaxAllowConnectUnunionRoadsTries() {
            return m_nMaxAllowConnectUnunionRoadsTries;
        }

        public Roads2DGeneratorConfig setMaxAllowRemoveAllShortCiclesLoopTries(int val, boolean bSetByUser) {
            this.m_nMaxAllowRemoveAllShortCiclesLoopTries = val;
            if (bSetByUser) {
                this.m_bSetByUserMaxAllowRemoveAllShortCiclesLoopTries = bSetByUser;
            }
            return this;
        }

        public Roads2DGeneratorConfig setMaxAllowRemoveAllShortCiclesLoopTries(int val) {
            setMaxAllowRemoveAllShortCiclesLoopTries(val, true);
            return this;
        }

        public boolean isSetAsUserMaxAllowRemoveAllShortCiclesLoopTries() {
            return m_bSetByUserMaxAllowRemoveAllShortCiclesLoopTries;
        }

        public int getMaxAllowRemoveAllShortCiclesLoopTries() {
            return m_nMaxAllowRemoveAllShortCiclesLoopTries;
        }

        public Roads2DGeneratorConfig setPresetExcludes(int x_start, int y_start, int x_end, int y_end) {
            for (int x = x_start; x <= x_end; x++) {
                for (int y = y_start; y <= y_end; y++) {
                    m_presets.put(new Roads2DGeneratorPair<>(x, y), false);
                }
            }
            return this;
        }

        public Map<Roads2DGeneratorPair<Integer, Integer>, Boolean> getPresets() {
            return new HashMap<>(m_presets);
        }
    }


    // Roads2DGenerator
    private List<List<Boolean>> m_vPixelMap;
    private Roads2DGeneratorPseudoRandom m_random;
    private Roads2DGeneratorConfig m_config;
    private String m_sErrorMessage;
    private List<Integer> m_cachePresets;

    public Roads2DGenerator() {
        m_random = new Roads2DGeneratorPseudoRandom();
        m_config = new Roads2DGeneratorConfig();
        m_sErrorMessage = "";
        m_cachePresets = new ArrayList<>();
        resetMap();
    }

    public String getErrorMessage() {
        return m_sErrorMessage;
    }

    public Roads2DGeneratorConfig getConfig() {
        return m_config;
    }

    public boolean generate(Roads2DGeneratorConfig config) {
        this.m_config = config;
        return generate();
    }

    public boolean generate() {
        // Calculate base coefficient for safety limits
        float num = m_config.getWidth() * m_config.getHeight();
        int nBaseCoefForSafeWhile = (int)(num * m_config.getDensity() * 2.0f);

        // Set default tries if not set by user
        if (!m_config.isSetByUserMaxAllowInitPointsTries()) {
            m_config.setMaxAllowInitPointsTries(nBaseCoefForSafeWhile, false);
        }
        if (!m_config.isSetByUserMaxAllowMoveDiagonalTailsTries()) {
            m_config.setMaxAllowMoveDiagonalTailsTries(nBaseCoefForSafeWhile / 10, false);
        }
        if (!m_config.isSetByUserAllowConnectUnunionRoadsTries()) {
            m_config.setMaxAllowConnectUnunionRoadsTries(nBaseCoefForSafeWhile / 100, false);
        }
        if (!m_config.isSetAsUserMaxAllowRemoveAllShortCiclesLoopTries()) {
            m_config.setMaxAllowRemoveAllShortCiclesLoopTries(nBaseCoefForSafeWhile / 10, false);
        }

        m_sErrorMessage = "";
        m_random.setInitSeed(m_config.getSeedInitRandom());

        resetMap();
        initPresets();

        // Initial generation steps
        if (!randomInitPoints()) {
            return false;
        }
        // if (true) {
        //     printMap();
        //     return false;
        // }

        if (!moveDiagonalTailsLoop()) {
            return false;
        }

        // TODO safecicle
        boolean bAgain = true;
        while (bAgain) {
            List<Roads2DGeneratorPoint> vPoints = findSinglePoints();
            if (vPoints.size() <= 1) {
                bAgain = false;
                break;
            }
            Roads2DGeneratorPoint p0 = vPoints.get(m_random.getNextRandom() % vPoints.size());
            Roads2DGeneratorPoint p1 = vPoints.get(m_random.getNextRandom() % vPoints.size());
            connectPoints(p0, p1);
            if (!moveDiagonalTailsLoop()) {
                return false;
            }
        }

        removeSinglePoints();
        removeRames();
        connectAllClosePoints();

        if (!removeAllShortCiclesLoop()) {
            return false;
        }
        removeRames();
        if (!moveDiagonalTailsLoop()) {
            return false;
        }

        tryConnectDeadlocksLoop();
        // commented: moveDiagonalTailsLoop()

        if (!removeAllShortCiclesLoop()) {
            return false;
        }
        removeRames();

        if (!moveDiagonalTailsLoop()) {
            return false;
        }
        removeDeadlocksLoop();
        removeSinglePoints();
        removeRames();

        if (!connectUnunionRoads()) {
            return false;
        }
        removeDeadlocksLoop();
        removeSinglePoints();
        removeRames();

        return true;
    }

    public void printMap() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        for (int y = 0; y < m_config.getHeight(); y++) {
            StringBuilder sLine = new StringBuilder();
            for (int x = 0; x < m_config.getWidth(); x++) {
                if (m_vPixelMap.get(x).get(y)) {
                    if (isWindows) {
                        sLine.append((char)219).append((char)219);
                    } else {
                        sLine.append("\u001B[0;30;47m  \u001B[0m");
                    }
                } else {
                    if (isWindows) {
                        sLine.append("  ");
                    } else {
                        sLine.append("\u001B[2;31;40m  \u001B[0m");
                    }
                }
            }
            System.out.println(sLine.toString());
        }
    }

    public List<List<String>> exportLikeTable() {
        List<List<String>> vResult = new ArrayList<>();
        for (int y = 0; y < m_config.getHeight(); y++) {
            List<String> vLine = new ArrayList<>();
            for (int x = 0; x < m_config.getWidth(); x++) {
                if (m_vPixelMap.get(x).get(y)) {
                    vLine.add(getRoadPart(x, y));
                } else {
                    vLine.add("");
                }
            }
            vResult.add(vLine);
        }
        return vResult;
    }

    public List<List<Boolean>> exportLikePixelMap() {
        List<List<Boolean>> copy = new ArrayList<>();
        for (List<Boolean> row : m_vPixelMap) {
            copy.add(new ArrayList<>(row));
        }
        return copy;
    }

    public Roads2DGeneratorGraph exportLikeGraph() {
        Roads2DGeneratorGraph graph = new Roads2DGeneratorGraph();
        for (int x = 0; x < m_config.getWidth() - 1; x++) {
            for (int y = 0; y < m_config.getHeight() - 1; y++) {
                if (m_vPixelMap.get(x).get(y)) {
                    int indexXY = graph.findOrAddPointGetIndex(new Roads2DGeneratorPoint(x, y));
                    if (m_vPixelMap.get(x+1).get(y)) {
                        int indexX1Y = graph.findOrAddPointGetIndex(new Roads2DGeneratorPoint(x+1, y));
                        graph.addConnection(indexXY, indexX1Y);
                    }
                    if (m_vPixelMap.get(x).get(y+1)) {
                        int indexXY1 = graph.findOrAddPointGetIndex(new Roads2DGeneratorPoint(x, y+1));
                        graph.addConnection(indexXY, indexXY1);
                    }
                }
            }
        }
        return graph;
    }

    public String exportLikeJsonPixelMap() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n  \"roads2dgen_pixelmap\": [\n");
        for (int y = 0; y < m_config.getHeight(); y++) {
            jsonBuilder.append("    [");
            for (int x = 0; x < m_config.getWidth(); x++) {
                jsonBuilder.append(m_vPixelMap.get(x).get(y) ? "1" : "0");
                if (x < m_config.getWidth() - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append(y < m_config.getHeight() - 1 ? "],\n" : "]\n");
        }
        jsonBuilder.append("  ]\n}\n");
        return jsonBuilder.toString();
    }

    public boolean exportLikeJsonPixelMapToFile(String sFilepath) {
        try (FileWriter writer = new FileWriter(sFilepath)) {
            writer.write(exportLikeJsonPixelMap());
            return true;
        } catch (IOException e) {
            m_sErrorMessage = "File write error: " + e.getMessage();
            System.err.println(m_sErrorMessage);
            return false;
        }
    }

    private void resetMap() {
        m_vPixelMap = new ArrayList<>(m_config.getWidth());
        for (int x = 0; x < m_config.getWidth(); x++) {
            List<Boolean> column = new ArrayList<>(m_config.getHeight());
            for (int y = 0; y < m_config.getHeight(); y++) {
                column.add(false);
            }
            m_vPixelMap.add(column);
        }
    }

    private void initPresets() {
        m_cachePresets.clear();
        Map<Roads2DGeneratorPair<Integer, Integer>, Boolean> presets = m_config.getPresets();
        for (Map.Entry<Roads2DGeneratorPair<Integer, Integer>, Boolean> entry : presets.entrySet()) {
            m_cachePresets.add(entry.getKey().hashCode());
        }
    }

    private boolean isBorder(int x, int y) {
        return x == 0 || x == m_config.getWidth() - 1 ||
               y == 0 || y == m_config.getHeight() - 1;
    }

    private boolean isPreset(int x, int y) {
        int mhash = x * 100000 + y;
        return m_cachePresets.contains(mhash);
    }

    private boolean isRame(int x, int y) {
        if (isBorder(x, y) || !m_vPixelMap.get(x).get(y)) {
            return false;
        }

        // Get all 8 surrounding pixels
        boolean b00 = m_vPixelMap.get(x-1).get(y-1);
        boolean b01 = m_vPixelMap.get(x-1).get(y);
        boolean b02 = m_vPixelMap.get(x-1).get(y+1);
        boolean b10 = m_vPixelMap.get(x).get(y-1);
        boolean b12 = m_vPixelMap.get(x).get(y+1);
        boolean b20 = m_vPixelMap.get(x+1).get(y-1);
        boolean b21 = m_vPixelMap.get(x+1).get(y);
        boolean b22 = m_vPixelMap.get(x+1).get(y+1);

        // Check for 4 possible ramé patterns
        return (b00 && b01 && b02 && !b10 && !b12 && !b20 && !b21 && !b22) ||  // Top pattern
            (b20 && b21 && b22 && !b00 && !b01 && !b02 && !b10 && !b12) ||  // Bottom pattern
            (b00 && b10 && b20 && !b02 && !b12 && !b22 && !b01 && !b21) ||  // Left pattern
            (b02 && b12 && b22 && !b00 && !b10 && !b20 && !b01 && !b21);   // Right pattern
    }

    private boolean isEqual(List<Roads2DGeneratorPoint> vLeft, List<Roads2DGeneratorPoint> vRight) {
        if (vLeft == null || vRight == null) {
            return false;
        }
        if (vLeft.size() != vRight.size()) {
            return false;
        }
        for (int i = 0; i < vLeft.size(); i++) {
            if (vLeft.get(i).getX() != vRight.get(i).getX() && vLeft.get(i).getY() != vRight.get(i).getY()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllowed(int x, int y) {
        if (isBorder(x, y)) {
            return false;
        }

        x = x - 1;
        y = y - 1;

        for (int x0 = 0; x0 < 2; x0++) {
            for (int y0 = 0; y0 < 2; y0++) {
                boolean b1 = m_vPixelMap.get(x + x0).get(y + y0);
                boolean b2 = m_vPixelMap.get(x + x0 + 1).get(y + y0);
                boolean b3 = m_vPixelMap.get(x + x0 + 1).get(y + y0 + 1);
                boolean b4 = m_vPixelMap.get(x + x0).get(y + y0 + 1);

                if (b1 && b2 && b3 && b4) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSinglePoint(int x, int y) {
        if (isBorder(x, y) || !m_vPixelMap.get(x).get(y)) {
            return false;
        }
        return getAroundCount(x, y) == 0;
    }

    private boolean tryChangeToTrue(int x, int y) {
        if (isPreset(x, y)) {
            return false;
        }
        m_vPixelMap.get(x).set(y, true);
        if (!isAllowed(x, y)) {
            m_vPixelMap.get(x).set(y, false);
            return false;
        }
        return true;
    }

    private boolean tryChangeToFalse(int x, int y) {
        if (isPreset(x, y)) {
            return false;
        }
        m_vPixelMap.get(x).set(y, false);
        return true;
    }

    private boolean randomInitPoints() {
        int immp = 0;
        Roads2DGeneratorSafeLoop safeLoop = new Roads2DGeneratorSafeLoop(m_config.getMaxAllowInitPointsTries());
        int nMaxMainPoints = m_config.getMaxInitPoints();

        while (immp < nMaxMainPoints) {
            safeLoop.doIncrement();

            int x = (m_random.getNextRandom() % (m_config.getWidth() - 2)) + 1;
            int y = (m_random.getNextRandom() % (m_config.getHeight() - 2)) + 1;

            if (tryChangeToTrue(x, y)) {
                immp++;
            }

            if (safeLoop.isOverMax()) {
                printMap();
                m_sErrorMessage = "Roads2DGenerator::randomInitPoints(), nSafeWhile = " + safeLoop.getLoopNumber();
                System.err.println(m_sErrorMessage);
                return false;
            }
        }
        return true;
    }

    private int moveDiagonalTails() {
        int ret = 0;

        // Iterate through each column (x)
        for (int x = 0; x < m_vPixelMap.size(); x++) {
            List<Boolean> column = m_vPixelMap.get(x);

            // Iterate through each row (y) in the column
            for (int y = 0; y < column.size(); y++) {
                ret += checkAndRandomMove(x, y) ? 1 : 0;
            }
        }

        return ret;
    }

    private boolean moveDiagonalTailsLoop() {
        int mdt = moveDiagonalTails();
        Roads2DGeneratorSafeLoop safeLoop = new Roads2DGeneratorSafeLoop(m_config.getMaxAllowMoveDiagonalTailsTries());
        while (mdt > 0) {
            safeLoop.doIncrement();
            mdt = moveDiagonalTails();
            if (safeLoop.isOverMax()) {
                printMap();
                m_sErrorMessage = "Roads2DGenerator::moveDiagonalTailsLoop(), nSafeWhile = " + safeLoop.getLoopNumber();
                System.err.println(m_sErrorMessage);
                return false;
            }
        }
        return true;
    }

    private boolean checkAndRandomMove(int x, int y) {
        if (isBorder(x, y)) {
            return false;
        }

        boolean modified = false;

        if (m_vPixelMap.get(x).get(y) &&
            m_vPixelMap.get(x+1).get(y+1) &&
            !m_vPixelMap.get(x).get(y+1) &&
            !m_vPixelMap.get(x+1).get(y)) {
            modified = true;
            tryChangeToFalse(x+1, y+1);
            if (m_random.getNextRandom() % 2 == 0) {
                tryChangeToTrue(x, y+1);
            } else {
                tryChangeToTrue(x+1, y);
            }
        }
        else if (!m_vPixelMap.get(x).get(y) &&
                !m_vPixelMap.get(x+1).get(y+1) &&
                m_vPixelMap.get(x).get(y+1) &&
                m_vPixelMap.get(x+1).get(y)) {
            modified = true;
            tryChangeToFalse(x, y+1);
            if (m_random.getNextRandom() % 2 == 0) {
                tryChangeToTrue(x, y);
            } else {
                tryChangeToTrue(x+1, y+1);
            }
        }
        return modified;
    }

    private int getAroundCount(int x, int y) {
        if (isBorder(x, y)) {
            return 4;  // Special case for border points
        }

        int count = 0;

        // Check 3x3 neighborhood (excluding center point)
        for (int dx = 0; dx < 3; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                int x0 = x + dx - 1;
                int y0 = y + dy - 1;
                if (x0 == x && y0 == y) {
                    continue;
                }
                if (m_vPixelMap.get(x0).get(y0)) {
                    count++;
                }
            }
        }
        return count;
    }

    private List<Roads2DGeneratorPoint> findSinglePoints() {
        List<Roads2DGeneratorPoint> singlePoints = new ArrayList<>();
        for (int x = 0; x < m_vPixelMap.size(); x++) {
            List<Boolean> column = m_vPixelMap.get(x);
            for (int y = 0; y < column.size(); y++) {
                if (isSinglePoint(x, y)) {
                    singlePoints.add(new Roads2DGeneratorPoint(x, y));
                }
            }
        }
        return singlePoints;
    }

    private int drawLineByY(int x0, int x1, int y) {
        int ret = 0;
        int ix = Math.min(x0, x1);
        int mx = Math.max(x0,x1);
        for (int i = ix; i <= mx; i++) {
            if (!m_vPixelMap.get(i).get(y)) {
                if (tryChangeToTrue(i,y)) {
                    ret += 1;
                }
            }
        }
        return ret;
    }

    private int drawLineByX(int y0, int y1, int x) {
        int ret = 0;
        int iy = Math.min(y0, y1);
        int my = Math.max(y0, y1);
        for (int i = iy; i <= my; i++) {
            if (!m_vPixelMap.get(x).get(i)) {
                if (tryChangeToTrue(x, i)) {
                    ret++;
                }
            }
        }
        return ret;
    }

    private int connectPoints(Roads2DGeneratorPoint p0, Roads2DGeneratorPoint p1) {
        int ret = 0;
        int x0 = p0.getX();
        int y0 = p0.getY();
        int x1 = p1.getX();
        int y1 = p1.getY();
        if (m_random.getNextRandom() % 2 == 0) {
            ret += drawLineByY(x0, x1, y0);
            ret += drawLineByX(y0, y1, x1);
        } else {
            ret += drawLineByX(y0, y1, x0);
            ret += drawLineByY(x0, x1, y1);
        }
        return ret;
    }

    private void removeSinglePoints() {
        List<Roads2DGeneratorPoint> vPoints = findSinglePoints();
        for (int i = 0; i < vPoints.size(); i++) {
            int x = vPoints.get(i).getX();
            int y = vPoints.get(i).getY();
            tryChangeToFalse(x,y);
            // write_map_to_image()
        }
    }

    private void removeRames() {
        for (int x = 0; x < m_vPixelMap.size(); x++) {
            List<Boolean> line = m_vPixelMap.get(x);
            for (int y = 0; y < line.size(); y++) {
                if (isRame(x, y)) {
                    tryChangeToFalse(x,y);
                    // write_map_to_image()
                }
            }
        }
    }

    private boolean canConnectClosePoints(int x, int y) {
         if (isBorder(x, y) || m_vPixelMap.get(x).get(y)) {
            return false;
        }
        boolean verticalConnect = m_vPixelMap.get(x).get(y+1) && m_vPixelMap.get(x).get(y-1);
        boolean horizontalConnect = m_vPixelMap.get(x+1).get(y) && m_vPixelMap.get(x-1).get(y);
        return verticalConnect || horizontalConnect;
    }

    private void connectAllClosePoints() {
        for (int x = 0; x < m_vPixelMap.size(); x++) {
            List<Boolean> column = m_vPixelMap.get(x);
            for (int y = 0; y < column.size(); y++) {
                int aroundCount = getAroundCount(x, y);
                if (canConnectClosePoints(x, y) && aroundCount < 6) {
                    tryChangeToTrue(x, y);
                }
            }
        }
    }

    private int removeAllShortCicles() {
        int ret = 0;
        for (int x = 0; x < m_vPixelMap.size(); x++) {
            List<Boolean> line = m_vPixelMap.get(x);
            for (int y = 0; y < line.size(); y++) {
                if (getAroundCount(x, y) == 8 && !m_vPixelMap.get(x).get(y)) {
                    int n = m_random.getNextRandom() % 4;
                    if (n == 0) {
                        tryChangeToFalse(x, y+1);
                    } else if (n == 1) {
                        tryChangeToFalse(x, y-1);
                    } else if (n == 2) {
                        tryChangeToFalse(x+1, y);
                    } else if (n == 2) {
                        tryChangeToFalse(x-1, y);
                    }
                    ret += 1;
                    // write_map_to_image()
                }
            }
        }
        return ret;
    }

    private boolean removeAllShortCiclesLoop() {
        Roads2DGeneratorSafeLoop safeLoop = new Roads2DGeneratorSafeLoop(m_config.getMaxAllowRemoveAllShortCiclesLoopTries());
        while (removeAllShortCicles() > 0) {
            safeLoop.doIncrement();
            if (safeLoop.isOverMax()) {
                printMap();
                m_sErrorMessage = "Roads2DGenerator::removeAllShortCiclesLoop(), nSafeWhile = " + safeLoop.getLoopNumber();
                System.err.println(m_sErrorMessage);
                return false;
            }
            continue;
        }
        return true;
    }

    private boolean isDeadlockPoint(int x, int y) {
        if (isBorder(x, y)) {
            return false;
        }
        if (!m_vPixelMap.get(x).get(y)) {
            return false;
        }
        int count = 0;
        if (m_vPixelMap.get(x-1).get(y)) count += 1;
        if (m_vPixelMap.get(x+1).get(y)) count += 1;
        if (m_vPixelMap.get(x).get(y+1)) count += 1;
        if (m_vPixelMap.get(x).get(y-1)) count += 1;
        return count == 1;
    }

    List<Roads2DGeneratorPoint> findDeadlockPoints() {
        List<Roads2DGeneratorPoint> vDeadlockPoints = new ArrayList<>();
        for (int x = 0; x < m_vPixelMap.size(); x++) {
            List<Boolean> line = m_vPixelMap.get(x);
            for (int y = 0; y < line.size(); y++) {
                if (isDeadlockPoint(x, y)) {
                    vDeadlockPoints.add(new Roads2DGeneratorPoint(x,y));
                }
            }
        }
        return vDeadlockPoints;
    }

    Roads2DGeneratorPoint findShortPointFrom(Roads2DGeneratorPoint p0, List<Roads2DGeneratorPoint> points) {
        int x0 = p0.getX();
        int y0 = p0.getY();
        int found_x1 = x0;
        int found_y1 = y0;
        int dist = m_vPixelMap.size() + m_vPixelMap.get(0).size() + 1; // max dist
        for (int i = 0; i < points.size(); i++) {
            int x1 = points.get(i).getX();
            int y1 = points.get(i).getY();
            if (x1 == x0 && y1 == y0) {
                continue;
            }
            int x_max = Math.max(x0, x1);
            int x_min = Math.min(x0, x1);
            int y_max = Math.max(y0, y1);
            int y_min = Math.min(y0, y1);
            int new_dist = (x_max - x_min) + (y_max - y_min);
            if (new_dist < dist) {
                dist = new_dist;
                found_x1 = x1;
                found_y1 = y1;
            }
        }
        return new Roads2DGeneratorPoint(found_x1, found_y1);
    }

    private void tryConnectDeadlocksLoop() {
        List<Roads2DGeneratorPoint> vDeadlocks = findDeadlockPoints();
        Roads2DGeneratorSafeLoop safeLoop = new Roads2DGeneratorSafeLoop(100);
        while (vDeadlocks.size() > 0) {
            safeLoop.doIncrement();
            int pn0 = m_random.getNextRandom() % vDeadlocks.size();
            Roads2DGeneratorPoint p0 = vDeadlocks.get(pn0);
            Roads2DGeneratorPoint p1 = findShortPointFrom(p0, vDeadlocks);
            int connected = connectPoints(p0, p1);
            if (connected == 0) {
                int x = p0.getX();
                int y = p0.getY();
                tryChangeToFalse(x, y);
            }
            List<Roads2DGeneratorPoint> vTmpDeadlocks = findDeadlockPoints();
            if (isEqual(vTmpDeadlocks, vDeadlocks)) {
                removeDeadlocksLoop();
            }
            removeSinglePoints();
            vDeadlocks = findDeadlockPoints();
            if (safeLoop.isOverMax()) {
                removeDeadlocksLoop();
                break;
            }
        }
    }


    private void removeDeadlocksLoop() {
        List<Roads2DGeneratorPoint> vDeadlocks = findDeadlockPoints();
        while (vDeadlocks.size() > 0) {
            int x = vDeadlocks.get(0).getX();
            int y = vDeadlocks.get(0).getY();
            tryChangeToFalse(x, y);
            // write_map_to_image();
            vDeadlocks = findDeadlockPoints();
        }
    }

    private boolean connectUnunionRoads() {
        List<Roads2DGeneratorConnectedComponent> comps = findConnectedComponents();
        Roads2DGeneratorSafeLoop safeLoop = new Roads2DGeneratorSafeLoop(m_config.getMaxAllowConnectUnunionRoadsTries());
        while (comps.size() > 1) {
            Roads2DGeneratorPoint p0 = comps.get(0).getPoints().get(m_random.getNextRandom() % comps.get(0).getPoints().size());
            Roads2DGeneratorPoint p1 = comps.get(1).getPoints().get(m_random.getNextRandom() % comps.get(1).getPoints().size());
            connectPoints(p0, p1);
            if (!moveDiagonalTailsLoop()) {
                return false;
            }
            comps = findConnectedComponents();

            safeLoop.doIncrement();
            if (safeLoop.isOverMax()) {
                printMap();
                m_sErrorMessage = "Roads2DGenerator::connectUnunionRoads(), nSafeWhile = " + safeLoop.getLoopNumber();
                System.err.println(m_sErrorMessage);
                return false;
            }
        }
        // std::cout << "comps.size() = " << comps.size() << std::endl;
        return true;
    }

    private String getRoadPart(int x, int y) {
        if (x < 0 || x >= m_config.getWidth() || y < 0 || y >= m_config.getHeight()) {
            return "error";
        }
        if (!m_vPixelMap.get(x).get(y)) {
            return "";
        }

        boolean left = m_vPixelMap.get(x).get(y-1);
        boolean right = m_vPixelMap.get(x).get(y+1);
        boolean top = m_vPixelMap.get(x-1).get(y);
        boolean bottom = m_vPixelMap.get(x+1).get(y);

        if (left && right && top && bottom) {
            return "cross";
        }
        if (left && right && ! top && ! bottom) {
            return "horizontal";
        }
        if (! left && ! right && top && bottom) {
            return "vertical";
        }
        if (! left && right && ! top && bottom) {
            return "right-down";
        }
        if (left && ! right && ! top && bottom) {
            return "left-down";
        }
        if (! left && right && top && ! bottom) {
            return "right-up";
        }
        if (left && ! right && top && ! bottom) {
            return "left-up";
        }
        if (left && ! right && top && bottom) {
            return "left-up-down";
        }
        if (! left && right && top && bottom) {
            return "right-up-down";
        }
        if (left && right && ! top && bottom) {
            return "left-right-down";
        }
        if (left && right && top && ! bottom) {
            return "left-right-up";
        }
        return "unknown";
    }

    private List<Roads2DGeneratorConnectedComponent> findConnectedComponents() {
        Roads2DGeneratorConnectedComponents components = new Roads2DGeneratorConnectedComponents();
        int nPoints = 0;
        for (int x = 0; x < m_config.getWidth() - 1; x++) {
            for (int y = 0; y < m_config.getHeight() - 1; y++) {
                Roads2DGeneratorPoint p0 = new Roads2DGeneratorPoint(x,y);
                Roads2DGeneratorPoint p1 = new Roads2DGeneratorPoint(x+1,y);
                Roads2DGeneratorPoint p2 = new Roads2DGeneratorPoint(x,y+1);
                if (m_vPixelMap.get(p0.getX()).get(p0.getY())) {
                    nPoints++;
                }
                if (m_vPixelMap.get(p0.getX()).get(p0.getY()) && m_vPixelMap.get(p1.getX()).get(p1.getY())) {
                    components.addConnectedPoints(p0, p1);
                }
                if (m_vPixelMap.get(p0.getX()).get(p0.getY()) && m_vPixelMap.get(p2.getX()).get(p2.getY())) {
                    components.addConnectedPoints(p0, p2);
                }
            }
        }
        List<Roads2DGeneratorConnectedComponent> vComponents = components.getComponents();
        return vComponents;
    }

}