package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.domain.*;
import be.howest.ti.adria.web.response.*;

import java.util.List;
import java.util.Map;

public class MockController implements Controller {
    @Override
    public UserResponseBody getUser(String token) {
        return new UserResponseBody(new User(1, "Adrian", "Somewhere", new Location( "3.975", "51.2"), "profilePicture"), true);
    }

    @Override
    public Map<String, Integer> getUserTransactions(String adrianId) {
        return Map.of("usesLeft", 1);
    }

    @Override
    public List<TeleporterResponseBody> getTeleporters(String token){
        Teleporter t = new Teleporter(1, "Teleporter","Somewhere 6",
                new Location( "3.975", "51.2"), "Public",
                new User(1, "Adrian", "Somewhere",
                        new Location( "3.975", "51.2"), "profilePicture"));

        return List.of(new TeleporterResponseBody(t, true));
    }

    @Override
    public List<GroupResponseBody> getGroups(String adriaId) {
        return List.of(new GroupResponseBody(new Group(1, "Group", new User(5, "Adrian", "Somewhere",
                new Location( "3.975", "51.2"),
                "profilePicture"))));
    }

    @Override
    public GroupResponseBody getGroup(int groupId, String token) {
        return new GroupResponseBody(new Group(1, "Group", new User(5, "Adrian", "Somewhere",
                new Location( "3.975", "51.2"), "profilePicture")));
    }

    @Override
    public String updateGroupMembers(String token, int groupId, List<Integer> membersId) {
        return "Group updated";
    }

    @Override
    public List<UserHistoryResponseBody> getUserHistory(String adriaId) {
        return List.of(new UserHistoryResponseBody(new Trip(1,
                new User(1, "Adrian", "Somewhere",
                        new Location( "3.975", "51.2"), "profilePicture"),
                new Group(1, "Group",
                        new User(5, "Adrian", "Somewhere",
                        new Location( "3.975", "51.2"), "profilePicture")),
                new Teleporter(1, "Teleporter", "Somewhere 6",
                        new Location( "3.975", "51.2"), "Public",
                        new User(1, "Adrian", "Somewhere",
                                new Location( "3.975", "51.2"), "profilePicture")),
                new Teleporter(1, "Teleporter", "Somewhere 6",
                        new Location( "3.975", "51.2"), "Public",
                        new User(1, "Adrian", "Somewhere",
                                new Location( "3.975", "51.2"), "profilePicture")),
                "2021-05-05 12:00:00", "2021-05-05 12:00:00")));
    }

    @Override
    public int addGroup(String groupName, List<Integer> membersId, String token, int leaderId) {
        return 1;
    }

    @Override
    public String updateGroup(int groupId, String groupName, String token) {
        return "Group updated";
    }

    @Override
    public String deleteGroup(int groupId) {
        return "Group deleted";
    }

    @Override
    public String addTrip(String adriaId, int from, int to, String departure, String arrival, int group) {
        return "User history added";
    }

    @Override
    public List<TeleporterHistoryResponseBody> getTeleporterHistory(String adriaId, int teleporterId) {
        return List.of(new TeleporterHistoryResponseBody(new Trip(1,
                new User(1, "Adrian", "Somewhere",
                        new Location( "3.975", "51.2"), "profilePicture"),
                new Group(1, "Group",
                        new User(5, "Adrian", "Somewhere",
                                new Location( "3.975", "51.2"), "profilePicture")),
                new Teleporter(1, "Teleporter", "Somewhere 6",
                        new Location( "3.975", "51.2"), "Public",
                        new User(1, "Adrian", "Somewhere",
                                new Location( "3.975", "51.2"), "profilePicture")),
                new Teleporter(1, "Teleporter", "Somewhere 6",
                        new Location( "3.975", "51.2"), "Public",
                        new User(1, "Adrian", "Somewhere",
                                new Location( "3.975", "51.2"), "profilePicture")),
                "2021-05-05 12:00:00", "2021-05-05 12:00:00"), 1));
    }

    @Override
    public TeleporterSettings getTeleporterSettings(int teleporterId, String token) {
        return new TeleporterSettings(new Teleporter(1, "Teleporter", "Somewhere 6",
                new Location("3.975", "51.2"), "Public",
                new User(1, "Adrian", "Somewhere",
                        new Location( "3.975", "51.2"), "profilePicture")),
                true);
    }

    @Override
    public String updateTeleporterSettings(int teleporterId, boolean visible, String token) {
        return "Teleporter settings updated";
    }

    @Override
    public UserPermissionsResponseBody getUserPermissions(String adriaId, int teleporterId) {
        return new UserPermissionsResponseBody(new UserPermissions(
                new User(1, "Adrian", "Somewhere",
                        new Location( "3.975", "51.2"), "profilePicture"),
                new Teleporter(1, "Teleporter", "Somewhere 6",new Location( "3.975", "51.2"),
                        "Public", new User(1, "Adrian", "Somewhere",
                        new Location( "3.975", "51.2"), "profilePicture")),
                true, true, true, true));
    }

    @Override
    public String updateUserPermissions(String adriaId, int teleporterId, boolean canAccessAllLogs, boolean canManageLists, boolean canAssignAdminPermissions, boolean canControlTeleporters) {
        return "User permissions updated";
    }

    @Override
    public List<FavouriteResponseBody> getFavourites(String adrianId) {
        return List.of(new FavouriteResponseBody(new Favourite(
                new User(1, "Adrian", "Somewhere",
                        new Location("3.975", "51.2"), "profilePicture"),
                new Teleporter(1, "Teleporter", "Somewhere 6",
                        new Location( "3.975", "51.2"), "Public",
                        new User(1, "Adrian", "Somewhere",
                                new Location( "3.975", "51.2"), "profilePicture")),
                "Public", "Favourite")));
    }

    @Override
    public String addFavourite(String adriaId, int teleporterId, String type, String name) {
        return "Favourite added";
    }

    @Override
    public String updateFavourite(String adriaId, int teleporterId, String type, String name) {
        return "Favourite updated";
    }

    @Override
    public String deleteFavourite(String adriaId, int teleporterId) {
        return "Favourite deleted";
    }

    @Override
    public List<User> getUsers(String token) {
        return List.of(new User(1, "Adrian", "Somewhere",
                new Location( "3.975", "51.2"), "profilePicture"));
    }

    @Override
    public String importFriends(String token) {
        return "Friends imported";
    }

    @Override
    public void generateTokens() {
        // do nothing
    }

    @Override
    public boolean isValidToken(String token) {
        return true;
    }

    @Override
    public void increaseUserUsesLeft(int adriaId) {
        // do nothing
    }
}
