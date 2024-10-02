package be.howest.ti.adria.logic.domain;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final int id;
    private final String name;
    private final User leader;
    List<User> groupMembers;

    public Group(int id, String name, User leader) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.groupMembers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getLeader() {
        return leader;
    }

    public List<User> getGroupMembers() {
        return groupMembers;
    }

    public void addMember(User user) {
        groupMembers.add(user);
    }
}
