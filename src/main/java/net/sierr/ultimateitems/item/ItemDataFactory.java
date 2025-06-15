package net.sierr.ultimateitems.item;

import net.sierr.ultimateitems.UltimateItems;
import net.sierr.ultimateitems.mods.ModSlotInfo;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class ItemDataFactory
{
    public static ItemData fromItemStack(UltimateItems api, ItemStack itemStack)
    {
        @Nullable String itemDataId = itemStack.getPersistentDataContainer().get(
                api.getItemDataKey(),
                PersistentDataType.STRING
        );

        if (itemDataId == null)
            throw new IllegalArgumentException("ItemStack doesn't have ItemData");

        @Nullable ItemData data = api.getItemDataStore().getItemData(itemDataId);

        if (data == null)
            throw new IllegalArgumentException("ItemStack doesn't have ItemData");

        return data;
    }

    public static ItemData fromConfigurationSection(String id, ConfigurationSection section,
                                                    List<ModSlotInfo> modSlots, Set<ItemStat> stats)
    {
        String name = section.getString("name");
        Material material = Material.matchMaterial(section.getString("material", "STONE"));
        List<String> lore = section.getStringList("description");

        return new ItemData(id, name, material, lore, modSlots, stats);
    }
}
