package de.deroq.punishmentsystem.models;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentUser {

    private String uuid;
    private String name;
    private PunishmentEntry punishmentEntry;

    private PunishmentUser(String uuid, String name, PunishmentEntry punishmentEntry) {
        this.uuid = uuid;
        this.name = name;
        this.punishmentEntry = punishmentEntry;
    }

    /* Public Constructor due to pojo exceptions. */
    public PunishmentUser() {
    }

    /* Public Setters due to pojo exceptions. */
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PunishmentEntry getPunishmentEntry() {
        return punishmentEntry;
    }

    public void setPunishmentEntry(PunishmentEntry punishmentEntry) {
        this.punishmentEntry = punishmentEntry;
    }

    public static PunishmentUser create(String uuid, String name, PunishmentEntry punishmentEntry) {
        return new PunishmentUser(
                uuid,
                name,
                punishmentEntry);
    }
}
