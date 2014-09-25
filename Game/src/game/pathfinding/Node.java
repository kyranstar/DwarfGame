// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package game.pathfinding;

/**
 * An interface to be implemented by classes that want a {@link PathFinder} to
 * find paths over them.
 *
 * @author Gene McCulley
 */
public interface Node<T extends Node<?>> {

	/**
	 * Returns the estimate of the cost to get from this node to the goal node.
	 * If unable to estimate, it is safe to return 0 or underestimate.
	 * Overestimates can result in failures to find a path.
	 *
	 * @return the estimate
	 */
	double pathCostEstimate(T goal);

	/**
	 * Returns the cost to get from this node to the dest node.
	 *
	 * @return the cost
	 */
	double traverseCost(T dest);

	/**
	 * Returns the neighbors of this node.
	 *
	 * @return the neighbors
	 */
	Iterable<T> neighbors();

}