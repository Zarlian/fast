package be.howest.ti.adria.logic.domain;

public class User {
    private final int id;
    private final String name;
    private final String address;
    private final Location location;
    private final String profilePicture;

    public User(int id, String name, String address, Location location, String profilePicture) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
        this.profilePicture = profilePicture;
    }

    public int getAdriaId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    @Override
    public String toString() {
        return name;
    }
}
