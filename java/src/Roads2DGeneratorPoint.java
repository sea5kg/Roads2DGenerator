package src;

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

