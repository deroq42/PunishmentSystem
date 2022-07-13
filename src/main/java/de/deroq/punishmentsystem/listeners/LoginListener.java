package de.deroq.punishmentsystem.listeners;

import com.google.common.cache.Cache;
import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.PunishmentEntry;
import de.deroq.punishmentsystem.models.PunishmentUser;
import de.deroq.punishmentsystem.models.ban.BanScreen;
import de.deroq.punishmentsystem.models.misc.PunishmentEntryType;
import jdk.Exported;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class LoginListener implements Listener {

    private final PunishmentSystem punishmentSystem;

    public LoginListener(PunishmentSystem punishmentSystem) {
        this.punishmentSystem = punishmentSystem;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        UUID uuid = event.getConnection().getUniqueId();

        PunishmentUser bannedUser = punishmentSystem.getPunishmentUserManager().getPunishmentUser(uuid).join();
        if (bannedUser == null) {
            return;
        }

        if (bannedUser.getPunishmentEntry() == null) {
            return;
        }

        if (!bannedUser.getPunishmentEntry().getPunishmentType().equals(PunishmentEntryType.BAN.toString())) {
            return;
        }

        PunishmentEntry punishmentEntry = bannedUser.getPunishmentEntry();
        event.setCancelled(true);
        event.setCancelReason(BanScreen.create(
                        punishmentEntry.getReason(),
                        punishmentEntry.getDuration()).toString());
    }
}
