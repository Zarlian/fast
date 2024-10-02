package be.howest.ti.adria.logic;

import be.howest.ti.adria.logic.controller.DefaultController;
import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.events.*;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;

class MapTest {
    DefaultController controller;
    private java.util.Map<String, String> friends;
    private static final String URL = "jdbc:h2:./db-04";
    private static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEyLCJvdGhlckRhdGEiOiJ3aGF0ZXZlciIsImlhdCI6MTcwMTI1MDE4MX0.1p4lqWyaGajDiFOle2AOvnuDrz3S_A0ONVLaOUb9BYA";
    private static final String secondToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzAxOTQ2NjcwLCJleHAiOjE3MDE5NTAyNzB9.zamzW9ojqt2HWSKKUfXHUK7GVyp747Zt5B0Pf3j33t4";

    @BeforeEach
    void setupTestSuite() {
        friends = new HashMap<>();
        controller = new DefaultController();

        JsonObject dbProperties = new JsonObject();
        dbProperties.put("url", URL);
        dbProperties.put("username", "");
        dbProperties.put("password", "");
        dbProperties.put("webconsole.port", 9000);

        Repositories.configure(dbProperties);
        controller.generateTokens();
    }

    @AfterEach
    void tearDownTestSuite() {
        Repositories.shutdown();
    }

    @Test
    void removeFriendRequest(){
        //todo
    }

    @Test
    void handleGetFriendRequestsEvent() {
        Map map = new Map();
        IncomingEvent e1 = new GetFriendRequestsEvent(token);

        List<OutgoingEvent> r1 = map.handleEvent(e1);

        assertEquals(EventType.GET_FRIENDREQUESTS, r1.get(0).getType());
    }

    @Test
    void handleFriendRequestAcceptEvent() {
        Map map = new Map();
        //send friend request from user 1 to user 2
        assertEquals(0, map.getFriendRequests(secondToken).size());
        IncomingEvent invite = new FriendRequestEvent(token, 2);
        List<OutgoingEvent> r1 = map.handleEvent(invite);
        assertEquals(1, map.getFriendRequests(secondToken).size());

        //IncomingEvent accept = new FriendRequestAcceptEvent(secondToken, 1, true);
        //List<OutgoingEvent> r2 = map.handleEvent(accept);
        //System.out.println(friends.size());
    }

    @Test
    void handleFriendRequestEvent() {
        Map map = new Map();
        IncomingEvent e1 = new FriendRequestEvent(token, 2);

        List<OutgoingEvent> r1 = map.handleEvent(e1);

        assertEquals(EventType.FRIENDREQUEST, r1.get(0).getType());
    }

    @Test
    void handleLocationEvent(){
        Map map = new Map();
        IncomingEvent e1 = new LocationEvent(token, "123","123");

        List<OutgoingEvent> r1 = map.handleEvent(e1);

        assertEquals(EventType.LOCATION, r1.get(0).getType());
    }



}