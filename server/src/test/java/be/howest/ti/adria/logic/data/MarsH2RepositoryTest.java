package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.Location;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.exceptions.RepositoryException;
import be.howest.ti.adria.web.response.FavouriteResponseBody;
import be.howest.ti.adria.web.response.UserHistoryResponseBody;
import io.vertx.core.json.JsonObject;
import static be.howest.ti.adria.logic.data.dbenums.ColumnNames.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.sql.*;
import java.util.*;

class AdriaH2RepositoryTest {
    private static final String URL = "jdbc:h2:./db-04";

    @BeforeEach
    void setupTestSuite() {
        JsonObject dbProperties = new JsonObject(Map.of("url", URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @AfterEach
    void tearDownTestSuite() {
        Repositories.shutdown();
    }

    @Test
    void cleanUp() {
        String filePath = "./db-04.mv.db";
        File file = new File(filePath);

        assertTrue(file.exists());
        Repositories.getH2Repo().cleanUp();
        assertFalse(file.exists());
    }

    @Test
    void generateData() {
        assertDoesNotThrow(() -> Repositories.getH2Repo().generateData());
    }

    @Test
    void handleSQLException() {
        assertThrows(RepositoryException.class, () -> Repositories.getH2Repo().handleSQLException("test", new SQLException(), "test"));
    }

    @Test
    void getUserTransactions() {
        assertEquals(3, Repositories.getH2Repo().getUserTransactions(1).size());
        assertEquals(1, Repositories.getH2Repo().getUserTransactions(2).size());

    }

    @Test
    void getTeleporters() {
        assertFalse(Repositories.getH2Repo().getTeleporters().isEmpty());
    }

    @Test
    void createLocationFromResultSet() {
        int adriaId = 1;
        int teleporterId = 6;
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_permissions_details WHERE adria_id = ? AND teleporter_id = ?;")) {

            stmt.setInt(1, adriaId);
            stmt.setInt(2, teleporterId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Location teleporterLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, LONGITUDE.name(), LATITUDE.name());
                    assertEquals("51.2101", teleporterLocation.getLat());
                    assertEquals("3.2233", teleporterLocation.getLon());
                }
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get user permissions.", ex, "Could not get user permissions.");
        }
    }

    @Test
    void createUserFromResultSet() {
        //todo
    }

    @Test
    void getGroups() {
        assertEquals(4, Repositories.getH2Repo().getGroups(1).size());
    }

    @Test
    void updateGroupMember() {
        Integer initSize = Repositories.getH2Repo().getGroup(1).getMembers().size();
        Repositories.getH2Repo().updateGroupMember(1, 4);
        assertEquals(initSize + 1, Repositories.getH2Repo().getGroup(1).getMembers().size());
    }

    @Test
    void getGroup() {
        assertNotNull(Repositories.getH2Repo().getGroup(1));
    }

    @Test
    void deleteGroupMembers() {
        Integer initSize = Repositories.getH2Repo().getGroup(1).getMembers().size();
        if (initSize == 0) {
            Repositories.getH2Repo().updateGroupMember(1, 2);
            assertEquals(1, Repositories.getH2Repo().getGroup(1).getMembers().size());
        }

        Repositories.getH2Repo().deleteGroupMembers(1);
        assertEquals(0, Repositories.getH2Repo().getGroup(1).getMembers().size());
    }

    @Test
    void getUser() {
        assertNotNull(Repositories.getH2Repo().getUser(1));
        assertEquals("FAST", Repositories.getH2Repo().getUser(1).getName());
    }

    @Test
    void getUserHistory() {
        List< UserHistoryResponseBody> test = Repositories.getH2Repo().getUserHistory(1);

        System.out.println(test.get(0).getDestination());

        assertEquals("Belfry Teleporter (Belfry Lane 789)", test.get(0).getDestination());
    }

    @Test
    void addGroup() {
        assertEquals(4, Repositories.getH2Repo().getGroups(1).size());
        Repositories.getH2Repo().addGroup("de boeren", 1);
        assertEquals(5, Repositories.getH2Repo().getGroups(1).size());
    }

    @Test
    void updateGroup() {
        assertEquals("Friends", Repositories.getH2Repo().getGroup(1).getName());
        Repositories.getH2Repo().updateGroup(1, "de boerkes");
        assertEquals("de boerkes", Repositories.getH2Repo().getGroup(1).getName());
    }


    @Test
    void getConnection() {
        //todo
    }


    @Test
    void deleteGroup() {
        Repositories.getH2Repo().addGroup("de boeren", 1);
        assertEquals(5, Repositories.getH2Repo().getGroups(1).size());
        Repositories.getH2Repo().deleteGroup(1);
        assertEquals(4, Repositories.getH2Repo().getGroups(1).size());

    }

    @Test
    void addTrip() {
        assertEquals(5, Repositories.getH2Repo().getUserHistory(1).size());
        Repositories.getH2Repo().addTrip(1, 1, 2, "2021-09-01 12:00:00", "2021-09-01 12:00:00", 1);
        assertEquals(6, Repositories.getH2Repo().getUserHistory(1).size());
    }

    @Test
    void getTeleporterHistory() {
        assertEquals(6, Repositories.getH2Repo().getTeleporterHistory(1, 1).size());
    }

    @Test
    void getTeleporterSettings() {
        assertEquals(1, Repositories.getH2Repo().getTeleporterSettings(1).getOwnerId());
    }

    @Test
    void updateTeleporterSettings() {
        assertTrue(Repositories.getH2Repo().getTeleporterSettings(1).isVisible());
        Repositories.getH2Repo().updateTeleporterSettings(1, false);
        assertFalse(Repositories.getH2Repo().getTeleporterSettings(1).isVisible());
    }

    @Test
    void getUserPermissions() {
        assertTrue( Repositories.getH2Repo().getUserPermissions(1, 1).isCanAccessAllLogs());
    }

    @Test
    void updateUserPermissions() {
        assertTrue( Repositories.getH2Repo().getUserPermissions(1, 1).isCanAccessAllLogs());
        Repositories.getH2Repo().updateUserPermissions(1, 1, false, false, false, false);
        assertFalse( Repositories.getH2Repo().getUserPermissions(1, 1).isCanAccessAllLogs());
    }

    @Test
    void getFavourites() {
        assertEquals(FavouriteResponseBody.class, Repositories.getH2Repo().getFavourites(1).get(0).getClass());
    }

    @Test
    void addFavourite() {
        Integer initSize = Repositories.getH2Repo().getFavourites(1).size();
        Repositories.getH2Repo().addFavourite(1, 10, "teleporter", "test");
        assertEquals(initSize + 1, Repositories.getH2Repo().getFavourites(1).size());
    }

    @Test
    void updateFavourite() {
        if (Repositories.getH2Repo().getFavourites(1).isEmpty()) {
            Repositories.getH2Repo().addFavourite(1, 1, "teleporter", "Home");
        }

        assertEquals("Home", Repositories.getH2Repo().getFavourites(1).get(0).getName());
        Repositories.getH2Repo().updateFavourite(1, 1, "teleporter", "updatedName");
        assertEquals("updatedName", Repositories.getH2Repo().getFavourites(1).get(0).getName());
    }

    @Test
    void deleteFavourite() {
        Integer initSize = Repositories.getH2Repo().getFavourites(1).size();
        if (initSize == 0) {
            Repositories.getH2Repo().addFavourite(1, 1, "teleporter", "Home");
            initSize = Repositories.getH2Repo().getFavourites(1).size();
        }

        Repositories.getH2Repo().deleteFavourite(1, 1);
        assertEquals(initSize - 1, Repositories.getH2Repo().getFavourites(1).size());
    }

    @Test
    void getUsers() {
        List<User> users = Repositories.getH2Repo().getUsers();

        assertEquals(20, users.size());
    }

    @Test
    void importFriends() {
        Integer initSize = Repositories.getH2Repo().getGroup(1).getMembers().size();
        Repositories.getH2Repo().importFriends(4, 1);
        assertTrue(Repositories.getH2Repo().getGroup(1).getMembers().size() > initSize);
    }

    @Test
    void getRandomFriends() {
        assertDoesNotThrow(() -> Repositories.getH2Repo().getRandomFriends(5, 1));
        assertEquals(5, Repositories.getH2Repo().getRandomFriends(5, 1).size());
    }

    @Test
    void generateTokens() {
        Repositories.getH2Repo().generateTokens();
        List<String> tokens = new ArrayList<>();

        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT token FROM users")) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tokens = Collections.singletonList(rs.getString("token"));
                }
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to check if token is valid.", ex, "Could not check if token is valid.");
        }

