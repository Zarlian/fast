package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPermissionsTest {
    private static User user;
    private static Teleporter teleporter;
    private static UserPermissions userPermissions1;
    private static UserPermissions userPermissions2;
    @BeforeAll
    static void setup(){
        Location location = new Location( "1.111", "2.222");
        user = new User(1, "Eva", "Fastlane 1", location, "assets/images/users/fast.png");
        teleporter = new Teleporter(1, "Home", "Fastlane 420", location, "Private", user);
        userPermissions1 = new UserPermissions(user, teleporter, true, true, true, true);
        userPermissions2 = new UserPermissions(user, teleporter, false, false, false, false);
    }

    @Test
    void getUser() {
        assertEquals(user, userPermissions1.getUser());
        assertEquals(user, userPermissions2.getUser());
    }

    @Test
    void getTeleporter() {
        assertEquals(teleporter, userPermissions1.getTeleporter());
        assertEquals(teleporter, userPermissions2.getTeleporter());
    }

    @Test
    void isCanAccessLogs() {
        assertTrue(userPermissions1.isCanAccessLogs());
        assertFalse(userPermissions2.isCanAccessLogs());
    }

    @Test
    void isCanManageLists() {
        assertTrue(userPermissions1.isCanManageLists());
        assertFalse(userPermissions2.isCanManageLists());
    }

    @Test
    void isCanAssignPermissions() {
        assertTrue(userPermissions1.isCanAssignPermissions());
        assertFalse(userPermissions2.isCanAssignPermissions());
    }

    @Test
    void isCanControlTeleporter() {
        assertTrue(userPermissions1.isCanControlTeleporter());
        assertFalse(userPermissions2.isCanControlTeleporter());
    }
}