package game.map;

import game.entity.Entity;
import game.map.weather.Calendar;
import game.map.weather.Cloud;
import game.map.weather.Cloud.CloudPart;
import game.map.weather.Weather;
import game.pathfinding.AStar;
import game.pathfinding.AbstractPathFinder;
import game.tiles.Displayable;
import game.tiles.GroundTile;
import game.tiles.Tile;
import graphics.Display;
import graphics.DrawingLayer;
import graphics.asciiPanel.AsciiCharacterData;
import graphics.asciiPanel.AsciiPanel;

import java.awt.Color;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameMap {
    public static final Duration TIME_PER_UPDATE = Duration.ofMinutes(180);

    private final Tile[][] background;
    private final int[][] averageTemperature;
    private final int[][] averagePrecipitation;
    private final double[][] waterLevel;
    private boolean[][] raining;

    Weather weather;
    private final Calendar calendar = new Calendar(0);

    List<Entity> entities = new ArrayList<>();

    private int viewportX, viewportY;

    private final int viewportWidthInTiles;
    private final int viewportHeightInTiles;

    public static final AbstractPathFinder<Tile> PATH_FINDER = new AStar<Tile>();

    public GameMap(final int width, final int height, final int viewportWidthInTiles, final int viewportHeightInTiles) {

	this.viewportWidthInTiles = viewportWidthInTiles;
	this.viewportHeightInTiles = viewportHeightInTiles;

	background = new Tile[width][height];
	averageTemperature = new int[width][height];
	averagePrecipitation = new int[width][height];
	waterLevel = new double[width][height];
	setRaining(new boolean[width][height]);
	weather = new Weather(this, 10);

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		waterLevel[i][j] = 0.3f;
		background[i][j] = new Tile(i, j, 0, Displayable.NULL_DISPLAYABLE);
	    }
	}

	attachAllNeighbors();
    }

    public Collection<Tile> getTiles() {
	final Collection<Tile> tiles = new ArrayList<Tile>();
	for (int i = 0; i < background.length; i++) {
	    for (int j = 0; j < background[0].length; j++) {
		tiles.add(background[i][j]);
	    }
	}
	return tiles;
    }

    public Tile getTile(final int x, final int y) {
	return background[x][y];
    }

    private void attachAllNeighbors() {
	for (int x = 0; x < background.length; x++) {
	    for (int y = 0; y < background[0].length; y++) {
		attachNeighbors(background[x][y]);
	    }
	}
    }

    public void addEntity(final Entity e) {
	entities.add(e);
    }

    public void draw(final Display display) {

	for (int x = getViewportX(); x < getViewportX() + viewportWidthInTiles; x++) {
	    for (int y = viewportY; y < viewportY + viewportHeightInTiles; y++) {
		if (background[x][y].height > waterLevel[x][y]) {
		    final char character = background[x][y].getCharacter();
		    final Color foreground = background[x][y].getForeground();
		    final Color backgroundColor = background[x][y].getBackground();
		    final AsciiCharacterData data = new AsciiCharacterData(character, foreground, backgroundColor);
		    display.setCharacterAt(x - getViewportX(), y - viewportY, background[x][y].getDrawingLayer(), data);
		} else if (background[x][y].height > waterLevel[x][y] - 0.05f) {
		    // If we are in between the water level and the deep water
		    // level
		    final char character = GroundTile.WATER.getCharacter();
		    final Color foreground = GroundTile.WATER.getForeground();
		    final Color backgroundColor = GroundTile.WATER.getBackground();
		    final AsciiCharacterData data = new AsciiCharacterData(character, foreground, backgroundColor);
		    display.setCharacterAt(x - getViewportX(), y - viewportY, GroundTile.WATER.getDrawingLayer(), data);
		} else {
		    final char character = GroundTile.DEEP_WATER.getCharacter();
		    final Color foreground = GroundTile.DEEP_WATER.getForeground();
		    final Color backgroundColor = GroundTile.DEEP_WATER.getBackground();
		    final AsciiCharacterData data = new AsciiCharacterData(character, foreground, backgroundColor);
		    display.setCharacterAt(x - getViewportX(), y - viewportY, GroundTile.DEEP_WATER.getDrawingLayer(), data);
		}
	    }
	}
	display.clearLayer(DrawingLayer.PRIMARY);
	for (int i = 0; i < entities.size(); i++) {
	    final Entity e = entities.get(i);

	    final char character = e.getCharacter();
	    final Color foreground = e.getForeground();
	    final Color backgroundColor = e.getBackground();
	    final AsciiCharacterData data = new AsciiCharacterData(character, foreground, backgroundColor);
	    display.setCharacterAt(e.getX() - getViewportX(), e.getY() - viewportY, e.getDrawingLayer(), data);
	}
	display.clearLayer(DrawingLayer.CLOUDS);

	// setViewportX((int)
	// weather.getClouds().get(0).getParts().get(0).getX());
	// setViewportY((int)
	// weather.getClouds().get(0).getParts().get(0).getY());
	for (final Cloud cloud : weather.getClouds()) {
	    for (final CloudPart p : cloud.getParts()) {
		Color c = new Color((int) (255 * p.getWaterContent()), (int) (255 * p.getWaterContent()), (int) (255 * p.getWaterContent()));
		// System.out.println(p.getWaterContent());
		if (cloud.isRaining()) {
		    c = new Color(0, 0, (int) (255 * p.getWaterContent()));
		}
		final char character = '#';
		final AsciiCharacterData data = new AsciiCharacterData(character, Color.WHITE, c);
		System.out.println(p.getX() + "," + p.getY());
		display.setCharacterAt((int) p.getX() - getViewportX(), (int) p.getY() - getViewportY(), DrawingLayer.CLOUDS, data);
	    }
	}
    }

    public int getHeight() {
	return background[0].length;
    }

    public int getWidth() {
	return background.length;
    }

    public void setBackgroundTile(final int x, final int y, final Tile tile) {
	background[x][y] = tile;
    }

    public void setBackgroundTile(final int x, final int y, final double height, final Displayable displayable) {
	final Tile t = new Tile(x, y, height, displayable);
	attachNeighbors(t);
	this.setBackgroundTile(x, y, t);
    }

    private void attachNeighbors(final Tile t) {
	final int maxX = background.length - 1;
	final int maxY = background[0].length - 1;

	final int x = t.getX();
	final int y = t.getY();

	if (x != 0) {
	    t.addNeighbor(background[x - 1][y]);
	}

	if (x != maxX) {
	    t.addNeighbor(background[x + 1][y]);
	}

	if (y != 0) {
	    t.addNeighbor(background[x][y - 1]);
	}

	if (y != maxY) {
	    t.addNeighbor(background[x][y + 1]);
	}
    }

    public int screenPixelXToTile(final int screenPixelX) {
	return screenPixelX / AsciiPanel.getCharWidth() + viewportX;
    }

    public int screenPixelYToTile(final int screenPixelY) {
	return screenPixelY / AsciiPanel.getCharHeight() + viewportY;
    }

    public int getViewportX() {
	return viewportX;
    }

    public void setViewportX(final int viewportX) {
	this.viewportX = viewportX;
	if (this.viewportX > getWidth() - viewportWidthInTiles) {
	    this.viewportX = getWidth() - viewportWidthInTiles;
	}
	if (this.viewportX < 0) {
	    this.viewportX = 0;
	}
    }

    public int getViewportY() {
	return viewportY;
    }

    public void setViewportY(final int viewportY) {
	this.viewportY = viewportY;
	if (this.viewportY > getHeight() - viewportHeightInTiles) {
	    this.viewportY = getHeight() - viewportHeightInTiles;
	}
	if (this.viewportY < 0) {
	    this.viewportY = 0;
	}
    }

    public int mapTileXToDisplayTileX(final int i) {
	return i - viewportX;
    }

    public int mapTileYToDisplayTileY(final int i) {
	return i - viewportY;
    }

    public void setAverageTemperature(final int x, final int y, final Integer avTemp) {
	averageTemperature[x][y] = avTemp;
    }

    public void setAveragePercipitation(final int x, final int y, final Integer avPer) {
	getAveragePrecipitation()[x][y] = avPer;
    }

    public void update() {
	calendar.addTime(TIME_PER_UPDATE);
	weather.update(TIME_PER_UPDATE);
    }

    public int[][] getAveragePrecipitation() {
	return averagePrecipitation;
    }

    public void clearRaining() {
	setRaining(new boolean[getWidth()][getHeight()]);
    }

    public boolean[][] getRaining() {
	return raining;
    }

    public void setRaining(final boolean[][] raining) {
	this.raining = raining;
    }

}
