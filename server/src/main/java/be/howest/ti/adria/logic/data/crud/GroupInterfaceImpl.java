package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.data.H2Repository;
import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.Group;
import be.howest.ti.adria.logic.domain.Location;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.exceptions.RepositoryException;
import be.howest.ti.adria.web.response.GroupResponseBody;

import static be.howest.ti.adria.logic.data.dbenums.ColumnNames.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static be.howest.ti.adria.logic.data.dbenums.Queries.*;

public class GroupInterfaceImpl implements GroupInterface {
    private static final Supplier<H2Repository> repositorySupplier = Repositories::getH2Repo;
    private static final Supplier<Connection> connectionSupplier = () -> {
        try {
            return repositorySupplier.get().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize H2Repository", e);
        }
    };

    public List<GroupResponseBody> getGroups(int adriaId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        List<Group> groups = new ArrayList<>();
        try (connection;
             PreparedStatement stmt = connection.prepareStatement(GET_GROUPS.getQuery())) {

            stmt.setInt(1, adriaId);
            try (ResultSet rs = stmt.executeQuery()) {
                int groupId = 0;
                int groupListCurrentIndex = -1;

                while (rs.next()) {
                    Location locationMember = repository.createLocationFromResultSet(rs, MEMBER_LOCATION_LON.name(), MEMBER_LOCATION_LAT.name());
                    User member =repository.createUserFromResultSet(rs, MEMBER_ID.name(), MEMBER_NAME.name(), MEMBER_ADDRESS.name(), MEMBER_PROFILE_PICTURE.name(), locationMember);

                    // If there is a new group, create a new group
                    if (groupId != rs.getInt(GROUP_ID.name()) || member == null) {
                        groupId = rs.getInt(GROUP_ID.name());

                        Location leaderLocation = Repositories.getH2Repo().createLocationFromResultSet(rs, LEADER_LOCATION_LON.name(), LEADER_LOCATION_LAT.name());
                        User leader = Repositories.getH2Repo().createUserFromResultSet(rs, LEADER_ID.name(), LEADER_NAME.name(), LEADER_ADDRESS.name(), LEADER_PROFILE_PICTURE.name(), leaderLocation);

                        Group group = new Group(groupId, rs.getString(GROUP_NAME.name()), leader);
                        groups.add(group);
                        groupListCurrentIndex++;
                    }

                    if(member != null) {
                        groups.get(groupListCurrentIndex).addMember(member);
                    }
                }
                return createGroupResponseBodyList(groups);
            }
        } catch (SQLException ex) {
            repository.handleSQLException("Failed to get groups.", ex, "Could not get groups.");
            return Collections.emptyList();
        }
    }

    private List<GroupResponseBody> createGroupResponseBodyList(List<Group> groups) {
        List<GroupResponseBody> groupResponseBodies = new ArrayList<>();
        for (Group group : groups) {
            groupResponseBodies.add(new GroupResponseBody(group));
        }
        return groupResponseBodies;
    }

    public boolean updateGroupMember(int groupId, int memberId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        try (connection;
             PreparedStatement stmt = connection.prepareStatement(UPDATE_GROUP_MEMBER.getQuery())) {

            stmt.setInt(1, groupId);
            stmt.setInt(2, memberId);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if any rows were affected
            if (rowsAffected == 0) {
                throw new RepositoryException("Member is not in the group.");
            }

            return true;

        } catch (SQLException ex) {
            repository.handleSQLException("Failed to update group member.", ex, "Could not update group member.");
            return false;
        }
    }

