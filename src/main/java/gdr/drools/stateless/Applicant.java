package gdr.drools.stateless;

public class Applicant {
	private String name;

	private int age;
	private boolean leagallyBlind;

	public Applicant(String name, int age, boolean leagallyBlind) {
		this.name = name;
		this.age = age;
		this.leagallyBlind = leagallyBlind;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public boolean isLeagallyBlind() {
		return leagallyBlind;
	}
}
