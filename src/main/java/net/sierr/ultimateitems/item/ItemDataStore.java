package net.sierr.ultimateitems.item;

import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ItemDataStore
{
    private final Set<ItemData> itemDataSet;

    public ItemDataStore()
    {
        this.itemDataSet = new HashSet<>();
    }

    /**
     * @param id The id of the item to get.
     * @return The ItemData, or null.
     * @since 0.1.0
     */
    public @Nullable ItemData getItemData(String id)
    {
        for (ItemData itemData : itemDataSet)
        {
            if (itemData.getId().equalsIgnoreCase(id))
                return itemData;
        }
        return null;
    }

    /**
     * @param data The data to add.
     * @since 0.1.0
     */
    public void addItemData(ItemData data)
    {
        this.itemDataSet.add(data);
    }
}
