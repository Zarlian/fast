package be.howest.ti.adria.logic.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnicastEventTest {

    @Test
    void getRecipient() {
        UnicastEvent uniEvent = new UnicastEvent("test-recipient", "test-sender", 1, 2);
        assertEquals("test-recipient", uniEvent.getRecipient());

        UnicastEvent uniEvent2 = new UnicastEvent("test-recipient-2", "10", "11", "test-sender-2");
        assertEquals("test-recipient-2", uniEvent2.getRecipient());

        UnicastEvent uniEvent3 = new UnicastEvent("test-recipient-3");
        assertEquals("test-recipient-3", uniEvent3.getRecipient());
    }

    @Test
    void getSender() {
        UnicastEvent uniEvent = new UnicastEvent("test-recipient", "test-sender", 1, 2);
        assertEquals("test-sender", uniEvent.getSender());

        UnicastEvent uniEvent2 = new UnicastEvent("test-recipient-2", "10", "11", "test-sender-2");
        assertEquals("test-sender-2", uniEvent2.getSender());

        UnicastEvent uniEvent3 = new UnicastEvent("test-recipient-3");
        assertEquals("", uniEvent3.getSender());
    }

    @Test
    void getSenderId() {
        UnicastEvent uniEvent = new UnicastEvent("test-recipient", "test-sender", 1, 2);
        assertEquals(1, uniEvent.getSenderId());

        UnicastEvent uniEvent2 = new UnicastEvent("test-recipient-2", "10", "11", "test-sender-2");
        assertEquals(0, uniEvent2.getSenderId());

        UnicastEvent uniEvent3 = new UnicastEvent("test-recipient-3");
        assertEquals(0, uniEvent3.getSenderId());
    }

    @Test
    void getGroupId() {
        UnicastEvent uniEvent = new UnicastEvent("test-recipient", "test-sender", 1, 2);
        assertEquals(2, uniEvent.getGroupId());

        UnicastEvent uniEvent2 = new UnicastEvent("test-recipient-2", "10", "11", "test-sender-2");
        assertEquals(0, uniEvent2.getGroupId());

        UnicastEvent uniEvent3 = new UnicastEvent("test-recipient-3");
        assertEquals(0, uniEvent3.getGroupId());
    }

    @Test
    void getEventType() {
        UnicastEvent uniEvent = new UnicastEvent("test-recipient", "test-sender", 1, 2);
        assertEquals(EventType.FRIENDREQUEST, uniEvent.getType());
    }
}
