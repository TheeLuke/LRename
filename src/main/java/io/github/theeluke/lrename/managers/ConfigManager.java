package io.github.theeluke.lrename.managers;

import io.github.theeluke.LRename;
import io.github.theeluke.lrename.utils.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final LRename plugin;

    public ConfigManager(LRename plugin) {
        this.plugin = plugin;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }

    public boolean isRenameEnabled() {
        return plugin.getConfig().getBoolean("settings.allow-renaming", true);
    }

    public boolean isLoreEnabled() {
        return plugin.getConfig().getBoolean("settings.allow-lore-editting", true);
    }

    public boolean isClipboardEnabled() {
        return plugin.getConfig().getBoolean("settings.allow-copy-paste", true);
    }

    public int getMaxLoreLines() {
        return plugin.getConfig().getInt("settings.max-lore-lines", 10);
    }

    public Component getMessage(String path) {
        FileConfiguration config = plugin.getConfig();

        String prefix = config.getString("messages.prefix", "<dark_gray>[<yellow>LRename</yellow>]</dark_gray> ");
        String message = config.getString(path, "<red>Message missing: \" + path + \"</red>");

        return TextUtil.parse(prefix + message);
    }

    public Component getMessage(String path, String target, String replacement) {
        FileConfiguration config = plugin.getConfig();

        String prefix = config.getString("messages.prefix", "&8[&bLRename&8] ");
        String message = config.getString("messages." + path, "<red>Message missing: " + path + "</red>");

        message = message.replace(target, replacement);

        return TextUtil.parse(prefix + message);
    }

    public Component getRawMessage(String path) {
        String message = plugin.getConfig().getString(path, "");
        return TextUtil.parse(message);
    }

    // blacklist

    public boolean isWordBlacklisted(String text) {
        if (text == null || text.isEmpty()) return false;

        String lowerText = text.toLowerCase();
        for (String word : plugin.getConfig().getStringList("blacklists.words")) {
            if (lowerText.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isMaterialBlacklisted(Material material) {
        if (material == null) return false;

        String materialName = material.name().toUpperCase();
        return plugin.getConfig().getStringList("blacklists.materials").contains(materialName);
    }
}
