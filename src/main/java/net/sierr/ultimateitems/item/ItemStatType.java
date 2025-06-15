package net.sierr.ultimateitems.item;

import java.util.Locale;

public enum ItemStatType
{
    DAMAGE,
    CRIT_CHANCE,
    CRIT_POWER;

    @Override
    public String toString()
    {
        return super.toString().substring(0, 1).toUpperCase(Locale.ROOT)
                + super.toString().substring(1).toLowerCase(Locale.ROOT);
    }
}
