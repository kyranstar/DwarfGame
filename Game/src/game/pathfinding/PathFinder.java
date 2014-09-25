// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package game.pathfinding;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;

/**
 * An interface implemented by classes that provide a mechanism to find routes
 * between nodes.
 *
 * When the path has been found or all possible paths have been exhausted, a
 * {@link PathEvent} will be sent.
 *
 * @author Gene McCulley
 */
public interface PathFinder<T extends Node<?>> {

	/**
	 * Cancels the execution.
	 */
	void cancel();

	/**
	 * Find a path between the start and the goal {@link Node}s.
	 *
	 * @param graph
	 *            the graph
	 * @param start
	 *            the starting @{link Node}
	 * @param goals
	 *            the goal @{link Node}s
	 * @return a {@link com.google.common.base.Optional} of
	 *         {@link java.util.List} of {@link Node} elements or an empty
	 *         optional if no path was found.
	 */
	public Optional<List<T>> findPath(Collection<T> graph, T start,
			Collection<T> goals);

	/**
	 * Find a path between the start and the goal {@link Node}s.
	 *
	 * @param graph
	 *            the graph
	 * @param start
	 *            the starting @{link Node}
	 * @param goal
	 *            the goal @{link Node}s
	 * @return a {@link com.google.common.base.Optional} of
	 *         {@link java.util.List} of {@link Node} elements or an empty
	 *         optional if no path was found.
	 */
	public Optional<List<T>> findPath(Collection<T> graph, T start, T goal);

	/**
	 * Add a listener for {@link PathEvent}s.
	 *
	 * @param l
	 *            the listener to add.
	 */
	void addPathListener(PathListener<T> l);

	/**
	 * Remove a listener for {@link PathEvent}s.
	 *
	 * @param l
	 *            the listener to remove.
	 */
	void removePathListener(PathListener<T> l);

}