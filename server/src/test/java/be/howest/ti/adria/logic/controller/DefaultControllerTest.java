package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.Location;
import be.howest.ti.adria.logic.domain.User;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DefaultControllerTest {
    private static final DefaultController controller = new DefaultController();
    private static final String URL = "jdbc:h2:./db-04";
    private static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEyLCJvdGhlckRhdGEiOiJ3aGF0ZXZlciIsImlhdCI6MTcwMTI1MDE4MX0.1p4lqWyaGajDiFOle2AOvnuDrz3S_A0ONVLaOUb9BYA";
    @BeforeEach
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
        controller.generateTokens();
    }


    @Test
    void getAdriaIdFromToken() {
        assertEquals(1, controller.getAdriaIdFromToken(token));
    }
    @Test
    void getUser() {
        assertEquals("FAST", controller.getUser(token).getName());
    }
    @Test
    void getUserFromInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getUser("123"));
    }
    @Test
    void getUserTransactions() {
        assertEquals(3, controller.getUserTransactions(token).get("usesLeft"));
    }
    @Test
    void getUserTransactionsFromInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getUserTransactions("123"));
    }

    @Test
    void getTeleporters() {
        assertFalse(controller.getTeleporters(token).isEmpty());
    }

    @Test
    void getTeleportersFromInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getTeleporters("123"));
    }

    @Test
    void getGroups() {
        assertEquals(4, controller.getGroups(token).size());
    }
    @Test
    void getGroupsFromInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getGroups("123"));
    }

    @Test
    void updateGroupMembers() {
        assertEquals("Group updated", controller.updateGroupMembers(token , 1, List.of(2)));
    }
    @Test
    void updateGroupMembersFromInvalidToken() {
        assertEquals("Token not valid", controller.updateGroupMembers("123" , 1, List.of(1, 2, 3)));
    }

    @Test
    void updateGroupMembersInvalidGroup() {
        assertEquals("No group found", controller.updateGroupMembers(token , 40, List.of(1, 2, 3)));
    }

    @Test
    void updateGroupMembersButNotLeader(){
        Integer id = controller.addGroup("test", List.of(1, 2, 3), token, 2);

        assertEquals("User is not leader", controller.updateGroupMembers(token , id, List.of(1, 2, 3)));
    }

    @Test
    void updateGroupMembersUserDoesntExist(){
        assertEquals("User not found", controller.updateGroupMembers(token , 1, List.of(1, 2, 35)));
    }
