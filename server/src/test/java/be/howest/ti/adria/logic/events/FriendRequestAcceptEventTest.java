package be.howest.ti.adria.logic.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FriendRequestAcceptEventTest {

    @Test
    void getRecipient() {
        FriendRequestAcceptEvent friendReqAccEvent = new FriendRequestAcceptEvent("test-token", 1, true);
        assertEquals(1, friendReqAccEvent.getRecipient());
    }

    @Test
    void getAccepted() {
        FriendRequestAcceptEvent friendReqAccEvent = new FriendRequestAcceptEvent("test-token", 1, true);
        assertTrue(friendReqAccEvent.getAccepted());
    }
}
