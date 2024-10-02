package be.howest.ti.adria.web.request;

import be.howest.ti.adria.web.bridge.Response;
import be.howest.ti.adria.web.response.FavouriteResponseBody;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class GetFavouritesRequest extends Request {
    private List<FavouriteResponseBody> favourites;
    public GetFavouritesRequest(RoutingContext ctx) {
        super(ctx);
    }

    public void setResponse(List<FavouriteResponseBody> favourites) {
        this.favourites = favourites;
    }

    public int getAdriaId() {
        return Integer.parseInt(ctx.request().getParam("adriaId"));
    }

    @Override
    public void sendResponse() {
        Response.sendJson(ctx, 200, favourites);
    }
}
