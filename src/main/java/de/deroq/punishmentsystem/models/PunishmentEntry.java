package de.deroq.punishmentsystem.models;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentEntry {

    private long timestamp;
    private String template;
    private String reason;
    private long duration;
    private String from;
    private String punishmentType;

    private PunishmentEntry(long timestamp, String template, String reason, long duration, String from, String punishmentType) {
        this.timestamp = timestamp;
        this.template = template;
        this.reason = reason;
        this.duration = duration;
        this.from = from;
        this.punishmentType = punishmentType;
    }

    /* Public constructor due to pojo exceptions. */
    public PunishmentEntry() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPunishmentType() {
        return punishmentType;
    }

    public void setPunishmentType(String punishmentType) {
        this.punishmentType = punishmentType;
    }

    public static class builder {

        private long timestamp;
        private String template;
        private String reason;
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

        public builder setReason(String reason) {
            this.reason = reason;
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
                    reason,
                    duration,
                    from,
                    punishmentType);
        }
    }
}
