package io.github.theeluke;

import co.aikar.commands.BukkitCommandManager;
import io.github.theeluke.lrename.commands.RenameCommand;
import io.github.theeluke.lrename.managers.ClipboardManager;
import io.github.theeluke.lrename.managers.ConfigManager;
import io.github.theeluke.lrename.managers.StorageManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class LRename extends JavaPlugin {

    private ConfigManager configManager;
    private ClipboardManager clipboardManager;
    private StorageManager storageManager;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);

        // config
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.storageManager = new StorageManager(this);
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
}
