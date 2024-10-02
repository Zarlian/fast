package be.howest.ti.adria.logic.events;

public class DiscardEvent extends IncomingEvent {
    public DiscardEvent(String token) {
        super(EventType.DISCARD, token);
    }
}
