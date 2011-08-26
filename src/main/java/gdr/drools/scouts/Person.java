package gdr.drools.scouts;

public class Person {
	private Gender gender;
	private int age;
	private String name;
	private String clubType;
	
	public Person(Gender gender, int age, String name) {
		this.gender = gender;
		this.age = age;
		this.name = name;
	}

	public String getGender() {
		return gender.name();		
	}

	public int getAge() {
		return age;
	}
	
	public String getName() {
		return name;
	}
	
	public void setClubType(String clubType) {
		this.clubType = clubType;
	}
	
	public String getClubType() {
		return clubType;
	}
	
	
}
