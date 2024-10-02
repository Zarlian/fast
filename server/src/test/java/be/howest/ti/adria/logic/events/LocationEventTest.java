package be.howest.ti.adria.logic.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationEventTest {

    @Test
    void getLatitude() {
        LocationEvent event = new LocationEvent("test-token", "11", "12");
        assertEquals("11", event.getLatitude());
    }

    @Test
    void getLongtitude() {
        LocationEvent event = new LocationEvent("test-token", "11", "12");
        assertEquals("12", event.getLongitude());
    }

    @Test
    void getToken() {
        LocationEvent event = new LocationEvent("test-token", "11", "12");
        assertEquals("test-token", event.getToken());
    }
}

