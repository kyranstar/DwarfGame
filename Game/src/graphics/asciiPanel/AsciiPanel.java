package graphics.asciiPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This simulates a code page 437 ASCII terminal display.
 *
 * @author Trystan Spangler
 */
public class AsciiPanel extends JPanel {
    private static final long serialVersionUID = -4167851861147593092L;

    /**
     * The color black (pure black).
     */
    public static Color black = new Color(0, 0, 0);

    /**
     * The color red.
     */
    public static Color red = new Color(128, 0, 0);

    /**
     * The color green.
     */
    public static Color green = new Color(0, 128, 0);

    /**
     * The color yellow.
     */
    public static Color yellow = new Color(128, 128, 0);

    /**
     * The color blue.
     */
    public static Color blue = new Color(0, 0, 128);

    /**
     * The color magenta.
     */
    public static Color magenta = new Color(128, 0, 128);

    /**
     * The color cyan.
     */
    public static Color cyan = new Color(0, 128, 128);

    /**
     * The color white (light gray).
     */
    public static Color white = new Color(192, 192, 192);

    /**
     * A brighter black (dark gray).
     */
    public static Color brightBlack = new Color(128, 128, 128);

    /**
     * A brighter red.
     */
    public static Color brightRed = new Color(255, 0, 0);

    /**
     * A brighter green.
     */
    public static Color brightGreen = new Color(0, 255, 0);

    /**
     * A brighter yellow.
     */
    public static Color brightYellow = new Color(255, 255, 0);

    /**
     * A brighter blue.
     */
    public static Color brightBlue = new Color(0, 0, 255);

    /**
     * A brighter magenta.
     */
    public static Color brightMagenta = new Color(255, 0, 255);

    /**
     * A brighter cyan.
     */
    public static Color brightCyan = new Color(0, 255, 255);

    /**
     * A brighter white (pure white).
     */
    public static Color brightWhite = new Color(255, 255, 255);
    private Image offscreenBuffer;
    private Graphics offscreenGraphics;
    private final int widthInCharacters;
    private final int heightInCharacters;
    private static final int charWidth = 9;
    private static final int charHeight = 16;

    private Color defaultBackgroundColor;
    private Color defaultForegroundColor;
    private int cursorX;
    private int cursorY;
    private BufferedImage glyphSprite;
    private final BufferedImage[] glyphs;
    private final char[][] chars;
    private final Color[][] backgroundColors;
    private final Color[][] foregroundColors;
    private final char[][] oldChars;
    private final Color[][] oldBackgroundColors;
    private final Color[][] oldForegroundColors;

    /**
     * Gets the height, in pixels, of a character.
     *
     * @return
     */
    public static int getCharHeight() {
	return charHeight;
    }

    /**
     * Gets the width, in pixels, of a character.
     *
     * @return
     */
    public static int getCharWidth() {
	return charWidth;
    }

    /**
     * Gets the height in characters. A standard terminal is 24 characters high.
     *
     * @return
     */
    public int getHeightInCharacters() {
	return heightInCharacters;
    }

    /**
     * Gets the width in characters. A standard terminal is 80 characters wide.
     *
     * @return
     */
    public int getWidthInCharacters() {
	return widthInCharacters;
    }

    /**
     * Gets the distance from the left new text will be written to.
     *
     * @return
     */
    public int getCursorX() {
	return cursorX;
    }

    /**
     * Sets the distance from the left new text will be written to. This should
     * be equal to or greater than 0 and less than the the width in characters.
     *
     * @param cursorX
     *            the distance from the left new text should be written to
     */
    public void setCursorX(final int cursorX) {
	if (cursorX < 0 || cursorX >= widthInCharacters)
	    throw new IllegalArgumentException("cursorX " + cursorX + " must be within range [0," + widthInCharacters + ").");

	this.cursorX = cursorX;
    }

    /**
     * Gets the distance from the top new text will be written to.
     *
     * @return
     */
    public int getCursorY() {
	return cursorY;
    }

    /**
     * Sets the distance from the top new text will be written to. This should
     * be equal to or greater than 0 and less than the the height in characters.
     *
     * @param cursorY
     *            the distance from the top new text should be written to
     */
    public void setCursorY(final int cursorY) {
	if (cursorY < 0 || cursorY >= heightInCharacters)
	    throw new IllegalArgumentException("cursorY " + cursorY + " must be within range [0," + heightInCharacters + ").");

	this.cursorY = cursorY;
    }

