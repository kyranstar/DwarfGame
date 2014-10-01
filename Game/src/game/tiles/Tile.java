package game.tiles;

// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.

import game.pathfinding.Node;
import graphics.DrawingLayer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Tile implements Node<Tile>, Displayable {

    private final int x;
    private final int y;
    private int avoidance;
    private boolean blocked;
    private transient List<Tile> neighbors;
    private final Displayable displayable;
    public final double height;

    public Tile(final int x, final int y, final double height, final Displayable t) {
	this.x = x;
	this.y = y;
	displayable = t;
	this.height = height;
	neighbors = new ArrayList<Tile>();
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public void setBlocked(final boolean blocked) {
	this.blocked = blocked;
    }

    public boolean getBlocked() {
	return blocked;
    }

    public int getAvoidance() {
	return avoidance;
    }

    public void setAvoidance(final int avoidance) {
	this.avoidance = avoidance;
    }

    public double getDistance(final Tile dest) {
	final double a = dest.x - x;
	final double b = dest.y - y;
	return Math.sqrt(a * a + b * b);
    }

    @Override
    public double pathCostEstimate(final Tile goal) {
	return getDistance(goal) * 0.99;
    }

    @Override
    public double traverseCost(final Tile target) {
	final double distance = getDistance(target);
	final double diff = target.getAvoidance() - getAvoidance();
	return Math.abs(diff) + distance;
    }

    @Override
    public Iterable<Tile> neighbors() {
	final List<Tile> realNeighbors = new ArrayList<Tile>();
	if (!blocked) {
	    for (final Tile loc : neighbors) {
		if (!loc.blocked) {
		    realNeighbors.add(loc);
		}
	    }
	}

	return realNeighbors;
    }

    public void addNeighbor(final Tile l) {
	neighbors.add(l);
    }

    @Override
    public String toString() {
	return "{x=" + x + ",y=" + y + "}";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + x;
	result = prime * result + y;
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
	final Tile other = (Tile) obj;
	if (x != other.x)
	    return false;
	if (y != other.y)
	    return false;
	return true;
    }

    @Override
    public char getCharacter() {
	return displayable.getCharacter();
    }

    @Override
    public Color getForeground() {
	return displayable.getForeground();
    }

    @Override
    public Color getBackground() {
	return displayable.getBackground();
    }

    @Override
    public DrawingLayer getDrawingLayer() {
	return displayable.getDrawingLayer();
    }

}