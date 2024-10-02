package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeleporterTest {
    private static Teleporter teleporter;
    private static Location locationTeleporter;
    private static User user;

    @BeforeAll
    static void setup(){
        locationTeleporter = new Location( "1.111", "2.222");
        Location locationUser = new Location("3.333", "4.444");
        user = new User(1, "name", "Fastlane 1", locationUser, "assets/images/users/fast.png");
        teleporter = new Teleporter(1, "name", "Rijselstaat 5, Brugge", locationTeleporter, "Private", user);
    }

    @Test
    void getId() {
        assertEquals(1, teleporter.getId());
    }

    @Test
    void getName() {
        assertEquals("name", teleporter.getName());
    }

    @Test
    void getLocation() {
        assertEquals(locationTeleporter, teleporter.getLocation());
    }

    @Test
    void getAddress() {
        assertEquals("Rijselstaat 5, Brugge", teleporter.getAddress());
    }

    @Test
    void getType() {
        assertEquals("Private", teleporter.getType());
    }

    @Test
    void getOwner() {
        assertEquals(user, teleporter.getOwner());
    }
}