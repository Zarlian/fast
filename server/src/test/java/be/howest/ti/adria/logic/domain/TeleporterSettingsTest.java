package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeleporterSettingsTest {
    private static Teleporter teleporter;


    @BeforeAll
    static void setup(){
        Location locationTeleporter = new Location("1.111", "2.222");
        Location locationUser = new Location("3.333", "4.444");
        User user = new User(1, "name", "Fastlane 1", locationUser, "assets/images/users/fast.png");
        teleporter = new Teleporter(1, "name", "Rijselstaat 5, Brugge", locationTeleporter, "Private", user);
    }

    @Test
    void isVisible() {
        TeleporterSettings teleporterSettings = new TeleporterSettings(teleporter, true);
        assertTrue(teleporterSettings.isVisible());
    }
    @Test
void isNotVisible() {
        TeleporterSettings teleporterSettings = new TeleporterSettings(teleporter, false);
        assertFalse(teleporterSettings.isVisible());
    }
}