/*
    @Test
    void updateGroupMembersUserAlreadyInGroup(){
        controller.updateGroupMembers(token , 1, List.of(1, 2, 3));

        assertEquals("Member already in group", controller.updateGroupMembers(token , 1, List.of(1)));
    }

 */

    @Test
    void getGroup() {
        assertEquals("Friends", controller.getGroup(1, token).getName());
    }

    @Test
    void getGroupFromInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getGroup(1, "123"));
    }

    @Test
    void getGroupInvalidGroup() {
        assertThrows(NoSuchElementException.class, () -> controller.getGroup(100, token));
    }

    @Test
    void getUserHistory() {
        assertEquals(5, controller.getUserHistory(token).size());
    }

    @Test
    void getUserHistoryFromInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getUserHistory("123"));
    }

    @Test
    void gerUser() {
        assertEquals("FAST", controller.getUser(token).getName());
    }

    @Test
    void gerUserFromInvalidID() {
        assertThrows(NoSuchElementException.class, () -> controller.gerUser(511));
    }

    @Test
    void addGroup() {
        assertEquals(4, controller.getGroups(token).size());
        controller.addGroup("testGroep", List.of(1, 2, 3), token, 1);
        assertEquals(5, controller.getGroups(token).size());
    }

    @Test
    void addGroupInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.addGroup("testGroep", List.of(1, 2, 3), "123", 1));
    }

    @Test
    void addGroupInvalidLeader() {
        assertThrows(NoSuchElementException.class, () -> controller.addGroup("testGroep", List.of(1, 2, 3), token, 100));
    }

    @Test
    void updateGroup() {
        Repositories.getH2Repo().addGroup("test", 2);

        assertEquals("Token not valid", controller.updateGroup(1, "testGroep", "blablabla"));
        assertEquals("Group updated", controller.updateGroup(1, "testGroep", token));
        assertEquals("No group found", controller.updateGroup(100, "testGroep", token));
        assertEquals("User is not leader", controller.updateGroup(2, "testGroep", token));
    }

    @Test
    void deleteGroup() {
        assertEquals(4, controller.getGroups(token).size());
        controller.deleteGroup(1);
        assertEquals(3, controller.getGroups(token).size());
    }

    @Test
    void deleteGroupInvalidGroup() {
        assertEquals("No group found",controller.deleteGroup(100));
    }

    @Test
    void addTrip() {
        assertEquals("User history added", controller.addTrip(token, 1, 2, "2023-11-20 08:00:00", "2023-11-20 08:00:00", 1));
        assertEquals("Token not valid", controller.addTrip("123", 1, 2,"2023-11-20 08:00:00", "2023-11-20 08:00:00", 1));
        assertEquals("No group found", controller.addTrip(token, 1, 2,"2023-11-20 08:00:00", "2023-11-20 08:00:00", 15));
    }

    @Test
    void getTeleporterHistory() {
        assertEquals(6, controller.getTeleporterHistory(token, 1).size());
    }

    @Test
    void getTeleporterHistoryInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getTeleporterHistory("123", 1));
    }

    @Test
    void getTeleporterSettings() {
        assertTrue(controller.getTeleporterSettings(1, token).isVisible());
    }

    @Test
    void getTeleporterSettingsInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getTeleporterSettings(1, "123"));
    }

    @Test
    void updateTeleporterSettings() {
        assertTrue(controller.getTeleporterSettings(1, token).isVisible());
        controller.updateTeleporterSettings(1, false, token);
        assertFalse(controller.getTeleporterSettings(1, token).isVisible());
    }
    @Test
    void updateTeleporterSettingsInvalidToken() {
        assertEquals("Token not valid", controller.updateTeleporterSettings(1, false, "123"));
    }

    @Test
    void getUserPermissions() {
        assertTrue(controller.getUserPermissions(token,1).isCanManageLists());
    }

    @Test
    void getUserPermissionsInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getUserPermissions("123",1));
    }

    @Test
    void updateUserPermissions() {
        assertTrue(controller.getUserPermissions(token,1).isCanAccessAllLogs());
        controller.updateUserPermissions(token, 1, false, false, false, false);
        assertFalse(controller.getUserPermissions(token,1).isCanAccessAllLogs());
    }

    @Test
    void updateUserPermissionsInvalidToken() {
        assertEquals("Token not valid", controller.updateUserPermissions("123", 1, false, false, false, false));
    }

    @Test
    void updateUserPermissionsInvalidTeleporter() {
        assertEquals("No teleporter found", controller.updateUserPermissions(token, 100, false, false, false, false));
    }

    @Test
    void getFavourites() {
        assertFalse(controller.getFavourites(token).isEmpty());
    }

    @Test
    void getFavouritesInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getFavourites("123"));
    }

    @Test
    void addFavourite() {
        Integer initSize = controller.getFavourites(token).size();
        assertEquals("Favorite added", controller.addFavourite(token, 10, "idk", "test"));
        assertEquals(initSize + 1, controller.getFavourites(token).size());
    }

    @Test
    void addFavouriteInvalidToken() {
        assertEquals("Token not valid", controller.addFavourite("123", 1, "idk", "home"));
    }

    @Test
    void addFavouriteInvalidTeleporter() {
        assertEquals("No teleporter found", controller.addFavourite(token, 100, "idk", "home"));
    }

    @Test
    void addFavouriteTwice(){
        controller.addFavourite(token, 1, "idk", "home");
        assertEquals("Teleporter already in favorites", controller.addFavourite(token, 1, "idk", "home"));
    }

    @Test
    void updateFavourite() {
        if (controller.getFavourites(token).isEmpty()){
            controller.addFavourite(token, 1, "idk", "Home");
        }

        assertEquals("Home", controller.getFavourites(token).get(0).getName());
        assertEquals("Favorite updated", controller.updateFavourite(token, 1, "idk", "test"));
        assertEquals("test", controller.getFavourites(token).get(0).getName());
    }

    @Test
    void updateFavouriteInvalidToken() {
        assertEquals("Token not valid", controller.updateFavourite("123", 1, "idk", "home"));
    }

    @Test
    void updateFavouriteInvalidTeleporter() {
        assertEquals("No teleporter found", controller.updateFavourite(token, 100, "idk", "home"));
    }

    @Test
    void deleteFavourite() {
        Integer initSize = controller.getFavourites(token).size();

        if (initSize == 0) {
            controller.addFavourite(token, 1, "idk", "home");
            initSize = controller.getFavourites(token).size();
        }

        assertTrue(!controller.getFavourites(token).isEmpty());

        controller.deleteFavourite(token, 1);
        assertEquals(initSize - 1 , controller.getFavourites(token).size());
    }

    @Test
    void deleteFavouriteInvalidToken() {
        assertEquals("Token not valid", controller.deleteFavourite("123", 1));
    }

    @Test
    void deleteFavouriteInvalidTeleporter() {
        assertEquals("No teleporter found", controller.deleteFavourite(token, 100));
    }

    @Test
    void getUsers() {
        assertEquals(20, controller.getUsers(token).size());
    }

    @Test
    void getUsersInvalidToken() {
        assertThrows(NoSuchElementException.class, () -> controller.getUsers("123"));
    }

    @Test
    void isFriendAlreadyInGroupFalse() {
        User u1 = new User(1, "test", "somestreet 1", new Location("123", "123"), "");
        assertFalse(controller.isFriendAlreadyInGroup(1, u1));
    }
    @Test
    void isFriendAlreadyInGroupTrue() {
        User u1 = new User(1, "test", "somestreet 1", new Location("123", "123"), "");
        controller.updateGroupMembers(token , 1, List.of(1, 2, 3));
        assertTrue(controller.isFriendAlreadyInGroup(1, u1));
    }

    @Test
    void importFriends() {
        assertEquals(3,controller.getGroups(token).get(0).getMembers().size());
        assertEquals("Friends imported", controller.importFriends(token));
        assertTrue(controller.getGroups(token).get(0).getMembers().size() > 3);
    }

    @Test
    void isValidToken() {
        assertTrue(controller.isValidToken(token));
        assertFalse(controller.isValidToken("12456"));
    }
}