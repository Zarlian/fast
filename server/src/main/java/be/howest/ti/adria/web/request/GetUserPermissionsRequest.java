package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import be.howest.ti.adria.web.response.UserPermissionsResponseBody;
import io.vertx.ext.web.RoutingContext;

public class GetUserPermissionsRequest extends Request{
    private UserPermissionsResponseBody userPermissions;

    public GetUserPermissionsRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(UserPermissionsResponseBody userPermissions) {
        this.userPermissions = userPermissions;
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    public int getTeleporterId() {
        return Integer.parseInt(ctx.request().getParam("teleporterId"));
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, userPermissions);
    }

}
