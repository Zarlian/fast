package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.data.FileReader;
import be.howest.ti.adria.logic.data.dbenums.Queries;
import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.Location;
import be.howest.ti.adria.logic.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;

import static be.howest.ti.adria.logic.data.dbenums.ColumnNames.*;

public class UserInterfaceImpl implements UserInterface {
    public User getUser(int adrianId) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(Queries.GET_USER.getQuery())) {

            stmt.setInt(1, adrianId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Location location = new Location(rs.getString(LONGITUDE.name()), rs.getString(LATITUDE.name()));
                    return new User(rs.getInt(ADRIA_ID.name()), rs.getString(NAME.name()), rs.getString(ADDRESS.name()), location, rs.getString(PROFILE_PICTURE.name()));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get user.", ex, "Could not get user.");
            return null;
        }
    }

    public List<User> getUsers() {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(Queries.GET_USERS.getQuery())) {

            List<User> users = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Location location = new Location(rs.getString(LONGITUDE.name()), rs.getString(LATITUDE.name()));
                    users.add(new User(rs.getInt(ADRIA_ID.name()), rs.getString(NAME.name()), rs.getString(ADDRESS.name()), location, rs.getString(PROFILE_PICTURE.name())));
                }

                return users;
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get users.", ex, "Could not get users.");
            return Collections.emptyList();
        }
    }

    public List<User> getRandomFriends(int amount, int excludedId) {
        // get a random amount of users
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(Queries.GET_RANDOM_FRIENDS.getQuery())) {

            stmt.setInt(1, excludedId);
            stmt.setInt(2, amount);

            List<User> users = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Location location = new Location(rs.getString(LONGITUDE.name()), rs.getString(LATITUDE.name()));
                    users.add(new User(rs.getInt(ADRIA_ID.name()), rs.getString(NAME.name()), rs.getString(ADDRESS.name()), location, rs.getString(PROFILE_PICTURE.name())));
                }

                return users;
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get random friends.", ex, "Could not get random friends.");
            return Collections.emptyList();
        }
    }

    public void generateTokens() {
        List<User> users = getUsers();

        for (User user : users) {
            String token = generateToken(user.getAdriaId());
            if (user.getAdriaId() == 1) {
                token = getAdminToken();
            } else if (user.getAdriaId() == 2) {
                token = getSecondaryToken();
            }
            updateUserToken(user.getAdriaId(), token);
        }
    }

    public String getAdminToken() {
        FileReader fileReader = FileReader.getInstance();

        return fileReader.read("admin.token");
    }

    public String getSecondaryToken() {
        FileReader fileReader = FileReader.getInstance();

        return fileReader.read("secondary.token");
    }

    private void updateUserToken(int userId, String token) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(Queries.UPDATE_USER_TOKEN.getQuery())) {

            stmt.setString(1, token);
            stmt.setInt(2, userId);

            stmt.executeUpdate();

        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to update user token.", ex, "Could not update user token.");
        }
    }

    public static String generateToken(int userId) {
        String key = FileReader.getInstance().read("jwt.key");
        long expirationTimeMillis = 3600000; // 1 hour

        return Jwts.builder()
                .setSubject(Integer.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public int getAdrianId(String token) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(Queries.GET_ADRIAN_ID.getQuery())) {

            stmt.setString(1, token);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(ADRIA_ID.name());
                } else {
                    return -1;
                }
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get adrian id.", ex, "Could not get adrian id.");
            return -1;
        }
    }

    public boolean isValidToken(String token) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(Queries.GET_ADRIAN_ID.getQuery())) {

            stmt.setString(1, token);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to check if token is valid.", ex, "Could not check if token is valid.");
            return false;
        }
    }

    public String getTokenFromAdrianId(int adriaId) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(Queries.GET_TOKEN_FROM_ADRIAN_ID.getQuery())) {

            stmt.setInt(1, adriaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(TOKEN.name());
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get token from adrian id.", ex, "Could not get token from adrian id.");
            return null;
        }
    }
    
    public void increaseUserUsesLeft(int adriaId) {
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(Queries.INCREASE_USER_USES_LEFT.getQuery())) {

            stmt.setInt(1, adriaId);

            stmt.executeUpdate();

        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to decrease user uses left.", ex, "Could not decrease user uses left.");
        }
    }
}