package game.map.generators;

import game.map.MapGenerator;
import game.map.SimplexNoise;
import game.tiles.GroundTile;
import main.Game;

public class TerrainGenerator implements MapGenerator<GroundTile> {

    @Override
    public GroundTile[][] generate(final int width, final int height) {

	final GroundTile[][] tiles = new GroundTile[width][height];

	// 3200 is random number to deviate from norm, but must be same every
	// time
	final SimplexNoise noise = new SimplexNoise(100, 0.5, Game.RAND_SEED + 3200);

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		final double result = (noise.getNoise(i, j) + 1) / 2;
		GroundTile toSet = null;
		if (result >= 0.6) {
		    toSet = GroundTile.ROCK;
		} else if (result >= 0.45) {
		    toSet = GroundTile.GRASS;
		} else if (result >= 0.4) {
		    toSet = GroundTile.DIRT;
		} else if (result >= 0.35) {
		    toSet = GroundTile.SAND;
		} else if (result >= 0.3) {
		    toSet = GroundTile.WATER;
		} else {
		    toSet = GroundTile.DEEP_WATER;
		}
		tiles[i][j] = toSet;
	    }
	}
	return tiles;
    }
}
