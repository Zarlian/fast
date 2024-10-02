package be.howest.ti.adria.web.response;

import be.howest.ti.adria.logic.domain.Group;
import java.util.List;

public class GroupResponseBody {
    private final int id;
    private final String name;
    private final List<UserGroupResponseBody> members;
    private final UserGroupResponseBody leader;

    public GroupResponseBody(Group group) {
        this.id = group.getId();
        this.name = group.getName();

        // Convert all members to UserGroupResponseBody
        this.members = group.getGroupMembers().stream()
                .map(UserGroupResponseBody::new)
                .toList();
        this.leader = new UserGroupResponseBody(group.getLeader());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<UserGroupResponseBody> getMembers() {
        return members;
    }

    public int getLeader() {
        return leader.getAdriaId();
    }
}
