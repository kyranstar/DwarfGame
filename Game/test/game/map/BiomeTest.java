package game.map;

import static org.junit.Assert.assertEquals;
import game.map.weather.Biome;
import game.math.units.DistanceUnit.Centimeter;
import game.math.units.TemperatureUnit.Celcius;

import org.junit.Test;

public class BiomeTest {

    @Test
    public void testGetBiome() {

	// Check that all combinations are covered
	for (int temp = -10; temp < 30; temp++) {
	    for (int perc = 0; perc < 400; perc++) {
		// Should not throw exception
		Biome.getBiome(Celcius.of(temp), Centimeter.of(perc));
	    }
	}

	assertEquals(Biome.SAVANNA, Biome.getBiome(Celcius.of(30), Centimeter.of(51)));
	assertEquals(Biome.SAVANNA, Biome.getBiome(Celcius.of(30), Centimeter.of(125)));
	assertEquals(Biome.SAVANNA, Biome.getBiome(Celcius.of(21), Centimeter.of(51)));
	assertEquals(Biome.SAVANNA, Biome.getBiome(Celcius.of(21), Centimeter.of(125)));

	assertEquals(Biome.BOREAL_FOREST, Biome.getBiome(Celcius.of(1), Centimeter.of(0)));
	assertEquals(Biome.BOREAL_FOREST, Biome.getBiome(Celcius.of(1), Centimeter.of(100)));
	assertEquals(Biome.BOREAL_FOREST, Biome.getBiome(Celcius.of(10), Centimeter.of(0)));
	assertEquals(Biome.BOREAL_FOREST, Biome.getBiome(Celcius.of(10), Centimeter.of(100)));

	assertEquals(Biome.COLD_DESERT, Biome.getBiome(Celcius.of(11), Centimeter.of(0)));
	assertEquals(Biome.COLD_DESERT, Biome.getBiome(Celcius.of(11), Centimeter.of(50)));
	assertEquals(Biome.COLD_DESERT, Biome.getBiome(Celcius.of(20), Centimeter.of(0)));
	assertEquals(Biome.COLD_DESERT, Biome.getBiome(Celcius.of(20), Centimeter.of(50)));

	assertEquals(Biome.SUBTROPICAL_DESERT, Biome.getBiome(Celcius.of(21), Centimeter.of(0)));
	assertEquals(Biome.SUBTROPICAL_DESERT, Biome.getBiome(Celcius.of(21), Centimeter.of(10)));
	assertEquals(Biome.SUBTROPICAL_DESERT, Biome.getBiome(Celcius.of(30), Centimeter.of(0)));
	assertEquals(Biome.SUBTROPICAL_DESERT, Biome.getBiome(Celcius.of(30), Centimeter.of(10)));

	assertEquals(Biome.TEMPERATE_RAINFOREST, Biome.getBiome(Celcius.of(11), Centimeter.of(126)));
	assertEquals(Biome.TEMPERATE_RAINFOREST, Biome.getBiome(Celcius.of(11), Centimeter.of(249)));
	assertEquals(Biome.TEMPERATE_RAINFOREST, Biome.getBiome(Celcius.of(20), Centimeter.of(126)));
	assertEquals(Biome.TEMPERATE_RAINFOREST, Biome.getBiome(Celcius.of(20), Centimeter.of(249)));

	assertEquals(Biome.TEMPERATE_SEASONAL_FOREST, Biome.getBiome(Celcius.of(1), Centimeter.of(126)));
	assertEquals(Biome.TEMPERATE_SEASONAL_FOREST, Biome.getBiome(Celcius.of(1), Centimeter.of(249)));
	assertEquals(Biome.TEMPERATE_SEASONAL_FOREST, Biome.getBiome(Celcius.of(10), Centimeter.of(126)));
	assertEquals(Biome.TEMPERATE_SEASONAL_FOREST, Biome.getBiome(Celcius.of(10), Centimeter.of(249)));

	assertEquals(Biome.TROPICAL_RAINFOREST, Biome.getBiome(Celcius.of(21), Centimeter.of(126)));
	assertEquals(Biome.TROPICAL_RAINFOREST, Biome.getBiome(Celcius.of(21), Centimeter.of(249)));
	assertEquals(Biome.TROPICAL_RAINFOREST, Biome.getBiome(Celcius.of(30), Centimeter.of(126)));
	assertEquals(Biome.TROPICAL_RAINFOREST, Biome.getBiome(Celcius.of(30), Centimeter.of(249)));

	assertEquals(Biome.TUNDRA, Biome.getBiome(Celcius.of(-10), Centimeter.of(0)));
	assertEquals(Biome.TUNDRA, Biome.getBiome(Celcius.of(-10), Centimeter.of(50)));
	assertEquals(Biome.TUNDRA, Biome.getBiome(Celcius.of(0), Centimeter.of(0)));
	assertEquals(Biome.TUNDRA, Biome.getBiome(Celcius.of(0), Centimeter.of(50)));

	assertEquals(Biome.WOODLAND, Biome.getBiome(Celcius.of(11), Centimeter.of(51)));
	assertEquals(Biome.WOODLAND, Biome.getBiome(Celcius.of(11), Centimeter.of(125)));
	assertEquals(Biome.WOODLAND, Biome.getBiome(Celcius.of(20), Centimeter.of(51)));
	assertEquals(Biome.WOODLAND, Biome.getBiome(Celcius.of(20), Centimeter.of(125)));

    }

}