    /**
     * Sets the x and y position of where new text will be written to. The
     * origin (0,0) is the upper left corner. The x should be equal to or
     * greater than 0 and less than the the width in characters. The y should be
     * equal to or greater than 0 and less than the the height in characters.
     *
     * @param x
     *            the distance from the left new text should be written to
     * @param y
     *            the distance from the top new text should be written to
     */
    public void setCursorPosition(final int x, final int y) {
	setCursorX(x);
	setCursorY(y);
    }

    /**
     * Gets the default background color that is used when writing new text.
     *
     * @return
     */
    public Color getDefaultBackgroundColor() {
	return defaultBackgroundColor;
    }

    /**
     * Sets the default background color that is used when writing new text.
     *
     * @param defaultBackgroundColor
     */
    public void setDefaultBackgroundColor(final Color defaultBackgroundColor) {
	if (defaultBackgroundColor == null)
	    throw new NullPointerException("defaultBackgroundColor must not be null.");

	this.defaultBackgroundColor = defaultBackgroundColor;
    }

    /**
     * Gets the default foreground color that is used when writing new text.
     *
     * @return
     */
    public Color getDefaultForegroundColor() {
	return defaultForegroundColor;
    }

    /**
     * Sets the default foreground color that is used when writing new text.
     *
     * @param defaultForegroundColor
     */
    public void setDefaultForegroundColor(final Color defaultForegroundColor) {
	if (defaultForegroundColor == null)
	    throw new NullPointerException("defaultForegroundColor must not be null.");

	this.defaultForegroundColor = defaultForegroundColor;
    }

    /**
     * Class constructor. Default size is 80x24.
     */
    public AsciiPanel() {
	this(80, 24);
    }

    /**
     * Class constructor specifying the width and height in characters.
     *
     * @param width
     * @param height
     */
    public AsciiPanel(final int width, final int height) {
	super();

	if (width < 1)
	    throw new IllegalArgumentException("width " + width + " must be greater than 0.");

	if (height < 1)
	    throw new IllegalArgumentException("height " + height + " must be greater than 0.");

	widthInCharacters = width;
	heightInCharacters = height;
	setPreferredSize(new Dimension(charWidth * widthInCharacters, charHeight * heightInCharacters));

	defaultBackgroundColor = black;
	defaultForegroundColor = white;

	chars = new char[widthInCharacters][heightInCharacters];
	backgroundColors = new Color[widthInCharacters][heightInCharacters];
	foregroundColors = new Color[widthInCharacters][heightInCharacters];

	oldChars = new char[widthInCharacters][heightInCharacters];
	oldBackgroundColors = new Color[widthInCharacters][heightInCharacters];
	oldForegroundColors = new Color[widthInCharacters][heightInCharacters];

	glyphs = new BufferedImage[256];

	loadGlyphs();

	AsciiPanel.this.clear();
    }

    @Override
    public void update(final Graphics g) {
	paint(g);
    }

    @Override
    public void paint(final Graphics g) {
	if (g == null)
	    throw new NullPointerException();

	if (offscreenBuffer == null) {
	    offscreenBuffer = createImage(getWidth(), getHeight());
	    offscreenGraphics = offscreenBuffer.getGraphics();
	}

	for (int x = 0; x < widthInCharacters; x++) {
	    for (int y = 0; y < heightInCharacters; y++) {
		if (oldBackgroundColors[x][y] == backgroundColors[x][y] && oldForegroundColors[x][y] == foregroundColors[x][y] && oldChars[x][y] == chars[x][y]) {
		    continue;
		}

		final Color bg = backgroundColors[x][y];
		final Color fg = foregroundColors[x][y];

		final LookupOp op = setColors(bg, fg);
		final BufferedImage img = op.filter(glyphs[chars[x][y]], null);
		offscreenGraphics.drawImage(img, x * charWidth, y * charHeight, null);

		oldBackgroundColors[x][y] = backgroundColors[x][y];
		oldForegroundColors[x][y] = foregroundColors[x][y];
		oldChars[x][y] = chars[x][y];
	    }
	}

	g.drawImage(offscreenBuffer, 0, 0, this);
    }

    private void loadGlyphs() {
	try {
	    glyphSprite = ImageIO.read(AsciiPanel.class.getResource("/cp437.png"));
	} catch (final IOException e) {
	    System.err.println("loadGlyphs(): " + e.getMessage());
	}

	for (int i = 0; i < 256; i++) {
	    final int sx = i % 32 * charWidth + 8;
	    final int sy = i / 32 * charHeight + 8;

	    glyphs[i] = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
	    glyphs[i].getGraphics().drawImage(glyphSprite, 0, 0, charWidth, charHeight, sx, sy, sx + charWidth, sy + charHeight, null);
	}
    }

