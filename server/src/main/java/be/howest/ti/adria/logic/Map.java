package be.howest.ti.adria.logic;

import be.howest.ti.adria.logic.data.H2Repository;
import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.events.*;
import be.howest.ti.adria.web.response.GroupResponseBody;
import be.howest.ti.adria.web.response.UserGroupResponseBody;

import java.util.*;

public class Map {
    private final java.util.Map<String, String> friends = new HashMap<>();
    private final java.util.Map<User, User> friendRequests = new HashMap<>();
    private static final String FRIENDS_GROUP_NAME = "Friends";

    public List<OutgoingEvent> handleEvent(IncomingEvent event) {
        List<OutgoingEvent> events = null;

        switch (event.getType()) {
            case LOCATION:
                events = handleLocationEvent((LocationEvent) event);
                break;
            case FRIENDREQUEST:
                addFriendRequest((FriendRequestEvent) event);
                events = handleFriendRequestEvent((FriendRequestEvent) event);
                break;
            case GET_FRIENDREQUESTS:
                events = handleGetFriendRequestsEvent((GetFriendRequestsEvent) event);
                break;
            case FRIENDREQUESTACCEPT:
                events = handleFriendRequestAcceptEvent((FriendRequestAcceptEvent) event);
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + event.getType());
        }

        return events;
    }

    private void removeFriendRequest(FriendRequestAcceptEvent event) {
        H2Repository repo = Repositories.getH2Repo();

        String token = event.getToken();
        int senderId = repo.getAdrianId(token);
        int recipientId = event.getRecipient();

        friendRequests.forEach((recipient, sender) -> {
            if (sender.getAdriaId() == senderId && recipient.getAdriaId() == recipientId) {
                friendRequests.remove(recipient, sender);
            }
        });
    }

    private void addFriendRequest(FriendRequestEvent event) {
        H2Repository repo = Repositories.getH2Repo();

        String token = event.getToken();
        int senderId = repo.getAdrianId(token);
        int recipientId = event.getRecipient();

        User sender = repo.getUser(senderId);
        User recipient = repo.getUser(recipientId);

        this.friendRequests.put(sender, recipient);
    }

    public java.util.Map<String, Integer> getFriendRequests(String recipientToken) {
        // get the friend requests where the recipient is the given recipientId
        java.util.Map<String, Integer> requests = new HashMap<>();

        for (java.util.Map.Entry<User, User> entry : this.friendRequests.entrySet()) {
            User sender = entry.getKey();
            User recipient = entry.getValue();

            int recipientId = Repositories.getH2Repo().getAdrianId(recipientToken);

            if (recipient.getAdriaId() == recipientId) {
                requests.put(sender.getName(), sender.getAdriaId());
            }
        }

        return requests;
    }

    private List<OutgoingEvent> handleGetFriendRequestsEvent(GetFriendRequestsEvent event) {
        String senderToken = event.getToken();

        H2Repository repo = Repositories.getH2Repo();

        int senderId = repo.getAdrianId(senderToken);

        List<OutgoingEvent> events = new ArrayList<>();

        if (senderId == 0) {
            return Collections.emptyList();
        } else {
            events.add(EventFactory.getInstance().createUnicastFriendRequestEvent(senderToken));
        }

        return events;
    }

    private List<OutgoingEvent> handleFriendRequestAcceptEvent(FriendRequestAcceptEvent event) {
        // This doesn't return an event it handles the event server side
        String token = event.getToken();

            H2Repository repo = Repositories.getH2Repo();

        int senderId = repo.getAdrianId(token);
        int recipientId = event.getRecipient();

        // Friends group id

        if (event.getAccepted()) {
            int groupIdSender = Objects.requireNonNull(repo.getGroups(senderId).stream().filter(g -> g.getName().equals(FRIENDS_GROUP_NAME)).findFirst().orElse(null)).getId();
            int groupIdRecipient = Objects.requireNonNull(repo.getGroups(recipientId).stream().filter(g -> g.getName().equals(FRIENDS_GROUP_NAME)).findFirst().orElse(null)).getId();

            // Add sender to recipient's friends group
            repo.updateGroupMember(groupIdRecipient, senderId);
            // Add recipient to sender's friends group
            repo.updateGroupMember(groupIdSender, recipientId);

            removeFriendRequest(event);
        } else {
            // remove friend from friend requests based on id
            removeFriendRequest(event);
        }

        return Collections.emptyList();
    }

    private List<OutgoingEvent> handleFriendRequestEvent(FriendRequestEvent event) {
        String token = event.getToken();

        H2Repository repo = Repositories.getH2Repo();

        int senderId = repo.getAdrianId(token);

        User sender = repo.getUser(senderId);
        String friendName = sender.getName();

        int groupId = Objects.requireNonNull(repo.getGroups(senderId).stream().filter(g -> g.getName().equals(FRIENDS_GROUP_NAME)).findFirst().orElse(null)).getId();

        List<OutgoingEvent> events = new ArrayList<>();

        String recipientToken = repo.getTokenFromAdrianId(event.getRecipient());

        if (senderId == 0) {
            return Collections.emptyList();
        } else {
            events.add(EventFactory.getInstance().createUnicastFriendRequestEvent(recipientToken, friendName, senderId, groupId));
        }

        return events;
    }

    private List<OutgoingEvent> handleLocationEvent(LocationEvent event) {
        String token = event.getToken();

        H2Repository repo = Repositories.getH2Repo();

        int senderId = repo.getAdrianId(token);

        User sender = repo.getUser(senderId);
        String senderName = sender.getName();

        List<GroupResponseBody> groups = repo.getGroups(senderId);

        if (groups == null) {
            return Collections.emptyList();
        }

        GroupResponseBody friendsGroup = groups.stream().filter(g -> g.getName().equals(FRIENDS_GROUP_NAME)).findFirst().orElse(null);

        assert friendsGroup != null;
        List<UserGroupResponseBody> users = friendsGroup.getMembers();

        for (UserGroupResponseBody user : users) {
            String uToken = repo.getTokenFromAdrianId(user.getAdriaId());
            String uName = user.getName();

            this.friends.put(uName, uToken);
        }

        List<OutgoingEvent> events = new ArrayList<>();

        if (senderId == 0) {
            return Collections.emptyList();
        } else {
            String longitude = event.getLongitude();
            String latitude = event.getLatitude();

            for (java.util.Map.Entry<String, String> entry : this.friends.entrySet()) {
                String friendToken = entry.getValue();
                events.add(EventFactory.getInstance().createUnicastLocationEvent(friendToken, latitude, longitude, senderName));
            }
        }

        return events;
    }
}
