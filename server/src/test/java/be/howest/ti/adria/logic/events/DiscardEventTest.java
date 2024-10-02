package be.howest.ti.adria.logic.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiscardEventTest {

    @Test
    void getToken() {
        DiscardEvent disEvent = new DiscardEvent("test-token");
        assertEquals("test-token", disEvent.getToken());
    }

    @Test
    void getEventType() {
        DiscardEvent disEvent = new DiscardEvent("test-token");
        assertEquals(EventType.DISCARD, disEvent.getType());
    }
}
