package src;

import java.util.ArrayList;
import java.util.List;

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
