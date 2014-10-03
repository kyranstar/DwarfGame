package game.math.units;

import java.util.Comparator;

public abstract class NumberDistance implements Comparator<NumberDistance>, Comparable<NumberDistance> {
    public abstract double getInCM();

    public abstract double getInM();

    public boolean lessThan(final NumberDistance other) {
	return getInCM() < other.getInCM();
    }

    public boolean lessThanOrEqual(final NumberDistance other) {
	return getInCM() <= other.getInCM();
    }

    public boolean greaterThan(final NumberDistance other) {
	return getInCM() > other.getInCM();
    }

    public boolean greaterThanOrEqual(final NumberDistance other) {
	return getInCM() >= other.getInCM();
    }

    @Override
    public int compareTo(final NumberDistance o) {
	if (getInCM() < o.getInCM())
	    return -1;
	if (getInCM() > o.getInCM())
	    return 1;
	return 0;
    }

    @Override
    public int compare(final NumberDistance d1, final NumberDistance d2) {
	if (d1.getInCM() < d2.getInCM())
	    return -1;
	if (d1.getInCM() > d2.getInCM())
	    return 1;
	return 0;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	long temp;
	temp = Double.doubleToLongBits(getInCM());
	result = prime * result + (int) (temp ^ temp >>> 32);
	return result;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final NumberDistance other = (NumberDistance) obj;
	if (Double.doubleToLongBits(getInCM()) != Double.doubleToLongBits(other.getInCM()))
	    return false;
	return true;
    }

    public static class Meter extends NumberDistance {
	final private double meters;

	public Meter(final double meters) {
	    this.meters = meters;
	}

	public static Meter of(final double meters) {
	    return new Meter(meters);
	}

	@Override
	public double getInCM() {
	    return meters * 100;
	}

	@Override
	public double getInM() {
	    return meters;
	}

    }

    public static class Centimeter extends NumberDistance {
	final private double centimeters;

	public Centimeter(final double centimeters) {
	    this.centimeters = centimeters;
	}

	public static Centimeter of(final double centimeters) {
	    return new Centimeter(centimeters);
	}

	@Override
	public double getInCM() {
	    return centimeters;
	}

	@Override
	public double getInM() {
	    return centimeters / 100;
	}

    }

}
