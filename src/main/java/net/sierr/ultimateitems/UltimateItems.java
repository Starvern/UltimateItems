package net.sierr.ultimateitems;

import net.sierr.ultimateitems.item.ItemDataStore;
import net.sierr.ultimateitems.macros.ItemMacro;
import net.sierr.ultimateitems.macros.ModMacro;
import net.sierr.ultimateitems.mods.ItemModStore;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;

public class UltimateItems
{
    private final UltimateItemsPlugin plugin;
    private final ItemDataStore itemDataStore;
    private final ItemModStore itemModStore;

    private final NamespacedKey itemDataKey;
    private final NamespacedKey itemModKey;

    private @Nullable UUI uuiApi;

    protected UltimateItems(UltimateItemsPlugin plugin)
    {
        this.plugin = plugin;
        this.itemDataStore = new ItemDataStore();
        this.itemModStore = new ItemModStore();
        this.itemDataKey = new NamespacedKey(this.plugin, "item_data");
        this.itemModKey = new NamespacedKey(this.plugin, "item_mods");

        RegisteredServiceProvider<UUI> provider = Bukkit.getServicesManager().getRegistration(UUI.class);
        if (provider != null)
            this.uuiApi = provider.getProvider();

        new ItemMacro(this).register();
        new ModMacro(this).register();
    }

    /**
     * @return The API's plugin.
     * @since 0.1.0
     */
    public UltimateItemsPlugin getPlugin()
    {
        return this.plugin;
    }

    /**
     * @return Instance of {@link UUI}, or null.
     * @since 0.1.0
     */
    public @Nullable UUI getUuiApi()
    {
        return this.uuiApi;
    }

    /**
     * @return All items registered on the server.
     * @since 0.1.0
     */
    public ItemDataStore getItemDataStore()
    {
        return this.itemDataStore;
    }

    /**
     * @return All mods registered on the server.
     * @since 0.1.0
     */
    public ItemModStore getItemModStore()
    {
        return itemModStore;
    }

    /**
     * @return The {@link NamespacedKey} involved with custom items.
     * @since 0.1.0
     */
    public NamespacedKey getItemDataKey()
    {
        return this.itemDataKey;
    }

    /**
     * @return The {@link NamespacedKey} involved with specific item mods.
     * @since 0.1.0
     */
    public NamespacedKey getItemModKey()
    {
        return this.itemModKey;
    }
}
