package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import be.howest.ti.adria.web.response.TeleporterResponseBody;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class GetTeleportersRequest extends Request{

    List<TeleporterResponseBody> teleporters;

    public GetTeleportersRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(List<TeleporterResponseBody> teleporters) {
        this.teleporters = teleporters;
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, teleporters);
    }
}
