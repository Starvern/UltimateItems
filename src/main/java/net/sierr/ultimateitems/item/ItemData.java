package net.sierr.ultimateitems.item;

import com.google.common.collect.MultimapBuilder;
import net.sierr.ultimateitems.UltimateItems;
import net.sierr.ultimateitems.data.ItemModHolderDataType;
import net.sierr.ultimateitems.mods.ItemModHolder;
import net.sierr.ultimateitems.mods.ModSlotInfo;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Extracts PDC / NBT data for easy access.
 * @since 0.1.0
 */
public class ItemData
{
    private final String id;
    private final String name;
    private final Material material;
    private final List<String> description;
    private final List<ModSlotInfo> defaultModSlots;
    private final Set<ItemStat> stats;

    public ItemData(String id, String name, Material material,
                    List<String> description, List<ModSlotInfo> defaultModSlots, Set<ItemStat> stats)
    {
        this.id = id;
        this.name = name;
        this.material = material;
        this.description = description;
        this.defaultModSlots = defaultModSlots;
        this.stats = stats;
    }

    /**
     * @return The ID of the item.
     * @since 0.1.0
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * @return The display name of the item.
     * @since 0.1.0
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return The material of the item.
     * @since 0.1.0
     */
    public Material getMaterial()
    {
        return this.material;
    }

    /**
     * @return The description of the item.
     * @since 0.1.0
     */
    public List<String> getDescription()
    {
        return this.description;
    }

    /**
     * @return The stats of the item.
     * @since 0.1.0
     */
    public Set<ItemStat> getStats()
    {
        return this.stats;
    }

    /**
     * @return The modification slots provided to the item.
     * @since 0.1.0
     */
    public List<ModSlotInfo> getDefaultModSlots()
    {
        return this.defaultModSlots;
    }

    /**
     * @param type The {@link ItemStatType} of the stat.
     * @return The {@link ItemStat} or null.
     * @since 0.1.0
     */
    public @Nullable ItemStat getStat(ItemStatType type)
    {
        for (ItemStat stat : this.stats)
        {
            if (stat.getType().equals(type))
                return stat;
        }
        return null;
    }

    /**
     * @param api The instance of API.
     * @return A new {@link ItemStack} with the given data.
     * @since 0.1.0
     */
    public ItemStack create(UltimateItems api)
    {
        ItemStack item = new ItemStack(this.material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.getPersistentDataContainer().set(
                api.getItemDataKey(),
                PersistentDataType.STRING,
                this.id
        );

        itemMeta.getPersistentDataContainer().set(
                api.getItemModKey(),
                new ItemModHolderDataType(),
                new ItemModHolder(this.getDefaultModSlots())
        );

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        itemMeta.setAttributeModifiers(MultimapBuilder.hashKeys().hashSetValues().build());

        item.setItemMeta(itemMeta);
        new ItemHelper(api, item).refresh();
        return item;
    }
}
