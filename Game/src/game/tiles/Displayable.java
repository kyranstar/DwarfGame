package game.tiles;

import graphics.DrawingLayer;

import java.awt.Color;

public interface Displayable {
	public final Displayable NULL_DISPLAYABLE = new Displayable() {

		@Override
		public char getCharacter() {
			return 'N';
		}

		@Override
		public Color getForeground() {
			return Color.WHITE;
		}

		@Override
		public Color getBackground() {
			return Color.red;
		}

		@Override
		public DrawingLayer getDrawingLayer() {
			return DrawingLayer.PRIMARY;
		}
	};

	public char getCharacter();

	public Color getForeground();

	public Color getBackground();

	public DrawingLayer getDrawingLayer();

}
