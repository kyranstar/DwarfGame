package game.entity.dwarf;

import game.map.GameMap;
import game.math.MathUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DwarfFactory {
    private static final String FILE_NAME = "/names/ElvenNames.txt";
    private static NameGenerator nameGenerator;

    static {
	try {
	    final InputStream stream = DwarfFactory.class.getResource(FILE_NAME).openStream();
	    nameGenerator = new NameGenerator(stream);
	} catch (final IOException e) {
	    throw new RuntimeException("Error opening file: " + FILE_NAME, e);
	}
    }

    public static Dwarf generateRandomDwarf(final GameMap map) {
	final int AGE_LOW = 14;
	final int AGE_HIGH = 45;

	final Dwarf dwarf = new Dwarf();
	dwarf.setFirstName(nameGenerator.compose(MathUtils.randBetweenInclusive(2, 3)));
	dwarf.setLastName(nameGenerator.compose(MathUtils.randBetweenInclusive(2, 3)));
	dwarf.setGender(Gender.getRandom());
	dwarf.setAge(MathUtils.randBetweenInclusive(AGE_LOW, AGE_HIGH));

	// random birthday
	final GregorianCalendar gc = new GregorianCalendar();
	gc.set(Calendar.YEAR, map.getCalendar().getDate().getYear() - dwarf.getAge());
	final int dayOfYear = MathUtils.randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
	gc.set(Calendar.DAY_OF_YEAR, dayOfYear);

	dwarf.setBirthday(LocalDateTime.of(gc.get(Calendar.YEAR), Month.of(gc.get(Calendar.MONTH) + 1), gc.get(Calendar.DAY_OF_MONTH), MathUtils.randBetween(0, 24),
		MathUtils.randBetween(0, 60)));

	return dwarf;
    }

    public static Dwarf getChildDwarf(final Dwarf dad, final Dwarf mom, final GameMap map) {
	final Dwarf dwarf = new Dwarf();
	dwarf.setAge(0);
	dwarf.setGender(Gender.getRandom());
	dwarf.setLastName(dad.getLastName());
	dwarf.setFirstName(nameGenerator.compose(MathUtils.randBetweenInclusive(2, 3)));
	dwarf.setBirthday(map.getCalendar().getDate());

	return dwarf;
    }
}
