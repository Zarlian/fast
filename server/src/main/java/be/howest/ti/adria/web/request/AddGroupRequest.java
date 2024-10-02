package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;


import java.util.List;
import java.util.Map;

public class AddGroupRequest extends Request{
    private Map<String, Integer> response;

    public AddGroupRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(int groupId) {
        this.response = Map.of("ID", groupId);
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, response);
    }

    // Get groupName from request body
    public String getGroupName() {
        return ctx.getBodyAsJson().getString("name");
    }

    // Get membersId from request body
    public List<Integer> getMembers() {
        // getJsonArray returns a JsonArray, which is a list of objects
        // We need to convert this list of objects to a list of integers
        return ctx.getBodyAsJson().getJsonArray("members").getList();
    }

    // Get leaderId from request body
    public int getLeader() {
        return ctx.getBodyAsJson().getInteger("leader");
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    public int getLeaderId() {
        return Integer.parseInt(ctx.getBodyAsJson().getString("leader"));
    }
}
