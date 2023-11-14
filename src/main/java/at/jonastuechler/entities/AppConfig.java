package at.jonastuechler.entities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private Properties configProps;

    public AppConfig() {
        configProps = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            // Load the properties file
            configProps.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getPropertyValue(String key) {
        return configProps.getProperty(key);
    }

}
