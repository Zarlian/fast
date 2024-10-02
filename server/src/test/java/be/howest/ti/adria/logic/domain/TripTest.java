package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {
    private static User user;
    private static Teleporter from;
    private static Teleporter to;
    private static Trip trip;
    @BeforeAll
    static void setup() {
        int id = 1;
        user = new User(1,"FAST", "Fastlane 12, 8000 Bruges", new Location("0.123","0.123"), "assets/images/users/fast.png");
        Group group = null;
        from = new Teleporter(1, "Bruges","Rijselstraat 5, Bruges" ,new Location("0.123","0.123"),"Public", null);
        to = new Teleporter(2, "Adria","Fastlane 1, Adria" ,new Location("0.123","0.123"),"Public", null);
        String departure = "12:00";
        String arrival = "12:30";
        trip = new Trip(id, user, group, from, to, departure, arrival);
    }
    @Test
    void getId() {
        assertEquals(1, trip.getId());
    }

    @Test
    void getUser() {
        assertEquals(user, trip.getUser());
    }

    @Test
    void getGroup() {
        assertNull(trip.getGroup());
    }

    @Test
    void getFrom() {
        assertEquals(from, trip.getFrom());
    }

    @Test
    void getTo() {
        assertEquals(to, trip.getTo());
    }

    @Test
    void getDeparture() {
        assertEquals("12:00", trip.getDeparture());
    }

    @Test
    void getArrival() {
        assertEquals("12:30", trip.getArrival());
    }
}