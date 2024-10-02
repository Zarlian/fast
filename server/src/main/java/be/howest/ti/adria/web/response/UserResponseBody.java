package be.howest.ti.adria.web.response;

import be.howest.ti.adria.logic.domain.User;

public class UserResponseBody extends User {
    private final boolean canManage;

    public UserResponseBody(User user, boolean canManage) {
        super(user.getAdriaId(), user.getName(), user.getAddress(), user.getLocation(), user.getProfilePicture());
        this.canManage = canManage;
    }

    public boolean getCanManage() {
        return canManage;
    }
}
