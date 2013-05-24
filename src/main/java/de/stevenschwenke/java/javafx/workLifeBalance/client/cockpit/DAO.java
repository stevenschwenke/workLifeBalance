package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView.CalendarDao;
import de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord.NewTimeRecordDao;

/**
 * The data access object for the application.
 * 
 * @author Steven Schwenke
 * 
 */
public class DAO implements NewTimeRecordDao, CalendarDao {

	private static Logger log = LogManager.getLogger(DAO.class.getName());

	private List<DayRecord> dayRecords;

	public DAO() {
		super();
		dayRecords = new ArrayList<DayRecord>();
		try {
			initDB();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			log.error("Error setting up the database: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void addNewDayRecord(DayRecord newRecord) {
		log.debug("addNewDayRecord: " + newRecord);
		dayRecords.add(newRecord);
	}

	public double calculateCareer() {
		double result = 0;
		for (DayRecord dr : dayRecords) {
			for (TimeRecord r : dr.getTimeRecordsToday()) {
				if (r.getAspect().equals(Aspect.CAREER)) {
					result += r.getHours();
				}
			}
		}
		return result;
	}

	public double calculateFamily() {
		double result = 0;
		for (DayRecord dr : dayRecords) {
			for (TimeRecord r : dr.getTimeRecordsToday()) {
				if (r.getAspect().equals(Aspect.FAMILY)) {
					result += r.getHours();
				}
			}
		}
		return result;
	}

	public double calculateHealth() {
		double result = 0;
		for (DayRecord dr : dayRecords) {
			for (TimeRecord r : dr.getTimeRecordsToday()) {
				if (r.getAspect().equals(Aspect.HEALTH)) {
					result += r.getHours();
				}
			}
		}
		return result;
	}

	public double calculateYou() {
		double result = 0;
		for (DayRecord dr : dayRecords) {
			for (TimeRecord r : dr.getTimeRecordsToday()) {
				if (r.getAspect().equals(Aspect.YOU)) {
					result += r.getHours();
				}
			}
		}
		return result;
	}

	public Long calculateOverallpoints(DayRecord dayRecord) {
		if (dayRecord.getTimeRecordsToday().isEmpty())
			return 0L;
		return 100L - calculateMalus(dayRecord) + calculateBonus(dayRecord);
	}

	/**
	 * Calculates the biggest <b>relative</b> deviation in time between the
	 * {@TimeRecord}s of a given {@link DayRecord}. For example the
	 * biggest deviation between the hours (1,1,1,9) is (9-1)/12 = 0.66. Hence,
	 * the deviation calculated by this method is always greater equal 0 and
	 * less equal 1.
	 * 
	 * @param dayRecord
	 * @return biggest deviation
	 */
	Double calculateBiggestRelativeDeviation(DayRecord dayRecord) {
		int total = 0;
		int biggestHour = 0;
		int smallestHour = 25;

		for (TimeRecord tr : dayRecord.getTimeRecordsToday()) {
			total += tr.getHours();

			if (tr.getHours() > biggestHour)
				biggestHour = tr.getHours();

			if (tr.getHours() < smallestHour)
				smallestHour = tr.getHours();
		}

		return ((biggestHour - smallestHour) / (double) total);
	}

	Long calculateMalus(DayRecord dayRecord) {
		return (long) (calculateBiggestRelativeDeviation(dayRecord) * 100);
	}

	Long calculateBonus(DayRecord dayRecord) {
		return 0L;
	}

	@Override
	public Long calculateOverallpoints() {
		// To calculate the overall points, all records have to be aggregated
		// into one record that is then used to calculate the points. Another
		// way would be to sum the points for each record and divide it by the
		// number of records. However, that would result in a strange behavior.

		DayRecord totalRecord = sumUpRecords(dayRecords);
		return calculateOverallpoints(totalRecord);

	}

	/**
	 * Sums the value of all given {@link DayRecord}s into one {@link DayRecord}
	 * .
	 * 
	 * @param dayRecords
	 *            that should be summed up
	 * @return Record with the sum of all given records
	 */
	private DayRecord sumUpRecords(List<DayRecord> dayRecords) {
		int healthTotal = 0;
		int careerTotal = 0;
		int familyTotal = 0;
		int youTotal = 0;

		for (DayRecord dr : dayRecords) {
			for (TimeRecord tr : dr.getTimeRecordsToday()) {
				if (tr.getAspect().equals(Aspect.CAREER))
					careerTotal += tr.getHours();

				if (tr.getAspect().equals(Aspect.FAMILY))
					familyTotal += tr.getHours();

				if (tr.getAspect().equals(Aspect.HEALTH))
					healthTotal += tr.getHours();

				if (tr.getAspect().equals(Aspect.YOU))
					youTotal += tr.getHours();
			}
		}
		DayRecord totalRecord = new DayRecord(new Date());
		totalRecord.addTimeRecord(new TimeRecord(Aspect.HEALTH, healthTotal));
		totalRecord.addTimeRecord(new TimeRecord(Aspect.CAREER, careerTotal));
		totalRecord.addTimeRecord(new TimeRecord(Aspect.FAMILY, familyTotal));
		totalRecord.addTimeRecord(new TimeRecord(Aspect.YOU, youTotal));
		return totalRecord;
	}

	@Override
	public List<DayRecord> getAllDayRecords() {
		return dayRecords;
	}

	public void initDB() throws ClassNotFoundException, SQLException,
			FileNotFoundException, IOException {

		Properties prop = new Properties();

		prop.load(new FileInputStream("config.properties"));

		String databasePath = prop.getProperty("dbpath");

		Class.forName(DAO.class.getName());
		Properties properties = new Properties();
		properties.put("user", "user1");
		properties.put("password", "user1");
		Connection connection = DriverManager.getConnection("jdbc:derby:"
				+ databasePath + ";create=true", properties);

		createTablesIfNecessary(connection);
		populateTableTestIfItHasNotBeenPopulatedYet(connection);
		showContentsOfTableTest(connection);

		connection.close();
	}

	/**
	 * @param connection
	 * @throws SQLException
	 */
	private static void showContentsOfTableTest(Connection connection)
			throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM test");
		int columnCnt = resultSet.getMetaData().getColumnCount();
		boolean shouldCreateTable = true;
		while (resultSet.next() && shouldCreateTable) {
			for (int i = 1; i <= columnCnt; i++) {
				System.out.print(resultSet.getString(i) + " ");
			}
			System.out.println();
		}
		resultSet.close();
		statement.close();
	}

	private static void populateTableTestIfItHasNotBeenPopulatedYet(
			Connection connection) throws SQLException {

		boolean shouldPopulateTable = true;
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("SELECT COUNT(*) FROM test");
		if (resultSet.next()) {
			shouldPopulateTable = resultSet.getInt(1) == 0;
		}
		resultSet.close();
		statement.close();

		if (shouldPopulateTable) {
			System.out.println("Populating Table test...");
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO test VALUES (?,?)");
			String[] data = { "AAA", "BBB", "CCC", "DDD", "EEE" };
			for (int i = 0; i < data.length; i++) {
				preparedStatement.setInt(1, i);
				preparedStatement.setString(2, data[i]);
				preparedStatement.execute();
			}
			preparedStatement.close();
		}
	}

	private static void createTablesIfNecessary(Connection connection)
			throws SQLException {
		ResultSet resultSet = connection.getMetaData().getTables("%", "%", "%",
				new String[] { "TABLE" });

		int columnCnt = resultSet.getMetaData().getColumnCount();

		boolean shouldCreateTable = true;

		while (resultSet.next() && shouldCreateTable) {
			if (resultSet.getString("TABLE_NAME").equalsIgnoreCase("TEST")) {
				shouldCreateTable = false;
			}
		}
		resultSet.close();
		if (shouldCreateTable) {
			log.debug("Creating Table test...");
			Statement statement = connection.createStatement();
			statement
					.execute("create table test (id int not null, data varchar(32))");
			statement.close();
		}
	}
}
