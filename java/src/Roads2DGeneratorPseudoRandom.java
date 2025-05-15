package src;

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
