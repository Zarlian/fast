package be.howest.ti.adria.web.response;

import be.howest.ti.adria.logic.domain.Trip;

public class TeleporterHistoryResponseBody {
    private final String destination;
    private final String memberName;
    private final String movement;
    private final String arrival;

    public TeleporterHistoryResponseBody(Trip trip, int privateTeleporterId) {
        this.destination = trip.getTo().getName() + " (" + trip.getTo().getAddress() + ")";
        this.memberName = trip.getUser().getName();
        this.movement = determineMovement(trip, privateTeleporterId);
        this.arrival = trip.getArrival();
    }

    private String determineMovement(Trip trip, int privateTeleporterId) {
        // Based on the teleporter determine if the user is going to or from the teleporter

        int teleporterTo = trip.getTo().getId();
        int teleporterFrom = trip.getFrom().getId();

        if (teleporterTo == privateTeleporterId) {
            return "In";
        } else if (teleporterFrom == privateTeleporterId) {
            return "Out";
        } else {
            return "unknown";
        }
    }

    public String getDestination() {
        return destination;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMovement() {
        return movement;
    }

    public String getArrival() {
        return arrival;
    }
}
