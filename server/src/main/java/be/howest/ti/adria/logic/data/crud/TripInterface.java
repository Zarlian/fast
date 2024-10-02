package be.howest.ti.adria.logic.data.crud;

import be.howest.ti.adria.web.response.TeleporterHistoryResponseBody;
import be.howest.ti.adria.web.response.UserHistoryResponseBody;

import java.util.List;

public interface TripInterface {
    List<UserHistoryResponseBody> getUserHistory(int adriaId);

    boolean addTrip(int adriaId, int from, int to, String departure, String arrival, int groupId);

    List<TeleporterHistoryResponseBody> getTeleporterHistory(int adrianId, int teleporterId);
}
