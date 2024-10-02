package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.TeleporterSettings;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.UserTransaction;
import be.howest.ti.adria.web.response.*;
import static be.howest.ti.adria.web.bridge.Responses.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * DefaultAdriaController is the default implementation for the AdriaController interface.
 * The controller shouldn't even know that it is used in an API context..
 *
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Keep libraries and frameworks out of the logic packages as much as possible.
 * Do not be afraid to create your own Java classes if needed.
 */
public class DefaultController implements Controller {
    private static final String FRIENDS_GROUP_NAME = "Friends";

    public int getAdriaIdFromToken(String token) {
        return Repositories.getH2Repo().getAdrianId(token);
    }

    @Override
    public UserResponseBody getUser(String token) {
        int adrianId = getAdriaIdFromToken(token);

        // Create a list of users for testing endpoint
        if (adrianId == -1)
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());

        List<Integer> teleporterIds = Repositories.getH2Repo().getTeleporters().stream().map(TeleporterResponseBody::getId).toList();

        boolean canManage = false;

        for (Integer teleporterId : teleporterIds) {
            if (null != Repositories.getH2Repo().getUserPermissions(adrianId, teleporterId)) {
                canManage = Repositories.getH2Repo().getUserPermissions(adrianId, teleporterId).isCanManageLists();
                break;
            }
        }


