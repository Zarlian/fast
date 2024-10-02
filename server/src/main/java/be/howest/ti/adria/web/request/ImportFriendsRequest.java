package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

public class ImportFriendsRequest extends Request {
    private String response;

    public ImportFriendsRequest(RoutingContext ctx) {
        super(ctx);
    }

    // send response
    public void sendResponse() {
        Response.sendJson(ctx, 200, response);
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
