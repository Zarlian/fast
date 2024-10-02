package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.web.response.GroupResponseBody;

import java.util.List;

public interface GroupInterface {
    List<GroupResponseBody> getGroups(int adriaId);
    boolean updateGroupMember(int groupId, int memberId);
    GroupResponseBody getGroup(int groupId);
    void deleteGroupMembers(int groupId);
    int addGroup(String groupName, int leaderId);

    boolean updateGroup(int groupId, String groupName);

    boolean deleteGroup(int groupId);

    boolean importFriends(int friendId, int groupId);

    boolean deleteGroupMember(int groupId, int memberId);
}
