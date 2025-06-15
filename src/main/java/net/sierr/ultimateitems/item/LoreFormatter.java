package net.sierr.ultimateitems.item;

import net.sierr.ultimateitems.ColorUtils;
import net.sierr.ultimateitems.UltimateItemsPlugin;
import net.sierr.ultimateitems.mods.ItemMod;
import net.sierr.ultimateitems.mods.ModSlotInfo;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoreFormatter
{
    private final FileConfiguration loreConfig;

    public LoreFormatter(UltimateItemsPlugin plugin)
    {
        this.loreConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "lore_format.yml"));
    }

    public void format(ItemHelper helper)
    {
        List<String> initial = this.loreConfig.getStringList("lore");

        List<String> lore = new ArrayList<>();

        for (String line : initial)
        {
            if (line.equalsIgnoreCase("%enchants%"))
            {
                boolean addSpace = false;
                for (Map.Entry<Enchantment, Integer> entry : helper.getItemStack().getEnchantments().entrySet())
                {
                    Enchantment enchantment = entry.getKey();
                    Integer level = entry.getValue();

                    List<String> enchantLore = this.loreConfig.getStringList("enchants_format");

                    enchantLore = enchantLore.stream()
                            .map(l -> l
                                    .replace("%enchant%", ColorUtils.capitalize(enchantment.getKey().value()))
                                    .replace("%level%", String.valueOf(level))
                            ).toList();

                    lore.addAll(enchantLore);
                    addSpace = true;
                }
                if (addSpace)
                    lore.add("");
                continue;
            }

            if (line.equalsIgnoreCase("%stats%"))
            {
                boolean addSpace = false;
                for (ItemStat stat : helper.getItemData().getStats())
                {
                    addSpace = true;
                    List<String> statLore = this.loreConfig
                            .getStringList("stats_format." + stat.getType().toString().toLowerCase(Locale.ROOT));

                    statLore = statLore.stream()
                            .map(l -> l.replace("%value%", String.valueOf(stat.getValue()))).toList();

                    lore.addAll(statLore);
                }
                if (addSpace)
                    lore.add("");
            }

            else if (line.equalsIgnoreCase("%mods%"))
            {
                boolean addSpace = false;
                for (Map.Entry<ModSlotInfo, String> entry : helper.getItemModHolder().entrySet())
                {
                    addSpace = true;
                    ModSlotInfo slotInfo = entry.getKey();
                    String modId = entry.getValue();
                    ItemMod mod = helper.getApi().getItemModStore().getItemMod(modId);

                    if (modId == null || mod == null) {
                        lore.addAll(this.loreConfig.getStringList("mod_format_empty." +
                                slotInfo.getType().toLowerCase(Locale.ROOT)));
                        continue;
                    }

                    List<String> modLore = this.loreConfig.getStringList("mod_format_filled." +
                            slotInfo.getType().toLowerCase(Locale.ROOT));

                    for (String modLine : modLore)
                    {
                        if (!modLine.equalsIgnoreCase("%stats%"))
                        {
                            lore.add(modLine.replace("%id%", mod.getName()));
                            continue;
                        }

                        for (ItemStat stat : mod.getStats()) {
                            List<String> statLore = this.loreConfig.getStringList("mod_stats_format."
                                    + stat.getType().toString().toLowerCase(Locale.ROOT));

                            String symbol = "+";
                            String percent = "";

                            switch (mod.getOperation()) {
                                case ADD -> symbol = "+";
                                case SUBTRACT -> symbol = "-";
                                case MULTIPLY -> symbol = "x";
                                case PERCENT_ADD -> percent = "%";
                                case PERCENT_SUBTRACT -> {
                                    percent = "%";
                                    symbol = "-";
                                }
                            }

                            String value = symbol + stat.getValue() + percent;

                            statLore = statLore.stream()
                                    .map(ld -> ld.replace("%value%", value)).toList();

                            lore.addAll(statLore);
                        }
                    }
                }
                if (addSpace)
                    lore.add("");
            }

            else if (line.equalsIgnoreCase("%description%"))
            {
                lore.addAll(helper.getItemData().getDescription());
            }
            else
                lore.add(line);
        }

        ItemMeta itemMeta = helper.getItemStack().getItemMeta();
        itemMeta.lore(ColorUtils.colorize(lore));
        helper.getItemStack().setItemMeta(itemMeta);
    }
}
