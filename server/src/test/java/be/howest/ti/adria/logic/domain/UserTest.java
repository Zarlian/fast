package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static Location locationUser1;
    private static Location locationUser21;

    private static User user1;
    private static User user21;

    @BeforeAll
    static void setup(){
        locationUser1 = new Location( "3.333", "4.444");
        locationUser21 = new Location( "1.111", "2.222");
        user1 = new User(1, "Eva", "Fastlane 1", locationUser1, "assets/images/users/fast.png");
        user21 = new User(21, "Bob", "Fastlane 2", locationUser21, "assets/images/users/fast.png");

    }

    @Test
    void getAdriaId() {
        assertEquals(1, user1.getAdriaId());
        assertEquals(21, user21.getAdriaId());
    }

    @Test
    void getName() {
        assertEquals("Eva", user1.getName());
        assertEquals("Bob", user21.getName());
    }

    @Test
    void getAddress() {
        assertEquals("Fastlane 1", user1.getAddress());
        assertEquals("Fastlane 2", user21.getAddress());
    }

    @Test
    void getLocation() {
        assertEquals(locationUser1, user1.getLocation());
        assertEquals(locationUser21, user21.getLocation());
    }

    @Test
    void getProfilePicture() {
        assertEquals("assets/images/users/fast.png", user1.getProfilePicture());
        assertEquals("assets/images/users/fast.png", user21.getProfilePicture());
    }

    @Test
    void toStringTest(){
        assertEquals("Eva", user1.toString());
    }
}