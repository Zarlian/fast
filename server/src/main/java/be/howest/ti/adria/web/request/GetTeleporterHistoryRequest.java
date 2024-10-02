package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import be.howest.ti.adria.web.response.TeleporterHistoryResponseBody;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class GetTeleporterHistoryRequest extends Request{

    private List<TeleporterHistoryResponseBody> teleporterHistories;

    public GetTeleporterHistoryRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(List<TeleporterHistoryResponseBody> teleporterHistories) {
        this.teleporterHistories = teleporterHistories;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, teleporterHistories);
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    public int getTeleporterId() {
        return Integer.parseInt(ctx.request().getParam("teleporterId"));
    }
}