    /**
     * Create a <code>LookupOp</code> object (lookup table) mapping the original
     * pixels to the background and foreground colors, respectively.
     *
     * @param bgColor
     *            the background color
     * @param fgColor
     *            the foreground color
     * @return the <code>LookupOp</code> object (lookup table)
     */
    private LookupOp setColors(final Color bgColor, final Color fgColor) {
	final short[] a = new short[256];
	final short[] r = new short[256];
	final short[] g = new short[256];
	final short[] b = new short[256];

	final byte bgr = (byte) bgColor.getRed();
	final byte bgg = (byte) bgColor.getGreen();
	final byte bgb = (byte) bgColor.getBlue();

	final byte fgr = (byte) fgColor.getRed();
	final byte fgg = (byte) fgColor.getGreen();
	final byte fgb = (byte) fgColor.getBlue();

	for (int i = 0; i < 256; i++) {
	    if (i == 0) {
		a[i] = (byte) 255;
		r[i] = bgr;
		g[i] = bgg;
		b[i] = bgb;
	    } else {
		a[i] = (byte) 255;
		r[i] = fgr;
		g[i] = fgg;
		b[i] = fgb;
	    }
	}

	final short[][] table = { r, g, b, a };
	return new LookupOp(new ShortLookupTable(0, table), null);
    }

