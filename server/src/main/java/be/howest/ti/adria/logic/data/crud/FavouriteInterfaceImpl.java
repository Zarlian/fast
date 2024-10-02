package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.Favourite;
import be.howest.ti.adria.logic.domain.Location;
import be.howest.ti.adria.logic.domain.Teleporter;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.web.response.FavouriteResponseBody;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static be.howest.ti.adria.logic.data.dbenums.Queries.*;
import static be.howest.ti.adria.logic.data.dbenums.ColumnNames.*;

public class FavouriteInterfaceImpl implements FavouriteInterface {
    public List<FavouriteResponseBody> getFavourites(int adriaId) {
        List<FavouriteResponseBody> favourites = new ArrayList<>();
        try(
            Connection con = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = con.prepareStatement(GET_FAVOURITES.getQuery())
        ) {
            stmt.setInt(1, adriaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Location ownerLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, OWNER_LOCATION_LONGITUDE.name(), OWNER_LOCATION_LATITUDE.name());
                    User owner = Repositories.getH2Repo().createUserFromResultSet(rs, OWNER_ID.name(), OWNER_NAME.name(), OWNER_ADDRESS.name(), OWNER_PROFILE_PICTURE.name(), ownerLocation);

                    Location teleporterLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, TELEPORTER_LOCATION_LON.name(), TELEPORTER_LOCATION_LAT.name());
                    Teleporter teleporter = new Teleporter(
                            rs.getInt(TELEPORTER_ID.name()),
                            rs.getString(TELEPORTER_NAME.name()),
                            rs.getString(TELEPORTER_ADDRESS.name()),
                            teleporterLocation,
                            rs.getString(TELEPORTER_TYPE.name()),
                            owner
                    );

                    Location userLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, USER_LOCATION_LONGITUDE.name(), USER_LOCATION_LATITUDE.name());
                    User user = Repositories.getH2Repo().createUserFromResultSet(rs, ADRIA_ID.name(), USER_NAME.name(), USER_ADDRESS.name(), PROFILE_PICTURE.name(), userLocation);

                    Favourite favourite = new Favourite(
                            user,
                            teleporter,
                            rs.getString(TYPE.name()),
                            rs.getString(NAME.name())
                    );
                    favourites.add(new FavouriteResponseBody(favourite));
                }

                if (favourites.isEmpty()) {
                    return Collections.emptyList();
                }
                return favourites;
            }
        } catch (SQLException e) {
            Repositories.getH2Repo().handleSQLException("Failed to get favourites", e, "Could not get favourites from database");
        }
        return Collections.emptyList();
    }

    public boolean addFavourite(int adriaId, int teleporterId, String type, String name) {
        try(
            Connection con = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = con.prepareStatement(ADD_FAVOURITE.getQuery())
        ) {
            stmt.setInt(1, adriaId);
            stmt.setInt(2, teleporterId);
            stmt.setString(3, type);
            stmt.setString(4, name);
            stmt.executeUpdate();

            int rowsAffected = stmt.getUpdateCount();

            return rowsAffected == 1;

        } catch (SQLException e) {
            Repositories.getH2Repo().handleSQLException("Failed to add favourite", e, "Could not add favourite to database");
        }
        return false;
    }
    public boolean updateFavourite(int adriaId, int teleporterId, String type, String name) {
        try(
            Connection con = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_FAVOURITE.getQuery())
        ) {
            stmt.setString(1, type);
            stmt.setString(2, name);
            stmt.setInt(3, adriaId);
            stmt.setInt(4, teleporterId);
            stmt.executeUpdate();

            int rowsAffected = stmt.getUpdateCount();

            return rowsAffected == 1;

        } catch (SQLException e) {
            Repositories.getH2Repo().handleSQLException("Failed to update favourite", e, "Could not update favourite in database");
        }
        return false;
    }

    public boolean deleteFavourite(int adriaId, int teleporterId) {
        try(
            Connection con = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_FAVOURITE.getQuery())
        ) {
            stmt.setInt(1, adriaId);
            stmt.setInt(2, teleporterId);
            stmt.executeUpdate();

            int rowsAffected = stmt.getUpdateCount();

            return rowsAffected == 1;

        } catch (SQLException e) {
            Repositories.getH2Repo().handleSQLException("Failed to delete favourite", e, "Could not delete favourite from database");
        }
        return false;
    }
}
