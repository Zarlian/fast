package be.howest.ti.adria.logic.domain;

public class TeleporterSettings {
    private final boolean visible;
    private final User onwer;

    public TeleporterSettings(Teleporter teleporter, boolean visible) {
        this.visible = visible;
        this.onwer = teleporter.getOwner();
    }

    public boolean isVisible() {
        return visible;
    }

    public int getOwnerId() {
        return onwer.getAdriaId();
    }
}
