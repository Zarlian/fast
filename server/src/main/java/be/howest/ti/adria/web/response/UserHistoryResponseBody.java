package be.howest.ti.adria.web.response;

import be.howest.ti.adria.logic.domain.Trip;

public class UserHistoryResponseBody {
    private final String destination;
    private final String arrival;

    public UserHistoryResponseBody(Trip trip) {
        this.destination = trip.getTo().getName() + " (" + trip.getTo().getAddress() + ")";
        this.arrival = trip.getArrival();
    }

    public String getDestination() {
        return destination;
    }

    public String getArrival() {
        return arrival;
    }
}
