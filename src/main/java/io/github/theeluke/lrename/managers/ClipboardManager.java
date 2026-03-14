package io.github.theeluke.lrename.managers;

import io.github.theeluke.lrename.models.ItemClipboard;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ClipboardManager {

    private final Map<UUID, ItemClipboard> activeClipboards = new HashMap<>();

    public boolean copyFromItem(UUID playerId, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        String name = meta.hasDisplayName() ? meta.getDisplayName() : null;
        var lore = meta.hasLore() ? meta.getLore() : null;
        Set<ItemFlag> flags = meta.getItemFlags();

        boolean hasValidName = name != null && !name.isEmpty();
        boolean hasValidLore = lore != null && !lore.isEmpty();
        boolean hasValidFlags = !flags.isEmpty();

        if (!hasValidName && !hasValidLore && !hasValidFlags) return false;

        ItemClipboard clipboard = new ItemClipboard(name, lore, flags);
        activeClipboards.put(playerId, clipboard);
        return true;
    }

    public ItemClipboard getClipboard(UUID playerId) {
        return activeClipboards.get(playerId);
    }

    public boolean hasClipboard(UUID playerId) {
        return activeClipboards.containsKey(playerId);
    }

    public void clearClipboard(UUID playerId) {
        activeClipboards.remove(playerId);
    }

    public void saveClipboardToDisk(UUID playerId, String templateName) {
        ItemClipboard clipboard = getClipboard(playerId);
        if(clipboard == null) return;

        // TODO
    }

}
