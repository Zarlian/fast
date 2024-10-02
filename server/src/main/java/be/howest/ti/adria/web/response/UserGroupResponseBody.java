package be.howest.ti.adria.web.response;

import be.howest.ti.adria.logic.domain.Location;
import be.howest.ti.adria.logic.domain.User;

public class UserGroupResponseBody {
    private final int id;
    private final String name;
    private final Location location;
    private final String profilePicture;

    public UserGroupResponseBody(User user) {
        this.id = user.getAdriaId();
        this.name = user.getName();
        this.location = user.getLocation();
        this.profilePicture = user.getProfilePicture();
    }

    public int getAdriaId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
