package net.sierr.ultimateitems;

import net.sierr.ultimateitems.item.ItemData;
import net.sierr.ultimateitems.item.ItemDataFactory;
import net.sierr.ultimateitems.item.ItemStat;
import net.sierr.ultimateitems.mods.ItemMod;
import net.sierr.ultimateitems.item.ItemStatType;
import net.sierr.ultimateitems.mods.ModOperation;
import net.sierr.ultimateitems.mods.ModSlotInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class FileConverter
{
    /**
     * Convert all the files in /items/ to {@link ItemData}.
     * @since 0.1.0
     */
    public static Set<ItemData> loadItems(UltimateItems api)
    {
        Set<ItemData> dataSet = new HashSet<>();

        File folder = new File(api.getPlugin().getDataFolder(), "items");
        File[] files = folder.listFiles();
        if (files == null) return dataSet;

        for (File file : files)
        {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String itemId : config.getKeys(false))
            {
                ConfigurationSection section = config.getConfigurationSection(itemId);
                if (section == null) continue;

                Set<ItemStat> stats = new HashSet<>();
                for (String statId : section.getConfigurationSection("stats").getKeys(false))
                {
                    int value = section.getInt("stats." + statId);
                    stats.add(new ItemStat(ItemStatType.valueOf(statId.toUpperCase(Locale.ROOT)), value));
                }

                List<ModSlotInfo> slots = new ArrayList<>();
                ConfigurationSection modSection = section.getConfigurationSection("mods");
                if (modSection != null)
                {
                    for (String modType : modSection.getKeys(false))
                    {
                        for (int index = 0; index < section.getInt("mods." + modType); index++)
                            slots.add(new ModSlotInfo(modType, index));
                    }
                }

                dataSet.add(ItemDataFactory.fromConfigurationSection(itemId, section, slots, stats));
            }
        }

        return dataSet;
    }

    /**
     * Convert all the files in /mods/ to {@link ItemMod}.
     * @since 0.1.0
     */
    public static Set<ItemMod> loadMods(UltimateItems api)
    {
        Set<ItemMod> dataSet = new HashSet<>();

        File folder = new File(api.getPlugin().getDataFolder(), "mods");
        File[] files = folder.listFiles();
        if (files == null) return dataSet;

        for (File file : files)
        {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String modId : config.getKeys(false))
            {
                ConfigurationSection section = config.getConfigurationSection(modId);
                if (section == null) continue;

                if (section.contains("item"))
                {
                    api.getItemDataStore().addItemData(ItemDataFactory.fromConfigurationSection(
                            modId, section.getConfigurationSection("item"), new ArrayList<>(), new HashSet<>())
                    );
                }

                ModOperation operation = ModOperation.valueOf(
                        section.getString("operation", "ADD").toUpperCase(Locale.ROOT)
                );
                String name = section.getString("name");
                String type = section.getString("type");
                Set<ItemStat> stats = new HashSet<>();

                for (String statId : section.getConfigurationSection("stats").getKeys(false))
                {
                    int value = section.getInt("stats." + statId);
                    stats.add(new ItemStat(ItemStatType.valueOf(statId.toUpperCase(Locale.ROOT)), value));
                }

                dataSet.add(new ItemMod(modId, name, type, stats, operation));
            }
        }

        return dataSet;
    }
}
