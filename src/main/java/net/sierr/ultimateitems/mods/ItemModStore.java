package net.sierr.ultimateitems.mods;

import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ItemModStore
{
    private final Set<ItemMod> modSet;

    public ItemModStore()
    {
        this.modSet = new HashSet<>();
    }

    public Set<ItemMod> getModSet()
    {
        return this.modSet;
    }

    public void addItemMod(ItemMod mod)
    {
        this.modSet.add(mod);
    }

    public @Nullable ItemMod getItemMod(String id)
    {
        for (ItemMod mod : this.modSet)
        {
            if (mod.getId().equalsIgnoreCase(id))
                return mod;
        }
        return null;
    }
}
