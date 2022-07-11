package de.deroq.punishmentsystem.utils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author deroq
 * @since 11.07.2022
 */

public enum DurationUtil {

    HOURS(60 * 60 * 1000, 'h'),
    DAYS(24 * HOURS.getDuration(), 'd'),
    WEEKS(7 * DAYS.getDuration(), 'w'),
    MONTHS(30 * WEEKS.getDuration(), 'm'),
    YEARS(12 * MONTHS.getDuration(), 'y'),
    PERMANENTLY(-1, 'p');

    private long duration;
    private char unit;

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
