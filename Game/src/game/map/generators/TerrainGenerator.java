package game.map.generators;

import game.map.MapGenerator;
import game.map.SimplexNoise;
import game.tiles.GroundTile;

public class TerrainGenerator implements MapGenerator<GroundTile> {

    private long seed;

    public long getSeed() {
	return seed;
    }

    @Override
    public GroundTile[][] generate(final int width, final int height) {

	final GroundTile[][] tiles = new GroundTile[width][height];

	seed = System.currentTimeMillis();
	final SimplexNoise noise = new SimplexNoise(100, 0.5, seed);

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
