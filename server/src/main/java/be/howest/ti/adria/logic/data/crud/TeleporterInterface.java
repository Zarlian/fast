package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.logic.domain.TeleporterSettings;
import be.howest.ti.adria.web.response.TeleporterResponseBody;
import be.howest.ti.adria.web.response.UserPermissionsResponseBody;

import java.util.List;

public interface TeleporterInterface {
    List<TeleporterResponseBody> getTeleporters();

    TeleporterSettings getTeleporterSettings(int teleporterId);

    boolean updateTeleporterSettings(int teleporterId, boolean visible);

    UserPermissionsResponseBody getUserPermissions(int adrianId, int teleporterId);

    boolean updateUserPermissions(int adriaId, int teleporterId, boolean accessLog, boolean assingPerm, boolean controlTele, boolean manageList);
}
