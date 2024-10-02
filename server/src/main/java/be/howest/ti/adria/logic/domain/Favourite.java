package be.howest.ti.adria.logic.domain;

public class Favourite {
    private User user;
    private Teleporter teleporter;
    private String type;
    private String name;

    public Favourite(User user, Teleporter teleporter, String type, String name) {
        this.user = user;
        this.teleporter = teleporter;
        this.type = type;
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public Teleporter getTeleporter() {
        return teleporter;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
