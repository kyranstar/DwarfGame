package game.map.weather;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAmount;

public class Calendar {
    private LocalDateTime time;

    // Beginning of spring
    public Calendar(final int year) {
	time = LocalDateTime.of(year, Month.MARCH, 20, 0, 0);
    }

    public Calendar(final int year, final Month month) {
	time = LocalDateTime.of(year, month, 0, 0, 0);
    }

    public void addTime(final TemporalAmount timeToAdd) {
	time = time.plus(timeToAdd);
    }

    public LocalDateTime getTime() {
	return time;
    }

    public Season getSeason() {
	return Season.getSeason(time.getMonth());
    }

    public static enum Season {
	SPRING(Month.MARCH, Month.APRIL, Month.MAY),
	SUMMER(Month.JUNE, Month.JULY, Month.AUGUST),
	FALL(Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER),
	WINTER(Month.DECEMBER, Month.JANUARY, Month.FEBRUARY);

	Month[] months;

	Season(final Month... months) {
	    this.months = months;
	}

	public static Season getSeason(final Month m) {
	    for (final Season season : values()) {
		for (final Month month : season.months) {
		    if (month == m)
			return season;
		}
	    }
	    return null;
	}
    }
}
