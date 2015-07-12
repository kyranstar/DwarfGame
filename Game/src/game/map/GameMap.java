package game.map;

import game.entity.Entity;
import game.map.weather.Calendar;
import game.map.weather.Cloud;
import game.map.weather.Cloud.CloudPart;
import game.map.weather.Weather;
import game.math.MathUtils;
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
    private static final double EVAPORATION_RATE = 0.9999f;
    private static final double RUNOFF_RATE = 0.01f;

    private final Tile[][] background;
    private final int[][] averageTemperature;
    private final int[][] averagePrecipitation;
    private final double[][] waterLevel;
    private final double averageWaterLevel;
    private double[][] raining;

    Weather weather;
    private final Calendar calendar = new Calendar(1000);

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
	raining = new double[width][height];
	weather = new Weather(this, 10);

	averageWaterLevel = 0.3f;

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		waterLevel[i][j] = averageWaterLevel;
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
	drawBackground(display);
	drawEntities(display);
	drawClouds(display);
    }

    /**
     * @param display
     */
    private void drawBackground(final Display display) {
	for (int x = getViewportX(); x < getViewportX() + viewportWidthInTiles; x++) {
	    for (int y = viewportY; y < viewportY + viewportHeightInTiles; y++) {
		if (background[x][y].height > waterLevel[x][y]) {
		    display.setCharacterAt(x - getViewportX(), y - viewportY, background[x][y].getDrawingLayer(), background[x][y].getData());
		} else if (background[x][y].height > waterLevel[x][y] - 0.05f) {
		    // If we are in between the water level and the deep water
		    // level
		    display.setCharacterAt(x - getViewportX(), y - viewportY, GroundTile.WATER.getDrawingLayer(), GroundTile.WATER.getData());
		} else {
		    display.setCharacterAt(x - getViewportX(), y - viewportY, GroundTile.DEEP_WATER.getDrawingLayer(), GroundTile.DEEP_WATER.getData());
		}
	    }
	}
    }

    /**
     * @param display
     */
    private void drawEntities(final Display display) {
	display.clearLayer(DrawingLayer.PRIMARY);
	for (int i = 0; i < entities.size(); i++) {
	    final Entity e = entities.get(i);
	    display.setCharacterAt(e.getX() - getViewportX(), e.getY() - viewportY, e.getDrawingLayer(), e.getData());
	}
    }

    /**
     * @param display
     */
    private void drawClouds(final Display display) {
	display.clearLayer(DrawingLayer.CLOUDS);

	for (final Cloud cloud : weather.getClouds()) {
	    for (final CloudPart p : cloud.getParts()) {
		final int col = 255 / 2 + (int) (255 / 2 * (1 - p.getWaterContent()));
		Color c = new Color(col, col, col);

		if (cloud.isRaining()) {
		    c = new Color(255 / 2, 255 / 2, col);
		}
		final char cloudCharacter = '#';
		final AsciiCharacterData data = new AsciiCharacterData(cloudCharacter, Color.WHITE, c);
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

    public void update(final Duration delta) {
	getCalendar().addTime(delta);
	weather.update(delta);

	updateWaterLevel();
    }

    /**
     * Evaporates from water levels that are above average. Also handles water
     * running off to lower water levels
     */
    private void updateWaterLevel() {
	for (int x = 0; x < getWidth(); x++) {
	    for (int y = 0; y < getHeight(); y++) {
		waterLevel[x][y] += raining[x][y];
		if (waterLevel[x][y] > averageWaterLevel) {
		    waterLevel[x][y] *= EVAPORATION_RATE;
		}

		raining[x][y] = 0;
		if (x != 0 && waterLevel[x - 1][y] < waterLevel[x][y]) {
		    final double dif = (waterLevel[x][y] - waterLevel[x - 1][y]) * RUNOFF_RATE;

		    waterLevel[x - 1][y] += dif;
		    waterLevel[x][y] -= dif;
		}
		if (y != 0 && waterLevel[x][y - 1] < waterLevel[x][y]) {
		    final double dif = (waterLevel[x][y] - waterLevel[x][y - 1]) * RUNOFF_RATE;

		    waterLevel[x][y - 1] += dif;
		    waterLevel[x][y] -= dif;
		}
		if (y != getHeight() - 1 && waterLevel[x][y + 1] < waterLevel[x][y]) {
		    final double dif = (waterLevel[x][y] - waterLevel[x][y + 1]) * RUNOFF_RATE;
		    waterLevel[x][y + 1] += dif;
		    waterLevel[x][y] -= dif;
		}
		if (x != getWidth() - 1 && waterLevel[x + 1][y] < waterLevel[x][y]) {
		    final double dif = (waterLevel[x][y] - waterLevel[x + 1][y]) * RUNOFF_RATE;
		    waterLevel[x + 1][y] += dif;
		    waterLevel[x][y] -= dif;
		}
	    }
	}
    }

    public int[][] getAveragePrecipitation() {
	return MathUtils.copyArr(averagePrecipitation);
    }

    public void clearRaining() {
	raining = new double[getWidth()][getHeight()];
    }

    public double[][] getRaining() {
	return MathUtils.copyArr(raining);
    }

    public Calendar getCalendar() {
	return calendar;
    }

}
