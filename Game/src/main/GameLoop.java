package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayDeque;
import java.util.Queue;

public abstract class GameLoop implements KeyListener, MouseListener,
MouseWheelListener {
	private int FRAMES_PER_SECOND;
	private boolean running = true;
	long targetTime;

	// ArrayDeque is supposed to be the fastest collection
	private final Queue<KeyEvent> keyEvents = new ArrayDeque<>();
	private final Queue<MouseEvent> mouseEvents = new ArrayDeque<>();
	private final Queue<MouseWheelEvent> mouseWheelEvents = new ArrayDeque<>();

	private long runningFPS;

	protected GameLoop(final int fps) {
		setTargetFPS(fps);
	}

	public void setTargetFPS(final int fps) {
		FRAMES_PER_SECOND = fps;
		targetTime = 1000 / FRAMES_PER_SECOND;
	}

	public void run() {
		int currentFPS = 0;
		long counterstart = System.nanoTime();
		long counterelapsed = 0;
		long start;
		long elapsed;
		long wait;
		targetTime = 1000 / FRAMES_PER_SECOND;

		while (running) {
			start = System.nanoTime();

			synchronized (keyEvents) {
				synchronized (mouseEvents) {
					synchronized (mouseWheelEvents) {
						processInput(keyEvents, mouseEvents, mouseWheelEvents);
						mouseEvents.clear();
						keyEvents.clear();
						mouseWheelEvents.clear();
					}
				}
			}
			update();
			// time to update and process input
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if (hasTimeToDraw(wait)) {
				draw();
				// Take account for the time it took to draw
				elapsed = System.nanoTime() - start;
				wait = targetTime - elapsed / 1000000;
			}
			counterelapsed = System.nanoTime() - counterstart;
			currentFPS++;

			// at the end of every second
			if (counterelapsed >= 1000000000L) {
				// runningFPS is how many frames we processed last second
				runningFPS = currentFPS;
				currentFPS = 0;
				counterstart = System.nanoTime();
			}

			// dont wanna wait for negative time
			if (wait > 0) {
				try {
					Thread.sleep(wait);
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public long getCurrentFPS() {
		return runningFPS;
	}

	private boolean hasTimeToDraw(final long wait) {
		// Not really sure how to implement this method... Maybe just time the
		// draw method and hardcode the threshold in?
		return true;
	}

	public void stop() {
		running = false;
	}

	public abstract void processInput(Queue<KeyEvent> keyEvents,
			Queue<MouseEvent> mouseEvent,
			Queue<MouseWheelEvent> mouseWheelEvents2);

	public abstract void update();

	public abstract void draw();

	@Override
	public void mouseClicked(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		synchronized (keyEvents) {
			keyEvents.add(e);
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		synchronized (keyEvents) {
			keyEvents.add(e);
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {
		synchronized (keyEvents) {
			keyEvents.add(e);

		}
	}

	@Override
	public void mouseWheelMoved(final MouseWheelEvent e) {
		synchronized (mouseWheelEvents) {
			mouseWheelEvents.add(e);
		}
	}
}