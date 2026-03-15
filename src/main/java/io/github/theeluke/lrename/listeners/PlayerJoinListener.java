package io.github.theeluke.lrename.listeners;

import io.github.theeluke.LRename;
import io.github.theeluke.lrename.utils.TextUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    public final LRename plugin;

    public PlayerJoinListener(LRename plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Only notify players with the admin permission
        if (!event.getPlayer().hasPermission("lrename.admin")) {
            return;
        }

        // We check the version that was stored in the main class (we will set this up next!)
        if (plugin.isUpdateAvailable()) {
            plugin.adventure().player(event.getPlayer()).sendMessage(TextUtil.parse(
                    "\n<yellow><b>[LRename]</b> A new update is available!</yellow>\n" +
                            "<gray>Current version: <red>" + plugin.getDescription().getVersion() + "</red></gray>\n" +
                            "<gray>New version: <green>" + plugin.getLatestVersion() + "</green></gray>\n" +
                            "<gray>Download it on SpigotMC!</gray>\n"
            ));
        }
    }
}