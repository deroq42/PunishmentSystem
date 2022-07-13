package de.deroq.punishmentsystem.listeners;

import com.google.common.cache.Cache;
import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.commands.mute.MuteCommand;
import de.deroq.punishmentsystem.models.PunishmentEntry;
import de.deroq.punishmentsystem.models.PunishmentUser;
import de.deroq.punishmentsystem.models.misc.PunishmentEntryType;
import de.deroq.punishmentsystem.models.mute.MuteMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class ChatListener implements Listener {

    private final PunishmentSystem punishmentSystem;

    public ChatListener(PunishmentSystem punishmentSystem) {
        this.punishmentSystem = punishmentSystem;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        UUID uuid = player.getUniqueId();

        if (event.getMessage().startsWith("/")) {
            return;
        }

        punishmentSystem.getPunishmentUserManager().getPunishmentUser(uuid).thenAcceptAsync(punishmentUser -> {
            if (punishmentUser == null) {
                return;
            }

            PunishmentEntry punishmentEntry = punishmentUser.getPunishmentEntry();
            if (punishmentEntry == null) {
                return;
            }

            if (!punishmentUser.getPunishmentEntry().getPunishmentType().equals(PunishmentEntryType.MUTE.toString())) {
                return;
            }

            event.setCancelled(true);
            player.sendMessage(MuteMessage.create(
                    punishmentEntry.getReason(),
                    punishmentEntry.getDuration()).toString());
        });
    }
}
