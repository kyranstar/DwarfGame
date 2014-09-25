package game.entity;

import game.math.MathUtils;

import java.io.IOException;
import java.io.InputStream;

public class DwarfFactory {
	private static final String FILE_NAME = "/names/ElvenNames.txt";
	private static NameGenerator nameGenerator;

	static {
		try {
			final InputStream stream = DwarfFactory.class
					.getResource(FILE_NAME).openStream();
			nameGenerator = new NameGenerator(stream);
		} catch (final IOException e) {
			throw new RuntimeException("Error opening file: " + FILE_NAME, e);
		}
	}

	public static Dwarf generateRandomDwarf() {
		final int AGE_LOW = 14;
		final int AGE_HIGH = 45;

		final Dwarf dwarf = new Dwarf();
		dwarf.setFirstName(nameGenerator.compose(MathUtils
				.randBetweenInclusive(2, 3)));
		dwarf.setLastName(nameGenerator.compose(MathUtils.randBetweenInclusive(
				2, 3)));
		dwarf.setGender(Gender.getRandom());
		dwarf.setAge(MathUtils.randBetweenInclusive(AGE_LOW, AGE_HIGH));

		return dwarf;
	}

	public static Dwarf getChildDwarf(final Dwarf dad, final Dwarf mom) {
		final Dwarf dwarf = new Dwarf();
		dwarf.setAge(0);
		dwarf.setGender(Gender.getRandom());
		dwarf.setLastName(dad.getLastName());
		dwarf.setFirstName(nameGenerator.compose(MathUtils
				.randBetweenInclusive(2, 3)));

		return dwarf;
	}
}
