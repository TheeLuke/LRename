package io.github.theeluke.lrename.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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

    public static String toItemString(String text) {
        Component component = parse(text);
        return ITEM_SERIALIZER.serialize(component);
    }
}
