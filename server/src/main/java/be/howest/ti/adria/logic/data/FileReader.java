package be.howest.ti.adria.logic.data;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReader {
    private static final FileReader INSTANCE = new FileReader();
    private static final String PATH = "/user.token";
    private final Properties properties = new Properties();
    private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

    public static FileReader getInstance() {
        return INSTANCE;
    }

    private FileReader() {
        try (InputStream is = getClass().getResourceAsStream(PATH)) {
            if (is != null) {
                properties.load(is);
            } else {
                LOGGER.log(Level.SEVERE, "File not found: " + PATH);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading properties from file", e);
        }
    }

    public String read(String key) {
        return properties.getProperty(key);
    }
}