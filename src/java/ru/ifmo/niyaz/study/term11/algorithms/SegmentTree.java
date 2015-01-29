
import java.util.Collection;

public abstract class SegmentTree {
	abstract void findPoints(int[] from, int[] to, Collection<int[]> result);

	public static SegmentTree create(int dimension, int[][] ps) {
		if (dimension < 2) {
			throw new UnsupportedOperationException();
		}
		if (dimension == 2) {
			return new SegmentTree2D(ps);
		} else {
			return new SegmentTreeD(dimension, ps);
		}
	}
}
