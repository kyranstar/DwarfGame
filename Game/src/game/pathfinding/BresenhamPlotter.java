package game.pathfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class BresenhamPlotter.
 */
public class BresenhamPlotter {
	
	public interface BresenhamPlotFunction {
		/**
		 * Plot function, pass it into a plotting algorithm.
		 *
		 * @param x
		 *            the x position
		 * @param y
		 *            the y position
		 */
		public void plotFunction(int x, int y);
	}

	/**
	 * Plots a line using the Bresenham line algorithm from (x0, y0) to (x1, y1) and plots each point using the plot function.
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
	public static void Line(int x0, int y0, final int x1, final int y1, final BresenhamPlotFunction plot) {
		final int w = x1 - x0;
		final int h = y1 - y0;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0) {
			dx1 = -1;
		} else if (w > 0) {
			dx1 = 1;
		}
		if (h < 0) {
			dy1 = -1;
		} else if (h > 0) {
			dy1 = 1;
		}
		if (w < 0) {
			dx2 = -1;
		} else if (w > 0) {
			dx2 = 1;
		}
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0) {
				dy2 = -1;
			} else if (h > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			plot.plotFunction(x0, y0);
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x0 += dx1;
				y0 += dy1;
			} else {
				x0 += dx2;
				y0 += dy2;
			}
		}
	}

	/**
	 * Plots a line using the Bresenham line algorithm from (x0, y0) to (x1, y1) and returns a list of the points.
	 *
	 * @param x0
	 *            the start x
	 * @param y0
	 *            the start y
	 * @param x1
	 *            the end x
	 * @param y1
	 *            the end y
	 * @return the list of plotted points
	 */
	public static List<Point> Line(int x0, int y0, final int x1, final int y1) {
		final List<Point> points = new ArrayList<>();
		final int w = x1 - x0;
		final int h = y1 - y0;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0) {
			dx1 = -1;
		} else if (w > 0) {
			dx1 = 1;
		}
		if (h < 0) {
			dy1 = -1;
		} else if (h > 0) {
			dy1 = 1;
		}
		if (w < 0) {
			dx2 = -1;
		} else if (w > 0) {
			dx2 = 1;
		}
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0) {
				dy2 = -1;
			} else if (h > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			points.add(new Point(x0, y0));
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x0 += dx1;
				y0 += dy1;
			} else {
				x0 += dx2;
				y0 += dy2;
			}
		}
		return points;
	}
}