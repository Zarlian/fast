package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavouriteTest {
    private static User user;
    private static Teleporter teleporter;
    private static Favourite favourite;

    @BeforeAll
    static void setup() {
        user = new User(1,"FAST", "Fastlane 12, 8000 Bruges", new Location("0.123","0.123"), "assets/images/users/fast.png");
        teleporter = new Teleporter(1, "Bruges","Rijselstraat 5, Bruges" ,new Location("0.123","0.123"),"Public", null);
        favourite = new Favourite(user,teleporter, "Home", "My bigass villa");
    }
    @Test
    void getUser() {
        assertEquals(user, favourite.getUser());
    }

    @Test
    void getTeleporter() {
        assertEquals(teleporter, favourite.getTeleporter());
    }

    @Test
    void getType() {
        assertEquals("Home", favourite.getType());
    }

    @Test
    void getName() {
        assertEquals("My bigass villa", favourite.getName());
    }
}