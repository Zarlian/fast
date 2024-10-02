package be.howest.ti.adria.web.response;

import be.howest.ti.adria.logic.domain.Teleporter;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.UserPermissions;

public class UserPermissionsResponseBody {
    private final int userId;
    private final int teleporterId;
    private final boolean canAccessAllLogs;
    private final boolean canAssignPermissions;
    private final boolean canControlTeleporter;
    private final boolean canManageLists;

    public UserPermissionsResponseBody(UserPermissions userPermissions) {
        User user = userPermissions.getUser();
        Teleporter teleporter = userPermissions.getTeleporter();

        this.userId = user.getAdriaId();
        this.teleporterId = teleporter.getId();
        this.canAccessAllLogs = userPermissions.isCanAccessLogs();
        this.canAssignPermissions = userPermissions.isCanAssignPermissions();
        this.canControlTeleporter = userPermissions.isCanControlTeleporter();
        this.canManageLists = userPermissions.isCanManageLists();
    }

    public int getAdriaId() {
        return userId;
    }

    public int getTeleporterId() {
        return teleporterId;
    }

    public boolean isCanAccessAllLogs() {
        return canAccessAllLogs;
    }

    public boolean isCanAssignPermissions() {
        return canAssignPermissions;
    }

    public boolean isCanControlTeleporter() {
        return canControlTeleporter;
    }

    public boolean isCanManageLists() {
        return canManageLists;
    }
}
