package game.pathfinding;

public class BresenhamPlotter {
	
	public interface BresenhamPlotFunction {
		/**
		 * Plot function, pass it into a plotting algorithm
		 *
		 * @param x
		 *            the x position
		 * @param y
		 *            the y position
		 * @return True to continue, false to stop the algorithm early
		 */
		public boolean plotFunction(int x, int y);
	}

	/**
	 * Line.
	 *
	 * @param x0
	 *            the start x
	 * @param y0
	 *            the start y
	 * @param x1
	 *            the end x
	 * @param y1
	 *            the end y
	 * @param plot
	 *            the plot function
	 */
	public static void Line(int x0, int y0, int x1, int y1, final BresenhamPlotFunction plot) {
		final boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		if (steep) {
			final int temp0 = x0;
			x0 = y0;
			y0 = temp0;
			
			final int temp1 = x1;
			x1 = y1;
			y1 = temp1;
		}
		if (x0 > x1) {
			final int tempX = x0;
			x0 = x1;
			x1 = tempX;

			final int tempY = y0;
			y0 = y1;
			y1 = tempY;
		}
		final int dX = x1 - x0, dY = Math.abs(y1 - y0);
		int err = dX / 2;
		final int ystep = y0 < y1 ? 1 : -1;
		int y = y0;
		
		for (int x = x0; x <= x1; ++x) {
			if (!(steep ? plot.plotFunction(x, y) : plot.plotFunction(x, y)))
				return;
			err = err - dY;
			if (err < 0) {
				y += ystep;
				err += dX;
			}
		}
	}
}
