package io.github.theeluke;

import co.aikar.commands.BukkitCommandManager;
import io.github.theeluke.lrename.commands.RenameCommand;
import io.github.theeluke.lrename.managers.ClipboardManager;
import io.github.theeluke.lrename.managers.ConfigManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public class LRename extends JavaPlugin {

    private ConfigManager configManager;
    private ClipboardManager clipboardManager;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);

        // config
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        BukkitCommandManager commandManager = new BukkitCommandManager(this);

        // clipboard
        this.clipboardManager = new ClipboardManager();

        commandManager.registerCommand(new RenameCommand(this));

        getLogger().info("LRename has been enabled");
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        getLogger().info("LRename has been disabled");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    public ClipboardManager getClipboardManager() {
        return this.clipboardManager;
    }
}
