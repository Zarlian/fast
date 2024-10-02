package be.howest.ti.adria.logic.data.dbenums;

public enum Queries {
    GET_USER("SELECT adria_id, name, address, profile_picture, location_id, longitude, latitude " +
            "FROM users " +
            "JOIN locations ON users.current_location = locations.location_id " +
            "WHERE adria_id = ?;"),

    GET_USER_TRANSACTIONS("SELECT * FROM users u " +
            "JOIN locations l ON u.current_location = l.location_id " +
            "JOIN user_transactions ut ON u.adria_id = ut.adria_id " +
            "JOIN transactions t ON ut.transact_id = t.transact_id " +
            "WHERE u.adria_id = ?;"),

    GET_TELEPORTERS("SELECT teleporters.teleporter_id, teleporters.name AS teleporter_name, teleporters.address AS teleporter_address, " +
            "teleporters.type, teleporters.location AS teleporter_location, users.profile_picture ,users.adria_id, users.name AS user_name, users.address AS user_address, " +
            "users.current_location, locations.longitude, locations.latitude, " +
            "user_locations.longitude AS user_location_longitude, user_locations.latitude AS user_location_latitude " +
            "FROM teleporters " +
            "LEFT JOIN users ON teleporters.owner = users.adria_id " +
            "LEFT JOIN locations ON teleporters.location = locations.location_id " +
            "LEFT JOIN locations user_locations ON users.current_location = user_locations.location_id;"),

    GET_GROUPS("SELECT" +
            " g.group_id," +
            " g.name AS group_name," +
            " leader.adria_id AS leader_id," +
            " leader.name AS leader_name," +
            " leader.address AS leader_address," +
            " leader.profile_picture AS leader_profile_picture," +
            " leader.current_location AS leader_current_location," +
            " leader_location.latitude AS leader_location_lat," +
            " leader_location.longitude AS leader_location_lon," +
            " member.adria_id AS member_id," +
            " member.profile_picture AS member_profile_picture," +
            " member.name AS member_name," +
            " member.address AS member_address," +
            " member.current_location AS member_current_location," +
            " member_location.location_id AS member_location_id," +
            " member_location.latitude AS member_location_lat," +
            " member_location.longitude AS member_location_lon" +
            " FROM groups g" +
            " JOIN" +
            " users AS leader ON g.leader_id = leader.adria_id" +
            " LEFT JOIN" +
            " group_members gm ON g.group_id = gm.group_id" +
            " LEFT JOIN" +
            " users AS member ON gm.adria_id = member.adria_id" +
            " JOIN" +
            " locations AS leader_location ON leader.current_location = leader_location.location_id" +
            " LEFT JOIN" +
            " locations AS member_location ON member.current_location = member_location.location_id" +
            " WHERE" +
            " g.leader_id = ?;"),

    UPDATE_GROUP_MEMBER("INSERT INTO group_members (group_id, adria_id) VALUES (?, ?);"),

    DELETE_GROUP_MEMBERS("DELETE FROM group_members WHERE group_id = ?;"),

    GET_GROUP("SELECT" +
            " groups.group_id," +
            " groups.name AS group_name," +
            " leader.adria_id AS leader_id," +
            " leader.name AS leader_name," +
            " leader.address AS leader_address," +
            " leader.profile_picture AS leader_profile_picture," +
            " leader.current_location AS leader_current_location," +
            " leader_location.latitude AS leader_location_lat," +
            " leader_location.longitude AS leader_location_lon," +
            " member.adria_id AS member_id," +
            " member.name AS member_name," +
            " member.address AS member_address," +
            " member.profile_picture AS member_profile_picture," +
            " member.current_location AS member_current_location," +
            " member_location.location_id AS member_location_id," +
            " member_location.latitude AS member_location_lat," +
            " member_location.longitude AS member_location_lon" +
            " FROM" +
            " groups" +
            " LEFT JOIN" +
            " group_members ON groups.group_id = group_members.group_id" +
            " JOIN" +
            " users AS leader ON groups.leader_id = leader.adria_id" +
            " LEFT JOIN" +
            " users AS member ON group_members.adria_id = member.adria_id" +
            " JOIN" +
            " locations AS leader_location ON leader.current_location = leader_location.location_id" +
            " LEFT JOIN" +
            " locations AS member_location ON member.current_location = member_location.location_id" +
            " WHERE" +
            " groups.group_id = ?;"),

    GET_USER_HISTORY("SELECT * FROM trip_details WHERE adria_id = ?"),

    ADD_GROUP("INSERT INTO groups (name, leader_id) VALUES (?, ?);"),

    UPDATE_GROUP("UPDATE groups SET name = ? WHERE group_id = ?;"),

    DELETE_GROUP("DELETE FROM groups WHERE group_id = ?;"),

    ADD_USER_HISTORY("INSERT INTO trips (adria_id, from_teleporter_id, to_teleporter_id, departure, arrival, group_id) VALUES (?, ?, ?, ?, ?, ?);"),
    GET_TELEPORTER_HISTORY("SELECT * FROM trip_details WHERE to_teleporter_id = ? OR from_teleporter_id = ?;"),
    GET_TELEPORTER_SETTINGS("SELECT * FROM teleporter_settings_details WHERE teleporter_id = ?;"),
    UPDATE_TELEPORTER_SETTINGS("UPDATE teleporter_settings SET visible = ? WHERE teleporter_id = ?;"),
    GET_USER_PERMISSIONS("SELECT * FROM user_permissions_details WHERE adria_id = ? AND teleporter_id = ?;"),
    UPDATE_USER_PERMISSIONS("UPDATE user_permissions " +
            "SET can_access_all_logs = ? " +
            "AND can_manage_lists = ? " +
            "AND can_assign_admin_permissions = ? " +
            "AND can_control_teleporters = ? " +
            "WHERE adria_id = ? AND teleporter_id = ?;"),

    GET_FAVOURITES("SELECT * FROM favourites_details WHERE adria_id = ?;"),
    ADD_FAVOURITE("INSERT INTO favourites (adria_id, teleporter_id, type, name) VALUES (?, ?, ?, ?);"),
    UPDATE_FAVOURITE("UPDATE favourites SET type = ?, name = ? WHERE adria_id = ? AND teleporter_id = ?;"),
    DELETE_FAVOURITE("DELETE FROM favourites WHERE adria_id = ? AND teleporter_id = ?;"),
    GET_USERS("SELECT * FROM users u " +
            "JOIN locations l ON u.current_location = l.location_id;"),
    IMPORT_FRIENDS(UPDATE_GROUP_MEMBER.getQuery()),
    GET_RANDOM_FRIENDS("SELECT * FROM users JOIN locations l ON users.current_location = l.location_id WHERE users.adria_id != ? \n ORDER BY RAND() LIMIT ?;"),
    UPDATE_USER_TOKEN("UPDATE users SET token = ? WHERE adria_id = ?;"),
    DELETE_GROUP_MEMBER("DELETE FROM group_members WHERE group_id = ? AND adria_id = ?;"),
    GET_ADRIAN_ID("SELECT adria_id FROM users WHERE token = ?;"),
    DELETE_GROUP_TRIPS("DELETE FROM trips WHERE group_id = ?;"),
    GET_TOKEN_FROM_ADRIAN_ID("SELECT token FROM users WHERE adria_id = ?;"),
    INCREASE_USER_USES_LEFT("UPDATE user_transactions " +
            "SET uses_left = CASE WHEN uses_left > 0 THEN uses_left - 1 ELSE 0 END " +
            "WHERE adria_id = ? AND uses_left > 0 LIMIT 1;");

    private final String query;

    Queries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}