package de.deroq.punishmentsystem.commands;

import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.misc.PunishmentEntryType;
import de.deroq.punishmentsystem.utils.Constants;
import de.deroq.punishmentsystem.utils.PunishmentUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class TemplateCommand extends Command {

    private final PunishmentSystem punishmentSystem;

    public TemplateCommand(String name, PunishmentSystem punishmentSystem) {
        super(name);
        this.punishmentSystem = punishmentSystem;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("punishment.templates")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("§cYou do not have permission to execute this command!"));
            return;
        }

        if (args.length >= 5) {
            if (!args[0].equalsIgnoreCase("create")) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "/template create <id> <duration> <type> <reason>"));
                return;
            }

            String id = args[1].toUpperCase();
            String durationString = args[2].toLowerCase();
            long duration = PunishmentUtils.validateDuration(durationString);

            if(duration == 0) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Bitte gib eine valide Dauer an, Beispiel: 15m, 5h, 30d"));
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Folgende Units gibt es: m-Minuten, h-Stunden, d-Tage, permanent"));
                return;
            }

            String type = args[3].toUpperCase();
            if (!EnumUtils.isValidEnum(PunishmentEntryType.class, type)) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Bitte gib einen validen Type an: " + Arrays.toString(PunishmentEntryType.values())));
                return;
            }

            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 4; i < args.length; i++) {
                reasonBuilder.append(" ").append(args[i]);
            }

            String reason = reasonBuilder.toString().trim();

            punishmentSystem.getPunishmentTemplateManager().createTemplate(id, duration, durationString, type, reason).thenAcceptAsync(exists -> {
                if (exists) {
                    commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieses Template gibt es bereits"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Template wurde erstellt"));
            });

            return;
        }

        switch (args.length) {
            case 1:
                switch (args[0].toUpperCase()) {
                    case "LIST":
                        punishmentSystem.getPunishmentTemplateManager().listTemplates().thenAcceptAsync(templates -> {
                            if (templates.size() == 0) {
                                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Es konnten keine Templates gefunden werden"));
                                return;
                            }

                            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Templateliste (ID | Dauer | Typ | Grund)"));
                            templates.forEach(banTemplate -> commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX +
                                    "§c" + banTemplate.getTuid() + " §3| " +
                                    "§e" + banTemplate.getDurationString() + " §3| " +
                                    "§a" + banTemplate.getPunishmentType() + " §3| " +
                                    "§b" + banTemplate.getReason())));
                        });
                        break;

                    case "DELETEALL":
                        punishmentSystem.getPunishmentTemplateManager().listTemplates().thenAcceptAsync(banTemplates -> {
                            banTemplates.forEach(banTemplate -> punishmentSystem.getPunishmentTemplateManager().deleteTemplate(banTemplate.getTuid()));
                            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Alle Templates wurden gelöscht"));
                        });
                        break;
                }
                break;

            case 2:
                if (args[0].equalsIgnoreCase("delete")) {
                    String id = args[1].toUpperCase();

                    punishmentSystem.getPunishmentTemplateManager().deleteTemplate(id).thenAcceptAsync(doesntExist -> {
                        if (doesntExist) {
                            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieses Template gibt es nicht"));
                            return;
                        }

                        commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Template wurde gelöscht"));
                    });
                } else {
                    commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "/template delete <id>"));
                }
                break;
            default:
                break;
        }
    }
}
