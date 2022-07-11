package de.deroq.punishmentsystem.models;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentEntry {

    private final long timestamp;
    private final String template;
    private final long duration;
    private final String from;
    private final String punishmentType;

    private PunishmentEntry(long timestamp, String template, long duration, String from, String punishmentType) {
        this.timestamp = timestamp;
        this.template = template;
        this.duration = duration;
        this.from = from;
        this.punishmentType = punishmentType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTemplate() {
        return template;
    }

    public long getDuration() {
        return duration;
    }

    public String getFrom() {
        return from;
    }

    public String getPunishmentType() {
        return punishmentType;
    }

    public static class builder {

        private long timestamp;
        private String template;
        private long duration;
        private String from;
        private String punishmentType;

        public builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public builder setTemplate(String template) {
            this.template = template;
            return this;
        }

        public builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public builder setPunishmentType(String punishmentType) {
            this.punishmentType = punishmentType;
            return this;
        }

        public PunishmentEntry build() {
            return new PunishmentEntry(
                    timestamp,
                    template,
                    duration,
                    from,
                    punishmentType);
        }
    }
}
