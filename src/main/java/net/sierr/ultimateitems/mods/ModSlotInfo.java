package net.sierr.ultimateitems.mods;

import java.io.Serial;
import java.io.Serializable;

public class ModSlotInfo implements Serializable
{
    private final String type;
    private final int index;

    public ModSlotInfo(String type, int index)
    {
        this.type = type;
        this.index = index;
    }

    public String getType()
    {
        return this.type;
    }

    public int getIndex()
    {
        return this.index;
    }
}
