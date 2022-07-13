package de.deroq.punishmentsystem.commands.mute;

import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.PunishmentEntry;
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

public class MuteCommand extends Command {

    private final PunishmentSystem punishmentSystem;

    public MuteCommand(String name, PunishmentSystem punishmentSystem) {
        super(name);
        this.punishmentSystem = punishmentSystem;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("punishment.mute")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("§cYou do not have permission to execute this command!"));
            return;
        }

        if (args.length < 3) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "/mute <player> <duration> <reason>"));
            return;
        }

        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(args[0]);
        if (targetPlayer == null) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler ist nicht online"));
            return;
        }

        UUID uuid = targetPlayer.getUniqueId();
        if (targetPlayer.equals(commandSender)) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Du kannst dich nicht selber muten"));
            return;
        }

        String durationString = args[1].toLowerCase();
        long duration = PunishmentUtils.validateDuration(durationString);

        if(duration == 0) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Bitte gib eine valide Dauer an, Beispiel: 15m, 5h, 30d"));
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Folgende Units gibt es: m-Minuten, h-Stunden, d-Tage, permanent"));
            return;
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for(int i = 2; i < args.length; i++) {
            reasonBuilder.append(" ").append(args[i]);
        }

        long timestamp = System.currentTimeMillis();
        String from = commandSender.getName();
        String punishmentType = PunishmentEntryType.MUTE.toString();
        String reason = reasonBuilder.toString().trim();
        duration = (duration != -1 ? duration + timestamp : duration);

        PunishmentEntry punishmentEntry = new PunishmentEntry.builder()
                .setTimestamp(timestamp)
                .setReason(reason)
                .setDuration(duration)
                .setFrom(from)
                .setPunishmentType(punishmentType)
                .build();

        punishmentSystem.getPunishmentUserManager().punishUser(uuid, targetPlayer.getName(), punishmentEntry).thenAcceptAsync(alreadyMuted -> {
            if (alreadyMuted) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler wurde bereits gemuted"));
                return;
            }

            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "§aSpieler wurde gemuted"));

            PunishmentUtils.getStaffMembers(commandSender.getName(), (staffMembers -> staffMembers.forEach(player -> {
                player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "§c" + targetPlayer.getName() + " §3wurde von §a" + commandSender.getName() + " §3gemuted"));
                player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Grund: §c" + reason));
                player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dauer: §e" + durationString));
            })));

            punishmentSystem.getPunishmentUserManager().addHistoryEntry(uuid, targetPlayer.getName(), punishmentEntry);
        });
    }
}
