package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import be.howest.ti.adria.web.response.UserHistoryResponseBody;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class GetUserHistoryRequest extends Request {
    List<UserHistoryResponseBody> trips;
    public GetUserHistoryRequest(RoutingContext ctx) {
        super(ctx);
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    public void setResponse(List<UserHistoryResponseBody> trips) {
        this.trips = trips;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, trips);
    }
}