    /**
     * Clear the entire screen to whatever the default background color is.
     *
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear() {
	return clear(' ', 0, 0, widthInCharacters, heightInCharacters, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Clear the entire screen with the specified character and whatever the
     * default foreground and background colors are.
     *
     * @param character
     *            the character to write
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(final char character) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	return clear(character, 0, 0, widthInCharacters, heightInCharacters, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Clear the entire screen with the specified character and whatever the
     * specified foreground and background colors are.
     *
     * @param character
     *            the character to write
     * @param foreground
     *            the foreground color or null to use the default
     * @param background
     *            the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(final char character, final Color foreground, final Color background) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	return clear(character, 0, 0, widthInCharacters, heightInCharacters, foreground, background);
    }

    /**
     * Clear the section of the screen with the specified character and whatever
     * the default foreground and background colors are.
     *
     * @param character
     *            the character to write
     * @param x
     *            the distance from the left to begin writing from
     * @param y
     *            the distance from the top to begin writing from
     * @param width
     *            the height of the section to clear
     * @param height
     *            the width of the section to clear
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(final char character, final int x, final int y, final int width, final int height) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	if (x < 0 || x >= widthInCharacters)
	    throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ").");

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ").");

	if (width < 1)
	    throw new IllegalArgumentException("width " + width + " must be greater than 0.");

	if (height < 1)
	    throw new IllegalArgumentException("height " + height + " must be greater than 0.");

	if (x + width > widthInCharacters)
	    throw new IllegalArgumentException("x + width " + (x + width) + " must be less than " + (widthInCharacters + 1) + ".");

	if (y + height > heightInCharacters)
	    throw new IllegalArgumentException("y + height " + (y + height) + " must be less than " + (heightInCharacters + 1) + ".");

	return clear(character, x, y, width, height, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Clear the section of the screen with the specified character and whatever
     * the specified foreground and background colors are.
     *
     * @param character
     *            the character to write
     * @param x
     *            the distance from the left to begin writing from
     * @param y
     *            the distance from the top to begin writing from
     * @param width
     *            the height of the section to clear
     * @param height
     *            the width of the section to clear
     * @param foreground
     *            the foreground color or null to use the default
     * @param background
     *            the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(final char character, final int x, final int y, final int width, final int height, final Color foreground, final Color background) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	if (x < 0 || x >= widthInCharacters)
	    throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

	if (width < 1)
	    throw new IllegalArgumentException("width " + width + " must be greater than 0.");

	if (height < 1)
	    throw new IllegalArgumentException("height " + height + " must be greater than 0.");

	if (x + width > widthInCharacters)
	    throw new IllegalArgumentException("x + width " + (x + width) + " must be less than " + (widthInCharacters + 1) + ".");

	if (y + height > heightInCharacters)
	    throw new IllegalArgumentException("y + height " + (y + height) + " must be less than " + (heightInCharacters + 1) + ".");

	for (int xo = x; xo < x + width; xo++) {
	    for (int yo = y; yo < y + height; yo++) {
		write(character, xo, yo, foreground, background);
	    }
	}
	return this;
    }

    /**
     * Write a character to the cursor's position. This updates the cursor's
     * position.
     *
     * @param character
     *            the character to write
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final char character) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	return write(character, cursorX, cursorY, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a character to the cursor's position with the specified foreground
     * color. This updates the cursor's position but not the default foreground
     * color.
     *
     * @param character
     *            the character to write
     * @param foreground
     *            the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final char character, final Color foreground) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	return write(character, cursorX, cursorY, foreground, defaultBackgroundColor);
    }

    /**
     * Write a character to the cursor's position with the specified foreground
     * and background colors. This updates the cursor's position but not the
     * default foreground or background colors.
     *
     * @param character
     *            the character to write
     * @param foreground
     *            the foreground color or null to use the default
     * @param background
     *            the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final char character, final Color foreground, final Color background) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	return write(character, cursorX, cursorY, foreground, background);
    }

    /**
     * Write a character to the specified position. This updates the cursor's
     * position.
     *
     * @param character
     *            the character to write
     * @param x
     *            the distance from the left to begin writing from
     * @param y
     *            the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final char character, final int x, final int y) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	if (x < 0 || x >= widthInCharacters)
	    throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

	return write(character, x, y, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a character to the specified position with the specified foreground
     * color. This updates the cursor's position but not the default foreground
     * color.
     *
     * @param character
     *            the character to write
     * @param x
     *            the distance from the left to begin writing from
     * @param y
     *            the distance from the top to begin writing from
     * @param foreground
     *            the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final char character, final int x, final int y, final Color foreground) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	if (x < 0 || x >= widthInCharacters)
	    throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

	return write(character, x, y, foreground, defaultBackgroundColor);
    }

    /**
     * Write a character to the specified position with the specified foreground
     * and background colors. This updates the cursor's position but not the
     * default foreground or background colors.
     *
     * @param character
     *            the character to write
     * @param x
     *            the distance from the left to begin writing from
     * @param y
     *            the distance from the top to begin writing from
     * @param foreground
     *            the foreground color or null to use the default
     * @param background
     *            the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final char character, final int x, final int y, Color foreground, Color background) {
	if (character < 0 || character >= glyphs.length)
	    throw new IllegalArgumentException("character " + character + " must be within range [0," + glyphs.length + "].");

	if (x < 0 || x >= widthInCharacters)
	    throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

	if (foreground == null) {
	    foreground = defaultForegroundColor;
	}
	if (background == null) {
	    background = defaultBackgroundColor;
	}

	chars[x][y] = character;
	foregroundColors[x][y] = foreground;
	backgroundColors[x][y] = background;
	cursorX = x + 1;
	cursorY = y;
	return this;
    }

    /**
     * Write a string to the cursor's position. This updates the cursor's
     * position.
     *
     * @param string
     *            the string to write
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final String string) {
	if (string == null)
	    throw new NullPointerException("string must not be null");

	if (cursorX + string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("cursorX + string.length() " + (cursorX + string.length()) + " must be less than " + widthInCharacters + ".");

	return write(string, cursorX, cursorY, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a string to the cursor's position with the specified foreground
     * color. This updates the cursor's position but not the default foreground
     * color.
     *
     * @param string
     *            the string to write
     * @param foreground
     *            the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final String string, final Color foreground) {
	if (string == null)
	    throw new NullPointerException("string must not be null");

	if (cursorX + string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("cursorX + string.length() " + (cursorX + string.length()) + " must be less than " + widthInCharacters + ".");

	return write(string, cursorX, cursorY, foreground, defaultBackgroundColor);
    }

    /**
     * Write a string to the cursor's position with the specified foreground and
     * background colors. This updates the cursor's position but not the default
     * foreground or background colors.
     *
     * @param string
     *            the string to write
     * @param foreground
     *            the foreground color or null to use the default
     * @param background
     *            the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final String string, final Color foreground, final Color background) {
	if (string == null)
	    throw new NullPointerException("string must not be null");

	if (cursorX + string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("cursorX + string.length() " + (cursorX + string.length()) + " must be less than " + widthInCharacters + ".");

	return write(string, cursorX, cursorY, foreground, background);
    }

    /**
     * Write a string to the specified position. This updates the cursor's
     * position.
     *
     * @param string
     *            the string to write
     * @param x
     *            the distance from the left to begin writing from
     * @param y
     *            the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final String string, final int x, final int y) {
	if (string == null)
	    throw new NullPointerException("string must not be null");

	if (x + string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("x + string.length() " + (x + string.length()) + " must be less than " + widthInCharacters + ".");

	if (x < 0 || x >= widthInCharacters)
	    throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

	return write(string, x, y, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a string to the specified position with the specified foreground
     * color. This updates the cursor's position but not the default foreground
     * color.
     *
     * @param string
     *            the string to write
     * @param x
     *            the distance from the left to begin writing from
     * @param y
     *            the distance from the top to begin writing from
     * @param foreground
     *            the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final String string, final int x, final int y, final Color foreground) {
	if (string == null)
	    throw new NullPointerException("string must not be null");

	if (x + string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("x + string.length() " + (x + string.length()) + " must be less than " + widthInCharacters + ".");

	if (x < 0 || x >= widthInCharacters)
	    throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

	return write(string, x, y, foreground, defaultBackgroundColor);
    }

    /**
     * Write a string to the specified position with the specified foreground
     * and background colors. This updates the cursor's position but not the
     * default foreground or background colors.
     *
     * @param string
     *            the string to write
     * @param x
     *            the distance from the left to begin writing from
     * @param y
     *            the distance from the top to begin writing from
     * @param foreground
     *            the foreground color or null to use the default
     * @param background
     *            the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(final String string, final int x, final int y, Color foreground, Color background) {
	if (string == null)
	    throw new NullPointerException("string must not be null.");

	if (x + string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("x + string.length() " + (x + string.length()) + " must be less than " + widthInCharacters + ".");

	if (x < 0 || x >= widthInCharacters)
	    throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ").");

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ").");

	if (foreground == null) {
	    foreground = defaultForegroundColor;
	}

	if (background == null) {
	    background = defaultBackgroundColor;
	}

	for (int i = 0; i < string.length(); i++) {
	    write(string.charAt(i), x + i, y, foreground, background);
	}
	return this;
    }

    /**
     * Write a string to the center of the panel at the specified y position.
     * This updates the cursor's position.
     *
     * @param string
     *            the string to write
     * @param y
     *            the distance from the top to begin writing from
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writeCenter(final String string, final int y) {
	if (string == null)
	    throw new NullPointerException("string must not be null");

	if (string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("string.length() " + string.length() + " must be less than " + widthInCharacters + ".");

	final int x = (widthInCharacters - string.length()) / 2;

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

	return write(string, x, y, defaultForegroundColor, defaultBackgroundColor);
    }

    /**
     * Write a string to the center of the panel at the specified y position
     * with the specified foreground color. This updates the cursor's position
     * but not the default foreground color.
     *
     * @param string
     *            the string to write
     * @param y
     *            the distance from the top to begin writing from
     * @param foreground
     *            the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writeCenter(final String string, final int y, final Color foreground) {
	if (string == null)
	    throw new NullPointerException("string must not be null");

	if (string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("string.length() " + string.length() + " must be less than " + widthInCharacters + ".");

	final int x = (widthInCharacters - string.length()) / 2;

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

	return write(string, x, y, foreground, defaultBackgroundColor);
    }

    /**
     * Write a string to the center of the panel at the specified y position
     * with the specified foreground and background colors. This updates the
     * cursor's position but not the default foreground or background colors.
     *
     * @param string
     *            the string to write
     * @param y
     *            the distance from the top to begin writing from
     * @param foreground
     *            the foreground color or null to use the default
     * @param background
     *            the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel writeCenter(final String string, final int y, Color foreground, Color background) {
	if (string == null)
	    throw new NullPointerException("string must not be null.");

	if (string.length() >= widthInCharacters)
	    throw new IllegalArgumentException("string.length() " + string.length() + " must be less than " + widthInCharacters + ".");

	final int x = (widthInCharacters - string.length()) / 2;

	if (y < 0 || y >= heightInCharacters)
	    throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ").");

	if (foreground == null) {
	    foreground = defaultForegroundColor;
	}

	if (background == null) {
	    background = defaultBackgroundColor;
	}

	for (int i = 0; i < string.length(); i++) {
	    write(string.charAt(i), x + i, y, foreground, background);
	}
	return this;
    }

    public void withEachTile(final TileTransformer transformer) {
	withEachTile(0, 0, widthInCharacters, heightInCharacters, transformer);
    }

    public void withEachTile(final int left, final int top, final int width, final int height, final TileTransformer transformer) {
	final AsciiCharacterData data = new AsciiCharacterData();

	for (int x0 = 0; x0 < width; x0++) {
	    for (int y0 = 0; y0 < height; y0++) {
		final int x = left + x0;
		final int y = top + y0;

		if (x < 0 || y < 0 || x >= widthInCharacters || y >= heightInCharacters) {
		    continue;
		}

		data.character = chars[x][y];
		data.foregroundColor = foregroundColors[x][y];
		data.backgroundColor = backgroundColors[x][y];

		transformer.transformTile(x, y, data);

		chars[x][y] = data.character;
		foregroundColors[x][y] = data.foregroundColor;
		backgroundColors[x][y] = data.backgroundColor;
	    }
	}
    }
}
