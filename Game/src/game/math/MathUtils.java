package game.math;

import java.util.Random;

/**
 * @author s-KADAMS
 *
 */
/**
 * @author s-KADAMS
 *
 */
/**
 * @author s-KADAMS
 *
 */
/**
 * @author s-KADAMS
 *
 */
public final class MathUtils {
    private MathUtils() {
    }

    /**
     * The class random
     */
    private static final Random RAND = new Random();

    /**
     * returns the minimum and maximum values in array arr
     *
     * @param arr
     * @return range
     */
    public static RangeDouble findMinAndMax(final double[][] arr) {
	double min = Integer.MAX_VALUE;
	double max = Integer.MIN_VALUE;

	for (int i = 0; i < arr.length; i++) {
	    for (int j = 0; j < arr[i].length; j++) {
		if (arr[i][j] < min) {
		    min = arr[i][j];
		}
		if (arr[i][j] > max) {
		    max = arr[i][j];
		}
	    }
	}
	return new RangeDouble(min, max);
    }

    public static double getSineWave(final double x, final double wavelength) {
	return Math.sin(Math.PI * x / wavelength);
    }

    /**
     * Maps a number num from a range (prevMin to prevMax) into a range (newMin,
     * newMax)
     *
     * @param num
     * @param prevMin
     * @param prevMax
     * @param newMin
     * @param newMax
     * @return mappedNum
     */
    public static double mapNumInRange(final double num, final double prevMin, final double prevMax, final double newMin, final double newMax) {
	return (num - prevMin) / (prevMax - prevMin) * (newMax - newMin) + newMin;
    }

    /**
     * Returns
     * {@link MathUtils#mapNumInRange(double, double, double,double, double)}
     * with values 0 and 1 for the newMin and newMax
     *
     * @param num
     * @param prevMin
     * @param prevMax
     * @return numMappedFrom0to1
     */
    public static double mapNum0to1(final double num, final double prevMin, final double prevMax) {
	return mapNumInRange(num, prevMin, prevMax, 0, 1);
    }

    public static int randBetween(final int low, final int high) {
	return RAND.nextInt(high - low) + low;
    }

    public static int randBetweenInclusive(final int low, final int high) {
	return RAND.nextInt(high - low + 1) + low;
    }

    public static class RangeDouble {
	public final double min;
	public final double max;

	public RangeDouble(final double min, final double max) {
	    this.min = min;
	    this.max = max;
	}

	@Override
	public String toString() {
	    return "RangeDouble{min=" + min + ", max=" + max + "}";
	}
    }

    public static double plusOrMinusRand(final double base, final double variation) {
	if (RAND.nextBoolean())
	    return base + RAND.nextDouble() * variation;
	else
	    return base - RAND.nextDouble() * variation;
    }
}
