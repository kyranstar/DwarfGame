package game.map.generators;

import game.map.GameMap;
import game.map.MapGenerator;
import game.map.SimplexNoise;
import game.tiles.GroundTile;

public class TerrainGenerator implements MapGenerator {

	@Override
	public GameMap generate(final GameMap map) {

		final SimplexNoise noise = new SimplexNoise(100, 0.5);

		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				final double result = (noise.getNoise(i, j) + 1) / 2;

				if (result >= 0.5) {
					map.setBackgroundTile(i, j, GroundTile.GRASS);
				} else if (result >= 0.4) {
					map.setBackgroundTile(i, j, GroundTile.DIRT);
				} else if (result >= 0.35) {
					map.setBackgroundTile(i, j, GroundTile.SAND);
				} else {
					map.setBackgroundTile(i, j, GroundTile.WATER);
				}
			}
		}
		return map;
	}
}
