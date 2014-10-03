package game.map.weather;

import static java.util.Arrays.asList;
import game.math.units.NumberDistance;
import game.math.units.NumberDistance.Centimeter;

import java.util.List;

public enum Biome {
    TUNDRA(asList(TempRange.FREEZE), asList(RainRange.ARID)),
    BOREAL_FOREST(asList(TempRange.TEMPERATE), asList(RainRange.ARID, RainRange.DRY)),
    ICE_DESERT(asList(TempRange.FREEZE), asList(RainRange.DRY)),
    SNOW_DESERT(asList(TempRange.FREEZE), asList(RainRange.HUMID)),
    ICE(asList(TempRange.FREEZE), asList(RainRange.MARSHY)),
    MARSH(asList(TempRange.TEMPERATE, TempRange.WARM), asList(RainRange.MARSHY)),
    COLD_DESERT(asList(TempRange.TEMPERATE, TempRange.WARM), asList(RainRange.ARID)),
    WOODLAND(asList(TempRange.WARM), asList(RainRange.DRY)),
    SUBTROPICAL_DESERT(asList(TempRange.HOT), asList(RainRange.ARID)),
    TEMPERATE_SEASONAL_FOREST(asList(TempRange.TEMPERATE), asList(RainRange.HUMID)),
    TEMPERATE_RAINFOREST(asList(TempRange.TEMPERATE, TempRange.WARM), asList(RainRange.HUMID)),
    SAVANNA(asList(TempRange.WARM, TempRange.HOT), asList(RainRange.DRY)),
    TROPICAL_RAINFOREST(asList(TempRange.HOT), asList(RainRange.HUMID, RainRange.MARSHY));

    private List<TempRange> temp;
    private List<RainRange> rain;

    Biome(final List<TempRange> temp, final List<RainRange> rain) {
	this.temp = temp;
	this.rain = rain;
    }

    public static Biome getBiome(final int temp, final NumberDistance percipitation) {
	for (final Biome biome : values()) {
	    boolean valid = false;
	    for (final TempRange tRange : biome.temp) {
		if (tRange.contains(temp)) {
		    // we have a valid temp range, lets move on to rain
		    valid = true;
		    break;
		}
	    }
	    if (!valid) {
		// if this does not have a valid temp range, go to next biome
		continue;
	    }
	    for (final RainRange rRange : biome.rain) {
		if (rRange.contains(percipitation))
		    return biome;
	    }
	}

	throw new RuntimeException("No biome found for Temp: " + temp + " and Percipitation: " + percipitation);
    }

    public static enum TempRange {
	// USES CELCIUS!
	FREEZE(Integer.MIN_VALUE, 0),
	TEMPERATE(0, 10),
	WARM(10, 20),
	HOT(20, Integer.MAX_VALUE);

	final int min;
	final int max;

	TempRange(final int min, final int max) {
	    this.min = min;
	    this.max = max;
	}

	public boolean contains(final int num) {
	    return num >= min && num <= max;
	}

	public static int getMin() {
	    return -10;
	}

	public static int getMax() {
	    return 30;
	}
    }

    public static enum RainRange {
	// USES CM!
	ARID(Integer.MIN_VALUE, 50),
	DRY(50, 125),
	HUMID(125, 250),
	MARSHY(250, Integer.MAX_VALUE);

	final NumberDistance min;
	final NumberDistance max;

	RainRange(final int min, final int max) {
	    this.min = Centimeter.of(min);
	    this.max = Centimeter.of(max);
	}

	public boolean contains(final NumberDistance num) {
	    return num.greaterThanOrEqual(min) && num.lessThanOrEqual(max);
	}

	public static int getMin() {
	    return 0;
	}

	public static int getMax() {
	    return 300;
	}
    }

}