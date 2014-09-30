package game.map;

import static java.util.Arrays.asList;

import java.util.List;

public enum Biome {
    TUNDRA(asList(TempRange.FREEZE), asList(RainRange.ARID)),
    BOREAL_FOREST(asList(TempRange.TEMPERATE), asList(RainRange.ARID,
	    RainRange.DRY)), COLD_DESERT(asList(TempRange.TEMPERATE,
	    TempRange.WARM), asList(RainRange.ARID));

    private List<TempRange> temp;
    private List<RainRange> rain;

    Biome(final List<TempRange> temp, final List<RainRange> rain) {
	this.temp = temp;
	this.rain = rain;
    }
}

enum TempRange {
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
}

enum RainRange {
    // USES CM!
    ARID(Integer.MIN_VALUE, 100),
    DRY(100, 200),
    HUMID(200, 300),
    MARSHY(300, Integer.MAX_VALUE);

    final int min;
    final int max;

    RainRange(final int min, final int max) {
	this.min = min;
	this.max = max;
    }

    public boolean contains(final int num) {
	return num >= min && num <= max;
    }
}
