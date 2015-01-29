
import java.util.*;

public class SegmentTree2D extends SegmentTree {
	int n;
	int[][][] a;
	int[][] toLeft;
	int[][] toRight;
	int[] xs;

	public SegmentTree2D(int[][] points) {
		Arrays.sort(points, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				return Integer.compare(o1[1], o2[1]);
			}
		});
		n = points.length == 1 ? 1
				: Integer.highestOneBit(points.length - 1) << 1;
		if (n < points.length) throw new AssertionError();
		a = new int[n * 2][][];
		toLeft = new int[n][];
		toRight = new int[n][];
		xs = new int[points.length];
		for (int i = 0; i < n; i++) {
			if (i < points.length) {
				a[i + n] = new int[][] { points[i] };
				xs[i] = points[i][1];
			} else {
				a[i + n] = new int[0][];
			}
		}
		for (int i = n - 1; i > 0; i--) {
			int lenLeft = a[i * 2].length;
			int lenRight = a[i * 2 + 1].length;
			int len = lenLeft + lenRight;
			a[i] = new int[len][];
			toLeft[i] = new int[len];
			toRight[i] = new int[len];
			for (int j = 0, curLeft = 0, curRight = 0; j < len; j++) {
				toLeft[i][j] = curLeft;
				toRight[i][j] = curRight;
				if (curRight >= lenRight || curLeft < lenLeft
						&& a[i * 2][curLeft][0] <= a[i * 2 + 1][curRight][0]) {
					a[i][j] = a[i * 2][curLeft++];
				} else {
					a[i][j] = a[i * 2 + 1][curRight++];
				}
			}
		}
	}

	@Override
	public void findPoints(int[] from, int[] to, Collection<int[]> result) {
		int left, right;
		{
			int l = -1;
			int r = xs.length;
			while (l < r - 1) {
				int mid = (l + r) >> 1;
				if (xs[mid] < from[1])
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
				if (xs[mid] > to[1])
					r = mid;
				else
					l = mid;
			}
			right = r;
		}
		int start;
		{
			int l = -1;
			int r = a[1].length;
			while (l < r - 1) {
				int mid = (l + r) >> 1;
				if (a[1][mid][0] < from[0])
					l = mid;
				else
					r = mid;
			}
			start = r;
		}
		findPoints(1, 0, n, left, right, start, to[0], result);
	}

	private void findPoints(int v, int l, int r, int left, int right,
			int start, int to, Collection<int[]> result) {
		if (r <= left || right <= l || start >= a[v].length)
			return;
		if (left <= l && r <= right) {
			for (int i = start; i < a[v].length; i++) {
				if (a[v][i][0] > to)
					break;
				result.add(a[v][i]);
			}
			return;
		}
		int mid = (l + r) >> 1;
		findPoints(v * 2, l, mid, left, right, toLeft[v][start], to, result);
		findPoints(v * 2 + 1, mid, r, left, right, toRight[v][start], to,
				result);
	}
}
