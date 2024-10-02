package be.howest.ti.adria.web.request;

import be.howest.ti.adria.logic.domain.TeleporterSettings;
import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

public class GetTeleporterSettingsRequest extends Request{

    private TeleporterSettings teleporterSettings;

    public GetTeleporterSettingsRequest(RoutingContext ctx) {
        super(ctx);
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, teleporterSettings);
    }

    public int getTeleporterId() {
        return Integer.parseInt(ctx.request().getParam("teleporterId"));
    }

    public void setResponse(TeleporterSettings teleporterSettings) {
        this.teleporterSettings = teleporterSettings;
    }
}
