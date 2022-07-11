package de.deroq.punishmentsystem.models.mute;

import de.deroq.punishmentsystem.utils.PunishmentUtils;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class MuteMessage {

    private final String reason;
    private final long duration;

    private MuteMessage(String reason, long duration) {
        this.reason = reason;
        this.duration = duration;
    }

    public static MuteMessage create(String reason, long duration) {
        return new MuteMessage(reason, duration);
    }

    @Override
    public String toString() {
        return "§3Du wurdest aus dem Chat gesperrt\n" +
                "§3Grund: §c" + reason + "\n" +
                "§3Dauer: §e" + PunishmentUtils.formatDate(duration) + "\n" +
                "\n" +
                "§aDu kannst unter guardian.gommehd.net einen Entbannungsantrag stellen";
    }
}
