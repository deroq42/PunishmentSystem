package de.deroq.punishmentsystem.commands.ban;

import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.PunishmentEntry;
import de.deroq.punishmentsystem.models.ban.BanScreen;
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

public class BtCommand extends Command {

    private final PunishmentSystem punishmentSystem;

    public BtCommand(String name, PunishmentSystem punishmentSystem) {
        super(name);
        this.punishmentSystem = punishmentSystem;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("punishment.ban")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("§cYou do not have permission to execute this command!"));
            return;
        }

        if (args.length != 2) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "/ban <player> <template>"));
            return;
        }

        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(args[0]);
        if (targetPlayer == null) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler ist nicht online"));
            return;
        }

        UUID uuid = targetPlayer.getUniqueId();
        if (targetPlayer.equals(commandSender)) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Du kannst dich nicht selber bannen"));
            return;
        }

        String template = args[1].toUpperCase();
        punishmentSystem.getPunishmentTemplateManager().getTemplate(template).thenAcceptAsync(punishmentTemplate -> {
            if (punishmentTemplate == null) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieses Template gibt es nicht"));
                return;
            }

            if(!punishmentTemplate.getPunishmentType().equals(PunishmentEntryType.BAN.toString())) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Bitte gib ein valides Ban-Template an"));
                return;
            }

            long timestamp = System.currentTimeMillis();
            long duration = (punishmentTemplate.getDuration() != -1 ? punishmentTemplate.getDuration() + timestamp : -1);
            String from = commandSender.getName();
            String reason = punishmentTemplate.getReason();
            String durationString = punishmentTemplate.getDurationString();
            String punishmentType = PunishmentEntryType.BAN.toString();

            PunishmentEntry punishmentEntry = new PunishmentEntry.builder()
                    .setTimestamp(timestamp)
                    .setTemplate(template)
                    .setReason(reason)
                    .setDuration(duration)
                    .setFrom(from)
                    .setPunishmentType(punishmentType)
                    .build();

            punishmentSystem.getPunishmentUserManager().punishUser(uuid, targetPlayer.getName(), punishmentEntry).thenAcceptAsync(alreadyBanned -> {
                if (alreadyBanned) {
                    commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler wurde bereits gebannt"));
                    return;
                }

                targetPlayer.disconnect(BanScreen.create(
                        reason,
                        duration).toString());

                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "§aSpieler wurde gebannt"));

                PunishmentUtils.getStaffMembers(commandSender.getName(), (staffMembers -> staffMembers.forEach(player -> {
                    player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "§c" + targetPlayer.getName() + " §3wurde von §a" + commandSender.getName() + " §3gebannt"));
                    player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Grund: §c" + template + "(" + reason + ")"));
                    player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dauer: §e" + durationString));
                })));

                punishmentSystem.getPunishmentUserManager().addHistoryEntry(uuid, targetPlayer.getName(), punishmentEntry);
            });
        });
    }
}
