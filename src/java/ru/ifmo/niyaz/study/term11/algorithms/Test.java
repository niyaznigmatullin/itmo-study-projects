import java.util.*;

public class Test {
	public static void main(String[] args) {
		Random rand = new Random(123);
		final int DIM = 2;
		while (true) {
			int n = 100000;
			final int MAXC = 1000000000;
			int[][] ps = new int[n][DIM];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < DIM; j++) {
					ps[i][j] = rand.nextInt(MAXC);
				}
			}
			SegmentTree tree = SegmentTree.create(DIM, ps);
			List<int[]> smart = new ArrayList<>(1 << 20);
			Set<int[]> stupid = new HashSet<>();
			long ans = 0;
			for (int it = 0; it < 100000; it++) {
				if (it % 1000 == 0) System.err.println(it);
				int[] from = new int[DIM];
				int[] to = new int[DIM];
				for (int i = 0; i < DIM; i++) {
					do {
						from[i] = rand.nextInt(10000000);
						to[i] = rand.nextInt(10000000);
					} while (from[i] > to[i]);
				}
				tree.findPoints(from, to, smart);
				ans += smart.size();
				smart.clear();
				// for (int[] f : ps) {
				// boolean ok = true;
				// for (int j = 0; j < DIM; j++) {
				// if (f[j] < from[j] || f[j] > to[j]) {
				// ok = false;
				// break;
				// }
				// }
				// if (ok)
				// stupid.add(f);
				// }
				// if (!smart.equals(stupid)) {
				// for (int[] f : smart) {
				// System.err.println(Arrays.toString(f));
				// }
				// for (int[] f : stupid) {
				// System.err.println(Arrays.toString(f));
				// }
				// throw new AssertionError();
				// }
				// for (int[] f : smart) {
				// System.err.println(Arrays.toString(f));
				// }
			}
			tree = null;
			ps = null;
			System.err.println("OK " + ans);
		}
	}
}


// 2D, random, 10^5
// 3D, random, 20000
// 5D, (i, i, i, i, i), 1000
// 2D, (i, -i), 10^5
// 7D, random, 50