package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

public class UpdateUserPermissionsRequest extends Request{
    private String response;
    public UpdateUserPermissionsRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    public int getTeleporterId() {
        return Integer.parseInt(ctx.request().getParam("teleporterId"));
    }

    public boolean getAccessLogs() {
        // from body
        return Boolean.parseBoolean(ctx.getBodyAsJson().getString("can_access_all_logs"));
    }

    public boolean getAssignPermissions() {
        // from body
        return Boolean.parseBoolean(ctx.getBodyAsJson().getString("can_assign_permissions"));
    }

    public boolean getControlTeleporter() {
        // from body
        return Boolean.parseBoolean(ctx.getBodyAsJson().getString("can_control_teleporter"));
    }

    public boolean getManageList() {
        // from body
        return Boolean.parseBoolean(ctx.getBodyAsJson().getString("can_manage_list"));
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, response);
    }
}
