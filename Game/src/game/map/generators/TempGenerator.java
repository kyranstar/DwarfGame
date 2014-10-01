package game.map.generators;

import game.map.noise.SimplexNoise;
import game.map.weather.Biome;
import game.math.MathUtils;
import main.Game;

public class TempGenerator implements MapGenerator<Integer> {

    private final int scale;

    public TempGenerator(final int scale) {
	this.scale = scale;
    }

    @Override
    public Integer[][] generate(final int width, final int height) {
	final Integer[][] tiles = new Integer[width][height];

	// -300 is random number to deviate from norm, but must be same every
	// time
	final SimplexNoise noise = new SimplexNoise(100, 0.5, Game.RAND_SEED - 300);

	double min = Integer.MAX_VALUE;
	double max = Integer.MIN_VALUE;

	final double[][] results = new double[width / scale + 1][height / scale + 1];

	for (int i = 0; i < width / scale; i++) {
	    for (int j = 0; j < height / scale; j++) {
		results[i][j] = (noise.getNoise(i, j) + 1) / 2;
		if (results[i][j] < min) {
		    min = results[i][j];
		}
		if (results[i][j] > max) {
		    max = results[i][j];
		}
	    }
	}
	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		// Maps number from the range of the perlin noise to the range
		// of the temperature
		// + 0.5 to round normally
		tiles[i][j] = (int) (MathUtils.mapNumInRange(results[i / scale][j / scale], min, max, Biome.TempRange.getMin(), Biome.TempRange.getMax()) + 0.5);
	    }
	}
	return tiles;
    }
}
