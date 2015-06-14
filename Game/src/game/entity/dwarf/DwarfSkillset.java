package game.entity.dwarf;

import java.util.HashMap;
import java.util.Map;

public class DwarfSkillset<T> {
    private final Map<DwarfSkill, T> skills = new HashMap<>();

    public enum DwarfSkill {
	ATTACK();
    }

    public T getSkill(final DwarfSkill s) {
	return skills.get(s);
    }

    public void setSkill(final DwarfSkill skill, final T amount) {
	skills.put(skill, amount);
    }
}
