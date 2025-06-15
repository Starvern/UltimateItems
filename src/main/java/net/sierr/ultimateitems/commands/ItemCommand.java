package net.sierr.ultimateitems.commands;

import net.kyori.adventure.text.Component;
import net.sierr.ultimateitems.Registrable;
import net.sierr.ultimateitems.UltimateItems;
import net.sierr.ultimateitems.UltimateItemsPlugin;
import net.sierr.ultimateitems.exceptions.InvalidModSlotException;
import net.sierr.ultimateitems.item.ItemData;
import net.sierr.ultimateitems.item.ItemHelper;
import net.sierr.ultimateitems.mods.ItemMod;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemCommand implements CommandExecutor, Registrable
{
    private final UltimateItemsPlugin plugin;
    private final UltimateItems api;

    public ItemCommand(UltimateItemsPlugin plugin)
    {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
    }

    @Override
    public void register()
    {
        PluginCommand command = plugin.getCommand("ultimateitems");
        if (command == null)
        {
            this.plugin.getLogger().severe("Invalid plugin.yml. Please re-install the plugin.");
            return;
        }
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage("Bad usage: /ultimateitems (get/mod) (item/mod/clear)");
            return false;
        }

        if (args[0].equalsIgnoreCase("get"))
        {
            Player player;
            if (args.length >= 3)
            {
                String playerName = args[2];
                player = Bukkit.getPlayer(playerName);
                if (player == null)
                {
                    sender.sendMessage("BAD!");
                    return false;
                }
            }
            else
            {
                if (sender instanceof Player p)
                    player = p;
                else
                {
                    sender.sendMessage("NOT PLAYER!!");
                    return false;
                }
            }

            @Nullable ItemData data = this.api.getItemDataStore().getItemData(args[1]);

            if (data == null)
            {
                player.sendMessage("Unknown item data.");
                return false;
            }

            ItemStack item = data.create(this.api);
            player.getInventory().addItem(item);
            return true;
        }

        if (!(sender instanceof Player player))
        {
            sender.sendMessage("You must be a player!");
            return false;
        }

        if (args[0].equalsIgnoreCase("mod"))
        {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (!item.getPersistentDataContainer().has(this.api.getItemDataKey()))
                return false;

            ItemHelper helper = new ItemHelper(this.api, item);

            if (args[1].equalsIgnoreCase("clear"))
            {
                helper.removeAllMods();
                helper.refresh();
                player.sendMessage("modded clear!");
                return true;
            }

            if (args[1].equalsIgnoreCase("list"))
            {
                for (ItemMod slot : helper.getItemMods())
                {
                    player.sendMessage(Component.text("type: " + slot.getType()));
                }
                return true;
            }

            @Nullable ItemMod mod = this.plugin.getApi().getItemModStore().getItemMod(args[1]);
            if (mod == null)
            {
                sender.sendMessage("Unknown mod.");
                return false;
            }

            if (args.length < 3)
            {
                player.sendMessage("Specify slot");
                return false;
            }

            try
            {
                helper.addMod(mod, Integer.parseInt(args[2]));
                helper.refresh();
                player.sendMessage("Modded item.");
            }
            catch (InvalidModSlotException e)
            {
                player.sendMessage("Invalid mod slot.");
            }
        }

        return true;
    }

}
