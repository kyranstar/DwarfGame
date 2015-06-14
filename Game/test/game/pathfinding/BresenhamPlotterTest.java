/**
 *
 */
package game.pathfinding;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * The Test Class BresenhamPlotterTest.
 *
 * @author Kyran Adams
 */
public class BresenhamPlotterTest {

    /** The constant line of points from (0,0) to (5,5). */
    final static List<Point> line0055 = new ArrayList<>();
    static {
	line0055.add(new Point(0, 0));
	line0055.add(new Point(1, 1));
	line0055.add(new Point(2, 2));
	line0055.add(new Point(3, 3));
	line0055.add(new Point(4, 4));
	line0055.add(new Point(5, 5));
    }

    /**
     * Test method for
     * {@link game.pathfinding.BresenhamPlotter#line(int, int, int, int, game.pathfinding.BresenhamPlotter.BresenhamPlotFunction)}
     * .
     */
    @Test
    public void testLineWithFunction() {
	final List<Point> points0055 = new ArrayList<>();
	BresenhamPlotter.line(0, 0, 5, 5, (x, y) -> points0055.add(new Point(x, y)));
	assertEquals(points0055, line0055);
    }

    /**
     * Test method for
     * {@link game.pathfinding.BresenhamPlotter#line(int, int, int, int)}.
     */
    @Test
    public void testLineWitoutFunction() {
	final List<Point> points0055 = BresenhamPlotter.line(0, 0, 5, 5);
	assertEquals(points0055, line0055);
    }

}
