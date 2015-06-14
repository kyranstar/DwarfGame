package main;

import game.entity.Entity;
import game.entity.dwarf.Dwarf;
import game.entity.dwarf.DwarfFactory;
import game.entity.dwarf.DwarfManager;
import game.map.GameMap;
import game.map.generators.GameMapCreator;
import graphics.Display;
import graphics.DisplayHighlighter;
import graphics.GameDisplay;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.time.Duration;
import java.util.Queue;

public class Game extends GameLoop {

    private final Display display;
    private final GameMap map;
    private final DisplayHighlighter highlighter;
    private final DwarfManager dwarfManager = new DwarfManager();

    public static final long RAND_SEED = System.currentTimeMillis();

    private static final int TARGET_FPS = 30;
    private static final int TARGET_UPS = 10;
    private static final Duration TIME_PER_UPDATE = Duration.ofMinutes(5);

    public Game(final GameDisplay gameDisplay) {
	super(TARGET_FPS, TARGET_UPS);
	display = new Display(gameDisplay.getAsciiPanel());
	highlighter = gameDisplay.getDisplayHighlighter();

	gameDisplay.addKeyListener(this);
	gameDisplay.addMouseListener(this);

	map = new GameMapCreator().createGameMap(display.getWidth() * 4, display.getHeight() * 4, gameDisplay);

	gameDisplay.getConsole().writeln("Seed: " + Game.RAND_SEED);
	for (int i = 0; i < 5; i++) {
	    final Dwarf d = DwarfFactory.generateRandomDwarf(map);
	    gameDisplay.getConsole().writeln(d.getWholeName());
	    d.setX(i * 10);
	    addEntity(d);
	}

    }

    private void addEntity(final Entity entity) {
	if (entity instanceof Dwarf) {
	    dwarfManager.addDwarf((Dwarf) entity);
	}
	map.addEntity(entity);
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

    @Override
    public void update() {
	map.update(TIME_PER_UPDATE);
	dwarfManager.update(map);
    }

    @Override
    public void draw() {
	map.draw(display);
	highlighter.draw(map.getViewportX(), map.getViewportY());

	display.repaint();
    }
}
