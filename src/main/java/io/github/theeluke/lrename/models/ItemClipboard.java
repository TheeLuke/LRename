package io.github.theeluke.lrename.models;

import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Set;

public record ItemClipboard(String displayName, List<String> lore, Set<ItemFlag> flags) {

    public boolean hasName() {
        return displayName != null && !displayName.isEmpty();
    }

    public boolean hasLore() {
        return lore != null && !lore.isEmpty();
    }

    public boolean hasFlags() {
        return flags != null && !flags.isEmpty();
    }
}
