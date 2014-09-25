package game.entity;

public class Dwarf extends Entity {
	private String firstName;
	private String lastName;
	private Gender gender;
	private int age;
	private final DwarfSkillset<Integer> skills = new DwarfSkillset<>();

	// Package private, only factories in package can create
	Dwarf() {

	}

	public String getWholeName() {
		return firstName + " " + lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(final Gender gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(final int age) {
		this.age = age;
	}

	public void incrementAge() {
		age++;
	}
}
