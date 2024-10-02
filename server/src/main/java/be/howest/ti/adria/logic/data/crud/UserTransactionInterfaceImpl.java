package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.Location;
import be.howest.ti.adria.logic.domain.Transaction;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.UserTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static be.howest.ti.adria.logic.data.dbenums.ColumnNames.*;
import static be.howest.ti.adria.logic.data.dbenums.Queries.*;

public class UserTransactionInterfaceImpl implements UserTransactionInterface {
    public List<UserTransaction> getUserTransactions(int adrianId) {
        List<UserTransaction> userTransactions = new ArrayList<>();
        try (Connection conn = Repositories.getH2Repo().getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_TRANSACTIONS.getQuery())
        ) {
            stmt.setInt(1, adrianId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = createTransactionFromResultSet(rs);
                    User user = createUserFromResultSet(rs);

                    UserTransaction ut = new UserTransaction(user, transaction, rs.getInt(USES_LEFT.name()));
                    userTransactions.add(ut);
                }
                return userTransactions;
            }
        } catch (SQLException ex) {
            Repositories.getH2Repo().handleSQLException("Failed to get user transactions.", ex, "Could not get user transactions.");
            return Collections.emptyList();
        }
    }

    private Transaction createTransactionFromResultSet(ResultSet rs) throws SQLException {
        int transactionId = rs.getInt(TRANSACT_ID.name());
        String type = rs.getString(TYPE.name());
        String name = rs.getString(NAME.name());
        double price = rs.getDouble(PRICE.name());
        int maxUses = rs.getInt(MAX_USES.name());

        return new Transaction(transactionId, type, name, price, maxUses);
    }
    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt(ADRIA_ID.name());
        String userName = rs.getString(NAME.name());
        String userAddress = rs.getString(ADDRESS.name());
        String longitude = rs.getString(LONGITUDE.name());
        String latitude = rs.getString(LATITUDE.name());
        String profilePicture = rs.getString(PROFILE_PICTURE.name());

        Location location = new Location(longitude, latitude);
        return new User(userId, userName, userAddress, location, profilePicture);
    }
}
