package io.github.theeluke.lrename.managers;

import io.github.theeluke.LRename;
import io.github.theeluke.lrename.models.ItemClipboard;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StorageManager {

    public final LRename plugin;
    private File file;
    private FileConfiguration config;

    public StorageManager(LRename plugin) {
        this.plugin = plugin;
        setup();
    }

    private void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), "templates.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create templates.yml!");
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save templates.yml!");
        }
    }

    public void saveTemplate(UUID uuid, String templateName, ItemClipboard clipboard) {
        String path = uuid.toString() + "." + templateName.toLowerCase();

        config.set(path + ".name", clipboard.displayName());
        config.set(path + ".lore", clipboard.lore());
        List<String> flagNames = new ArrayList<>();
        if (clipboard.flags() != null) {
            for(ItemFlag flag : clipboard.flags()) {
                flagNames.add(flag.name());
            }
        }
        config.set(path + ".flags", flagNames);
        save();
    }

    public ItemClipboard loadTemplate(UUID uuid, String templateName) {
        String path = uuid.toString() + "." + templateName.toLowerCase();

        if (!config.contains(path)) {
            return null;
        }

        String name = config.getString(path + ".name");
        List<String> lore = config.contains(path + ".lore") ? config.getStringList(path + ".lore") : null;

        Set<ItemFlag> flags = new HashSet<>();
        if (config.contains(path + ".flags")) {
            for(String flagName : config.getStringList(path + ".flags")) {
                try {
                    flags.add(ItemFlag.valueOf(flagName.toUpperCase()));
                } catch (IllegalArgumentException ignored) {}
            }
        }

        return new ItemClipboard(name, lore, flags);
    }

    public Set<String> getPlayerTemplates(UUID uuid) {
        if (!config.contains(uuid.toString())) {
            return null;
        }

        return Objects.requireNonNull(config.getConfigurationSection(uuid.toString())).getKeys(false);
    }

    public boolean deleteTemplate(UUID uuid, String templateName) {
        String path = uuid.toString() + "." + templateName.toLowerCase();
        if (!config.contains(path)) {
            return false;
        }

        config.set(path, null);
        save();
        return true;
    }

}
