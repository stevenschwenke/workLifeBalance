package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

/**
 * The data access object for the cockpit.
 * 
 * @author Steven Schwenke
 * 
 */
public interface CockpitDao {

	public double calculateCareer();

	public double calculateFamily();

	public double calculateHealth();

	public double calculateYou();

	/**
	 * @return overall points of the player.
	 */
	public Long calculateOverallpoints();
}
