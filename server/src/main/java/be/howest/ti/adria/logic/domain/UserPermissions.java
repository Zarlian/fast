package be.howest.ti.adria.logic.domain;

public class UserPermissions {
    private final User user;
    private final Teleporter teleporter;
    private final boolean canAccessLogs;
    private final boolean canManageLists;
    private final boolean canAssignPermissions;
    private final boolean canControlTeleporter;

    public UserPermissions(User user, Teleporter teleporter,
                           boolean canAccessLogs, boolean canManageLists,
                           boolean canAssignPermissions, boolean canControlTeleporter) {
        this.user = user;
        this.teleporter = teleporter;
        this.canAccessLogs = canAccessLogs;
        this.canManageLists = canManageLists;
        this.canAssignPermissions = canAssignPermissions;
        this.canControlTeleporter = canControlTeleporter;
    }

    public User getUser() {
        return user;
    }

    public Teleporter getTeleporter() {
        return teleporter;
    }

    public boolean isCanAccessLogs() {
        return canAccessLogs;
    }

    public boolean isCanManageLists() {
        return canManageLists;
    }

    public boolean isCanAssignPermissions() {
        return canAssignPermissions;
    }

    public boolean isCanControlTeleporter() {
        return canControlTeleporter;
    }
}
