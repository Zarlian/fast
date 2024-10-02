package be.howest.ti.adria.logic.events;


public class FriendRequestEvent extends IncomingEvent {
    private final int recipient;

    public FriendRequestEvent(String token, int recipientId) {
        super(EventType.FRIENDREQUEST, token);
        this.recipient = recipientId;
    }

    public int getRecipient() {
        return recipient;
    }
}
