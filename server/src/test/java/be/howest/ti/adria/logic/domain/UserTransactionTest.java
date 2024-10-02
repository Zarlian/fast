package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTransactionTest {
    private static UserTransaction userTransaction;
    private static User user;
    private static Transaction transaction;
    @BeforeAll
    static void setup(){
        user = new User(1, "Eva", "Fastlane 1", new Location( "3.333", "4.444"), "assets/images/users/fast.png");
        transaction = new Transaction(1, "Teleporter", "Teleport to the moon", 70.0, 40);
        userTransaction = new UserTransaction(user, transaction,6);
    }

    @Test
    void getUser() {
        assertEquals(user, userTransaction.getUser());
    }

    @Test
    void getTransaction() {
        assertEquals(transaction, userTransaction.getTransaction());
    }

    @Test
    void getUsesLeft() {
        assertEquals(6, userTransaction.getUsesLeft());
    }
}