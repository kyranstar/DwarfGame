package game.entity.tree;

import game.entity.Entity;

import java.awt.Color;

public class Tree extends Entity {
	
	@Override
	public char getCharacter() {
		return 'T';
	}
	
	@Override
	public Color getForeground() {
		return Color.GREEN;
	}
	
	@Override
	public Color getBackground() {
		return new Color(50, 230, 100);
	}
	
}
