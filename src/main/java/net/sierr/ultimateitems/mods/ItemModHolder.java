package net.sierr.ultimateitems.mods;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;

public class ItemModHolder implements Serializable
{
    private final Map<ModSlotInfo, @Nullable String> slots;

    public ItemModHolder()
    {
        this.slots = new HashMap<>();
    }

    public ItemModHolder(List<ModSlotInfo> initial)
    {
        this.slots = new HashMap<>();
        for (ModSlotInfo slot : initial)
            this.slots.put(slot, null);
    }

    public Set<Map.Entry<ModSlotInfo, @Nullable String>> entrySet()
    {
        return this.slots.entrySet();
    }

    public List<ModSlotInfo> getSlots()
    {
        return this.slots.keySet().stream().toList();
    }

    public List<String> getModIds()
    {
        return this.slots.values().stream().toList();
    }

    private @Nullable ModSlotInfo getModSlotInfo(int index)
    {
        for (ModSlotInfo slot : this.slots.keySet())
        {
            if (slot.getIndex() == index)
                return slot;
        }
        return null;
    }

    public @Nullable String getModId(int index)
    {
        @Nullable ModSlotInfo modSlotInfo = this.getModSlotInfo(index);
        if (modSlotInfo == null) return null;
        return this.slots.get(modSlotInfo);
    }

    public void addModId(int index, String modId)
    {
        @Nullable ModSlotInfo modSlotInfo = this.getModSlotInfo(index);
        if (modSlotInfo == null) return;
        this.slots.put(modSlotInfo, modId); // Should ALWAYS already exist.
    }

    public @Nullable String removeModId(int index)
    {
        @Nullable ModSlotInfo modSlotInfo = this.getModSlotInfo(index);
        if (modSlotInfo == null) return null;
        String modId = this.slots.get(modSlotInfo);
        this.slots.put(modSlotInfo, null);
        return modId;
    }

    public void removeModIds()
    {
        this.slots.replaceAll((s, v) -> null);
    }
}
