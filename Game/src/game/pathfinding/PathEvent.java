// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package game.pathfinding;

import java.util.EventObject;
import java.util.List;

/**
 * An encapsulation of the data describing an event related to a
 * {@link PathFinder}.
 *
 * @author Gene McCulley
 */
public abstract class PathEvent<T extends Node<?>> extends EventObject {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new path event.
     *
     * @param source
     *            the source of this event
     * @param path
     *            the path related to the event
     */
    protected PathEvent(final Object source) {
	super(source);
    }

    /**
     * Returns the path related to this event.
     *
     * @return the path related to the event in the form of a
     *         {@link java.util.List} of {@link Node}s or <tt>null</tt> if no
     *         path was found
     */
    public abstract List<T> getPath();

}