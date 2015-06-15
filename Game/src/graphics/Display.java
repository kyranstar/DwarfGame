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
    }

    public void repaint() {
	for (int x = 0; x < widthInCharacters; x++) {
	    for (int y = 0; y < heightInCharacters; y++) {
		for (int z = 0; z < Z_LEVELS; z++) {
		    final AsciiCharacterData c = getCharacterAt(x, y, DrawingLayer.get(z));
		    if (c.character != TRANSPARENT_CHAR) {
			displayPanel.write(c.character, x, y, c.foregroundColor, c.backgroundColor);
		    }
		}
	    }
	}

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
