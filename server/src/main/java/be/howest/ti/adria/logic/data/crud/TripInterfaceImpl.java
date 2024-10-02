package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.*;
import be.howest.ti.adria.web.response.TeleporterHistoryResponseBody;
import be.howest.ti.adria.web.response.UserHistoryResponseBody;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static be.howest.ti.adria.logic.data.dbenums.ColumnNames.*;
import static be.howest.ti.adria.logic.data.dbenums.Queries.*;

public class TripInterfaceImpl implements TripInterface {
    public List<UserHistoryResponseBody> getUserHistory(int adriaId) {
        List<UserHistoryResponseBody> trips = new ArrayList<>();

        try(
                Connection con = Repositories.getH2Repo().getConnection();
                PreparedStatement stmt = con.prepareStatement(GET_USER_HISTORY.getQuery())
        ) {
            stmt.setInt(1, adriaId);

            try(ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Trip trip = createTripFromResultSet(rs);

                    trips.add(new UserHistoryResponseBody(trip));
                }
            }

        } catch (SQLException e) {
            Repositories.getH2Repo().handleSQLException("Failed to get user history.", e, "Could not get user history.");
        }

        return trips;
    }

    public Trip createTripFromResultSet(ResultSet rs) throws SQLException {
        Location userLocation = new Location(
                rs.getString(OWNER_LOCATION_LONGITUDE.name()),
                rs.getString(OWNER_LOCATION_LATITUDE.name())
        );
        User user = new User(
                rs.getInt(OWNER_ID.name()),
                rs.getString(OWNER_NAME.name()),
                rs.getString(OWNER_ADDRESS.name()),
                userLocation,
                rs.getString(OWNER_PROFILE_PICTURE.name())
        );

        Location leaderLocation = new Location(
                rs.getString(LEADER_LOCATION_LON.name()),
                rs.getString(LEADER_LOCATION_LAT.name())
        );

        User leader = new User(
                rs.getInt(LEADER_ID.name()),
                rs.getString(LEADER_NAME.name()),
                rs.getString(LEADER_ADDRESS.name()),
                leaderLocation,
                rs.getString(LEADER_PROFILE_PICTURE.name())
        );

        Group group = new Group(
                rs.getInt(GROUP_ID.name()),
                rs.getString(GROUP_NAME.name()),
                leader
        );

        Location fromTeleporterLocation = new Location(
                rs.getString(FROM_TELEPORTER_LOCATION_LONGITUDE.name()),
                rs.getString(FROM_TELEPORTER_LOCATION_LATITUDE.name())
        );

        User fromTeleporterOwner = new User(
                rs.getInt(FROM_TELEPORTER_OWNER_ID.name()),
                rs.getString(FROM_TELEPORTER_OWNER_NAME.name()),
                rs.getString(FROM_TELEPORTER_OWNER_ADDRESS.name()),
                fromTeleporterLocation,
                rs.getString(FROM_TELEPORTER_OWNER_PROFILE_PICTURE.name())
        );

        Teleporter from = new Teleporter(
                rs.getInt(FROM_TELEPORTER_ID.name()),
                rs.getString(FROM_TELEPORTER_NAME.name()),
                rs.getString(FROM_TELEPORTER_ADDRESS.name()),
                fromTeleporterLocation,
                rs.getString(FROM_TELEPORTER_TYPE.name()),
                fromTeleporterOwner
        );

        Location toTeleporterLocation = new Location(
                rs.getString(TO_TELEPORTER_LOCATION_LONGITUDE.name()),
                rs.getString(TO_TELEPORTER_LOCATION_LATITUDE.name())
        );

        User toTeleporterOwner = new User(
                rs.getInt(TO_TELEPORTER_OWNER_ID.name()),
                rs.getString(TO_TELEPORTER_OWNER_NAME.name()),
                rs.getString(TO_TELEPORTER_OWNER_ADDRESS.name()),
                toTeleporterLocation,
                rs.getString(TO_TELEPORTER_OWNER_PROFILE_PICTURE.name())
        );

        Teleporter to = new Teleporter(
                rs.getInt(TO_TELEPORTER_ID.name()),
                rs.getString(TO_TELEPORTER_NAME.name()),
                rs.getString(TO_TELEPORTER_ADDRESS.name()),
                toTeleporterLocation,
                rs.getString(TO_TELEPORTER_TYPE.name()),
                toTeleporterOwner
        );

        return new Trip(
                rs.getInt(TRIP_ID.name()),
                user,
                group,
                from,
                to,
                rs.getString(DEPARTURE.name()),
                rs.getString(ARRIVAL.name())
        );
    }

    public boolean addTrip(int adriaId, int from, int to, String departure, String arrival, int groupId) {
        try(
                Connection con = Repositories.getH2Repo().getConnection();
                PreparedStatement stmt = con.prepareStatement(ADD_USER_HISTORY.getQuery())
        ) {
            stmt.setInt(1, adriaId);
            stmt.setInt(2, from);
            stmt.setInt(3, to);
            stmt.setString(4, departure);
            stmt.setString(5, arrival);

            if (groupId == 0) {
                stmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(6, groupId);
            }

            stmt.executeUpdate();

            int rowsAffected = stmt.getUpdateCount();

            return rowsAffected != 0;

        } catch (SQLException e) {
            Repositories.getH2Repo().handleSQLException("Failed to add user history.", e, "Could not add user history.");
        }

        return false;
    }

    public List<TeleporterHistoryResponseBody> getTeleporterHistory(int adriaId, int teleporterId) {
        List<TeleporterHistoryResponseBody> teleporterHistoryResponse = new ArrayList<>();
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_TELEPORTER_HISTORY.getQuery())) {
            stmt.setInt(1, teleporterId);
            stmt.setInt(2, teleporterId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Trip trip = createTripFromResultSet(rs);

                    teleporterHistoryResponse.add(new TeleporterHistoryResponseBody(trip, teleporterId));
                }
                return teleporterHistoryResponse;
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get teleporter history.", ex, "Could not get teleporter history.");
            return Collections.emptyList();
        }
    }
}
