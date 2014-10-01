package game.map.generators;

import game.map.noise.SimplexNoise;
import game.math.MathUtils;
import game.tiles.GroundTile;
import game.tiles.Tile;
import main.Game;

public class TerrainGenerator implements MapGenerator<Tile> {

    @Override
    public Tile[][] generate(final int width, final int height) {

	final Tile[][] tiles = new Tile[width][height];

	// 3200 is random number to deviate from norm, but must be same every
	// time
	final SimplexNoise noise = new SimplexNoise(100, 0.5, Game.RAND_SEED + 3200);

	double min = Double.MAX_VALUE;
	double max = Double.MIN_VALUE;

	final double[][] result = new double[width][height];

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		result[i][j] = (noise.getNoise(i, j) + 1) / 2;
		if (result[i][j] > max) {
		    max = result[i][j];
		}
		if (result[i][j] < min) {
		    min = result[i][j];
		}
	    }
	}

	// TODO: map values from 0 to 1
	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		final double mappedResult = MathUtils.mapNumInRange(result[i][j], min, max, 0, 1);

		GroundTile toSet = null;
		if (mappedResult >= 0.8) {
		    toSet = GroundTile.ROCK;
		} else if (mappedResult >= 0.5) {
		    toSet = GroundTile.GRASS;
		} else if (mappedResult >= 0.4) {
		    toSet = GroundTile.DIRT;
		} else {
		    toSet = GroundTile.SAND;
		}
		tiles[i][j] = new Tile(i, j, mappedResult, toSet);
	    }
	}
	return tiles;
    }
}
