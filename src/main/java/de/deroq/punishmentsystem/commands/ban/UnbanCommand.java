package de.deroq.punishmentsystem.commands.ban;

import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.misc.PunishmentEntryType;
import de.deroq.punishmentsystem.utils.Constants;
import de.deroq.punishmentsystem.utils.PunishmentUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class UnbanCommand extends Command {

    private final PunishmentSystem punishmentSystem;

    public UnbanCommand(String name, PunishmentSystem punishmentSystem) {
        super(name);
        this.punishmentSystem = punishmentSystem;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("punishment.unban")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("§cYou do not have permission to execute this command!"));
            return;
        }

        if (args.length != 1) {
            commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "/unban <player>"));
            return;
        }

        String name = args[0];
        punishmentSystem.getPunishmentUserManager().getPunishmentUser(name).thenAcceptAsync(punishmentUser -> {
            if (punishmentUser == null) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler ist nicht gebannt"));
                return;
            }

            if (!punishmentUser.getPunishmentEntry().getPunishmentType().equals(PunishmentEntryType.BAN.toString())) {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Dieser Spieler ist nicht gebannt"));
                return;
            }

            punishmentSystem.getPunishmentUserManager().pardonUser(name).thenAcceptAsync(b -> {
                commandSender.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "Spieler wurde entbannt"));
                PunishmentUtils.getStaffMembers(commandSender.getName(), (staffMembers -> staffMembers.forEach(player -> player.sendMessage(TextComponent.fromLegacyText(Constants.PREFIX + "§c" + name + " §3wurde von §a" + commandSender.getName() + " §3entmuted")))));
            });
        });
    }
}
