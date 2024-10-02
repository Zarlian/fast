package be.howest.ti.adria.logic.events;

public class GetFriendRequestsEvent extends IncomingEvent {
    public GetFriendRequestsEvent(String token) {
        super(EventType.GET_FRIENDREQUESTS, token);
    }
}
