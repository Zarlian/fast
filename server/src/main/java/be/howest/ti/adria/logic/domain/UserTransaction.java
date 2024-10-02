package be.howest.ti.adria.logic.domain;

public class UserTransaction {
    private final User user;
    private final Transaction transaction;
    private final int usesLeft;

    public UserTransaction(User user, Transaction transaction, int usesLeft) {
        this.user = user;
        this.transaction = transaction;
        this.usesLeft = usesLeft;
    }

    public User getUser() {
        return user;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public int getUsesLeft() {
        return usesLeft;
    }

}
