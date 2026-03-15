package io.github.theeluke.lrename.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TextUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
    private static final LegacyComponentSerializer ITEM_SERIALIZER = LegacyComponentSerializer.builder()
            .character('§')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static Component parse(String text) {
        if (text == null || text.isEmpty()) return Component.empty();

        if (text.contains("<") && text.contains(">")) return MINI_MESSAGE.deserialize(text);

        return LEGACY_SERIALIZER.deserialize(text);
    }

    public static Component parse(Player player, String text) {
        if (text == null || text.isEmpty()) return Component.empty();

        if (player != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            text = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
        }

        if (text.contains("<") && text.contains(">")) return MINI_MESSAGE.deserialize(text);

        return LEGACY_SERIALIZER.deserialize(text);
    }

    public static String toItemString(Player player, String text) {
        Component component = parse(player, text);
        return ITEM_SERIALIZER.serialize(component);
    }
}
