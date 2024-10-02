package be.howest.ti.adria.logic.events;

import io.vertx.core.json.JsonObject;

public class EventFactory {
    private static final EventFactory INSTANCE = new EventFactory();

    public static EventFactory getInstance() {
        return INSTANCE;
    }

    public IncomingEvent createIncomingEvent(JsonObject json) {
        EventType type = EventType.fromString(json.getString("type"));
        String token = json.getString("sender");

        IncomingEvent event = new DiscardEvent(token);

        switch (type) {
            case LOCATION:
                String latitude = json.getString("latitude");
                String longitude = json.getString("longitude");
                event = new LocationEvent(token, latitude, longitude);
                break;
            case FRIENDREQUEST:
                int receiver = json.getInteger("receiver");
                event = new FriendRequestEvent(token, receiver);
                break;
            case GET_FRIENDREQUESTS:
                event = new GetFriendRequestsEvent(token);
                break;
            case FRIENDREQUESTACCEPT:
                int receiver2 = json.getInteger("receiver");
                boolean accepted = json.getBoolean("accepted");
                event = new FriendRequestAcceptEvent(token, receiver2, accepted);
                break;
            default:
                break;
        }

        return event;
    }

    public UnicastEvent createUnicastLocationEvent(String recipientToken, String latitude, String longitude, String sender) {
        return new UnicastEvent(recipientToken, latitude, longitude, sender);
    }

    public UnicastEvent createUnicastFriendRequestEvent(String recipientToken, String sender, int senderId, int groupId) {
        return new UnicastEvent(recipientToken, sender, senderId, groupId);
    }

    public UnicastEvent createUnicastFriendRequestEvent(String recipientToken) {
        return new UnicastEvent(recipientToken);
    }



    public UnicastEvent createUnicastFriendRequestAcceptEvent(String recipientToken, String sender, int senderId, int groupId) {
        return new UnicastEvent(recipientToken, sender, senderId, groupId);
    }
}
