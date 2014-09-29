package main;

import game.entity.Dwarf;
import game.entity.DwarfFactory;
import game.map.GameMap;
import game.map.generators.TerrainGenerator;
import game.pathfinding.BresenhamPlotter;
import graphics.Display;
import graphics.DisplayHighlighter;
import graphics.GameDisplay;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Queue;

public class Game extends GameLoop {
	
	private final Display display;
	private final GameMap map;
	private final DisplayHighlighter highlighter;
	
	private static final int TARGET_FPS = 60;
	
	public Game(final GameDisplay gameDisplay) {
		super(TARGET_FPS);
		display = new Display(gameDisplay.getAsciiPanel());
		highlighter = gameDisplay.getDisplayHighlighter();
		
		gameDisplay.setFocusable(true);
		gameDisplay.addKeyListener(this);
		gameDisplay.addMouseListener(this);
		
		map = new TerrainGenerator().generate(new GameMap(display.getWidth() * 4, display.getHeight() * 4, gameDisplay.getAsciiPanel().getWidthInCharacters(), gameDisplay
				.getAsciiPanel().getHeightInCharacters()));
		Dwarf d = DwarfFactory.generateRandomDwarf();
		d.setX(10);
		map.addEntity(d);
		d = DwarfFactory.generateRandomDwarf();
		d.setX(20);
		map.addEntity(d);
		d = DwarfFactory.generateRandomDwarf();
		d.setX(30);
		map.addEntity(d);
		d = DwarfFactory.generateRandomDwarf();
		d.setX(40);
		map.addEntity(d);
		
		BresenhamPlotter.Line(3, 0, 3, 5, (x, y) -> highlighter.highlightTile(DisplayHighlighter.createBlinker(x, y, 50)));
	}
	
	@Override
	public void processInput(final Queue<KeyEvent> keyEvents, final Queue<MouseEvent> mouseEvent, final Queue<MouseWheelEvent> mouseWheelEvents) {
		for (final KeyEvent e : keyEvents) {
			if (e instanceof KeyEvent) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_W:
						map.setViewportY(map.getViewportY() - 1);
						break;
					case KeyEvent.VK_A:
						map.setViewportX(map.getViewportX() - 1);
						break;
					case KeyEvent.VK_S:
						map.setViewportY(map.getViewportY() + 1);
						break;
					case KeyEvent.VK_D:
						map.setViewportX(map.getViewportX() + 1);
						break;
				}
			}
		}
	}
	
	int i = 0;
	
	@Override
	public void update() {
		i++;
		highlighter.highlightTile(DisplayHighlighter.createBlinker(0, 0, 50));
	}
	
	@Override
	public void draw() {
		map.draw(display);
		highlighter.draw(map.getViewportX(), map.getViewportY());
	}
}
