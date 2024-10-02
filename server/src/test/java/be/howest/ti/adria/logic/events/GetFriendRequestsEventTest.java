package be.howest.ti.adria.logic.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetFriendRequestsEventTest {

    @Test
    void GetToken() {
        GetFriendRequestsEvent getFriendReqEvent = new GetFriendRequestsEvent("test-token");
        assertEquals("test-token", getFriendReqEvent.getToken());
    }

    @Test
    void GetEventType() {
        GetFriendRequestsEvent getFriendReqEvent = new GetFriendRequestsEvent("test-token");
        assertEquals(EventType.GET_FRIENDREQUESTS, getFriendReqEvent.getType());
    }
}
