package game.entity;

import game.tiles.Displayable;
import graphics.DrawingLayer;

public abstract class Entity implements Displayable {

	int xPos, yPos;
	
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
