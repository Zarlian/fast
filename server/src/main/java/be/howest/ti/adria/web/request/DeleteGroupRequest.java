package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

public class DeleteGroupRequest extends Request{
    private String response;
    public DeleteGroupRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, response);
    }

    public int getGroupId() {
        return Integer.parseInt(ctx.pathParam("groupId"));
    }
}
