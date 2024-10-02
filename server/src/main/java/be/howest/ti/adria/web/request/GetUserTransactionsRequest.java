package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

public class GetUserTransactionsRequest extends Request {
    private Map<String, Integer> usesLeft;
    public GetUserTransactionsRequest(RoutingContext ctx) {
        super(ctx);
    }

    public int getAdriaId() {
        return params.pathParameter("adriaId").getInteger();
    }

    public void setResponse(Map<String, Integer> usesLeft) {
        this.usesLeft = usesLeft;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, usesLeft);
    }
}
