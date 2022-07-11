package de.deroq.punishmentsystem.models.ban;

import de.deroq.punishmentsystem.utils.PunishmentUtils;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class BanScreen {

    private final String reason;
    private final long duration;

    private BanScreen(String reason, long duration) {
        this.reason = reason;
        this.duration = duration;
    }

    public static BanScreen create(String reason, long duration) {
        return new BanScreen(reason, duration);
    }

    @Override
    public String toString() {
        return "§3Du wurdest vom Server gebannt\n" +
                "§3Grund: §c" + reason + "\n" +
                "§3Dauer: §e" + PunishmentUtils.formatDate(duration) + "\n" +
                "\n" +
                "§aDu kannst unter guardian.gommehd.net einen Entbannungsantrag stellen";
    }
}
