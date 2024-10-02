package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.domain.User;

import java.util.List;

public interface UserInterface {
    // ENDPOINTS FOR FAST
    User getUser(int adrianId);

    List<User> getUsers();

    List<User> getRandomFriends(int amount, int excludedId);

    void generateTokens();

    int getAdrianId(String token);

    boolean isValidToken(String token);

    String getTokenFromAdrianId(int adriaId);

    void increaseUserUsesLeft(int adriaId);
}
