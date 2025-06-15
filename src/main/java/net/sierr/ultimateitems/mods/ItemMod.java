package net.sierr.ultimateitems.mods;

import net.sierr.ultimateitems.item.ItemStat;

import java.util.Set;

/**
 * Represents a modification which changes items stats.
 * @since 0.1.0
 */
public class ItemMod
{
    private final String id;
    private final String name;
    private final String type;
    private final Set<ItemStat> stats;
    private final ModOperation operation;

    public ItemMod(String id, String name, String type, Set<ItemStat> stats, ModOperation operation)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.stats = stats;
        this.operation = operation;
    }

    /**
     * @return The ID of this mod.
     * @since 0.1.0
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * @return The display name of the modification when applied.
     * @since 0.1.0
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return The type of mod this is (usage: for slots) (ex: swords, bows, etc.)
     * @since 0.1.0
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * @return The stats this mod affects.
     * @since 0.1.0
     */
    public Set<ItemStat> getStats()
    {
        return this.stats;
    }

    /**
     * @return How this mod applies its stats to the item.
     * @since 0.1.0
     */
    public ModOperation getOperation()
    {
        return this.operation;
    }
}
