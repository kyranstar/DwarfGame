// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package game.pathfinding;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Ordering;

/**
 * An implementation of the A* path finding algorithm.
 *
 * @author Gene McCulley
 */
public class AStar<T extends Node<T>> extends AbstractPathFinder<T> {

	private class State extends NodeState<T> implements Comparable<State> {

		private final double costFromStart;
		private final double costToGoal;

		private State(final T node, final double costFromStart,
				final State parent, final Collection<T> goals) {
			super(node, parent);
			this.costFromStart = costFromStart;
			costToGoal = minimumPathCostEstimate(node, goals);
		}

		private double minimumPathCostEstimate(final T node,
				final Collection<T> goals) {
			double min = Double.MAX_VALUE;
			for (final T goal : goals) {
				final double cost = node.pathCostEstimate(goal);
				if (cost < min) {
					min = cost;
				}
			}

			return min;
		}

		private double totalCost() {
			return costFromStart + costToGoal;
		}

		@Override
		public int compareTo(final State other) {
			return (int) (totalCost() - other.totalCost());
		}

	}

	@Override
	public Optional<List<T>> findPath(final Collection<T> graph, final T start,
			final T goal) {
		return findPath(graph, start, Collections.singleton(goal));
	}

	@Override
	public Optional<List<T>> findPath(final Collection<T> graph, final T start,
			final Collection<T> goals) {
		canceled = false;
		final Map<T, State> open = new HashMap<T, State>();
		final Map<T, State> closed = new HashMap<T, State>();
		final State startState = new State(start, 0, null, goals);
		open.put(start, startState);
		final Ordering<Map.Entry<T, State>> orderByEntryValue = orderByEntryValue();
		while (!(open.isEmpty() || canceled)) {
			final State state = open.remove(orderByEntryValue.min(
					open.entrySet()).getKey());
			fireConsidered(new PathEvent<T>(this) {

				private static final long serialVersionUID = 1L;

				@Override
				public List<T> getPath() {
					return state.makePath();
				}

			});
			if (goals.contains(state.node))
				return Optional.of(state.makePath());
			else {
				for (final T newNode : state.node.neighbors()) {
					final double newCost = state.costFromStart
							+ state.node.traverseCost(newNode);
					final State openNode = open.get(newNode);
					if (openNode != null && openNode.costFromStart <= newCost) {
						continue;
					}

					final State closedNode = closed.get(newNode);
					if (closedNode != null
							&& closedNode.costFromStart <= newCost) {
						continue;
					}

					if (closedNode != null) {
						closed.remove(newNode);
					}

					if (openNode != null) {
						open.remove(newNode);
					}

					final State newState = new State(newNode, newCost, state,
							goals);
					open.put(newNode, newState);
				}
			}

			closed.put(state.node, state);
		}

		return Optional.absent();
	}

	static private <K, V extends Comparable<V>> Ordering<Map.Entry<K, V>> orderByEntryValue() {
		return new Ordering<Map.Entry<K, V>>() {

			@Override
			public int compare(final Map.Entry<K, V> o1,
					final Map.Entry<K, V> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}

		};
	}

}