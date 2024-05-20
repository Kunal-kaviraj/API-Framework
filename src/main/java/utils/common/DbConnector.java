package utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class is responsible for making database connection.
 * @author Kunal
 */
public class DbConnector {

	private static final String SRC_MAIN_RESOURCES_CONFIG_CONFIG_XML = "./src/main/resources/config/config.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(DbConnector.class);
	private static Connection con;

	/**
	 * @Functionality: It will create the database connection.
	 * @return: A connection (session) with a specific database
	 * @author: Kunal Kaviraj
	 * @param: databaseName
	 */
	public static Connection getDbConnection(String database)
	{
		String dbClass = XMLConfig.getConfig(database, "Class", SRC_MAIN_RESOURCES_CONFIG_CONFIG_XML);
		String dbUrl = XMLConfig.getConfig(database, "URL", SRC_MAIN_RESOURCES_CONFIG_CONFIG_XML);
		String dbUser = XMLConfig.getConfig(database, "User", SRC_MAIN_RESOURCES_CONFIG_CONFIG_XML);
		String dbPassword = XMLConfig.getConfig(database, "Password", SRC_MAIN_RESOURCES_CONFIG_CONFIG_XML);
		String secret = XMLConfig.getConfig(database, "Secret", SRC_MAIN_RESOURCES_CONFIG_CONFIG_XML);

		try
		{
			dbPassword = CommonUtils.decryptPassword(dbPassword, secret);
			Class.forName(dbClass).getConstructor().newInstance();
			con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			LOGGER.info("Connection established with DB");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return con;
	}

}
