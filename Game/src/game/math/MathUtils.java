package game.math;

import java.util.Random;

public final class MathUtils {
    private MathUtils() {
    }

    private static final Random RAND = new Random();

    public static double mapNumInRange(final double results, final double min, final double max, final double newMin, final double newMax) {
	return (results - min) / (max - min) * (newMax - newMin) + newMin;
    }

    public static int randBetween(final int low, final int high) {
	return RAND.nextInt(high - low) + low;
    }

    public static int randBetweenInclusive(final int low, final int high) {
	return RAND.nextInt(high - low + 1) + low;
    }

}
