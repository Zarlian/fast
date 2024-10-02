package be.howest.ti.adria.logic.events;

public class LocationEvent extends IncomingEvent {
    private final String latitude;
    private final String longitude;

    public LocationEvent(String token, String latitude, String longitude) {
        super(EventType.LOCATION, token);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }
}
