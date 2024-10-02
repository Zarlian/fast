package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

public class UpdateGroupRequest extends Request{
    private String response;
    public UpdateGroupRequest(RoutingContext ctx) {
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
        return Integer.parseInt(ctx.request().getParam("groupId"));
    }

    public String getGroupName() {
        // Get groupName from body
        return ctx.getBodyAsJson().getString("name");
    }
}
