package game.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testMapNumInRange() {
	assertEquals(255, MathUtils.mapNumInRange(1, 0, 1, 0, 255), 0.00001);
	assertEquals(0, MathUtils.mapNumInRange(0, 0, 1, 0, 255), 0.00001);
	assertEquals(5, MathUtils.mapNumInRange(5, 0, 10, 0, 10), 0.00001);
	assertEquals(50, MathUtils.mapNumInRange(5, 0, 10, 0, 100), 0.00001);
	assertEquals(25, MathUtils.mapNumInRange(2.5, 0, 10, 0, 100), 0.00001);
	assertEquals(.25, MathUtils.mapNumInRange(25, 0, 100, 0, 1), 0.00001);
    }

}
