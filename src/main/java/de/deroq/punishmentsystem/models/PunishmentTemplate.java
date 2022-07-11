package de.deroq.punishmentsystem.models;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentTemplate {

    private String tuid;
    private long duration;
    private String durationString;
    private String punishmentType;
    private String reason;

    private PunishmentTemplate(String tuid, long duration, String durationString, String punishmentType, String reason) {
        this.tuid = tuid;
        this.duration = duration;
        this.durationString = durationString;
        this.punishmentType = punishmentType;
        this.reason = reason;
    }

    /* Public Constructor due to pojo exceptions. */
    public PunishmentTemplate() {
    }

    /* Public Setters due to pojo exceptions. */
    public String getTuid() {
        return tuid;
    }

    public void setTuid(String tuid) {
        this.tuid = tuid;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public String getPunishmentType() {
        return punishmentType;
    }

    public void setPunishmentType(String punishmentType) {
        this.punishmentType = punishmentType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class builder {

        private String tuid;
        private long duration;
        private String durationString;
        private String punishmentType;
        private String reason;

        public builder setTuid(String tuid) {
            this.tuid = tuid;
            return this;
        }

        public builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public builder setDurationString(String durationString) {
            this.durationString = durationString;
            return this;
        }

        public builder setPunishmentType(String punishmentType) {
            this.punishmentType = punishmentType;
            return this;
        }

        public builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        public PunishmentTemplate build() {
            return new PunishmentTemplate(
                    tuid,
                    duration,
                    durationString,
                    punishmentType,
                    reason);
        }
    }
}
