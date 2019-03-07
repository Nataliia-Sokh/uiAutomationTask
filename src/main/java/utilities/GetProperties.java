package utilities;

import com.google.common.base.Strings;

import java.io.*;
import java.util.Properties;

public class GetProperties {

    private static String fs = File.separator;
    private static String propFile = String.format(".%ssrc%sbddTest%sresources%senvironments%slocalEnv.properties",fs ,fs ,fs ,fs ,fs);

    private static Properties properties = setProperties();

    private static Properties setProperties() {
        if (properties == null) {
            try {
                InputStream inputStream;
                inputStream = new FileInputStream(propFile);
                if (inputStream != null) {
                    Properties properties = new Properties();
                    properties.load(inputStream);
                    return properties;
                } else {
                    throw new FileNotFoundException("property file '" + propFile + "' not found in the classpath");
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }

    public static String get(String propertyName) {
        String propertyValue = properties.getProperty(propertyName);
        if (Strings.isNullOrEmpty(propertyValue)) {
             throw new NullPointerException("Unable to find " + propertyName + " property. Make sure property is set");
        } else {
            return propertyValue;
        }
    }

    public static void setGmailAPIWait(int seconds) {properties.setProperty("gmailAPIWait", String.valueOf(seconds));}
}
