package graphics.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.Timer;

public class JConsole extends JComponent implements HierarchyListener {
	private static final long serialVersionUID = 3571518591759968333L;

	private static final Color DEFAULT_FOREGROUND = Color.LIGHT_GRAY;
	private static final Color DEFAULT_BACKGROUND = Color.BLACK;
	private static final Font DEFAULT_FONT = new Font("Courier New",
			Font.PLAIN, 14);
	private static final int DEFAULT_BLINKRATE = 200;
	private static final boolean DEFAULT_BLINK_ON = true;

	private final ConsoleData data = new ConsoleData();

	private int fontWidth;
	private int fontHeight;
	private int fontYOffset;

	private boolean cursorVisible = false;
	private boolean cursorBlinkOn = true;
	private boolean cursorInverted = true;

	private int cursorX = 0;
	private int cursorY = 0;
	private Font mainFont = null;
	private Font currentFont = null;
	private Color currentForeground = DEFAULT_FOREGROUND;
	private Color currentBackground = DEFAULT_BACKGROUND;

	private Timer blinkTimer;

	public JConsole(final int columns, final int rows) {
		setMainFont(DEFAULT_FONT);
		setFont(mainFont);
		init(columns, rows);
		if (DEFAULT_BLINK_ON) {
			setCursorBlink(true);
		}
	}

	/**
	 * Sets the main font of the console, which is used to determine the size of
	 * characters
	 *
	 * @param font
	 */
	public void setMainFont(final Font font) {
		mainFont = font;

		final FontRenderContext fontRenderContext = new FontRenderContext(
				mainFont.getTransform(), false, false);
		final Rectangle2D charBounds = mainFont.getStringBounds("X",
				fontRenderContext);
		fontWidth = (int) charBounds.getWidth();
		fontHeight = (int) charBounds.getHeight();
		fontYOffset = -(int) charBounds.getMinY();

		setPreferredSize(new Dimension(data.columns * fontWidth, data.rows
				* fontHeight));

		repaint();
	}

