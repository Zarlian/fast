package be.howest.ti.adria.logic.events;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutgoingEventTest {

    @Test
    void getLatidude() {
        OutgoingEvent outEvent = new OutgoingEvent(EventType.UNICAST, "10", "11");
        assertEquals("10", outEvent.getLatidude());
    }

    @Test
    void getLongitude() {
        OutgoingEvent outEvent = new OutgoingEvent(EventType.UNICAST, "10", "11");
        assertEquals("11", outEvent.getLongitude());
    }
}
