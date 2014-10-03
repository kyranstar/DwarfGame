package game.math;

import java.awt.geom.Point2D;

/**
 * An extension to the relatively impotent java.awt.geom.Point2D.Double,
 * Vector2D allows mathematical manipulation of 2-component vectors.
 *
 * @author Jadrian Miles
 * @version 20031122
 */
public class Vec2D extends Point2D.Double {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.geom.Point2D.Double#Point2D.Double()
     */
    public Vec2D() {
	super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.geom.Point2D.Double#Point2D.Double()
     */
    public Vec2D(final double x, final double y) {
	super(x, y);
    }

    /**
     * Copy constructor
     */
    public Vec2D(final Vec2D v) {
	x = v.x;
	y = v.y;
    }

    /**
     * @return the radius (length, modulus) of the vector in polar coordinates
     */
    public double getR() {
	return Math.sqrt(x * x + y * y);
    }

    /**
     * @return the angle (argument) of the vector in polar coordinates in the
     *         range [-pi/2, pi/2]
     */
    public double getTheta() {
	return Math.atan2(y, x);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.geom.Point2D.Double#setLocation(double, double)
     */
    public void set(final double x, final double y) {
	super.setLocation(x, y);
    }

    /**
     * Sets the vector given polar arguments.
     *
     * @param r
     *            The new radius
     * @param t
     *            The new angle, in radians
     */
    public void setPolar(final double r, final double t) {
	super.setLocation(r * Math.cos(t), r * Math.sin(t));
    }

    /** Sets the vector's radius, preserving its angle. */
    public void setR(final double r) {
	final double t = getTheta();
	setPolar(r, t);
    }

    /** Sets the vector's angle, preserving its radius. */
    public void setTheta(final double t) {
	final double r = getR();
	setPolar(r, t);
    }

    /** The sum of the vector and rhs */
    public Vec2D plus(final Vec2D rhs) {
	return new Vec2D(x + rhs.x, y + rhs.y);
    }

    /** The difference of the vector and rhs: this - rhs */
    public Vec2D minus(final Vec2D rhs) {
	return new Vec2D(x - rhs.x, y - rhs.y);
    }

    public boolean equals(final Vec2D rhs) {
	return x == rhs.x && y == rhs.y;
    }

    /** Product of the vector and scalar */
    public Vec2D scalarMult(final double scalar) {
	return new Vec2D(scalar * x, scalar * y);
    }

    /** Dot product of the vector and rhs */
    public double dotProduct(final Vec2D rhs) {
	return x * rhs.x + y * rhs.y;
    }

    /**
     * Since Vector2D works only in the x-y plane, (u x v) points directly along
     * the z axis. This function returns the value on the z axis that (u x v)
     * reaches.
     *
     * @return signed magnitude of (this x rhs)
     */
    public double crossProduct(final Vec2D rhs) {
	return x * rhs.y - y * rhs.x;
    }

    /** Product of components of the vector: compenentProduct( <x y>) = x*y. */
    public double componentProduct() {
	return x * y;
    }

    /** Componentwise product: <this.x*rhs.x, this.y*rhs.y> */
    public Vec2D componentwiseProduct(final Vec2D rhs) {
	return new Vec2D(x * rhs.x, y * rhs.y);
    }

    /**
     * An alias for getR()
     * 
     * @return the length of this
     */
    public double length() {
	return getR();
    }

    /**
     * Returns a new vector with the same direction as the vector but with
     * length 1, except in the case of zero vectors, which return a copy of
     * themselves.
     */
    public Vec2D unitVector() {
	if (getR() != 0)
	    return new Vec2D(x / getR(), y / getR());
	return new Vec2D(0, 0);
    }

    /** Polar version of the vector, with radius in x and angle in y */
    public Vec2D toPolar() {
	return new Vec2D(Math.sqrt(x * x + y * y), Math.atan2(y, x));
    }

    /** Rectangular version of the vector, assuming radius in x and angle in y */
    public Vec2D toRect() {
	return new Vec2D(x * Math.cos(y), x * Math.sin(y));
    }

    /** @return Standard string representation of a vector: "<x, y>" */
    @Override
    public String toString() {
	return "<" + x + ", " + y + ">";
    }
}