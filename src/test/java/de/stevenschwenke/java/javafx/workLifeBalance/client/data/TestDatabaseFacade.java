package de.stevenschwenke.java.javafx.workLifeBalance.client.data;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import liquibase.Liquibase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit.MyBatisDao;

/**
 * This class offers methods for tests using the database.
 * 
 * @author Steven Schwenke
 * 
 */
public class TestDatabaseFacade {

	/** path to test config for the in-memory database */
	private static final String PATH_TO_TEST_CONFIG = "de/stevenschwenke/java/javafx/workLifeBalance/client/data/mybatis-test-config.xml";

	/** Connection to the in-memory database */
	private Connection connection;

	private Liquibase liquibase;

	public void createInMemoryDatabase() {

		String changelog = "changelog.sql";

		try {

			Class.forName("org.hsqldb.jdbcDriver");

			connection = DriverManager.getConnection("jdbc:hsqldb:mem:mydb", "sa", "");
			HsqlConnection hsqlConnection = new HsqlConnection(connection);

			liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), hsqlConnection);

			liquibase.update(null);

		} catch (SQLException | LiquibaseException | ClassNotFoundException e) {
			fail();
			e.printStackTrace();
		}
	}

	public static String getPathToTestConfig() {
		return PATH_TO_TEST_CONFIG;
	}

	public MyBatisDao createDao() {
		return new MyBatisDao(PATH_TO_TEST_CONFIG);
	}

	public Connection getConnection() {
		return connection;
	}

	public Liquibase getLiquibase() {
		return liquibase;
	}
}
