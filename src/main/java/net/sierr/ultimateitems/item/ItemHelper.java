package net.sierr.ultimateitems.item;

import net.sierr.ultimateitems.ColorUtils;
import net.sierr.ultimateitems.UltimateItems;
import net.sierr.ultimateitems.data.ItemModHolderDataType;
import net.sierr.ultimateitems.exceptions.InvalidModSlotException;
import net.sierr.ultimateitems.mods.ItemMod;
import net.sierr.ultimateitems.mods.ItemModHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemHelper
{
    private final UltimateItems api;

    private final ItemStack itemStack;
    private final ItemData itemData;
    private final ItemModHolder itemMods;

    public ItemHelper(UltimateItems api, ItemStack itemStack) throws IllegalArgumentException
    {
        this.api = api;
        this.itemStack = itemStack;
        this.itemData = ItemDataFactory.fromItemStack(api, itemStack);
        if (itemStack.getPersistentDataContainer().has(api.getItemModKey()))
            this.itemMods = itemStack.getPersistentDataContainer().get(api.getItemModKey(), new ItemModHolderDataType());
        else
            this.itemMods = new ItemModHolder(this.itemData.getDefaultModSlots());
    }

    /**
     * @return The {@link ItemStack} this helper is based on.
     * @since 0.1.0
     */
    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    /**
     * @return The {@link ItemData} stored on this item.
     * @since 0.1.0
     */
    public ItemData getItemData()
    {
        return this.itemData;
    }

    /**
     * @param mod The {@link ItemMod} to add to this item.
     * @param slot The mod slot (must be within {@link ItemData#getDefaultModSlots()})
     * @throws InvalidModSlotException If the slot is not within the parameters.
     */
    public void addMod(ItemMod mod, int slot) throws InvalidModSlotException
    {
        this.itemMods.addModId(slot, mod.getId());
        this.refresh();
    }

    /**
     * Removes all {@link ItemMod} on this item.
     * @since 0.1.0
     */
    public void removeAllMods()
    {
        this.itemMods.removeModIds();
        this.refresh();
    }

    /**
     * Updates the {@link ItemMeta} with the latest data.
     * @since 0.1.0
     */
    public void refresh()
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(this.api.getItemModKey(), new ItemModHolderDataType(), this.itemMods);
        itemMeta.displayName(ColorUtils.colorize(itemData.getName()));
        itemStack.setItemMeta(itemMeta);
        new LoreFormatter(this.api.getPlugin()).format(this);
    }

    /**
     * @return All {@link ItemMod} on this item.
     * @since 0.1.0
     */
    public List<ItemMod> getItemMods()
    {
        return this.itemMods.getModIds().stream().map(id -> this.api.getItemModStore().getItemMod(id)).toList();
    }

    public ItemModHolder getItemModHolder()
    {
        return this.itemMods;
    }

    public UltimateItems getApi() {
        return api;
    }
}
