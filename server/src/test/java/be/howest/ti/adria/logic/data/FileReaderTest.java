package be.howest.ti.adria.logic.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    @Test
    void getInstance() {
        assertDoesNotThrow(FileReader::getInstance);
    }

    @Test
    void read() {
        FileReader fileReader = FileReader.getInstance();

        assertEquals("keyboard cat", fileReader.read("jwt.key"));

    }
}