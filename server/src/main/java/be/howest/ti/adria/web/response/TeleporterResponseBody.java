package be.howest.ti.adria.web.response;

import be.howest.ti.adria.logic.domain.Teleporter;

public class TeleporterResponseBody  {
    private final int id;
    private final String name;
    private final String address;
    private final String type;
    private final String ownerName;
    private final String teleporterLongitude;
    private final String teleporterLatitude;
    private final boolean visible;

    public TeleporterResponseBody(Teleporter teleporter, boolean visible) {
        this.id = teleporter.getId();
        this.name = teleporter.getName();
        this.address = teleporter.getAddress();
        this.type = teleporter.getType();
        if (teleporter.getOwner() == null) {
            this.ownerName = null;
        } else {
            this.ownerName = teleporter.getOwner().getName();
        }
        this.teleporterLongitude = teleporter.getLocation().getLon();
        this.teleporterLatitude = teleporter.getLocation().getLat();
        this.visible = visible;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getTeleporterLongitude() {
        return teleporterLongitude;
    }

    public String getTeleporterLatitude() {
        return teleporterLatitude;
    }
    public boolean isVisible() {
        return visible;
    }
}
