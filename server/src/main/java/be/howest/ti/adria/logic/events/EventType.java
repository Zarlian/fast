package be.howest.ti.adria.logic.events;

public enum EventType {
    UNICAST("unicast"),
    LOCATION("location"),
    DISCARD("discard"),
    FRIENDREQUEST("friendRequest"),
    FRIENDREQUESTACCEPT("friendRequestAccept"),
    GET_FRIENDREQUESTS("getFriendRequests");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public static EventType fromString(String type) {
        for(EventType eventType: EventType.values()){
            if (eventType.type.equals(type)) {
                return eventType;
            }
        }
        return EventType.DISCARD;
    }
}
