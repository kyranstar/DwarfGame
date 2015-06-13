package game.map.weather;

import static java.util.Arrays.asList;
import game.math.units.DistanceUnit;
import game.math.units.DistanceUnit.Centimeter;
import game.math.units.TemperatureUnit;
import game.math.units.TemperatureUnit.Celcius;

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

    public static Biome getBiome(final TemperatureUnit temp, final DistanceUnit percipitation) {
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
		// if this does not have a valid temperature range, go to next
		// biome
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
	FREEZE(Celcius.of(Integer.MIN_VALUE), Celcius.of(0)),
	TEMPERATE(Celcius.of(0), Celcius.of(10)),
	WARM(Celcius.of(10), Celcius.of(20)),
	HOT(Celcius.of(20), Celcius.of(Integer.MAX_VALUE));

	final TemperatureUnit min;
	final TemperatureUnit max;

	TempRange(final TemperatureUnit min, final TemperatureUnit max) {
	    this.min = min;
	    this.max = max;
	}

	public boolean contains(final TemperatureUnit num) {
	    return num.greaterThanOrEqual(min) && num.lessThanOrEqual(max);
	}

	public static TemperatureUnit getMin() {
	    return Celcius.of(-10);
	}

	public static TemperatureUnit getMax() {
	    return Celcius.of(30);
	}
    }

    public static enum RainRange {
	ARID(Centimeter.of(Integer.MIN_VALUE), Centimeter.of(50)),
	DRY(Centimeter.of(50), Centimeter.of(125)),
	HUMID(Centimeter.of(125), Centimeter.of(250)),
	MARSHY(Centimeter.of(250), Centimeter.of(Integer.MAX_VALUE));

	final DistanceUnit min;
	final DistanceUnit max;

	RainRange(final DistanceUnit min, final DistanceUnit max) {
	    this.min = min;
	    this.max = max;
	}

	public boolean contains(final DistanceUnit num) {
	    return num.greaterThanOrEqual(min) && num.lessThanOrEqual(max);
	}

	public static DistanceUnit getMin() {
	    return Centimeter.of(0);
	}

	public static DistanceUnit getMax() {
	    return Centimeter.of(300);
	}
    }

}