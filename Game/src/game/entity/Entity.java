package game.entity;

import game.tiles.Displayable;
import graphics.DrawingLayer;

import java.awt.Color;

public class Entity implements Displayable {

	int xPos, yPos;

	@Override
	public char getCharacter() {
		return 169;
	}

	@Override
	public Color getForeground() {
		return Color.BLACK;
	}

	@Override
	public Color getBackground() {
		return Color.WHITE;
	}

	public void setX(final int xPos) {
		this.xPos = xPos;
	}

	public void setY(final int yPos) {
		this.yPos = yPos;
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	@Override
	public DrawingLayer getDrawingLayer() {
		return DrawingLayer.PRIMARY;
	}

}
