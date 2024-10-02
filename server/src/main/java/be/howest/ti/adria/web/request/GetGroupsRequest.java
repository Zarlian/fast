package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import be.howest.ti.adria.web.response.GroupResponseBody;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

public class GetGroupsRequest extends Request{
    List<GroupResponseBody> groups = new ArrayList<>();
    public GetGroupsRequest(RoutingContext ctx) {
        super(ctx);
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    public void setResponse(List<GroupResponseBody> groups) {
        this.groups = groups;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, groups);
    }
}
