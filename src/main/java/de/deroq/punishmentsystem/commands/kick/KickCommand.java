package de.deroq.punishmentsystem.commands.kick;

import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.PunishmentEntry;
import de.deroq.punishmentsystem.models.kick.KickScreen;
import de.deroq.punishmentsystem.models.misc.PunishmentEntryType;
import de.deroq.punishmentsystem.utils.Constants;
import de.deroq.punishmentsystem.utils.PunishmentUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class KickCommand extends Command {

    private final PunishmentSystem punishmentSystem;

    public KickCommand(String name, PunishmentSystem punishmentSystem) {
        super(name);
        this.punishmentSystem = punishmentSystem;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("punishment.kick")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("§cYou do not have permission to execute this command!"));
            return;
        }

        if (args.length < 2) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "/kick <player> <reason>"));
            return;
        }

        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(args[0]);
        if (targetPlayer == null) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler ist nicht online"));
            return;
        }

        UUID uuid = targetPlayer.getUniqueId();
        if (targetPlayer.equals(commandSender)) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Du kannst dich nicht selber kicken"));
            return;
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            reasonBuilder.append(" ").append(args[i]);
        }

        String reason = reasonBuilder.toString().trim();
        targetPlayer.disconnect(KickScreen.create(reason).toString());

        PunishmentUtils.getStaffMembers(commandSender.getName(), (staffMembers -> staffMembers.forEach(player -> {
            player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "§c" + targetPlayer.getName() + " §3wurde von §a" + commandSender.getName() + " §3gekickt"));
            player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Grund: §c" + reason));
        })));

        long timestamp = System.currentTimeMillis();
        String from = commandSender.getName();
        String punishmentType = PunishmentEntryType.KICK.toString();

        PunishmentEntry punishmentEntry = new PunishmentEntry.builder()
                .setTimestamp(timestamp)
                .setPunishmentType(punishmentType)
                .setFrom(from)
                .setReason(reason)
                .build();

        punishmentSystem.getPunishmentUserManager().addHistoryEntry(uuid, targetPlayer.getName(), punishmentEntry);
    }
}
