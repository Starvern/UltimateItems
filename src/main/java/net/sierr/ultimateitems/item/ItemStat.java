package net.sierr.ultimateitems.item;

public class ItemStat
{
    private final ItemStatType type;
    private final float value;

    public ItemStat(ItemStatType type, float value)
    {
        this.type = type;
        this.value = value;
    }

    /**
     * @return The type of {@link ItemStatType}
     * @since 0.1.0
     */
    public ItemStatType getType()
    {
        return this.type;
    }

    /**
     * @return The value of the stat.
     * @since 0.1.0
     */
    public float getValue()
    {
        return this.value;
    }
}
