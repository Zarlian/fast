package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.domain.UserTransaction;

import java.util.List;

public interface UserTransactionInterface {
    List<UserTransaction> getUserTransactions(int adrianId);
}
