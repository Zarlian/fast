package be.howest.ti.adria.logic.domain;

public class Teleporter {
    private final int id;
    private final String name;
    private final String address;
    private final Location location;
    private final String type;
    private final User user;

    public Teleporter(int id, String name, String address, Location location, String type, User user) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
        this.type = type;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public User getOwner() {
        return user;
    }
}
