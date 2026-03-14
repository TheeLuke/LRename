package io.github.theeluke.lrename.managers;

import io.github.theeluke.lrename.utils.TextUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

    public static void clearLore(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setLore(null);
        item.setItemMeta(meta);
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

}
