// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package game.pathfinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic functionality of path finders to make it easy to create implementations
 * of {@link PathFinder}.
 *
 * @author Gene McCulley
 */
public abstract class AbstractPathFinder<T extends Node<T>> implements
PathFinder<T> {

	protected final List<PathListener<T>> listeners = new ArrayList<PathListener<T>>();
	protected volatile boolean canceled;

	@Override
	public void cancel() {
		canceled = true;
	}

	protected void fireConsidered(final PathEvent<T> pathEvent) {
		for (final PathListener<T> listener : listeners) {
			listener.considered(pathEvent);
		}
	}

	@Override
	public void addPathListener(final PathListener<T> l) {
		listeners.add(l);
	}

	@Override
	public void removePathListener(final PathListener<T> l) {
		listeners.remove(l);
	}

}