package Selenium;

import com.google.common.base.Strings;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SeleniumProperties {

    private static Properties properties = setProperties();

    private static Properties setProperties() {
        if (properties == null) {
            try {
                ClassLoader classLoader = SeleniumProperties.class.getClassLoader();
                String filePath = classLoader.getResource("selenium.properties").getPath();
                InputStream inputStream = new FileInputStream(filePath);
                Properties properties = new Properties();
                properties.load(inputStream);
                return properties;
            } catch (FileNotFoundException e) {
                throw new RuntimeException("selenium.properties file not found under main>resources folder. Please add it");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return properties;
        }
    }

    public static long getImplicitWaitTime() {
        String implicitWait = properties.getProperty("wait.implicit");
        //If implicit time is not specified it will default to 10 seconds
        if(Strings.isNullOrEmpty(implicitWait)) {
            implicitWait = "10";
        }
        return Long.parseLong(implicitWait);
    }

    public static String getProperty(String propertyName) {
        String propertyValue = properties.getProperty(propertyName);
        if (Strings.isNullOrEmpty(propertyValue)) {
            throw new NullPointerException("Unable to find " + propertyName + " property. Make sure property is set");
        } else {
            return propertyValue;
        }
    }

    public static String getDriverProperty() {
        return properties.getProperty("browser");
    }
}
