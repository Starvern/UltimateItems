package net.sierr.ultimateitems;

import net.sierr.ultimateitems.commands.ItemCommand;
import net.sierr.ultimateitems.events.ItemListener;
import net.sierr.ultimateitems.item.ItemData;
import net.sierr.ultimateitems.mods.ItemMod;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;

public final class UltimateItemsPlugin extends JavaPlugin
{
    private UltimateItems api;

    @Override
    public void onEnable()
    {
        this.api = new UltimateItems(this);
        new ItemListener(this.api).register();
        new ItemCommand(this).register();

        this.load();
    }

    /**
     * @return This plugin's API.
     * @since 0.1.0
     */
    public UltimateItems getApi()
    {
        return this.api;
    }

    /**
     * Loads the relevant data from the file system.
     * @since 0.1.0
     */
    public void load()
    {
        File itemsFolder = new File(this.getDataFolder(), "items");
        if (itemsFolder.mkdirs())
        {
            this.getLogger().info("Created items folder. [UltimateItems/items]");
            this.saveResource("items/default.yml", false);
        }
        File modsFolder = new File(this.getDataFolder(), "mods");
        if (modsFolder.mkdirs())
        {
            this.getLogger().info("Created mods folder. [UltimateItems/mods]");
            this.saveResource("mods/default.yml", false);
        }

        this.saveResource("lore_format.yml", false);

        Set<ItemData> dataSet = FileConverter.loadItems(this.api);

        for (ItemData data : dataSet)
            this.api.getItemDataStore().addItemData(data);

        Set<ItemMod> modSet = FileConverter.loadMods(this.api);

        for (ItemMod mod : modSet)
            this.api.getItemModStore().addItemMod(mod);
    }
}
