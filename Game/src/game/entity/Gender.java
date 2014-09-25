package game.entity;

import game.math.MathUtils;

public enum Gender {
	MALE, FEMALE;

	public static Gender getRandom() {
		return Gender.values()[MathUtils.randBetween(0, Gender.values().length)];
	}
}
