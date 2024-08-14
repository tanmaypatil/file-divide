package com.filedivide;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileDivideProperties {
    public static Properties getProperties() throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(
                "C:\\Users\\u725561\\file-divide\\file-divide\\fdiv\\src\\main\\java\\com\\filedivide\\config.properties")) {
            // Load the properties file
            properties.load(input);

            // Access properties by key
            String linesPerFile = properties.getProperty("linesPerFile");

            // Print the properties
            System.out.println("linesPerFile: " + linesPerFile);

        }
        return properties;
    }
}
