package be.howest.ti.adria.logic.domain;

public class Trip {
    private int id;
    private User user;
    private Group group;
    private Teleporter from;
    private Teleporter to;
    private String departure;
    private String arrival;

    public Trip(int id, User user, Group group, Teleporter from, Teleporter to, String departure, String arrival) {
        this.id = id;
        this.user = user;
        this.group = group;
        this.from = from;
        this.to = to;
        this.departure = departure;
        this.arrival = arrival;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }

    public Teleporter getFrom() {
        return from;
    }

    public Teleporter getTo() {
        return to;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }
}
