package be.howest.ti.adria.logic.events;

public class FriendRequestAcceptEvent extends IncomingEvent {
    private final int recipient;
    private final boolean accepted;

    public FriendRequestAcceptEvent(String token, int recipient, boolean accepted) {
        super(EventType.FRIENDREQUESTACCEPT, token);
        this.recipient = recipient;
        this.accepted = accepted;
    }

    public int getRecipient() {
        return recipient;
    }

    public boolean getAccepted() {
        return accepted;
    }
}
