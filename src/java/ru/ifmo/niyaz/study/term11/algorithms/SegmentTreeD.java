
import java.util.*;

public class SegmentTreeD extends SegmentTree {
	int n;
	int[][][] a;
	SegmentTree[] trees;
	int[] xs;
	int dimension;

	public SegmentTreeD(final int dimension, int[][] points) {
		this.dimension = dimension;
		Arrays.sort(points, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				return Integer.compare(o1[dimension - 1], o2[dimension - 1]);
			}
		});
		n = points.length == 1 ? 1
				: Integer.highestOneBit(points.length - 1) << 1;
		a = new int[n * 2][][];
		trees = new SegmentTree[n * 2];
		xs = new int[points.length];
		if (n < points.length) throw new AssertionError();
		for (int i = 0; i < n; i++) {
			if (i < points.length) {
				a[i + n] = new int[][] { points[i] };
				xs[i] = points[i][dimension - 1];
			} else {
				a[i + n] = new int[0][];
			}
		}
		for (int i = n - 1; i > 0; i--) {
			int lenLeft = a[i * 2].length;
			int lenRight = a[i * 2 + 1].length;
			int len = lenLeft + lenRight;
			a[i] = new int[len][];
			for (int j = 0, curLeft = 0, curRight = 0; j < len; j++) {
				if (curRight >= lenRight || curLeft < lenLeft
						&& a[i * 2][curLeft][0] <= a[i * 2 + 1][curRight][0]) {
					a[i][j] = a[i * 2][curLeft++];
				} else {
					a[i][j] = a[i * 2 + 1][curRight++];
				}
			}
		}
		for (int i = 1; i < a.length; i++) {
			trees[i] = create(dimension - 1, a[i]);
		}
	}

	@Override
	void findPoints(int[] from, int[] to, Collection<int[]> result) {
		int left, right;
		{
			int l = -1;
			int r = xs.length;
			while (l < r - 1) {
				int mid = (l + r) >> 1;
				if (xs[mid] < from[dimension - 1])
					l = mid;
				else
					r = mid;
			}
			left = r;
		}
		{
			int l = -1;
			int r = xs.length;
			while (l < r - 1) {
				int mid = (l + r) >> 1;
				if (xs[mid] > to[dimension - 1])
					r = mid;
				else
					l = mid;
			}
			right = l;
		}
		left += n;
		right += n;
		while (left <= right) {
			if ((left & 1) == 1) {
				trees[left++].findPoints(from, to, result);
			}
			if ((right & 1) == 0) {
				trees[right--].findPoints(from, to, result);
			}
			left >>= 1;
			right >>= 1;
		}
	}
}
