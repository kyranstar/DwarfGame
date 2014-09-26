package graphics.console;

import java.awt.Color;
import java.awt.Font;

public final class ConsoleData {
	private int capacity = 0;
	public int rows;
	public int columns;
	public Color[] background;
	public Color[] foreground;
	public Font[] font;
	public char[] text;

	ConsoleData() {
		// create empty console data
	}

	private void ensureCapacity(final int minCapacity) {
		if (capacity >= minCapacity)
			return;

		final char[] newText = new char[minCapacity];
		final Color[] newBackground = new Color[minCapacity];
		final Color[] newForeground = new Color[minCapacity];
		final Font[] newFont = new Font[minCapacity];

		final int size = rows * columns;
		if (size > 0) {
			System.arraycopy(text, 0, newText, 0, size);
			System.arraycopy(foreground, 0, newForeground, 0, size);
			System.arraycopy(background, 0, newBackground, 0, size);
			System.arraycopy(font, 0, newFont, 0, size);
		}

		text = newText;
		foreground = newForeground;
		background = newBackground;
		font = newFont;
		capacity = minCapacity;
	}

	void init(final int columns, final int rows) {
		ensureCapacity(rows * columns);
		this.rows = rows;
		this.columns = columns;
	}

	/**
	 * Sets a single character position
	 */
	public void setDataAt(final int column, int row, final char c,
			final Color fg, final Color bg, final Font f) {
		if (row >= rows) {
			row = rows - 1;
			System.arraycopy(text, columns, text, 0, text.length - columns);
		}
		final int pos = column + row * columns;
		text[pos] = c;
		foreground[pos] = fg;
		background[pos] = bg;
		font[pos] = f;
	}

	public char getCharAt(final int column, final int row) {
		final int offset = column + row * columns;
		return text[offset];
	}

	public Color getForegroundAt(final int column, final int row) {
		final int offset = column + row * columns;
		return foreground[offset];
	}

	public Color getBackgroundAt(final int column, final int row) {
		final int offset = column + row * columns;
		return background[offset];
	}

	public Font getFontAt(final int column, final int row) {
		final int offset = column + row * columns;
		return font[offset];
	}

	public void fillArea(final char c, final Color fg, final Color bg,
			final Font f, final int column, final int row, final int width,
			final int height) {
		for (int q = Math.max(0, row); q < Math.min(row + height, rows); q++) {
			for (int p = Math.max(0, column); p < Math.min(column + width,
					columns); p++) {
				final int offset = p + q * columns;
				text[offset] = c;
				foreground[offset] = fg;
				background[offset] = bg;
				font[offset] = f;
			}
		}
	}
}