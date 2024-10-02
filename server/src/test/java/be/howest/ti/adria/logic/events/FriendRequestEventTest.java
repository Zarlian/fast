package be.howest.ti.adria.logic.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FriendRequestEventTest {

    @Test
    void getRecipient() {
        FriendRequestEvent friendReqEvent = new FriendRequestEvent("test-token", 1);
        assertEquals(1, friendReqEvent.getRecipient());
    }
}
