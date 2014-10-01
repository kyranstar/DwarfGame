package game.map.generators;

import game.map.GameMap;
import game.tiles.Tile;
import graphics.GameDisplay;

public class GameMapCreator {
    private static final int BIOME_SCALE = 3;

    MapGenerator<Tile> tileCreator = new TerrainGenerator();
    MapGenerator<Integer> tempGenerator = new TempGenerator(BIOME_SCALE);
    MapGenerator<Integer> percipitationGenerator = new PercipitationGenerator(BIOME_SCALE);

    public GameMap createGameMap(final int width, final int height, final GameDisplay gameDisplay) {
	final GameMap map = new GameMap(width, height, gameDisplay.getAsciiPanel().getWidthInCharacters(), gameDisplay.getAsciiPanel().getHeightInCharacters());
	final Tile[][] tiles = tileCreator.generate(width, height);
	final Integer[][] temperature = tempGenerator.generate(width, height);
	final Integer[][] percipitation = tempGenerator.generate(width, height);
	for (int x = 0; x < tiles.length; x++) {
	    for (int y = 0; y < tiles[0].length; y++) {
		map.setBackgroundTile(x, y, tiles[x][y]);
		map.setAverageTemperature(x, y, temperature[x][y]);
		map.setAveragePercipitation(x, y, percipitation[x][y]);
	    }
	}
	return map;
    }
}
