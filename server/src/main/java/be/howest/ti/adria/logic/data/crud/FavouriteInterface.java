package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.web.response.FavouriteResponseBody;

import java.util.List;

public interface FavouriteInterface {
    List<FavouriteResponseBody> getFavourites(int adriaId);

    boolean addFavourite(int adriaId, int teleporterId, String type, String name);

    boolean updateFavourite(int adriaId, int teleporterId, String type, String name);

    boolean deleteFavourite(int adriaId, int teleporterId);
}
