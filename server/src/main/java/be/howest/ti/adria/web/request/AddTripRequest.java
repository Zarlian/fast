package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import io.vertx.ext.web.RoutingContext;

public class AddTripRequest extends Request {
    private String response;

    public AddTripRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    // Get from teleporter from body
    public int getFrom() {
        return Integer.parseInt(ctx.getBodyAsJson().getString("from"));
    }

    public int getTo() {
        return Integer.parseInt(ctx.getBodyAsJson().getString("to"));
    }

    public String getDeparture() {
        return ctx.getBodyAsJson().getString("departure");
    }

    public String getArrival() {
        return ctx.getBodyAsJson().getString("arrival");
    }

    public int getGroup() {

        // group can be null
        if (ctx.getBodyAsJson().getString("group") == null) {
            return 0;
        }

        return Integer.parseInt(ctx.getBodyAsJson().getString("group"));
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, response);
    }

    public String getType() {
        return ctx.getBodyAsJson().getString("type");
    }
}
