package graphics;

import graphics.asciiPanel.AsciiCharacterData;
import graphics.asciiPanel.AsciiPanel;

public class Display {
    private static final char TRANSPARENT_CHAR = ' ';

    private final AsciiPanel displayPanel;
    private final int widthInCharacters, heightInCharacters;

    private final static int Z_LEVELS = DrawingLayer.values().length;

    private final AsciiCharacterData[][][] characterMap;

    public Display(final AsciiPanel panel) {
	displayPanel = panel;
	widthInCharacters = panel.getWidthInCharacters();
	heightInCharacters = panel.getHeightInCharacters();

	characterMap = new AsciiCharacterData[widthInCharacters][heightInCharacters][Z_LEVELS];
	for (int x = 0; x < widthInCharacters; x++) {
	    for (int y = 0; y < heightInCharacters; y++) {
		for (int z = 0; z < Z_LEVELS; z++) {
		    characterMap[x][y][z] = new AsciiCharacterData(TRANSPARENT_CHAR, displayPanel.getDefaultForegroundColor(), displayPanel.getDefaultBackgroundColor());
		}
	    }
	}
    }

    public void setCharacterAt(final int x, final int y, final DrawingLayer z, final AsciiCharacterData c) {
	if (x < 0 || x >= widthInCharacters || y < 0 || y >= heightInCharacters)
	    return;
	characterMap[x][y][z.layer] = c;

	// if z is not the top level
	if (z.layer != Z_LEVELS - 1) {
	    // check all levels above
	    for (int i = z.layer + 1; i < Z_LEVELS; i++) {
		// if there is an opaque character
		if (characterMap[x][y][i].character != TRANSPARENT_CHAR)
		    // we dont need to draw anything
		    return;
	    }
	}

	if (c.character == TRANSPARENT_CHAR) {
	    // loop through all characters under the transparent character
	    for (int i = z.layer - 1; i >= 0; i--) {
		// if we find a non transparent character
		if (characterMap[x][y][i].character != TRANSPARENT_CHAR) {
		    // display that one instead
		    displayPanel.write(characterMap[x][y][i].character, x, y, characterMap[x][y][i].foregroundColor, characterMap[x][y][i].backgroundColor);
		    return;
		}
	    }
	    // if there were no non trasparent characters
	    displayPanel.write(TRANSPARENT_CHAR, x, y, displayPanel.getDefaultForegroundColor(), displayPanel.getDefaultBackgroundColor());
	    // if we are a highlighter, we draw the below character and then
	    // just draw on top
	} else {
	    displayPanel.write(c.character, x, y, c.foregroundColor, c.backgroundColor);
	}
    }

    public void repaint() {
	displayPanel.repaint();
    }

    public AsciiCharacterData getCharacterAt(final int x, final int y, final DrawingLayer z) {
	return characterMap[x][y][z.layer];
    }

    public int getWidth() {
	return widthInCharacters;
    }

    public int getHeight() {
	return heightInCharacters;
    }

    public void clearLayer(final DrawingLayer layer) {
	for (int x = 0; x < widthInCharacters; x++) {
	    for (int y = 0; y < heightInCharacters; y++) {
		setCharacterAt(x, y, layer, new AsciiCharacterData(TRANSPARENT_CHAR, displayPanel.getDefaultForegroundColor(), displayPanel.getDefaultBackgroundColor()));
	    }
	}
    }
}
