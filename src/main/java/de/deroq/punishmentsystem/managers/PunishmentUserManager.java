package de.deroq.punishmentsystem.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.deroq.database.services.mongo.MongoDatabaseServiceMethods;
import de.deroq.punishmentsystem.PunishmentSystem;
import de.deroq.punishmentsystem.models.PunishmentEntry;
import de.deroq.punishmentsystem.models.PunishmentHistory;
import de.deroq.punishmentsystem.models.PunishmentUser;
import de.deroq.punishmentsystem.utils.Constants;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentUserManager {

    private final MongoDatabaseServiceMethods databaseServiceMethods;
    private final MongoCollection<PunishmentUser> punishmentUserCollection;
    private final MongoCollection<PunishmentHistory> punishmentHistoryCollection;

    public PunishmentUserManager(PunishmentSystem punishmentSystem) {
        this.databaseServiceMethods = punishmentSystem.getDatabaseService().getDatabaseServiceMethods();
        this.punishmentUserCollection = punishmentSystem.getDatabaseService().getCollection("punishments", PunishmentUser.class);
        this.punishmentHistoryCollection = punishmentSystem.getDatabaseService().getCollection("histories", PunishmentHistory.class);
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
                punishmentUserCollection,
                Filters.eq("uuid", uuid.toString()),
                punishmentUser);
    }

    /**
     * Pardons a user by the users uuid.
     *
     * @param uuid The uuid of the user.
     * @return a Future with a Boolean which returns false if the PunishmentUser has been deleted.
     */
    public CompletableFuture<Boolean> pardonUser(UUID uuid) {
        return databaseServiceMethods.onDelete(
                punishmentUserCollection,
                Filters.eq("uuid", uuid.toString()));
    }

    /**
     * Pardons a user by the users name.
     *
     * @param name The name of the user.
     * @return a Future with a Boolean which returns false if the PunishmentUser has been deleted.
     */
    public CompletableFuture<Boolean> pardonUser(String name) {
        return databaseServiceMethods.onDelete(
                punishmentUserCollection,
                Filters.eq("name", name));
    }

    /**
     * Gets a PunishmentUser by the users uuid.
     *
     * @param uuid The uuid of the user.
     * @return a Future with a PunishmentUser.
     */
    public CompletableFuture<PunishmentUser> getPunishmentUser(UUID uuid) {
        return databaseServiceMethods.getAsync(
                punishmentUserCollection,
                Filters.eq("uuid", uuid.toString()));
    }

    /**
     * Gets a PunishmentUser by the users name.
     *
     * @param name The name of the user.
     * @return a Future with a PunishmentUser.
     */
    public CompletableFuture<PunishmentUser> getPunishmentUser(String name) {
        return databaseServiceMethods.getAsync(
                punishmentUserCollection,
                Filters.eq("name", name));
    }

    /**
     * Adds an entry to the users punishment history.
     *
     * @param uuid            The uuid of the user.
     * @param name            The name of the user.
     * @param punishmentEntry The entry which gets added.
     */
    public void addHistoryEntry(UUID uuid, String name, PunishmentEntry punishmentEntry) {
        CompletableFuture.runAsync(() -> {
            Bson history = Filters.eq("uuid", uuid.toString());
            PunishmentHistory punishmentHistory = punishmentHistoryCollection.find(history).first();

            if (punishmentHistory == null) {
                punishmentHistory = PunishmentHistory.create(
                        uuid.toString(),
                        name,
                        Collections.singletonList(punishmentEntry));

                punishmentHistoryCollection.insertOne(punishmentHistory);
            } else {
                punishmentHistory.getEntries().add(punishmentEntry);
                punishmentHistoryCollection.replaceOne(history, punishmentHistory);
            }
        }, Constants.EXECUTOR_SERVICE);
    }

    /**
     * Gets the PunishmentHistory by the users name.
     *
     * @param name The name of the user.
     * @return a Future with the PunishmentHistory.
     */
    public CompletableFuture<PunishmentHistory> getHistory(String name) {
        return databaseServiceMethods.getAsync(
                punishmentHistoryCollection,
                Filters.eq("name", name));
    }
}
