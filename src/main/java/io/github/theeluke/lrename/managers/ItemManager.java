package io.github.theeluke.lrename.managers;

import io.github.theeluke.lrename.utils.TextUtil;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemManager {

    public static void renameItem(ItemStack item, String newName) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(TextUtil.toItemString(newName));
        item.setItemMeta(meta);
    }

    public static void addLore(ItemStack item, String loreLine) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        assert lore != null;
        lore.add(TextUtil.toItemString(loreLine));

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void clearName(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(null);
        item.setItemMeta(meta);
    }

    public static void clearLore(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setLore(null);
        item.setItemMeta(meta);
    }

    public static void clearAll(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(null);
        meta.setLore(null);

        item.setItemMeta(meta);
    }

    public static boolean removeLoreLine(ItemStack item, int line) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return false;

        List<String> lore = meta.getLore();
        int index = line - 1;

        if (index < 0 || index >= Objects.requireNonNull(lore).size()) return false;
        lore.remove(index);

        meta.setLore(lore.isEmpty() ? null : lore);
        item.setItemMeta(meta);
        return true;
    }

    public static void setLore(ItemStack item, List<String> newLore) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        List<String> formattedLore = new ArrayList<>();
        for (String line : newLore) {
            formattedLore.add(TextUtil.toItemString(line));
        }

        meta.setLore(formattedLore);
        item.setItemMeta(meta);
    }

    public static void setItemFlag(ItemStack item, ItemFlag flag, boolean apply) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (apply) meta.addItemFlags(flag);
        else meta.removeItemFlags(flag);

        item.setItemMeta(meta);
    }

}
