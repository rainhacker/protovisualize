package pv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads properties from protovisualize.properties file
 *
 * @author Parth Solanki
 */
public class PropertyLoader {

    static Properties getProperties() {

        Properties properties = new Properties();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        InputStream input = classloader.getResourceAsStream("protovisualize.properties");

        try {

            properties.load(input);

        } catch (IOException e) {

            System.out.println("Error in reading protovisualize.properties file.");

            e.printStackTrace();
        }

        System.out.println("Loading properties:");

        System.out.println("visualizationviewer.width=" + properties.getProperty("visualizationviewer.width").trim());

        System.out.println("visualizationviewer.height=" + properties.getProperty("visualizationviewer.height").trim());

        System.out.println("proto.file.path="+ properties.getProperty("proto.file.path").trim());

        return properties;
    }
}
