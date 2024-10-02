package be.howest.ti.adria.logic.domain;

public class Location {
    private final String longitude;
    private final String latitude;

    public Location(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLon() {
        return longitude;
    }

    public String getLat() {
        return latitude;
    }
}