    public GroupResponseBody getGroup(int groupId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        try (connection;
             PreparedStatement stmt = connection.prepareStatement(GET_GROUP.getQuery())) {

            stmt.setInt(1, groupId);

            try (ResultSet rs = stmt.executeQuery()) {
                // Check if there is a group
                boolean createdGroup = false;
                Group group = null;

                while (rs.next()) {
                    if (!createdGroup) {
                        Location leaderLocation = new Location(rs.getString(LEADER_LOCATION_LON.name()), rs.getString(LEADER_LOCATION_LAT.name()));
                        User leader = new User(rs.getInt(LEADER_ID.name()), rs.getString(LEADER_NAME.name()), rs.getString(LEADER_ADDRESS.name()), leaderLocation, rs.getString(LEADER_PROFILE_PICTURE.name()));

                        group = new Group(rs.getInt(GROUP_ID.name()), rs.getString(GROUP_NAME.name()), leader);
                        createdGroup = true;
                    }

                    // Create and add members to the group
                    Location memberLocation = new Location(rs.getString(MEMBER_LOCATION_LON.name()), rs.getString(MEMBER_LOCATION_LAT.name()));
                    User member = new User(rs.getInt(MEMBER_ID.name()), rs.getString(MEMBER_NAME.name()), rs.getString(MEMBER_ADDRESS.name()), memberLocation,  rs.getString(MEMBER_PROFILE_PICTURE.name()));

                    if (member.getAdriaId() != 0) {
                        group.addMember(member);
                    }
                }
                if (group != null) {
                    return new GroupResponseBody(group);
                } else {
                    return null;
                }

            } catch (SQLException ex) {
                repository.handleSQLException("Failed to get group.", ex, "Could not get group.");
                return null;
            }

        } catch (SQLException ex) {
            repository.handleSQLException("Failed to get group.", ex, "Could not get group.");
            return null;
        }
    }

    public void deleteGroupMembers(int groupId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        try (
                connection;
                PreparedStatement stmt = connection.prepareStatement(DELETE_GROUP_MEMBERS.getQuery())
        ) {
            stmt.setInt(1, groupId);
            stmt.execute();
        } catch (SQLException ex) {
            repository.handleSQLException("Failed to delete group members.", ex, "Could not delete group members.");
        }
    }

    public void deleteGroupTrips(int groupId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        try (
                connection;
                PreparedStatement stmt = connection.prepareStatement(DELETE_GROUP_TRIPS.getQuery())
        ) {
            stmt.setInt(1, groupId);
            stmt.execute();
        } catch (SQLException ex) {
            repository.handleSQLException("Failed to delete group trips.", ex, "Could not delete group trips.");
        }
    }

    public int addGroup(String groupName, int leaderId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        try (connection;
             PreparedStatement stmt = connection.prepareStatement(ADD_GROUP.getQuery(), Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, groupName);
            stmt.setInt(2, leaderId);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if any rows were affected
            if (rowsAffected == 0) {
                throw new RepositoryException("Creating group failed, no rows affected.");
            }

            // Get the generated key
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new RepositoryException("Creating group failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            repository.handleSQLException("Failed to add group.", ex, "Could not add group.");
            return -1;
        }
    }

    public boolean updateGroup(int groupId, String groupName) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        try (connection;
             PreparedStatement stmt = connection.prepareStatement(UPDATE_GROUP.getQuery())) {

            stmt.setString(1, groupName);
            stmt.setInt(2, groupId);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if any rows were affected
            return rowsAffected != 0;

        } catch (SQLException ex) {
            repository.handleSQLException("Failed to update group.", ex, "Could not update group.");
        }

        return false;
    }

    public boolean deleteGroup(int groupId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        deleteGroupMembers(groupId);
        deleteGroupTrips(groupId);

        try (connection;
             PreparedStatement stmt = connection.prepareStatement(DELETE_GROUP.getQuery())) {

            stmt.setInt(1, groupId);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if any rows were affected
            return rowsAffected != 0;

        } catch (SQLException ex) {
            repository.handleSQLException("Failed to delete group.", ex, "Could not delete group.");
        }

        return false;
    }

    public boolean importFriends(int friendId, int groupId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        try (connection;
             PreparedStatement stmt = connection.prepareStatement(IMPORT_FRIENDS.getQuery())) {

            stmt.setInt(1, groupId);
            stmt.setInt(2, friendId);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if any rows were affected
            return rowsAffected != 0;

        } catch (SQLException ex) {
            repository.handleSQLException("Failed to import friends.", ex, "Could not import friends.");
        }

        return false;
    }

    public boolean deleteGroupMember(int groupId, int memberId) {
        Connection connection = connectionSupplier.get();
        H2Repository repository = repositorySupplier.get();

        try (connection;
             PreparedStatement stmt = connection.prepareStatement(DELETE_GROUP_MEMBER.getQuery())) {

            stmt.setInt(1, groupId);
            stmt.setInt(2, memberId);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if any rows were affected
            return rowsAffected != 0;

        } catch (SQLException ex) {
            repository.handleSQLException("Failed to delete group member.", ex, "Could not delete group member.");
        }

        return false;
    }
}
