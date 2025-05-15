package src;

import java.util.HashMap;
import java.util.Map;

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
