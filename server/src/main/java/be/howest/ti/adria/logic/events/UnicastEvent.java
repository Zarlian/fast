package be.howest.ti.adria.logic.events;

public class UnicastEvent extends OutgoingEvent {

    private final String recipientToken;
    private final String sender;
    private final int senderId;
    private final int groupId;

    public UnicastEvent(String recipientToken, String latitude, String longitude, String sender) {
        super(EventType.LOCATION, latitude, longitude);
        this.recipientToken = recipientToken;
        this.sender = sender;
        this.senderId = 0;
        this.groupId = 0;
    }

    public UnicastEvent(String recipientToken, String sender, int senderId, int groupId) {
        super(EventType.FRIENDREQUEST);
        this.recipientToken = recipientToken;
        this.sender = sender;
        this.senderId = senderId;
        this.groupId = groupId;
    }

    public UnicastEvent(String recipientToken) {
        super(EventType.GET_FRIENDREQUESTS);
        this.recipientToken = recipientToken;
        this.sender = "";
        this.senderId = 0;
        this.groupId = 0;
    }

    public String getRecipient() {
        return recipientToken;
    }

    public String getSender() {
        return sender;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getGroupId() {
        return groupId;
    }
}
