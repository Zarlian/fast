package be.howest.ti.adria.logic.events;

public abstract class IncomingEvent extends Event {
    private final String token;

    protected IncomingEvent(EventType type, String token) {
        super(type);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
