package game.map.generators;

import game.map.Biome;
import game.map.MapGenerator;
import game.map.SimplexNoise;
import game.math.MathUtils;
import main.Game;

public class PercipitationGenerator implements MapGenerator<Integer> {

    private final int scale;

    public PercipitationGenerator(final int scale) {
	this.scale = scale;
    }

    @Override
    public Integer[][] generate(final int width, final int height) {
	final Integer[][] tiles = new Integer[width][height];

	// 234 is random number to deviate from norm, but must be same every
	// time
	final SimplexNoise noise = new SimplexNoise(100, 0.5, Game.RAND_SEED + 234);

	double min = Integer.MAX_VALUE;
	double max = Integer.MIN_VALUE;

	final double[][] results = new double[width / scale][height / scale];

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
		// + 0.5 to round normally
		tiles[i][j] = (int) (MathUtils.mapNumInRange(results[i / scale][j / scale], min, max, Biome.RainRange.getMin(), Biome.RainRange.getMax()) + 0.5);
	    }
	}
	return tiles;
    }
}
