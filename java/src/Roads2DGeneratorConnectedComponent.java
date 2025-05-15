package src;

import java.util.ArrayList;
import java.util.List;

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
