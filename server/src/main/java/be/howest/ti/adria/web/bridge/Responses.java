package be.howest.ti.adria.web.bridge;

public enum Responses {
    NO_USERS_FOUND("No users found"),
    NO_TELEPORTERS_FOUND("No teleporters found"),
    NO_GROUPS_FOUND("No groups found"),
    NO_GROUP_FOUND("No group found"),
    USER_NOT_FOUND("User not found"),
    FAILED_TO_UPDATE_GROUP_MEMBERS("Failed to update group members"),
    NO_HISTORY_FOUND("No history found"),
    LEADER_NOT_FOUND("Leader not found"),
    FAILED_TO_ADD_GROUP("Failed to add group"),
    MEMEBER_NOT_FOUND("Member not found"),
    FAILED_TO_UPDATE_GROUP("Failed to update group"),
    GROUP_UPDATED("Group updated"),
    FAILED_TO_DELETE_GROUP("Failed to delete group"),
    GROUP_DELETED("Group deleted"),
    FAILED_TO_ADD_USER_HISTORY("Failed to add user history"),
    USER_HISTORY_ADDED("User history added"),
    NO_SETTINGS_FOUND("No settings found"),
    NO_TELEPORTER_FOUND("No teleporter found"),
    FAILED_TO_UPDATE_TELEPORTER_SETTINGS("Failed to update teleporter settings"),
    TELEPORTER_SETTINGS_UPDATED("Teleporter settings updated"),
    NO_PERMISSIONS_FOUND("No permissions found"),
    FAILED_TO_UPDATE_PERMISSIONS("Failed to update permissions"),
    NO_FAVORITES_FOUND("No favorites found"),
    TELEPORTER_ALREADY_IN_FAVORITES("Teleporter already in favorites"),
    FAILED_TO_ADD_FAVORITE("Failed to add favorite"),
    FAVORITE_ADDED("Favorite added"),
    FAILED_TO_UPDATE_FAVORITE("Failed to update favorite"),
    FAVORITE_UPDATED("Favorite updated"),
    FAILED_TO_DELETE_FAVORITE("Failed to delete favorite"),
    FAVORITE_DELETED("Favorite deleted"),
    FAILED_TO_FIND_GROUP("Failed to find group"),
    FRIENDS_IMPORTED("Friends imported"),
    USER_PERMISSIONS_UPDATED("User permissions updated"),
    NO_FAVOURITES_FOUND("No favourites found"),
    FAILED_TO_GENERATE_TOKENS("Failed to generate tokens"),
    MEMBER_NOT_IN_GROUP("Member not in group"),
    MEMBER_ALREADY_IN_GROUP("Member already in group"),
    FAILED_TO_DELETE_GROUP_MEMBERS("Failed to delete group members"),
    TOKEN_NOT_VALID("Token not valid"),
    USER_NOT_LEADER("User is not leader"),
    USER_CANNOT_MANAGE("User cannot manage this teleporter");

    private final String message;

    Responses(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
