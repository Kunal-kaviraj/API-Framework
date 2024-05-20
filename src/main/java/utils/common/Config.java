package utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * SINGLETON : This class is responsible for loading configuration from
 * properties files.
 */
public class Config
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
	private static String CONFIG_FOLDER_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "config";

	// to make this singleton
	private Config()
	{
	}

	private static final Properties PROPERTIES = new Properties();

	static
	{
		loadPropertyFiles(CONFIG_FOLDER_PATH);
	}

	/**
	 * It will load all the properties files present in config folder.
	 *
	 * @param configFolderPath
	 */
	private static void loadPropertyFiles(String configFolderPath)
	{
		loadPropertiesFile(configFolderPath);
		LOGGER.info(" All property configuration files loaded.");
	}

	/**
	 * It will load the given properties file
	 *
	 * @param: filePath
	 */
	private static void loadPropertiesFile(String filePath)
	{
		LOGGER.info("Loading properties file : " + filePath);
		ClassLoader CLDR = Config.class.getClassLoader();
		InputStream inputStream = CLDR.getResourceAsStream("config/config.properties");
		InputStreamReader reader = new InputStreamReader(inputStream);
		try
		{
			PROPERTIES.load(reader);
		}
		catch (Exception e)
		{
			LOGGER.info("!!!! Exception while loading properties file : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * It will give the property value present in properties file.
	 *
	 * @param propertyName
	 * @return property value
	 */
	public static String getProperty(String propertyName)
	{
		return PROPERTIES.getProperty(propertyName);
	}
}
