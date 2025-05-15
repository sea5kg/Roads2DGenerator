package src;

import java.util.ArrayList;
import java.util.List;

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
