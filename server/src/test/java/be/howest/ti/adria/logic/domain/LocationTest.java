package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    private static Location location1;
    private static Location location2;

    @BeforeAll
    static void setup(){
        location1 = new Location("0.123","0.123");
        location2 = new Location("0.321","0.420");
    }

    @Test
    void getLon() {
        assertEquals("0.123", location1.getLon());
        assertEquals("0.321", location2.getLon());
    }

    @Test
    void getLat() {
        assertEquals("0.123", location1.getLat());
        assertEquals("0.420", location2.getLat());
    }
}