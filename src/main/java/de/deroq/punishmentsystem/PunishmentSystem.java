package de.deroq.punishmentsystem;

import de.deroq.database.models.DatabaseService;
import de.deroq.database.models.DatabaseServiceType;
import de.deroq.database.services.mongo.MongoDatabaseService;
import de.deroq.punishmentsystem.commands.TemplateCommand;
import de.deroq.punishmentsystem.managers.PunishmentTemplateManager;
import de.deroq.punishmentsystem.managers.PunishmentUserManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class PunishmentSystem extends Plugin {

    private MongoDatabaseService databaseService;
    private PunishmentTemplateManager punishmentTemplateManager;
    private PunishmentUserManager punishmentUserManager;

    @Override
    public void onEnable() {
        initDatabase();
        initManagers();
        registerCommands();

        getLogger().info("PunishmentSystem has been enabled.");
    }

    @Override
    public void onDisable() {

    }

    private void initDatabase() {
        this.databaseService = (MongoDatabaseService) new DatabaseService.builder(DatabaseServiceType.MONGO)
                .setHost("localhost")
                .setUsername("root")
                .setDatabase("punishmentsystem")
                .setPassword("123456")
                .setPort(27017)
                .build();

        databaseService.connect();
    }

    private void initManagers() {
        this.punishmentTemplateManager = new PunishmentTemplateManager(this);
        this.punishmentUserManager = new PunishmentUserManager(this);
    }

    private void registerCommands() {
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerCommand(this, new TemplateCommand("template", this));
    }

    public MongoDatabaseService getDatabaseService() {
        return databaseService;
    }

    public PunishmentTemplateManager getPunishmentTemplateManager() {
        return punishmentTemplateManager;
    }

    public PunishmentUserManager getPunishmentUserManager() {
        return punishmentUserManager;
    }
}
