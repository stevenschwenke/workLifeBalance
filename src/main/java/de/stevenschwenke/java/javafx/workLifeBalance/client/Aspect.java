package de.stevenschwenke.java.javafx.workLifeBalance.client;

/**
 * An aspect of your life.
 * 
 * @author Steven Schwenke
 * 
 */
public enum Aspect {

	CAREER("Career"), FAMILY("Family"), HEALTH("Health"), YOU("You");
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Aspect(String name) {
		this.name = name;
	}

}
