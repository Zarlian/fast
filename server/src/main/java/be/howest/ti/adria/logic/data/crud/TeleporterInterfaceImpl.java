package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.*;
import be.howest.ti.adria.web.response.TeleporterResponseBody;
import be.howest.ti.adria.web.response.UserPermissionsResponseBody;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static be.howest.ti.adria.logic.data.dbenums.Queries.*;
import static be.howest.ti.adria.logic.data.dbenums.ColumnNames.*;

public class TeleporterInterfaceImpl implements TeleporterInterface {
    public List<TeleporterResponseBody> getTeleporters() {
        List<TeleporterResponseBody> teleportersResponse = new ArrayList<>();
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_TELEPORTERS.getQuery())) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Location location = Repositories.getH2Repo().createLocationFromResultSet(rs, LONGITUDE.name(), LATITUDE.name());
                    Location userLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, USER_LOCATION_LONGITUDE.name(), USER_LOCATION_LATITUDE.name());

                    User owner = Repositories.getH2Repo().createUserFromResultSet(rs, ADRIA_ID.name(), USER_NAME.name(), ADDRESS.name(), PROFILE_PICTURE.name(), userLocation);

                    Teleporter teleporter = new Teleporter(
                            rs.getInt(TELEPORTER_ID.name()),
                            rs.getString(NAME.name()),
                            rs.getString(ADDRESS.name()),
                            location,
                            rs.getString(TYPE.name()),
                            owner);

                    boolean visible = this.getTeleporterSettings(teleporter.getId()).isVisible();

                    teleportersResponse.add(new TeleporterResponseBody(teleporter, visible));
                }
                return teleportersResponse;
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get teleporters.", ex, "Could not get teleporters.");
            return Collections.emptyList();
        }
    }

    public TeleporterSettings getTeleporterSettings(int teleporterId) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_TELEPORTER_SETTINGS.getQuery())) {

            stmt.setInt(1, teleporterId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Location teleporterLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, LONGITUDE.name(), LATITUDE.name());
                    Location ownerLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, OWNER_LOCATION_LONGITUDE.name(), OWNER_LOCATION_LATITUDE.name());

                    User owner = Repositories.getH2Repo().createUserFromResultSet(rs, OWNER_ID.name(), OWNER_NAME.name(), OWNER_ADDRESS.name(), PROFILE_PICTURE.name(), ownerLocation);

                    Teleporter teleporter = new Teleporter(
                            rs.getInt(TELEPORTER_ID.name()),
                            rs.getString(NAME.name()),
                            rs.getString(ADDRESS.name()),
                            teleporterLocation,
                            rs.getString(TYPE.name()),
                            owner);

                    boolean visible = rs.getBoolean(VISIBLE.name());

                    return new TeleporterSettings(teleporter, visible);
                }
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get teleporter settings.", ex, "Could not get teleporter settings.");
            return null;
        }
        return null;
    }

    public boolean updateTeleporterSettings(int teleporterId, boolean visible) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_TELEPORTER_SETTINGS.getQuery())) {

            stmt.setBoolean(1, visible);
            stmt.setInt(2, teleporterId);

            stmt.executeUpdate();

            int rowsAffected = stmt.getUpdateCount();

            return rowsAffected != 0;
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to update teleporter settings.", ex, "Could not update teleporter settings.");
            return false;
        }
    }

    public UserPermissionsResponseBody getUserPermissions(int adriaId, int teleporterId) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_PERMISSIONS.getQuery())) {

            stmt.setInt(1, adriaId);
            stmt.setInt(2, teleporterId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Location teleporterLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, LONGITUDE.name(), LATITUDE.name());
                    Location ownerLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, OWNER_LOCATION_LONGITUDE.name(), OWNER_LOCATION_LATITUDE.name());

                    User owner = Repositories.getH2Repo().createUserFromResultSet(rs, OWNER_ID.name(), OWNER_NAME.name(), OWNER_ADDRESS.name(), PROFILE_PICTURE.name(), ownerLocation);

                    Location userLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, USER_LOCATION_LONGITUDE.name(), USER_LOCATION_LATITUDE.name());
                    User user = Repositories.getH2Repo().createUserFromResultSet(rs, ADRIA_ID.name(), USER_NAME.name(), USER_ADDRESS.name(), PROFILE_PICTURE.name(), userLocation);

                    Teleporter teleporter = new Teleporter(
                            rs.getInt(TELEPORTER_ID.name()),
                            rs.getString(NAME.name()),
                            rs.getString(ADDRESS.name()),
                            teleporterLocation,
                            rs.getString(TYPE.name()),
                            owner);

                    boolean canAccessAllLogs = rs.getBoolean(CAN_ACCESS_ALL_LOGS.name());
                    boolean canManageLists = rs.getBoolean(CAN_MANAGE_LISTS.name());
                    boolean canAssignPermissions = rs.getBoolean(CAN_ASSIGN_ADMIN_PERMISSIONS.name());
                    boolean canControlTeleporters = rs.getBoolean(CAN_CONTROL_TELEPORTERS.name());

                    return new UserPermissionsResponseBody(new UserPermissions(user, teleporter, canAccessAllLogs, canManageLists, canAssignPermissions, canControlTeleporters));
                }
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get user permissions.", ex, "Could not get user permissions.");
            return null;
        }
        return null;
    }

    public boolean updateUserPermissions(int adriaId, int teleporterId, boolean canAccessAllLogs, boolean canManageLists, boolean canAssignAdminPermissions, boolean canControlTeleporters) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_PERMISSIONS.getQuery())) {

            stmt.setBoolean(1, canAccessAllLogs);
            stmt.setBoolean(2, canManageLists);
            stmt.setBoolean(3, canAssignAdminPermissions);
            stmt.setBoolean(4, canControlTeleporters);
            stmt.setInt(5, adriaId);
            stmt.setInt(6, teleporterId);

            stmt.executeUpdate();

            int rowsAffected = stmt.getUpdateCount();

            return rowsAffected != 0;

        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to update user permissions.", ex, "Could not update user permissions.");
            return false;
        }
    }
}
