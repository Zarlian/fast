package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.data.crud.*;
import be.howest.ti.adria.logic.domain.*;
import be.howest.ti.adria.logic.exceptions.RepositoryException;
import be.howest.ti.adria.web.response.*;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start project there was no need for a Java interface AdriaRepository.
Please always use interfaces when needed.

To make this class useful, please complete it with the topics seen in the module OOA & SD
 */

public class H2Repository implements UserInterface, UserTransactionInterface, TeleporterInterface,
        GroupInterface {
    private static final Logger LOGGER = Logger.getLogger(H2Repository.class.getName());

    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    // Interfaces
    private final UserInterface userInterface;
    private final UserTransactionInterface userTransactionInterface;
    private final TeleporterInterface teleporterInterface;
    private final GroupInterface groupInterface;
    private final TripInterface tripInterface;
    private final FavouriteInterface favouriteInterface;

    public H2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database web console started on port: {0}", console);
            this.generateData();

            this.userInterface = new UserInterfaceImpl();
            this.userTransactionInterface = new UserTransactionInterfaceImpl();
            this.teleporterInterface = new TeleporterInterfaceImpl();
            this.groupInterface = new GroupInterfaceImpl();
            this.tripInterface = new TripInterfaceImpl();
            this.favouriteInterface = new FavouriteInterfaceImpl();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure AdriaH2repository");
        }
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();

        try {
            Files.deleteIfExists(Path.of("./db-04.mv.db"));
            Files.deleteIfExists(Path.of("./db-04.trace.db"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Database cleanup failed.", e);
            throw new RepositoryException("Database cleanup failed.");
        }
    }

    public void generateData() {
        try {
            executeScript("db-create.sql");
            executeScript("db-populate-users.sql");
            executeScript("db-populate-transactions.sql");
            executeScript("db-populate-teleporters.sql");
            executeScript("db-populate-groups.sql");
            executeScript("db-populate-trips.sql");
            executeScript("db-populate-settings.sql");
            executeScript("db-populate-user-permissions.sql");
            executeScript("db-populate-favourites.sql");

            // execute script inside views folder
            executeScript("views/db-history-view.sql");
            executeScript("views/db-teleporter-view.sql");
            executeScript("views/db-favourite-view.sql");

        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql)
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public void handleSQLException(String s, SQLException ex, String s1) {
        LOGGER.log(Level.SEVERE, s, ex);
        throw new RepositoryException(s1);
    }

    public List<UserTransaction> getUserTransactions(int adrianId) {
        return userTransactionInterface.getUserTransactions(adrianId);
    }

    public List<TeleporterResponseBody> getTeleporters() {
        return teleporterInterface.getTeleporters();
    }
    public Location createLocationFromResultSet(ResultSet rs, String longitudeColumn, String latitudeColumn) throws SQLException {
        String longitude = rs.getString(longitudeColumn);
        String latitude = rs.getString(latitudeColumn);

        return new Location(longitude, latitude);
    }
    public User createUserFromResultSet(ResultSet rs, String userIdColumn, String userNameColumn, String userAddressColumn, String profilePictureColumn, Location userLocation) throws SQLException {
        int userId = rs.getInt(userIdColumn);
        String userName = rs.getString(userNameColumn);
        String userAddress = rs.getString(userAddressColumn);
        String profilePicture = rs.getString(profilePictureColumn);

        return (userId != 0) ? new User(userId, userName, userAddress, userLocation, profilePicture) : null;
    }

    public List<GroupResponseBody> getGroups(int adriaId) {
        return groupInterface.getGroups(adriaId);
    }

    public boolean updateGroupMember(int groupId, int memberId) {
        return groupInterface.updateGroupMember(groupId, memberId);
    }

    public GroupResponseBody getGroup(int groupId) {
        return groupInterface.getGroup(groupId);
    }

    public void deleteGroupMembers(int groupId) {
        groupInterface.deleteGroupMembers(groupId);
    }

    public User getUser(int adrianId) {
        return userInterface.getUser(adrianId);
    }

    public List<UserHistoryResponseBody> getUserHistory(int adriaId) {
        return tripInterface.getUserHistory(adriaId);
    }

    public int addGroup(String groupName, int leaderId) {
        return groupInterface.addGroup(groupName, leaderId);
    }

    public boolean updateGroup(int groupId, String groupName) {
        return groupInterface.updateGroup(groupId, groupName);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public boolean deleteGroup(int groupId) {
        return groupInterface.deleteGroup(groupId);
    }

    public boolean addTrip(int adriaId, int from, int to, String departure, String arrival, int groupId) {
        return tripInterface.addTrip(adriaId, from, to, departure, arrival, groupId);
    }

    public List<TeleporterHistoryResponseBody> getTeleporterHistory(int adrianId, int teleporterId) {
        return tripInterface.getTeleporterHistory(adrianId, teleporterId);
    }

    public TeleporterSettings getTeleporterSettings(int teleporterId) {
        return teleporterInterface.getTeleporterSettings(teleporterId);
    }

    public boolean updateTeleporterSettings(int teleporterId, boolean visible) {
        return teleporterInterface.updateTeleporterSettings(teleporterId, visible);
    }

    public UserPermissionsResponseBody getUserPermissions(int adrianId, int teleporterId) {
        return teleporterInterface.getUserPermissions(adrianId, teleporterId);
    }

    public boolean updateUserPermissions(int adriaId, int teleporterId, boolean accessLog, boolean assingPerm, boolean controlTele, boolean manageList) {
        return teleporterInterface.updateUserPermissions(adriaId, teleporterId, accessLog, assingPerm, controlTele, manageList);
    }

    public List<FavouriteResponseBody> getFavourites(int adrianId) {
        return favouriteInterface.getFavourites(adrianId);
    }

    public boolean addFavourite(int adriaId, int teleporterId, String type, String name) {
        return favouriteInterface.addFavourite(adriaId, teleporterId, type, name);
    }

    public boolean updateFavourite(int adriaId, int teleporterId, String type, String name) {
        return favouriteInterface.updateFavourite(adriaId, teleporterId, type, name);
    }

    public boolean deleteFavourite(int adriaId, int teleporterId) {
        return favouriteInterface.deleteFavourite(adriaId, teleporterId);
    }

    public List<User> getUsers() {
        return userInterface.getUsers();
    }

    public boolean importFriends(int friendId, int groupId) {
        return groupInterface.importFriends(friendId, groupId);
    }

    public List<User> getRandomFriends(int amount, int excludedId) {
        return userInterface.getRandomFriends(amount, excludedId);
    }

    public void generateTokens() {
        userInterface.generateTokens();
    }

    public boolean deleteGroupMember(int groupId, int memberId) {
        return groupInterface.deleteGroupMember(groupId, memberId);
    }

    public int getAdrianId(String token) {
        return userInterface.getAdrianId(token);
    }

    public boolean isValidToken(String token) {
        return userInterface.isValidToken(token);
    }

    public String getTokenFromAdrianId(int adriaId) {
        return userInterface.getTokenFromAdrianId(adriaId);
    }

    public void increaseUserUsesLeft(int adriaId) {
        userInterface.increaseUserUsesLeft(adriaId);
    }
}
