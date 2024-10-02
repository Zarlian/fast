package be.howest.ti.adria.web.response;

import be.howest.ti.adria.logic.domain.Favourite;

public class FavouriteResponseBody {
    private final int teleporterId;
    private final String name;
    private final String type;

    public FavouriteResponseBody(Favourite favourite) {
        this.teleporterId = favourite.getTeleporter().getId();
        this.name = favourite.getName();
        this.type = favourite.getType();
    }

    public int getTeleporterId() {
        return teleporterId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
