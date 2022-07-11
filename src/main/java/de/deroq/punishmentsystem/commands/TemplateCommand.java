package de.deroq.punishmentsystem.commands;

import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.misc.PunishmentType;
import de.deroq.punishmentsystem.utils.Constants;
import de.deroq.punishmentsystem.utils.DurationUtil;
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
        if (!commandSender.hasPermission("punishment.template")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("§cDazu hast du keine Rechte!"));
            return;
        }

        if (args.length >= 5) {
            if (!args[0].equalsIgnoreCase("create")) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "/template create <id> <duration> <type> <reason>"));
                return;
            }

            String id = args[1].toUpperCase();
            String durationString = args[2].toLowerCase();
            String type = args[3].toUpperCase();
            StringBuilder reason = new StringBuilder();
            DurationUtil durationUtil;
            long duration;

            if (durationString.equals("permanent")) {
                duration = -1;
            } else {
                if (durationString.length() != 2) {
                    commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Bitte gib eine valide Dauer an, sie darf nur aus zwei Zeichen bestehen. Beispiel: 3d, 4w, 3m"));
                    return;
                }

                char unit = durationString.charAt(1);
                if (!PunishmentUtils.isUnitValid(durationString, unit)) {
                    commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Bitte gib eine valide Unit an: h-Stunden, d-Tage, w-Wochen, m-Monate, y-Jahre, permanent"));
                    return;
                }

                durationUtil = DurationUtil.getDurationUtilByUnit(unit).get();
                duration = durationUtil.getDuration() * Character.digit(durationString.charAt(0), 24);
            }

            if (!EnumUtils.isValidEnum(PunishmentType.class, type)) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Bitte gib einen validen Type an: " + Arrays.toString(PunishmentType.values())));
                return;
            }

            for (int i = 4; i < args.length; i++) {
                reason.append(" ").append(args[i]);
            }

            punishmentSystem.getPunishmentTemplateManager().createTemplate(id, duration, durationString, type, reason.toString().trim()).thenAcceptAsync(exists -> {
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
                }
                break;
            default:
                break;
        }
    }
}
