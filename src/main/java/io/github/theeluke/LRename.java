package io.github.theeluke;

import co.aikar.commands.BukkitCommandManager;
import io.github.theeluke.lrename.commands.RenameCommand;
import io.github.theeluke.lrename.listeners.PlayerJoinListener;
import io.github.theeluke.lrename.managers.ClipboardManager;
import io.github.theeluke.lrename.managers.ConfigManager;
import io.github.theeluke.lrename.managers.EconomyManager;
import io.github.theeluke.lrename.managers.StorageManager;
import io.github.theeluke.lrename.utils.UpdateChecker;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class LRename extends JavaPlugin {

    private ConfigManager configManager;
    private ClipboardManager clipboardManager;
    private StorageManager storageManager;
    private EconomyManager  economyManager;

    private Economy vaultEconomy = null;
    private BukkitAudiences adventure;

    private boolean updateAvailable = false;
    private String latestVersion = "";

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);

        // config
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.storageManager = new StorageManager(this);

        // bstats
        int pluginId = 30137;
        Metrics metrics = new Metrics(this, pluginId);

        if (setupVaultEconomy()) {
            getLogger().info("Vault found! Economy support is enabled.");
        } else {
            getLogger().info("Vault not found. Vault economy features will be disabled (XP mode will still work).");
        }

        this.economyManager = new EconomyManager(this, vaultEconomy);
        BukkitCommandManager commandManager = new BukkitCommandManager(this);

        // clipboard
        this.clipboardManager = new ClipboardManager();

        // tab completion for ACF
        commandManager.getCommandCompletions().registerAsyncCompletion("templates", context -> {
            if (context.getPlayer() == null) return java.util.Collections.emptyList();

            java.util.Set<String> templates = storageManager.getPlayerTemplates(context.getPlayer().getUniqueId());
            return templates != null ? templates : java.util.Collections.emptyList();
        });

        commandManager.getCommandCompletions().registerAsyncCompletion("lorelines", context -> {
            if (context.getPlayer() == null) return java.util.Collections.emptyList();

            org.bukkit.inventory.ItemStack item = context.getPlayer().getInventory().getItemInMainHand();
            if (item.getType() == org.bukkit.Material.AIR || !item.hasItemMeta() || !Objects.requireNonNull(item.getItemMeta()).hasLore()) {
                return java.util.Collections.singletonList("<no_lore_found>");
            }

            int size = Objects.requireNonNull(item.getItemMeta().getLore()).size();
            java.util.List<String> lines = new java.util.ArrayList<>();
            for (int i = 1; i <= size; i++) {
                lines.add(String.valueOf(i));
            }
            return lines;
        });

        commandManager.registerCommand(new RenameCommand(this));

        getLogger().info("LRename has been enabled.");

        new UpdateChecker(this, 133456).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("You are running the latest version of LRename!");
            } else {
                getLogger().warning("There is a new update available for LRename! (v" + version + ")");
                this.updateAvailable = true;
                this.latestVersion = version;
            }
        });

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
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

    public StorageManager getStorageManager() {
        return this.storageManager;
    }

    public EconomyManager getEconomyManager() {
        return this.economyManager;
    }

    private boolean setupVaultEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        vaultEconomy = rsp.getProvider();
        return true;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}
