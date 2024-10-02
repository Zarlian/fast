package be.howest.ti.adria.web.request;

import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class GetUsersRequest extends Request {

    private List<User> users;

    public GetUsersRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(List<User> users) {
        this.users = users;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, users);
    }
}