	/**
	 * Utility class to handle the cursor blink animations
	 */
	private class TimerAction implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			if (cursorBlinkOn && isShowing()) {
				cursorInverted = !cursorInverted;
				repaintArea(getCursorX(), getCursorY(), 1, 1);
			} else {
				stopBlinking();
			}
		}
	}

	private void stopBlinking() {
		if (blinkTimer != null) {
			blinkTimer.stop();
			cursorInverted = true;
		}
	}

	private void startBlinking() {
		// getTimer().start();
	}

	public void setCursorBlink(final boolean blink) {
		if (blink) {
			cursorBlinkOn = true;
			startBlinking();
		} else {
			cursorBlinkOn = false;
			stopBlinking();
		}
	}

	public void setBlinkDelay(final int millis) {
		getTimer().setDelay(millis);
	}

	private Timer getTimer() {
		if (blinkTimer == null) {
			blinkTimer = new Timer(DEFAULT_BLINKRATE, new TimerAction());
			blinkTimer.setRepeats(true);
			if (cursorBlinkOn) {
				startBlinking();
			}
		}
		return blinkTimer;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		addHierarchyListener(this);
	}

	@Override
	public void removeNotify() {
		removeHierarchyListener(this);
		super.removeNotify();
	}

	public void setRows(final int rows) {
		resize(data.columns, rows);
	}

	@Override
	public void setFont(final Font f) {
		currentFont = f;
	}

	public void setCursorVisible(final boolean visible) {
		cursorVisible = visible;
	}

	public int getRows() {
		return data.rows;
	}

	public void setColumns(final int columns) {
		resize(columns, data.rows);
	}

	public int getColumns() {
		return data.columns;
	}

	public int getFontWidth() {
		return fontWidth;
	}

	public int getFontHeight() {
		return fontHeight;
	}

	/**
	 * Fires a repaint event on a specified rectangle of characters in the
	 * console
	 */
	public void repaintArea(final int column, final int row, final int width,
			final int height) {
		final int fw = getFontWidth();
		final int fh = getFontHeight();
		repaint(column * fw, row * fh, width * fw, height * fh);
	}

	/**
	 * Initialises the console to a specified size
	 */
	protected void init(final int columns, final int rows) {
		data.init(columns, rows);
		Arrays.fill(data.background, DEFAULT_BACKGROUND);
		Arrays.fill(data.foreground, DEFAULT_FOREGROUND);
		Arrays.fill(data.font, DEFAULT_FONT);
		Arrays.fill(data.text, ' ');

		setPreferredSize(new Dimension(columns * fontWidth, rows * fontHeight));
	}

	@Override
	public void resize(final int columns, final int rows) {
		// throw new UnsupportedOperationException();
	}

	public void clear() {
		clearArea(0, 0, data.columns, data.rows);
	}

	public void resetCursor() {
		repaintArea(cursorX, cursorY, 0, 0);
		cursorX = 0;
		cursorY = 0;
		repaintArea(cursorX, cursorY, 0, 0);
	}

	public void clearScreen() {
		clear();
		resetCursor();
	}

	private void clearArea(final int column, final int row, final int width,
			final int height) {
		data.fillArea(' ', currentForeground, currentBackground, currentFont,
				column, row, width, height);
		repaintArea(0, 0, width, height);
	}

	@Override
	public void paintComponent(final Graphics graphics) {
		final Graphics2D g = (Graphics2D) graphics;
		final Rectangle r = g.getClipBounds();

		// AffineTransform textTransform=new AffineTransform();
		// textTransform.scale(fontWidth, fontHeight);
		// g.setTransform(textTransform);

		// calculate x and y range to redraw
		final int x1 = (int) (r.getMinX() / fontWidth);
		final int x2 = (int) (r.getMaxX() / fontWidth) + 1;
		final int y1 = (int) (r.getMinY() / fontWidth);
		final int y2 = (int) (r.getMaxY() / fontWidth) + 1;

		final int curX = getCursorX();
		final int curY = getCursorY();

		for (int j = Math.max(0, y1); j < Math.min(y2, data.rows); j++) {
			final int offset = j * data.columns;
			int start = Math.max(x1, 0);
			final int end = Math.min(x2, data.columns);

			while (start < end) {
				Color nfg = data.foreground[offset + start];
				Color nbg = data.background[offset + start];
				final Font nf = data.font[offset + start];

				// index of ending position
				int i = start + 1;

				if (j == curY && start == curX) {
					if (cursorVisible && cursorBlinkOn && cursorInverted) {
						// swap foreground and background colours
						final Color t = nfg;
						nfg = nbg;
						nbg = t;
					}
				} else {
					// detect run
					while (i < end && !(j == curY && i == curX)
							&& nfg == data.foreground[offset + i]
							&& nbg == data.background[offset + i]
							&& nf == data.font[offset + i]) {
						i++;
					}
				}

				// set font
				g.setFont(nf);

				// draw background
				g.setBackground(nbg);
				g.clearRect(fontWidth * start, j * fontHeight, fontWidth
						* (i - start), fontHeight);

				// draw chars up to this point
				g.setColor(nfg);
				for (int k = start; k < i; k++) {
					g.drawChars(data.text, offset + k, 1, k * fontWidth, j
							* fontHeight + fontYOffset);
				}
				start = i;
			}
		}
	}

	public void setCursorPos(final int column, final int row) {
		if (column < 0 || column >= data.columns)
			throw new Error("Invalid X cursor position: " + column);
		if (row < 0 || row >= data.rows)
			throw new Error("Invalid Y cursor position: " + row);
		cursorX = column;
		cursorY = row;
	}

	public int getCursorX() {
		return cursorX;
	}

	public int getCursorY() {
		return cursorY;
	}

	@Override
	public void setForeground(final Color c) {
		currentForeground = c;
	}

	@Override
	public void setBackground(final Color c) {
		currentBackground = c;
	}

	@Override
	public Color getForeground() {
		return currentForeground;
	}

	@Override
	public Color getBackground() {
		return currentBackground;
	}

	public char getCharAt(final int column, final int row) {
		return data.getCharAt(column, row);
	}

	public Color getForegroundAt(final int column, final int row) {
		return data.getForegroundAt(column, row);
	}

	public Color getBackgroundAt(final int column, final int row) {
		return data.getBackgroundAt(column, row);
	}

	public Font getFontAt(final int column, final int row) {
		return data.getFontAt(column, row);
	}

	/**
	 * Redirects System.out to this console by calling System.setOut
	 */
	public void captureStdOut() {
		final PrintStream ps = new PrintStream(System.out) {
			@Override
			public void println(final String x) {
				writeln(x);
			}
		};

		System.setOut(ps);
	}

	public void write(final char c) {
		// if (c == '\n') {
		// while (cursorX != 0 && cursorX < getColumns()) {
		// write(' ');
		// }
		// } else {
		data.setDataAt(cursorX, cursorY, c, currentForeground,
				currentBackground, currentFont);
		moveCursor(c);
		// }
	}

	private void moveCursor(final char c) {
		switch (c) {
		case '\n':
			cursorY++;
			cursorX = 0;
			break;
		default:
			cursorX++;
			if (cursorX >= data.columns) {
				cursorX = 0;
				cursorY++;
			}
			break;
		}
	}

	public void writeln(final String line) {
		write(line);
		write('\n');
	}

	public void write(final String string, final Color foreGround,
			final Color backGround) {
		final Color foreTemp = currentForeground;
		final Color backTemp = currentBackground;
		setForeground(foreGround);
		setBackground(backGround);
		write(string);
		setForeground(foreTemp);
		setBackground(backTemp);
	}

	public void write(final String string) {
		for (int i = 0; i < string.length(); i++) {
			final char c = string.charAt(i);
			write(c);
		}
	}

	public void fillArea(final char c, final Color fg, final Color bg,
			final int column, final int row, final int width, final int height) {
		data.fillArea(c, fg, bg, currentFont, column, row, width, height);
		repaintArea(column, row, width, height);
	}

	@Override
	public void hierarchyChanged(final HierarchyEvent e) {
		if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
			if (isShowing()) {
				startBlinking();
			} else {
				stopBlinking();
			}
		}
	}

}