package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import be.howest.ti.adria.web.response.GroupResponseBody;
import io.vertx.ext.web.RoutingContext;

public class GetGroupRequest extends Request {
    private GroupResponseBody group;

    public GetGroupRequest(RoutingContext ctx) {
        super(ctx);
    }

    public int getGroupId() {
        return Integer.parseInt(ctx.pathParam("groupId"));
    }

    public void setResponse(GroupResponseBody group) {
        this.group = group;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, group);
    }
}
