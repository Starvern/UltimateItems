package net.sierr.ultimateitems.macros;

import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.sierr.ultimateitems.ColorUtils;
import net.sierr.ultimateitems.UltimateItems;
import net.sierr.ultimateitems.item.ItemData;
import net.sierr.ultimateitems.item.ItemHelper;
import net.sierr.ultimateitems.mods.ItemMod;
import net.sierr.ultimateitems.mods.ItemModHolder;
import net.sierr.ultimateitems.mods.ModSlotInfo;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.properties.GuiProperty;
import self.starvern.ultimateuserinterface.properties.impl.StringProperty;

import java.util.List;
import java.util.Map;

public class ItemMacro extends Macro
{
    private final UltimateItems api;

    public ItemMacro(UltimateItems api) {
        super(api.getUuiApi(), api.getPlugin(), "item_slot");
        this.api = api;
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!(action.getHolder() instanceof GuiPage page))
            return;

        if (action.getArguments().isEmpty())
        {
            this.api.getPlugin().getLogger().warning("[UltimateItems::item_slot] (here) <--please provide an item id");
            return;
        }

        String itemId = action.getArguments().getFirst();

        if (action.getArguments().size() < 2)
        {
            this.api.getPlugin().getLogger().warning("[UltimateItems::item_slot] " +
                    itemId + " (here) <--please provide mod ids");
            return;
        }

        List<String> modSlotIds = action.getArguments().subList(1, action.getArguments().size());

        List<SlottedGuiItem> possibleItems = page.getSlottedItems(itemId);
        if (possibleItems.isEmpty()) return;
        SlottedGuiItem item = possibleItems.getFirst();
        ItemStack itemStack = event.getPage().getInventory().getItem(item.getSlot());
        if (itemStack == null) return;

        PersistentDataContainerView containerView = itemStack.getPersistentDataContainer();

        if (!containerView.has(this.api.getItemDataKey()) || !containerView.has(this.api.getItemModKey()))
        {
            for (String modSlotId : modSlotIds)
                page.getProperties().setProperty(new StringProperty(modSlotId, "BAD"), true);
            return;
        }

        ItemHelper helper = new ItemHelper(this.api, itemStack);
        ItemModHolder holder = helper.getItemModHolder();

        for (Map.Entry<ModSlotInfo, String> entry : holder.entrySet())
        {
            ModSlotInfo slotInfo = entry.getKey();
            String modId = (entry.getValue() == null) ? "NULL" : entry.getValue();
            String propKey =  slotInfo.getType() + "@" + slotInfo.getIndex();

            @Nullable ItemMod mod = this.api.getItemModStore().getItemMod(modId);
            @Nullable ItemData modItem = this.api.getItemDataStore().getItemData(modId);

            page.getProperties().setProperty(
                    new StringProperty(propKey, modId),
                    true
            );

            if (mod != null)
            {
                if (modItem != null)
                {
                    String displayName = LegacyComponentSerializer.legacySection().serialize(ColorUtils.colorize(modItem.getName()));
                    page.getProperties().setProperty(
                            new StringProperty(propKey + "name", displayName),
                            true
                    );
                    page.getProperties().setProperty(
                            new StringProperty(propKey + "material", modItem.getMaterial().toString()),
                            true
                    );
                }
            }
        }
    }
}
