package be.howest.ti.adria.logic.events;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventFactoryTest {
    static JsonObject json = new JsonObject();

    @BeforeAll
    static void setup() {

        json.put("type", "location");
        json.put("sender", "test-sender");
        json.put("latitude", 10);
        json.put("longitude", 11);
        json.put("receiver", 1);
        json.put("accepted", true);
    }

    @Test
    void getEventType() {
        IncomingEvent incoming = EventFactory.getInstance().createIncomingEvent(json);

        assertEquals(EventType.LOCATION, incoming.getType());
    }

    @Test
    void getTokenFromSender() {
        IncomingEvent incoming = EventFactory.getInstance().createIncomingEvent(json);

        assertEquals("test-sender", incoming.getToken());
    }

    @Test
    void createDiscardEvent() {
        IncomingEvent incoming = EventFactory.getInstance().createIncomingEvent(json);
        IncomingEvent event = new DiscardEvent(incoming.getToken());

        assertEquals("test-sender", event.getToken());
    }
}
