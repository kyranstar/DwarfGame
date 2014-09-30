package main;

import game.entity.dwarf.Dwarf;
import game.entity.dwarf.DwarfFactory;
import game.map.GameMap;
import game.map.generators.TerrainGenerator;
import game.tiles.Displayable;
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
	gameDisplay.requestFocus();
	gameDisplay.addKeyListener(this);
	gameDisplay.addMouseListener(this);

	final TerrainGenerator gen = new TerrainGenerator();

	map = new GameMap(display.getWidth() * 4, display.getHeight() * 4,
		gameDisplay.getAsciiPanel().getWidthInCharacters(), gameDisplay
		.getAsciiPanel().getHeightInCharacters());
	final Displayable[][] tiles = gen.generate(map.getWidth(),
		map.getHeight());
	for (int x = 0; x < tiles.length; x++) {
	    for (int y = 0; y < tiles[0].length; y++) {
		map.setBackgroundTile(x, y, tiles[x][y]);
	    }
	}

	gameDisplay.getConsole().writeln("Seed: " + gen.getSeed());
	Dwarf d = DwarfFactory.generateRandomDwarf();
	gameDisplay.getConsole().writeln(d.getWholeName());
	d.setX(10);
	map.addEntity(d);
	d = DwarfFactory.generateRandomDwarf();
	gameDisplay.getConsole().writeln(d.getWholeName());
	d.setX(20);
	map.addEntity(d);
	d = DwarfFactory.generateRandomDwarf();
	gameDisplay.getConsole().writeln(d.getWholeName());
	d.setX(30);
	map.addEntity(d);
	d = DwarfFactory.generateRandomDwarf();
	gameDisplay.getConsole().writeln(d.getWholeName());
	d.setX(40);
	map.addEntity(d);
    }

    @Override
    public void processInput(final Queue<KeyEvent> keyEvents,
	    final Queue<MouseEvent> mouseEvent,
	    final Queue<MouseWheelEvent> mouseWheelEvents) {
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

    @Override
    public void update() {

    }

    @Override
    public void draw() {
	map.draw(display);
	highlighter.draw(map.getViewportX(), map.getViewportY());
    }
}
