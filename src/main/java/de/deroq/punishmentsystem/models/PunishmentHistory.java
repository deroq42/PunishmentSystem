package de.deroq.punishmentsystem.models;

import java.util.List;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentHistory {

    private String uuid;
    private String name;
    private List<PunishmentEntry> entries;

    private PunishmentHistory(String uuid, String name, List<PunishmentEntry> entries) {
        this.uuid = uuid;
        this.name = name;
        this.entries = entries;
    }

    /* Public constructor due to pojo exceptions. */
    public PunishmentHistory() {
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

    public List<PunishmentEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<PunishmentEntry> entries) {
        this.entries = entries;
    }

    public static PunishmentHistory create(String uuid, String name, List<PunishmentEntry> entries) {
        return new PunishmentHistory(
                uuid,
                name,
                entries);
    }
}
