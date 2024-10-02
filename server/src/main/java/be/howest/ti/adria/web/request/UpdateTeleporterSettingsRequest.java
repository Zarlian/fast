package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

public class UpdateTeleporterSettingsRequest extends Request{
    private String response;
    public UpdateTeleporterSettingsRequest(RoutingContext ctx) {
        super(ctx);
    }

    public int getTeleporterId() {
        return Integer.parseInt(ctx.request().getParam("teleporterId"));
    }

    public boolean getVisibility() {
        return Boolean.parseBoolean(ctx.getBodyAsJson().getString("visible"));
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, response);
    }
}
