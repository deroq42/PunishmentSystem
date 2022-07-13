package de.deroq.punishmentsystem.utils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author deroq
 * @since 11.07.2022
 */

public enum DurationUtil {

    MINUTES(60 * 1000, 'm'),
    HOURS(60 * MINUTES.getDuration(), 'h'),
    DAYS(24 * HOURS.getDuration(), 'd'),
    PERMANENTLY(-1, 'p');

    private final long duration;
    private final char unit;

    DurationUtil(long duration, char unit) {
        this.duration = duration;
        this.unit = unit;
    }

    public long getDuration() {
        return duration;
    }

    public char getUnit() {
        return unit;
    }

    public static Optional<DurationUtil> getDurationUtilByUnit(char unit) {
        return Arrays.stream(values()).filter(durationUtil -> durationUtil.getUnit() == unit).findFirst();
    }
}
