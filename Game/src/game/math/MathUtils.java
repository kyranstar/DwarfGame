package game.math;

import java.util.Random;

public final class MathUtils {
    private MathUtils() {
    }

    private static final Random RAND = new Random();

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

    public static double mapNumInRange(final double num, final double prevMin, final double prevMax, final double newMin, final double newMax) {
	return (num - prevMin) / (prevMax - prevMin) * (newMax - newMin) + newMin;
    }

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
}
