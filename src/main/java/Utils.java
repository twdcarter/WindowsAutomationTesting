import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Utils {
    InputStream inputStream;

    /**
     * Takes a given lookup keyword and returns the corresponding value from the config.properties file.
     *
     * @param lookup - Lookup work
     * @return - Corresponding value against given lookup
     * @throws IOException
     */
    public String getPropertyValue(String lookup) throws IOException {
        String result = "";
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream((propFileName));

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath.");
            }

            result = prop.getProperty(lookup);

        } catch (IOException e){
            System.out.println(e);
        } finally {
            inputStream.close();
        }
        return result;
    }

    /**
     * Takes a given lookup keyword and returns the corresponding value from the expectedResults.properties file.
     *
     * @param lookup - Lookup work
     * @return - Corresponding value against given lookup
     * @throws IOException
     */
    public String getExpectedResult(String lookup) throws IOException {
        String result = "";
        try {
            Properties prop = new Properties();
            String propFileName = "expectedResults.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream((propFileName));

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath.");
            }

            result = prop.getProperty(lookup);

        } catch (Exception e){
            System.out.println(e);
        } finally {
            inputStream.close();
        }
        return result;
    }
}
