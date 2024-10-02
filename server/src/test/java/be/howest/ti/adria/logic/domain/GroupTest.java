package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    private static User user1;
    private static Group group1;
    @BeforeAll
    static void setup() {
        user1 = new User(1,"Bob", "Fastlane 12, 8000 Bruges", new Location("0.123","0.123"), "assets/images/users/bob.png");
        group1 = new Group(1,"Friends", user1);
    }

    @Test
    void getId() {
        assertEquals(1, group1.getId());
    }

    @Test
    void getName() {
        assertEquals("Friends", group1.getName());
    }

    @Test
    void getLeader() {
        assertEquals(user1, group1.getLeader());
    }

    @Test
    void getGroupMembers() {
        assertEquals(0, group1.getGroupMembers().size());
    }

    @Test
    void addMember() {
        assertEquals(0, group1.getGroupMembers().size());
        User user2 = new User(2,"Alice", "Fastlane 12, 8000 Bruges", new Location("0.123","0.123"), "assets/images/users/alice.png");
        group1.addMember(user2);
        assertEquals(1, group1.getGroupMembers().size());
    }

}