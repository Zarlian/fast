package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UpdateGroupMemberRequest extends Request{

    private String response;

    Logger logger = Logger.getLogger(UpdateGroupMemberRequest.class.getName());
    public UpdateGroupMemberRequest(RoutingContext ctx) {
        super(ctx);
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.pathParam("adriaId"));
    }

    public int getGroupId() {
        return Integer.parseInt(ctx.pathParam("groupId"));
    }

    public List<Integer> getMembersId() {
        // use ctx to get the body of the request

        // use ctx.getBodyAsJson() to get the body as a JsonObject
        List<Integer> membersId = new ArrayList<>();

        // loop over ctx.getBodyAsJson().getJsonArray("membersId") and add each member to the membersId list
        for (Object member : ctx.getBodyAsJson().getJsonArray("IDs")) {
            membersId.add((Integer) member);
        }

        return membersId;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, response);
    }
}
