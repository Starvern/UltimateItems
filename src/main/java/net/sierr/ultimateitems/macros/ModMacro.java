package net.sierr.ultimateitems.macros;

import net.sierr.ultimateitems.UltimateItems;
import net.sierr.ultimateitems.item.ItemData;
import net.sierr.ultimateitems.item.ItemHelper;
import net.sierr.ultimateitems.item.ItemStat;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.api.GuiClickEvent;
import self.starvern.ultimateuserinterface.api.GuiCustomEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.ActionType;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

import java.util.List;

public class ModMacro extends Macro
{
    private final UltimateItems api;

    public ModMacro(UltimateItems api) {
        super(api.getUuiApi(), api.getPlugin(), "mod_set");
        this.api = api;
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!(action.getHolder() instanceof SlottedGuiItem))
            return;

        if (!action.getTrigger().getType().equals(ActionType.CLICK))
            return;

        if (action.getArguments().size() < 2)
            return;

        String itemId = action.getArguments().getFirst();
        int index = Integer.parseInt(action.getArguments().get(1));

        List<SlottedGuiItem> possibleItems = event.getPage().getSlottedItems(itemId);
        if (possibleItems.isEmpty())
            return;

        SlottedGuiItem item = possibleItems.getFirst();
        ItemStack inventoryItem = event.getPage().getInventory().getItem(item.getSlot());
        if (inventoryItem == null || inventoryItem.isEmpty()) return;

        GuiClickEvent clickEvent = (GuiClickEvent) event;
        ItemStack cursor = clickEvent.getCursor();

        ItemHelper helper = new ItemHelper(this.api, inventoryItem);
        if (cursor == null || cursor.isEmpty())
        {
            String modId = helper.getItemModHolder().removeModId(index);
            if (modId != null)
            {
                @Nullable ItemData modItem = this.api.getItemDataStore().getItemData(modId);
                if (modItem != null)
                    event.getHuman().getInventory().addItem(modItem.create(api));
            }
        }
        else
        {
            String modId = new ItemHelper(this.api, cursor).getItemData().getId();
            cursor.setAmount(cursor.getAmount()-1);

            helper.getItemModHolder().addModId(index, modId);
        }

        helper.refresh();
    }
}
