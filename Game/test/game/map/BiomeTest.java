package game.map;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BiomeTest {

    @Test
    public void testGetBiome() {

	// Check that all combinations are covered
	for (int temp = -10; temp < 30; temp++) {
	    for (int perc = 0; perc < 400; perc++) {
		// Should not throw exception
		Biome.getBiome(temp, perc);
	    }
	}

	assertEquals(Biome.SAVANNA, Biome.getBiome(30, 51));
	assertEquals(Biome.SAVANNA, Biome.getBiome(30, 125));
	assertEquals(Biome.SAVANNA, Biome.getBiome(21, 51));
	assertEquals(Biome.SAVANNA, Biome.getBiome(21, 125));

	assertEquals(Biome.BOREAL_FOREST, Biome.getBiome(1, 0));
	assertEquals(Biome.BOREAL_FOREST, Biome.getBiome(1, 100));
	assertEquals(Biome.BOREAL_FOREST, Biome.getBiome(10, 0));
	assertEquals(Biome.BOREAL_FOREST, Biome.getBiome(10, 100));

	assertEquals(Biome.COLD_DESERT, Biome.getBiome(11, 0));
	assertEquals(Biome.COLD_DESERT, Biome.getBiome(11, 50));
	assertEquals(Biome.COLD_DESERT, Biome.getBiome(20, 0));
	assertEquals(Biome.COLD_DESERT, Biome.getBiome(20, 50));

	assertEquals(Biome.SUBTROPICAL_DESERT, Biome.getBiome(21, 0));
	assertEquals(Biome.SUBTROPICAL_DESERT, Biome.getBiome(21, 10));
	assertEquals(Biome.SUBTROPICAL_DESERT, Biome.getBiome(30, 0));
	assertEquals(Biome.SUBTROPICAL_DESERT, Biome.getBiome(30, 10));

	assertEquals(Biome.TEMPERATE_RAINFOREST, Biome.getBiome(11, 126));
	assertEquals(Biome.TEMPERATE_RAINFOREST, Biome.getBiome(11, 249));
	assertEquals(Biome.TEMPERATE_RAINFOREST, Biome.getBiome(20, 126));
	assertEquals(Biome.TEMPERATE_RAINFOREST, Biome.getBiome(20, 249));

	assertEquals(Biome.TEMPERATE_SEASONAL_FOREST, Biome.getBiome(1, 126));
	assertEquals(Biome.TEMPERATE_SEASONAL_FOREST, Biome.getBiome(1, 249));
	assertEquals(Biome.TEMPERATE_SEASONAL_FOREST, Biome.getBiome(10, 126));
	assertEquals(Biome.TEMPERATE_SEASONAL_FOREST, Biome.getBiome(10, 249));

	assertEquals(Biome.TROPICAL_RAINFOREST, Biome.getBiome(21, 126));
	assertEquals(Biome.TROPICAL_RAINFOREST, Biome.getBiome(21, 249));
	assertEquals(Biome.TROPICAL_RAINFOREST, Biome.getBiome(30, 126));
	assertEquals(Biome.TROPICAL_RAINFOREST, Biome.getBiome(30, 249));

	assertEquals(Biome.TUNDRA, Biome.getBiome(-10, 0));
	assertEquals(Biome.TUNDRA, Biome.getBiome(-10, 50));
	assertEquals(Biome.TUNDRA, Biome.getBiome(0, 0));
	assertEquals(Biome.TUNDRA, Biome.getBiome(0, 50));

	assertEquals(Biome.WOODLAND, Biome.getBiome(11, 51));
	assertEquals(Biome.WOODLAND, Biome.getBiome(11, 125));
	assertEquals(Biome.WOODLAND, Biome.getBiome(20, 51));
	assertEquals(Biome.WOODLAND, Biome.getBiome(20, 125));

    }

}
