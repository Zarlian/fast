package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

public class DeleteFavouriteRequest extends Request {
    private String response;
    public DeleteFavouriteRequest(RoutingContext ctx) {
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

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, response);
    }
}
