package src;

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
