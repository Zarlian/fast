package be.howest.ti.adria.logic.events;

public class OutgoingEvent extends Event {
    private final String latidude;
    private final String longitude;

    public OutgoingEvent(EventType type, String latidude, String longitude) {
        super(type);
        this.latidude = latidude;
        this.longitude = longitude;
    }

    public OutgoingEvent(EventType type) {
        super(type);
        this.latidude = null;
        this.longitude = null;
    }

    public String getLatidude() {
        return latidude;
    }

    public String getLongitude() {
        return longitude;
    }
}
