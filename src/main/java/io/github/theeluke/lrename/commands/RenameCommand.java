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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.Set;

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

        if (plugin.getConfigManager().isMaterialBlacklisted(item.getType())) {
            sendMessage(player, "blacklisted-material");
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
        plugin.adventure().player(player).sendMessage(TextUtil.parse(player,
                "<yellow><b>--- LRename Commands ---</b></yellow>\n" +
                        "<gray>/lr rename <name></gray> <dark_gray>-</dark_gray> <white>Rename your item</white>\n" +
                        "<gray>/lr lore add <text></gray> <dark_gray>-</dark_gray> <white>Add a line of lore</white>\n" +
                        "<gray>/lr lore delline <#></gray> <dark_gray>-</dark_gray> <white>Delete a specific lore line</white>\n" +
                        "<gray>/lr clear [name|lore]</gray> <dark_gray>-</dark_gray> <white>Clear specific or all text</white>\n" +
                        "<gray>/lr copy</gray> | <gray>/lr paste</gray> <dark_gray>-</dark_gray> <white>Clone item text</white>\n" +
                        "<gray>/lr hide [enchants|attributes]</gray> <dark_gray>-</dark_gray> <white>Hide item flags</white>\n" +
                        "<gray>/lr unhide [enchants|attributes]</gray> <dark_gray>-</dark_gray> <white>Unhide item flags</white>"
        ));
    }

    @Subcommand("rename")
    @CommandPermission("lrename.rename")
    @Syntax("<name>")
    @CommandCompletion("<new_name>")
    public void onRename(Player player, String newName) {
        if (!plugin.getConfigManager().isRenameEnabled()) {
            sendMessage(player, "feature-disabled");
            return;
        }

        if (!hasValidItem(player)) return;

        if (plugin.getConfigManager().isWordBlacklisted(newName)) {
            sendMessage(player, "blacklisted-word");
            return;
        }

        if (!plugin.getEconomyManager().chargePlayer(player, "rename")) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemManager.renameItem(player, item, newName);
        sendMessage(player, "item-renamed");
    }

    @Subcommand("lore add")
    @CommandPermission("lrename.lore")
    @Syntax("<text>")
    @CommandCompletion("<lore_text>")
    public void onLoreAdd(Player player, String loreLine) {
        if (!plugin.getConfigManager().isLoreEnabled()) {
            sendMessage(player, "feature-disabled");
            return;
        }

        if (!hasValidItem(player)) return;

        if (plugin.getConfigManager().isWordBlacklisted(loreLine)) {
            sendMessage(player, "blacklisted-word");
            return;
        }

        if (!plugin.getEconomyManager().chargePlayer(player, "lore-add")) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLore() && Objects.requireNonNull(meta.getLore()).size() >= plugin.getConfigManager().getMaxLoreLines()) {
            sendMessage(player, "reached-max-lore-lines");
            return;
        }

        ItemManager.addLore(player, item, loreLine);
        sendMessage(player, "item-renamed");
    }

    @Subcommand("lore delline")
    @CommandPermission("lrename.lore")
    @Syntax("<line_number>")
    @CommandCompletion("@lorelines")
    public void onLoreDelline(Player player, int lineNumber) {
        if (!plugin.getConfigManager().isLoreEnabled()) {
            sendMessage(player, "feature-disabled");
            return;
        }

        if (!hasValidItem(player)) return;

        if (!plugin.getEconomyManager().chargePlayer(player, "lore-delline")) {
            return;
        }

        boolean success = ItemManager.removeLoreLine(player.getInventory().getItemInMainHand(), lineNumber);

        if (success) {
            Component successMsg = plugin.getConfigManager().getMessage("lore-line-deleted", "%line%", String.valueOf(lineNumber));
            plugin.adventure().player(player).sendMessage(successMsg);
        } else {
            sendMessage(player, "invalid-line");
        }
    }

    @Subcommand("clear")
    @CommandPermission("lrename.clear")
    public void onClearAll(Player player) {
        if (!hasValidItem(player)) return;

        if (!plugin.getEconomyManager().chargePlayer(player, "clear-all")) {
            return;
        }

        ItemManager.clearAll(player.getInventory().getItemInMainHand());
        sendMessage(player, "all-cleared");
    }

    @Subcommand("clear name")
    @CommandPermission("lrename.clear")
    public void onClearName(Player player) {
        if (!hasValidItem(player)) return;

        if (!plugin.getEconomyManager().chargePlayer(player, "clear-name")) {
            return;
        }

        ItemManager.clearName(player.getInventory().getItemInMainHand());
        sendMessage(player, "name-cleared");
    }

    @Subcommand("clear lore")
    @CommandPermission("lrename.clear")
    public void onClearLore(Player player) {
        if (!hasValidItem(player)) return;

        if (!plugin.getEconomyManager().chargePlayer(player, "clear-lore")) {
            return;
        }

        ItemManager.clearLore(player.getInventory().getItemInMainHand());
        sendMessage(player, "lore-cleared");
    }

    @Subcommand("copy")
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

    @Subcommand("paste")
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

        if (!plugin.getEconomyManager().chargePlayer(player, "paste")) {
            return;
        }

        ItemClipboard clipboard = plugin.getClipboardManager().getClipboard(player.getUniqueId());
        setTemplateDetails(player, clipboard);

        sendMessage(player, "pasted");
    }

    @Subcommand("hide enchants")
    @CommandPermission("lrename.hide")
    public void onHideEnchants(Player player) {
        if (!hasValidItem(player)) return;

        ItemManager.setItemFlag(player.getInventory().getItemInMainHand(), ItemFlag.HIDE_ENCHANTS, true);

        sendMessage(player, "hidden-enchants");
    }

    @Subcommand("hide attributes")
    @CommandPermission("lrename.hide")
    public void onHideAttributes(Player player) {
        if (!hasValidItem(player)) return;

        ItemManager.setItemFlag(player.getInventory().getItemInMainHand(), ItemFlag.HIDE_ATTRIBUTES, true);
        sendMessage(player, "hidden-attributes");
    }

    @Subcommand("unhide enchants")
    @CommandPermission("lrename.hide")
    public void onUnhideEnchants(Player player) {
        if (!hasValidItem(player)) return;

        ItemManager.setItemFlag(player.getInventory().getItemInMainHand(), ItemFlag.HIDE_ENCHANTS, false);
        sendMessage(player, "unhidden-enchants");
    }

    @Subcommand("unhide attributes")
    @CommandPermission("lrename.hide")
    public void onUnhideAttributes(Player player) {
        if (!hasValidItem(player)) return;

        ItemManager.setItemFlag(player.getInventory().getItemInMainHand(), ItemFlag.HIDE_ATTRIBUTES, false);
        sendMessage(player, "unhidden-attributes");
    }

    @Subcommand("template save")
    @CommandPermission("lrename.templates")
    @Syntax("<template_name>")
    @CommandCompletion("<name_to_save>")
    public void onTemplate(Player player, String templateName) {
        if (!hasValidItem(player)) return;

        boolean success = plugin.getClipboardManager().copyFromItem(player.getUniqueId(), player.getInventory().getItemInMainHand());

        if (!success) {
            sendMessage(player, "templates-not-saved");
            return;
        }

        ItemClipboard clipboard = plugin.getClipboardManager().getClipboard(player.getUniqueId());
        plugin.getStorageManager().saveTemplate(player.getUniqueId(), templateName, clipboard);

        Component successMsg = plugin.getConfigManager().getMessage("template-saved", "%template%", templateName);
        plugin.adventure().player(player).sendMessage(successMsg);
    }

    @Subcommand("template load")
    @CommandPermission("lrename.templates")
    @Syntax("<template_name>")
    @CommandCompletion("@templates")
    public void onTemplateLoad(Player player, String templateName) {
        if (!hasValidItem(player)) return;

        ItemClipboard template = plugin.getStorageManager().loadTemplate(player.getUniqueId(), templateName);

        if (template == null) {
            sendMessage(player, "template-not-found");
            return;
        }

        setTemplateDetails(player, template);

        Component successMsg = plugin.getConfigManager().getMessage("loaded-template", "%template%", templateName);
        plugin.adventure().player(player).sendMessage(successMsg);
    }

    @Subcommand("templates")
    @CommandPermission("lrename.templates")
    public void onTemplatesList(Player player) {
        Set<String> templates = plugin.getStorageManager().getPlayerTemplates(player.getUniqueId());

        if (templates == null || templates.isEmpty()) {
            sendMessage(player, "templates-not-found");
            return;
        }

        String list = String.join("</gray>, <yellow>", templates);
        plugin.adventure().player(player).sendMessage(TextUtil.parse(player,"<green>Your Templates: <yellow>" + list + "</yellow></green>"));
    }

    @Subcommand("template delete")
    @CommandPermission("lrename.templates")
    @Syntax("<template_name>")
    @CommandCompletion("@templates")
    public void onTemplateDelete(Player player, String templateName) {
        boolean deleted = plugin.getStorageManager().deleteTemplate(player.getUniqueId(), templateName);

        if (deleted) {
            Component successMsg = plugin.getConfigManager().getMessage("deleted-template", "%template%", templateName);
            plugin.adventure().player(player).sendMessage(successMsg);
        } else {
            sendMessage(player, "template-not-found");
        }
    }

    @Subcommand("reload")
    @CommandPermission("lrename.admin")
    public void onReload(CommandSender sender) {
        plugin.getConfigManager().reloadConfig();
        sendMessage(sender, "config-reloaded");
    }

    //helper
    private void setTemplateDetails(Player player, ItemClipboard clipboard) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(clipboard.hasName() ? clipboard.displayName() : null);
            meta.setLore(clipboard.hasLore() ? clipboard.lore() : null);

            for (org.bukkit.inventory.ItemFlag flag : org.bukkit.inventory.ItemFlag.values()) {
                meta.removeItemFlags(flag);
            }
            if (clipboard.hasFlags()) {
                meta.addItemFlags(clipboard.flags().toArray(new ItemFlag[0]));
            }

            item.setItemMeta(meta);
        }
    }
}
