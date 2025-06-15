package net.sierr.ultimateitems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.Locale;

public class ColorUtils
{
    public static Component colorize(String raw)
    {
        return MiniMessage.miniMessage().deserialize(raw);
    }

    public static List<Component> colorize(List<String> raw)
    {
        return raw.stream().map(ColorUtils::colorize).toList();
    }

    public static String capitalize(String input)
    {
        return input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1).toLowerCase(Locale.ROOT);
    }
}
