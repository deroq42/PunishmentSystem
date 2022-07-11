package de.deroq.punishmentsystem.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.deroq.database.services.mongo.MongoDatabaseServiceMethods;
import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.PunishmentEntry;
import de.deroq.punishmentsystem.models.PunishmentUser;
import org.bson.conversions.Bson;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentUserManager {

    private final MongoDatabaseServiceMethods databaseServiceMethods;
    private final MongoCollection<PunishmentUser> collection;

    public PunishmentUserManager(PunishmentSystem punishmentSystem) {
        this.databaseServiceMethods = punishmentSystem.getDatabaseService().getDatabaseServiceMethods();
        this.collection = punishmentSystem.getDatabaseService().getCollection("punishments", PunishmentUser.class);
    }


    /**
     * Punishes a user.
     *
     * @param uuid            The uuid of the user.
     * @param name            The name of the user.
     * @param punishmentEntry The entry of the punishment.
     * @return a Future with a Boolean which returns false if the PunishmentUser has been inserted.
     */
    public CompletableFuture<Boolean> punishUser(UUID uuid, String name, PunishmentEntry punishmentEntry) {
        PunishmentUser punishmentUser = PunishmentUser.create(
                uuid.toString(),
                name,
                punishmentEntry);

        return databaseServiceMethods.onInsert(
                collection,
                Filters.eq("uuid", uuid.toString()),
                punishmentUser);
    }

    /**
     * Pardons a user.
     *
     * @param uuid The uuid of the user.
     * @return a Future with a Boolean which returns false if the PunishmentUser has been deleted.
     */
    public CompletableFuture<Boolean> pardonUser(UUID uuid) {
        return databaseServiceMethods.onDelete(
                collection,
                Filters.eq("uuid", uuid.toString()));
    }

    /**
     * Pardons a user.
     *
     * @param name The name of the user.
     * @return a Future with a Boolean which returns false if the PunishmentUser has been deleted.
     */
    public CompletableFuture<Boolean> pardonUser(String name) {
        return databaseServiceMethods.onDelete(
                collection,
                Filters.eq("name", name));
    }

    /**
     * Gets a PunishmentUser.
     *
     * @param uuid The uuid of the user.
     * @return a Future with a PunishmentUser.
     */
    public CompletableFuture<PunishmentUser> getPunishmentUser(UUID uuid) {
        return databaseServiceMethods.getAsync(
                collection,
                Filters.eq("uuid", uuid.toString()));
    }
}
