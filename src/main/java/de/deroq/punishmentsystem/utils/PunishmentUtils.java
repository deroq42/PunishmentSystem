package de.deroq.punishmentsystem.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentUtils {

    public static String formatDate(long duration) {
        if (duration == -1) {
            return "PERMANENT";
        }

        Date date = new Date(duration);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        return simpleDateFormat.format(date);
    }

    public static void getStaffMembers(String playerNotifyBypass, Consumer<Collection<ProxiedPlayer>> members) {
        Collection<ProxiedPlayer> staffMembers = ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(proxiedPlayer -> proxiedPlayer.hasPermission("punishment.notify"))
                .filter(proxiedPlayer -> !proxiedPlayer.getName().equals(playerNotifyBypass))
                .collect(Collectors.toList());

        members.accept(staffMembers);
    }

    public static long validateDuration(String durationString) {
        if (durationString.equals("permanent")) {
            return -1;
        }

        StringBuilder digitBuilder = new StringBuilder();
        char unit = 'a';

        for(int i = 0; i < durationString.length(); i++) {
            char c = durationString.charAt(i);
            if (Character.isDigit(c)) {
                digitBuilder.append(c);
            } else {
                unit = c;
                break;
            }
        }

        if (!isUnitValid(durationString, unit)) {
            return 0;
        }

        int digit = Integer.parseInt(digitBuilder.toString());
        DurationUtil durationUtil = DurationUtil.getDurationUtilByUnit(unit).get();
        return durationUtil.getDuration() * digit;
    }

    private static boolean isUnitValid(String duration, char unit) {
        return Character.isDigit(duration.charAt(0)) && DurationUtil.getDurationUtilByUnit(unit).isPresent();
    }
}