        return new UserResponseBody(Repositories.getH2Repo().getUser(adrianId), canManage);
    }

    @Override
    public Map<String, Integer> getUserTransactions(String token) {

        // Check if token is valid
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        // Create a list of users for testing endpoint
        if (null == Repositories.getH2Repo().getUserTransactions(adrianId))
            throw new NoSuchElementException(NO_USERS_FOUND.getMessage());

        // count all usesLeft
        int usesLeft = 0;

        for (UserTransaction userTransaction : Repositories.getH2Repo().getUserTransactions(adrianId)) {
            usesLeft += userTransaction.getUsesLeft();
        }

        return Map.of("usesLeft", usesLeft);
    }

    @Override
    public List<TeleporterResponseBody> getTeleporters(String token) {

        // Check if token is valid
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        // Create a list of users for testing endpoint
        if (null == Repositories.getH2Repo().getTeleporters())
            throw new NoSuchElementException(NO_TELEPORTERS_FOUND.getMessage());

        return Repositories.getH2Repo().getTeleporters();
    }

    @Override
    public List<GroupResponseBody> getGroups(String token) {

        // Check if token is valid
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        // Create a list of users for testing endpoint
        if (null == Repositories.getH2Repo().getGroups(adrianId))
            throw new NoSuchElementException(NO_GROUPS_FOUND.getMessage());

        return Repositories.getH2Repo().getGroups(adrianId);
    }

    @Override
    public String updateGroupMembers(String token, int groupId, List<Integer> membersId) {
        int adrianId = getAdriaIdFromToken(token);
        if (adrianId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        GroupResponseBody foundGroup = Repositories.getH2Repo().getGroup(groupId);
        if (foundGroup == null) {
            return NO_GROUP_FOUND.getMessage();
        }

        // CHECK IF USER IS LEADER OF GROUP
        if (foundGroup.getLeader() != adrianId) {
            return USER_NOT_LEADER.getMessage();
        }

        for (int memberId : membersId) {
            if (null == Repositories.getH2Repo().getUser(memberId))
                return USER_NOT_FOUND.getMessage();

            if (Repositories.getH2Repo().getGroup(groupId).getMembers().stream().noneMatch(member -> member.getAdriaId() == memberId)) {
                // Add members to group
                boolean isUpdated = Repositories.getH2Repo().updateGroupMember(groupId, memberId);

                if (!isUpdated) {
                    return FAILED_TO_UPDATE_GROUP_MEMBERS.getMessage();
                }

            } else {
                // delete member from group
                boolean isDeleted;
                boolean isDeleted2 = true;
                if (foundGroup.getName().equals(FRIENDS_GROUP_NAME)) {
                    isDeleted = Repositories.getH2Repo().deleteGroupMember(groupId, memberId);

                    // Get group of member
                    GroupResponseBody foundGroup2 = Repositories.getH2Repo().getGroups(memberId).stream().filter(group -> group.getName().equals(FRIENDS_GROUP_NAME)).findFirst().orElse(null);

                    if (foundGroup2 != null) {
                        isDeleted2 = Repositories.getH2Repo().deleteGroupMember(foundGroup2.getId(), adrianId);
                    }

                } else {
                    isDeleted = Repositories.getH2Repo().deleteGroupMember(groupId, memberId);
                }

                if (!isDeleted || !isDeleted2) {
                    return FAILED_TO_DELETE_GROUP_MEMBERS.getMessage();
                }

                return MEMBER_ALREADY_IN_GROUP.getMessage();
            }
        }
        return GROUP_UPDATED.getMessage();
    }

    @Override
    public GroupResponseBody getGroup(int groupId, String token) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        // CHECK IF GROUP EXISTS WITH THAT USER
        GroupResponseBody foundGroup = Repositories.getH2Repo().getGroup(groupId);
        if (foundGroup == null) {
            throw new NoSuchElementException(NO_GROUPS_FOUND.getMessage());
        }

        return foundGroup;
    }

    @Override
    public List<UserHistoryResponseBody> getUserHistory(String token) {

        // Check if token is valid
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        // Create a list of users for testing endpoint
        if (null == Repositories.getH2Repo().getUserHistory(adrianId))
            throw new NoSuchElementException(NO_HISTORY_FOUND.getMessage());

        return Repositories.getH2Repo().getUserHistory(adrianId);
    }

    public UserResponseBody gerUser(int userId) {
        // Create a list of users for testing endpoint

        // CHECK IF USER EXISTS
        if (null == Repositories.getH2Repo().getUser(userId))
            throw new NoSuchElementException(USER_NOT_FOUND.getMessage());

        return new UserResponseBody(Repositories.getH2Repo().getUser(userId), false);
    }

    @Override
    public int addGroup(String groupName, List<Integer> membersId, String token, int leaderId) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int user = getAdriaIdFromToken(token);

        if (user == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        UserResponseBody leader = gerUser(leaderId);
        if (leader == null) {
            throw new NoSuchElementException(USER_NOT_FOUND.getMessage());
        }

        // CHECK IF USER EXISTS
        if (null == Repositories.getH2Repo().getUser(leaderId)) {
            throw new NoSuchElementException(USER_NOT_FOUND.getMessage());
        }

        // Add group to database
        int groupId = Repositories.getH2Repo().addGroup(groupName, leaderId);

        if (groupId == 0) {
            throw new NoSuchElementException(FAILED_TO_ADD_GROUP.getMessage());
        }

        // Add members to group
        for (Integer memberId : membersId) {
            // CHECK IF USER EXISTS
            if (null == Repositories.getH2Repo().getUser(memberId)) {
                throw new NoSuchElementException(USER_NOT_FOUND.getMessage());
            }

            // First check if the user is already in the group
            boolean isUpdated = Repositories.getH2Repo().updateGroupMember(groupId, memberId);

            if (!isUpdated) {
                throw new NoSuchElementException(FAILED_TO_UPDATE_GROUP_MEMBERS.getMessage());
            }
        }

        return groupId;
    }

    @Override
    public String updateGroup(int groupId, String groupName, String token) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        // CHECK IF GROUP EXISTS WITH THAT USER
        GroupResponseBody foundGroup = Repositories.getH2Repo().getGroup(groupId);
        if (foundGroup == null) {
            return NO_GROUP_FOUND.getMessage();
        }

        // CHECK IF USER IS LEADER OF GROUP
        if (foundGroup.getLeader() != adrianId) {
            return USER_NOT_LEADER.getMessage();
        }

        // Update group name
        boolean isUpdated = Repositories.getH2Repo().updateGroup(groupId, groupName);

        if (!isUpdated) {
            return FAILED_TO_UPDATE_GROUP.getMessage();
        }

        return GROUP_UPDATED.getMessage();
    }

    @Override
    public String deleteGroup(int groupId) {
        // Create a list of users for testing endpoint

        // CHECK IF GROUP EXISTS WITH THAT USER
        GroupResponseBody foundGroup = Repositories.getH2Repo().getGroup(groupId);
        if (foundGroup == null) {
            return NO_GROUP_FOUND.getMessage();
        }

        // Delete group
        boolean isDeleted = Repositories.getH2Repo().deleteGroup(groupId);

        if (!isDeleted) {
            return FAILED_TO_DELETE_GROUP.getMessage();
        }

        return GROUP_DELETED.getMessage();
    }

    @Override
    public String addTrip(String token, int from, int to, String departure, String arrival, int groupId) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adriaId = getAdriaIdFromToken(token);

        if (adriaId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        // CHECK IF USER EXISTS
        if (null == Repositories.getH2Repo().getUser(adriaId))
            return USER_NOT_FOUND.getMessage();

        // CHECK IF GROUP EXISTS WITH THAT USER
        GroupResponseBody foundGroup = Repositories.getH2Repo().getGroup(groupId);

        if (foundGroup == null && groupId != 0) {
            return NO_GROUP_FOUND.getMessage();
        }

        // Add user history to database
        boolean userHistoryAdded = Repositories.getH2Repo().addTrip(adriaId, from, to, departure, arrival, groupId);

        if (!userHistoryAdded) {
            return FAILED_TO_ADD_USER_HISTORY.getMessage();
        }

        return USER_HISTORY_ADDED.getMessage();
    }

    @Override
    public List<TeleporterHistoryResponseBody> getTeleporterHistory(String token, int teleporterId) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        if (null == Repositories.getH2Repo().getTeleporterHistory(adrianId, teleporterId))
            throw new NoSuchElementException(NO_HISTORY_FOUND.getMessage());

        return Repositories.getH2Repo().getTeleporterHistory(adrianId,teleporterId);
    }

    @Override
    public TeleporterSettings getTeleporterSettings(int teleporterId, String token) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        if (null == Repositories.getH2Repo().getTeleporterSettings(teleporterId))
            throw new NoSuchElementException(USER_CANNOT_MANAGE.getMessage());

        // check if the user is the owner of the teleporter
        boolean isOwner = Repositories.getH2Repo().getTeleporterSettings(teleporterId).getOwnerId() == adrianId;
        boolean canManage = Repositories.getH2Repo().getUserPermissions(adrianId, teleporterId).isCanManageLists();

        if (!isOwner && !canManage) {
            throw new NoSuchElementException(NO_SETTINGS_FOUND.getMessage());
        }

        return Repositories.getH2Repo().getTeleporterSettings(teleporterId);
    }

    @Override
    public String updateTeleporterSettings(int teleporterId, boolean visible, String token) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        // check if the user is the owner of the teleporter
        boolean isOwner = Repositories.getH2Repo().getTeleporterSettings(teleporterId).getOwnerId() == adrianId;
        boolean canManage = Repositories.getH2Repo().getUserPermissions(adrianId, teleporterId).isCanManageLists();

        if (!isOwner && !canManage) {
            throw new NoSuchElementException(USER_CANNOT_MANAGE.getMessage());
        }

        // CHECK IF TELEPORTER EXISTS
        List<TeleporterResponseBody> teleporters = Repositories.getH2Repo().getTeleporters();

        // check if the teleporter is in the list of teleporters
        if (teleporters.stream().noneMatch(teleporter -> teleporter.getId() == teleporterId)) {
            return NO_TELEPORTER_FOUND.getMessage();
        }

        // Update teleporter settings
        boolean isUpdated = Repositories.getH2Repo().updateTeleporterSettings(teleporterId, visible);

        if (!isUpdated) {
            return FAILED_TO_UPDATE_TELEPORTER_SETTINGS.getMessage();
        }

        return TELEPORTER_SETTINGS_UPDATED.getMessage();
    }

    @Override
    public UserPermissionsResponseBody getUserPermissions(String token, int teleporterId) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        if (null == Repositories.getH2Repo().getUserPermissions(adrianId, teleporterId))
            throw new NoSuchElementException(NO_PERMISSIONS_FOUND.getMessage());

        return Repositories.getH2Repo().getUserPermissions(adrianId, teleporterId);
    }

    @Override
    public String updateUserPermissions(String token, int teleporterId, boolean accessLog, boolean assingPerm, boolean controlTele, boolean manageList) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adriaId = getAdriaIdFromToken(token);

        if (adriaId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        // CHECK IF USER EXISTS
        if (null == Repositories.getH2Repo().getUser(adriaId))
            return USER_NOT_FOUND.getMessage();

        // CHECK IF TELEPORTER EXISTS
        List<TeleporterResponseBody> teleporters = Repositories.getH2Repo().getTeleporters();

        // check if the teleporter is in the list of teleporters
        if (teleporters.stream().noneMatch(teleporter -> teleporter.getId() == teleporterId)) {
            return NO_TELEPORTER_FOUND.getMessage();
        }

        // Update user permissions
        boolean isUpdated = Repositories.getH2Repo().updateUserPermissions(adriaId, teleporterId, accessLog, assingPerm, controlTele, manageList);

        if (!isUpdated) {
            return FAILED_TO_UPDATE_PERMISSIONS.getMessage();
        }

        return USER_PERMISSIONS_UPDATED.getMessage();
    }

    @Override
    public List<FavouriteResponseBody> getFavourites(String token) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(TOKEN_NOT_VALID.getMessage());
        }

        if (null == Repositories.getH2Repo().getFavourites(adrianId))
            throw new NoSuchElementException(NO_FAVOURITES_FOUND.getMessage());

        return Repositories.getH2Repo().getFavourites(adrianId);
    }

    @Override
    public String addFavourite(String token, int teleporterId, String type, String name) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adriaId = getAdriaIdFromToken(token);

        if (adriaId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        // CHECK IF USER EXISTS
        if (null == Repositories.getH2Repo().getUser(adriaId))
            return USER_NOT_FOUND.getMessage();

        // CHECK IF TELEPORTER EXISTS
        List<TeleporterResponseBody> teleporters = Repositories.getH2Repo().getTeleporters();

        // check if the teleporter is in the list of teleporters
        if (teleporters.stream().noneMatch(teleporter -> teleporter.getId() == teleporterId)) {
            return NO_TELEPORTER_FOUND.getMessage();
        }

        // check if the teleporter is already in the favourites
        List<FavouriteResponseBody> favourites = Repositories.getH2Repo().getFavourites(adriaId);

        if (favourites.stream().anyMatch(favourite -> favourite.getTeleporterId() == teleporterId)) {
            return TELEPORTER_ALREADY_IN_FAVORITES.getMessage();
        }

        // Add favourite to database
        boolean favouriteAdded = Repositories.getH2Repo().addFavourite(adriaId, teleporterId, type, name);

        if (!favouriteAdded) {
            return FAILED_TO_ADD_FAVORITE.getMessage();
        }

        return FAVORITE_ADDED.getMessage();
    }

    @Override
    public String updateFavourite(String token, int teleporterId, String type, String name) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adriaId = getAdriaIdFromToken(token);

        if (adriaId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        // CHECK IF USER EXISTS
        if (null == Repositories.getH2Repo().getUser(adriaId))
            return USER_NOT_FOUND.getMessage();

        // CHECK IF TELEPORTER EXISTS
        List<TeleporterResponseBody> teleporters = Repositories.getH2Repo().getTeleporters();

        // check if the teleporter is in the list of teleporters
        if (teleporters.stream().noneMatch(teleporter -> teleporter.getId() == teleporterId)) {
            return NO_TELEPORTER_FOUND.getMessage();
        }

        // Update favourite
        boolean isUpdated = Repositories.getH2Repo().updateFavourite(adriaId, teleporterId, type, name);

        if (!isUpdated) {
            return FAILED_TO_UPDATE_FAVORITE.getMessage();
        }

        return FAVORITE_UPDATED.getMessage();
    }

    @Override
    public String deleteFavourite(String token, int teleporterId) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adriaId = getAdriaIdFromToken(token);

        if (adriaId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        // CHECK IF USER EXISTS
        if (null == Repositories.getH2Repo().getUser(adriaId))
            return  USER_NOT_FOUND.getMessage();

        // CHECK IF TELEPORTER EXISTS
        List<TeleporterResponseBody> teleporters = Repositories.getH2Repo().getTeleporters();

        // check if the teleporter is in the list of teleporters
        if (teleporters.stream().noneMatch(teleporter -> teleporter.getId() == teleporterId)) {
            return NO_TELEPORTER_FOUND.getMessage();
        }

        // Delete favourite
        boolean isDeleted = Repositories.getH2Repo().deleteFavourite(adriaId, teleporterId);

        if (!isDeleted) {
            return FAILED_TO_DELETE_FAVORITE.getMessage();
        }

        return FAVORITE_DELETED.getMessage();
    }

    @Override
    public List<User> getUsers(String token) {

        // Check if token is valid
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            throw new NoSuchElementException(NO_USERS_FOUND.getMessage());
        }

        // Create a list of users for testing endpoint
        if (null == Repositories.getH2Repo().getUsers())
            throw new NoSuchElementException(NO_USERS_FOUND.getMessage());

        return Repositories.getH2Repo().getUsers();
    }

    public boolean isFriendAlreadyInGroup(int groupId, User friend) {
        return Repositories.getH2Repo().getGroup(groupId).getMembers().stream().anyMatch(member -> member.getAdriaId() == friend.getAdriaId());
    }

    @Override
    public String importFriends(String token) {
        // Create a list of users for testing endpoint

        // CHECK IF TOKEN IS VALID
        int adrianId = getAdriaIdFromToken(token);

        if (adrianId == -1) {
            return TOKEN_NOT_VALID.getMessage();
        }

        List<User> randomFriends = Repositories.getH2Repo().getRandomFriends(8, adrianId);

        List<GroupResponseBody> groupsOfLeader = Repositories.getH2Repo().getGroups(adrianId);

        // find group with name "Friends"
        GroupResponseBody friendsGroup = groupsOfLeader.stream().filter(group -> group.getName().equals(FRIENDS_GROUP_NAME)).findFirst().orElse(null);

        assert friendsGroup != null;
        int groupId = friendsGroup.getId();

        if (groupId == 0 || randomFriends.isEmpty()) {
            return FAILED_TO_FIND_GROUP.getMessage();
        }

        // check if one of the friends is already in the group
        // remove friend from list
        randomFriends.removeIf(friend -> isFriendAlreadyInGroup(groupId, friend));

        for (User friend : randomFriends) {
            // CHECK IF USER EXISTS
            if (null == Repositories.getH2Repo().getUser(friend.getAdriaId()))
                return  USER_NOT_FOUND.getMessage();

            // Add members to group
            boolean isUpdated = Repositories.getH2Repo().updateGroupMember(groupId, friend.getAdriaId());

            if (!isUpdated) {
                return FAILED_TO_UPDATE_GROUP_MEMBERS.getMessage();
            }
        }

        return FRIENDS_IMPORTED.getMessage();
    }

    @Override
    public void increaseUserUsesLeft(int adriaId) {
        Repositories.getH2Repo().increaseUserUsesLeft(adriaId);
    }

    @Override
    public void generateTokens() {
        Repositories.getH2Repo().generateTokens();
    }

    @Override
    public boolean isValidToken(String token) {
        return Repositories.getH2Repo().isValidToken(token);
    }
}