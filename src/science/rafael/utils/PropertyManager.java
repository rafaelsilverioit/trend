package science.rafael.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read properties files
 * @author rafael
 */
public class PropertyManager {
	
	/**
	 * Read properties files from directory "confs"
	 * 
	 * @param fieName
	 *            file name
	 * @return configuration properties
	 * @throws FileNotFoundException
	 */
	public static Properties readProperties(String fileName)
			throws FileNotFoundException {
		Properties prop;

		try {
			prop = new Properties();
			InputStream inFileProp = new FileInputStream("confs/" + fileName);

			prop.load(inFileProp);

			inFileProp.close();
		} catch (IOException e) {
			throw new FileNotFoundException(
					"Property file not found in resources.");
		}
		return prop;
	}
}