        assertNotNull(tokens);
        assertFalse(tokens.isEmpty());
    }

    @Test
    void deleteGroupMember() {
        Integer initSize = Repositories.getH2Repo().getGroup(1).getMembers().size();
        if (initSize == 0) {
            Repositories.getH2Repo().importFriends(4, 1);
            assertEquals(1, Repositories.getH2Repo().getGroup(1).getMembers().size());
            initSize = Repositories.getH2Repo().getGroup(1).getMembers().size();
        }

        Repositories.getH2Repo().deleteGroupMember(1, 4);
        assertEquals(initSize - 1, Repositories.getH2Repo().getGroup(1).getMembers().size());
    }

    @Test
    void getAdrianId() {
        Repositories.getH2Repo().generateTokens();
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEyLCJvdGhlckRhdGEiOiJ3aGF0ZXZlciIsImlhdCI6MTcwMTI1MDE4MX0.1p4lqWyaGajDiFOle2AOvnuDrz3S_A0ONVLaOUb9BYA";
        assertEquals(1, Repositories.getH2Repo().getAdrianId(token));
    }

    @Test
    void isValidToken() {
        Repositories.getH2Repo().generateTokens();
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEyLCJvdGhlckRhdGEiOiJ3aGF0ZXZlciIsImlhdCI6MTcwMTI1MDE4MX0.1p4lqWyaGajDiFOle2AOvnuDrz3S_A0ONVLaOUb9BYA";

        assertTrue(Repositories.getH2Repo().isValidToken(token));
    }


}
