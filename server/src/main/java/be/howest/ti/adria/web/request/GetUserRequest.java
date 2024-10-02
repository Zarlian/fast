package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import be.howest.ti.adria.web.response.UserResponseBody;
import io.vertx.ext.web.RoutingContext;

public class GetUserRequest extends Request {

    private UserResponseBody user;

    public GetUserRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(UserResponseBody user) {
        this.user = user;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, user);
    }
}


