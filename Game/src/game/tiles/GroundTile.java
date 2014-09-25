package game.tiles;

import graphics.DrawingLayer;

import java.awt.Color;

public enum GroundTile implements Displayable {

	GRASS('^', Color.GREEN, new Color(50, 150, 50)), DIRT('a', new Color(165,
			82, 0), new Color(124, 62, 0)), WATER('~', Color.BLUE, new Color(
					50, 100, 200)), SAND('s', Color.WHITE, new Color(200, 200, 100));

	private char character;
	private Color foreground;
	private Color background;

	private GroundTile(final char character, final Color foreground,
			final Color background) {
		this.character = character;
		this.foreground = foreground;
		this.background = background;
	}

	@Override
	public char getCharacter() {
		return character;
	}

	@Override
	public Color getForeground() {
		return foreground;
	}

	@Override
	public Color getBackground() {
		return background;
	}

	@Override
	public DrawingLayer getDrawingLayer() {
		return DrawingLayer.BACKGROUND;
	}

}
