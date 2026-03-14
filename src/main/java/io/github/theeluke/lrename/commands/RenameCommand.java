package io.github.theeluke.lrename.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.theeluke.LRename;
import io.github.theeluke.lrename.managers.ItemManager;
import io.github.theeluke.lrename.models.ItemClipboard;
import io.github.theeluke.lrename.utils.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

@CommandAlias("lr|lrename")
public class RenameCommand extends BaseCommand {


    private final LRename plugin;

    public RenameCommand(LRename plugin) {
            this.plugin = plugin;
        }


    private void sendMessage(CommandSender sender, String messagePath) {
        Component msg = plugin.getConfigManager().getMessage("messages." + messagePath);
        plugin.adventure().sender(sender).sendMessage(msg);
    }

    private boolean hasValidItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            sendMessage(player, "no-item");
            return false;
        }
        return true;
    }

    // ==========================================
    // COMMANDS
    // ==========================================

    @Default
    @CatchUnknown
    public void onDefault(Player player) {
        plugin.adventure().player(player).sendMessage(TextUtil.parse(
                "<yellow>LRename Commands:</yellow>\n" +
                        "<gray>/lr rename <name></gray>\n" +
                        "<gray>/lr lore add <text></gray>\n" +
                        "<gray>/lr lore clear</gray>\n" +
                        "<gray>/lr copy</gray> | <gray>/lr paste</gray>"
        ));
    }

    @Subcommand("rename|r")
    @CommandPermission("lrename.rename")
    @Syntax("<name>")
    public void onRename(Player player, String newName) {
        if (!plugin.getConfigManager().isRenameEnabled()) {
            sendMessage(player, "feature-disabled");
            return;
        }

        if (!hasValidItem(player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemManager.renameItem(item, newName);
        sendMessage(player, "item-renamed");
    }

    @Subcommand("lore add|l a")
    @CommandPermission("lrename.lore")
    @Syntax("<text>")
    public void onLoreAdd(Player player, String loreLine) {
        if (!plugin.getConfigManager().isLoreEnabled()) {
            sendMessage(player, "feature-disabled");
            return;
        }

        if (!hasValidItem(player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLore() && Objects.requireNonNull(meta.getLore()).size() >= plugin.getConfigManager().getMaxLoreLines()) {
            sendMessage(player, "reached-max-lore-lines");
            return;
        }

        ItemManager.addLore(item, loreLine);
        sendMessage(player, "item-renamed");
    }

    @Subcommand("lore clear|l c")
    @CommandPermission("lrename.lore")
    public void onLoreClear(Player player) {
        if (!plugin.getConfigManager().isLoreEnabled()) {
            sendMessage(player, "feature-disabled");
            return;
        }

        if (!hasValidItem(player)) return;

        ItemManager.clearLore(player.getInventory().getItemInMainHand());
        sendMessage(player, "item-renamed");
    }

    @Subcommand("copy|c")
    @CommandPermission("lrename.copypaste")
    public void onCopy(Player player) {
        if (!plugin.getConfigManager().isClipboardEnabled()) {
            sendMessage(player, "feature-disabled");
            return;
        }

        if (!hasValidItem(player)) return;

        boolean success = plugin.getClipboardManager().copyFromItem(player.getUniqueId(), player.getInventory().getItemInMainHand());

        if (success) {
            sendMessage(player, "copied");
        } else {
            sendMessage(player, "nothing-to-copy");
        }
    }

    @Subcommand("paste|p")
    @CommandPermission("lrename.copypaste")
    public void onPaste(Player player) {
        if (!plugin.getConfigManager().isClipboardEnabled()) {
            sendMessage(player, "feature-disabled");
            return;
        }

        if (!hasValidItem(player)) return;

        if (!plugin.getClipboardManager().hasClipboard(player.getUniqueId())) {
            sendMessage(player, "nothing-to-paste");
            return;
        }

        ItemClipboard clipboard = plugin.getClipboardManager().getClipboard(player.getUniqueId());
        ItemStack item = player.getInventory().getItemInMainHand();

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (clipboard.hasName()) {
                meta.setDisplayName(clipboard.displayName());
            }
            if (clipboard.hasLore()) {
                meta.setLore(clipboard.lore());
            }
            item.setItemMeta(meta);
        }

        sendMessage(player, "pasted");
    }

    @Subcommand("reload")
    @CommandPermission("lrename.admin")
    public void onReload(CommandSender sender) {
        plugin.getConfigManager().reloadConfig();
        sendMessage(sender, "config-reloaded");
    }
}
