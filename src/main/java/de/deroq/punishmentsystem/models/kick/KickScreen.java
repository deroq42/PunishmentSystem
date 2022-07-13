package de.deroq.punishmentsystem.models.kick;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class KickScreen {

    private final String reason;

    private KickScreen(String reason) {
        this.reason = reason;
    }

    public static KickScreen create(String reason) {
        return new KickScreen(reason);
    }

    @Override
    public String toString() {
        return "§3Du wurdest vom Server gekickt\n" +
                "§3Grund: §c" + reason;
    }
}
