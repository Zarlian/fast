package be.howest.ti.adria.logic.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventTypeTest {

    @Test
    void fromString() {
        EventType type = EventType.fromString("location");
        assertEquals(EventType.LOCATION, type);
    }

    @Test
    void fromStringDiscardEvent() {
        EventType type = EventType.fromString("notype");
        assertEquals(EventType.DISCARD, type);
    }
}
