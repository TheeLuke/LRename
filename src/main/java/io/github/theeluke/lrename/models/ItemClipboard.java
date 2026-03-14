package io.github.theeluke.lrename.models;

import java.util.List;

public record ItemClipboard(String displayName, List<String> lore) {

    public boolean hasName() {
        return displayName != null && !displayName.isEmpty();
    }

    public boolean hasLore() {
        return lore != null && !lore.isEmpty();
    }
}
