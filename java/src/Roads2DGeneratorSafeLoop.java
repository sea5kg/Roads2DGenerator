package src;

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