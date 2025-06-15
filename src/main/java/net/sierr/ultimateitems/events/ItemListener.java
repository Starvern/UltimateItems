package net.sierr.ultimateitems.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.sierr.ultimateitems.Registrable;
import net.sierr.ultimateitems.UltimateItems;
import net.sierr.ultimateitems.item.ItemHelper;
import net.sierr.ultimateitems.item.ItemStat;
import net.sierr.ultimateitems.mods.ItemMod;
import net.sierr.ultimateitems.item.ItemStatType;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemListener implements Listener, Registrable
{
    private final UltimateItems api;

    public ItemListener(UltimateItems api)
    {
        this.api = api;
    }

    @Override
    public void register()
    {
        Bukkit.getPluginManager().registerEvents(this, this.api.getPlugin());
    }

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event)
    {
        for (ItemStack item : event.getInventory().getContents())
        {
            if (item == null)
                continue;
            if (item.getPersistentDataContainer().has(this.api.getItemDataKey()))
                new ItemHelper(this.api, item).refresh();
        }
    }

    @EventHandler
    public void onEntityDropItemEvent(EntityDropItemEvent event)
    {
        ItemStack itemStack = event.getItemDrop().getItemStack();

        if (itemStack.getPersistentDataContainer().has(this.api.getItemDataKey()))
            new ItemHelper(this.api, itemStack).refresh();
    }

    @EventHandler
    public void onItemEnchant(EnchantItemEvent event)
    {
        ItemStack item = event.getItem();
        if (!item.getItemMeta().getPersistentDataContainer().has(this.api.getItemDataKey()))
            return;
        event.getEnchanter().sendMessage("enchanted!");
        ItemHelper helper = new ItemHelper(this.api, item);
        helper.refresh();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player player))
            return;

        ItemStack weapon = player.getInventory().getItemInMainHand();

        if (!weapon.getPersistentDataContainer().has(this.api.getItemDataKey()))
            return;

        ItemHelper helper = new ItemHelper(this.api, weapon);

        @Nullable ItemStat damageStat = helper.getItemData().getStat(ItemStatType.DAMAGE);
        @Nullable ItemStat critStat = helper.getItemData().getStat(ItemStatType.CRIT_POWER);

        if (damageStat == null)
            return;

        double damage = damageStat.getValue();
        double baseDamage = damage;

        if (critStat != null && event.isCritical())
            damage += baseDamage * critStat.getValue()/100;

        for (ItemMod mod : helper.getItemMods())
        {
            if (mod == null) continue;
            Bukkit.getLogger().info(damageStat.getType().toString());
            for (ItemStat stat : mod.getStats())
            {
                if (stat.getType().equals(ItemStatType.DAMAGE))
                {
                    switch(mod.getOperation())
                    {
                        case ADD -> damage += stat.getValue();
                        case SUBTRACT -> damage -= stat.getValue();
                        case MULTIPLY -> damage = baseDamage * stat.getValue();
                        case PERCENT_ADD -> damage += baseDamage * stat.getValue()/100;
                        case PERCENT_SUBTRACT -> damage -= baseDamage * stat.getValue()/100;
                    }
                }
            }
        }

        damage += baseDamage * 0.05 * weapon.getEnchantmentLevel(Enchantment.SHARPNESS);
        damage *= player.getAttackCooldown();
        damage = (double) Math.round(damage * 10) / 10;

        event.setDamage(damage);

        Entity entity = event.getEntity();

        // TODO: fix override name tags
        if (!entity.isCustomNameVisible())
        {
            entity.setCustomNameVisible(true);
            entity.customName(Component.text("-" + damage).color(TextColor.color(255, 0, 0)));

            Bukkit.getScheduler().runTaskLater(this.api.getPlugin(), () -> {
                entity.setCustomNameVisible(false);
                entity.customName(Component.empty());
            }, 30);
        }
        else
        {
            ArmorStand display = (ArmorStand) player.getWorld()
                    .spawnEntity(event.getEntity().getLocation(), EntityType.ARMOR_STAND);

            display.customName(Component.text("-" + damage).color(TextColor.color(255, 0, 0)));
            display.setInvisible(true);
            display.setInvulnerable(true);
            display.setSmall(true);
            display.setCustomNameVisible(true);

            Bukkit.getScheduler().runTaskLater(this.api.getPlugin(), display::remove, 30);
        }

        this.api.getPlugin().getLogger().info("Dealt " + event.getDamage() + " damage.");
    }
}
