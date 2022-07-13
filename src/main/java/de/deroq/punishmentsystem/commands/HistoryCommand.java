package de.deroq.punishmentsystem.commands;

import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.utils.Constants;
import de.deroq.punishmentsystem.utils.PunishmentUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author deroq
 * @since 13.07.2022
 */

public class HistoryCommand extends Command {

    private final PunishmentSystem punishmentSystem;

    public HistoryCommand(String name, PunishmentSystem punishmentSystem) {
        super(name);
        this.punishmentSystem = punishmentSystem;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("punishment.history")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("§cYou do not have permission to execute this command!"));
            return;
        }

        if(args.length != 1) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "/history <name>"));
            return;
        }

        String name = args[0];
        punishmentSystem.getPunishmentUserManager().getHistory(name).thenAcceptAsync(punishmentHistory -> {
            if(punishmentHistory == null) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler hat keine Einträge"));
                return;
            }

            if(punishmentHistory.getEntries().size() == 0) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler hat keine Einträge"));
                return;
            }

            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Einträge (Timestamp | Template | Grund | Typ | Dauer | Von)"));

            punishmentHistory.getEntries().forEach(punishmentEntry -> {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX +
                        "§e" + PunishmentUtils.formatDate(punishmentEntry.getTimestamp()) + " §3| " +
                        "§c" + punishmentEntry.getTemplate() + " §3| " +
                        "§b" + punishmentEntry.getReason() + " §3| " +
                        "§a" + punishmentEntry.getPunishmentType() + " §3| " +
                        "§e" + PunishmentUtils.formatDate(punishmentEntry.getDuration()) + " §3| " +
                        "§a" + punishmentEntry.getFrom()));
                return;
            });
        });
    }
}
