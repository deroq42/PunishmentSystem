package de.deroq.punishmentsystem.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.deroq.database.services.mongo.MongoDatabaseServiceMethods;
import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.PunishmentTemplate;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentTemplateManager {

    private final MongoDatabaseServiceMethods databaseServiceMethods;
    private final MongoCollection<PunishmentTemplate> collection;

    public PunishmentTemplateManager(PunishmentSystem punishmentSystem) {
        this.databaseServiceMethods = punishmentSystem.getDatabaseService().getDatabaseServiceMethods();
        this.collection = punishmentSystem.getDatabaseService().getCollection("templates", PunishmentTemplate.class);
    }

    /**
     * Creates a PunishmentTemplate.
     *
     * @param tuid           The id of the template.
     * @param duration       The duration of the punishment.
     * @param durationString The duration in string format.
     * @param punishmentType The type of the punishment.
     * @param reason         The reason of the punishment.
     * @return a Future with a Boolean which returns false if the PunishmentTemplate has been inserted.
     */
    public CompletableFuture<Boolean> createTemplate(String tuid, long duration, String durationString, String punishmentType, String reason) {
        PunishmentTemplate punishmentTemplate = new PunishmentTemplate.builder()
                .setTuid(tuid)
                .setDuration(duration)
                .setDurationString(durationString)
                .setPunishmentType(punishmentType)
                .setReason(reason)
                .build();

        return databaseServiceMethods.onInsert(
                collection,
                Filters.eq("tuid", tuid),
                punishmentTemplate);
    }

    /**
     * Deletes a PunishmentTemplate.
     *
     * @param tuid The id of the template.
     * @return a Future with a Boolean which returns false if the PunishmentTemplate has been deleted.
     */
    public CompletableFuture<Boolean> deleteTemplate(String tuid) {
        return databaseServiceMethods.onDelete(
                collection,
                Filters.eq("tuid", tuid));
    }

    /**
     * Gets a PunishmentTemplate.
     *
     * @param tuid The id of the template.
     * @return a Future with a PunishmentTemplate.
     */
    public CompletableFuture<PunishmentTemplate> getTemplate(String tuid) {
        return databaseServiceMethods.getAsync(
                collection,
                Filters.eq("tuid", tuid));
    }

    /**
     * @return a Collection with all PunishmentTemplates.
     */
    public CompletableFuture<Collection<PunishmentTemplate>> listTemplates() {
        return databaseServiceMethods.getAsyncCollection(collection);
    }
}
