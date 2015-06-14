package game.math.units;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("serial")
public abstract class DistanceUnit implements Comparator<DistanceUnit>, Comparable<DistanceUnit>, Serializable {
    public abstract double getInCM();

    public abstract double getInM();

    public boolean lessThan(final DistanceUnit other) {
	return getInCM() < other.getInCM();
    }

    public boolean lessThanOrEqual(final DistanceUnit other) {
	return getInCM() <= other.getInCM();
    }

    public boolean greaterThan(final DistanceUnit other) {
	return getInCM() > other.getInCM();
    }

    public boolean greaterThanOrEqual(final DistanceUnit other) {
	return getInCM() >= other.getInCM();
    }

    @Override
    public int compareTo(final DistanceUnit o) {
	return Double.compare(getInCM(), o.getInCM());
    }

    @Override
    public int compare(final DistanceUnit d1, final DistanceUnit d2) {
	return Double.compare(d1.getInCM(), d2.getInCM());
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
	final DistanceUnit other = (DistanceUnit) obj;
	if (Double.doubleToLongBits(getInCM()) != Double.doubleToLongBits(other.getInCM()))
	    return false;
	return true;
    }

    public static class Meter extends DistanceUnit {
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

    public static class Centimeter extends DistanceUnit {
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
