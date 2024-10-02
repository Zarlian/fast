package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.domain.TeleporterSettings;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.web.response.*;

import java.util.List;
import java.util.Map;

public interface Controller {
    // ENDPOINTS FOR FAST
    UserResponseBody getUser(String token);

    Map<String, Integer> getUserTransactions(String adrianId);

    List<TeleporterResponseBody> getTeleporters(String token);

    List<GroupResponseBody> getGroups(String token);
    GroupResponseBody getGroup(int groupId, String token);

    String updateGroupMembers(String token, int groupId, List<Integer> membersId);

    List<UserHistoryResponseBody> getUserHistory(String adriaId);

    int addGroup(String groupName, List<Integer> membersId, String token, int leaderId);

    String updateGroup(int groupId, String groupName, String token);

    String deleteGroup(int groupId);

    String addTrip(String adriaId, int from, int to, String departure, String arrival, int group);

    List<TeleporterHistoryResponseBody> getTeleporterHistory(String adriaId, int teleporterId);

    TeleporterSettings getTeleporterSettings(int teleporterId, String token);

    String updateTeleporterSettings(int telepporter, boolean visibility, String token);

    UserPermissionsResponseBody getUserPermissions(String adriaId, int telepporter);

    String updateUserPermissions(String adriaId, int telepporter, boolean accessLog, boolean assingPerm, boolean controlTele, boolean manageList);

    List<FavouriteResponseBody> getFavourites(String adriaId);

    String addFavourite(String adriaId, int teleporterId, String type, String name);

    String updateFavourite(String adriaId, int teleporterId, String type, String name);

    String deleteFavourite(String adriaId, int teleporterId);

    List<User> getUsers(String token);

    String importFriends(String token);
    void generateTokens();

    boolean isValidToken(String token);
    void increaseUserUsesLeft(int adriaId);
}
