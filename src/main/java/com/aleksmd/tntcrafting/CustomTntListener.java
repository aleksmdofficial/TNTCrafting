package com.aleksmd.tntcrafting;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class CustomTntListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onTntBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        if (b.getType() != Material.TNT)
            return;
        if (!b.hasMetadata("customTntId"))
            return;
        String id = ((MetadataValue)b.getMetadata("customTntId").get(0)).asString();
        b.removeMetadata("customTntId", (Plugin)TNTCrafting.getInstance());
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        if (!TNTCrafting.getInstance().getTnts().containsKey(id))
            return;
        CustomTnt tnt = TNTCrafting.getInstance().getTnts().get(id);
        e.setDropItems(false);
        b.getWorld().dropItem(b.getLocation().add(0.5D, 0.0D, 0.5D), tnt.getItemStack());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTntPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType() != Material.TNT)
            return;
        Block b = e.getBlock();
        if (b.hasMetadata("customTntId"))
            b.removeMetadata("customTntId", (Plugin)TNTCrafting.getInstance());
        ItemStack i = e.getItemInHand();
        if (!i.hasItemMeta())
            return;
        String id = TNTCrafting.getInstance().getTntFromItem(i).getId();
        if (TNTCrafting.getInstance().getTnts().containsKey(id)) {
            b.setMetadata("customTntId", (MetadataValue)new FixedMetadataValue((Plugin)TNTCrafting.getInstance(), id));
        } else {
            return;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrime(TNTPrimeEvent e) {
        Block b = e.getBlock();
        if (b.hasMetadata("customTntId")) {
            String id = ((MetadataValue)b.getMetadata("customTntId").get(0)).asString();
            CustomTnt tnt = TNTCrafting.getInstance().getTnts().get(id);
            if (e.getCause() == TNTPrimeEvent.PrimeCause.EXPLOSION && !tnt.getExplosionFromOtherTnt()) {
                e.setCancelled(true);
                b.setType(Material.TNT);
                return;
            }
            e.setCancelled(true);
            b.setType(Material.AIR);
            tnt.createPrimedTnt(b.getLocation(), (e.getCause() == TNTPrimeEvent.PrimeCause.EXPLOSION));
            b.removeMetadata("customTntId", (Plugin)TNTCrafting.getInstance());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplode(ExplosionPrimeEvent e) {
        Entity ee = e.getEntity();
        if (ee == null || ee.getType() != EntityType.PRIMED_TNT)
            return;
        if (ee.getCustomName() == null || !TNTCrafting.getInstance().getTnts().containsKey(((MetadataValue)ee.getMetadata("customTntId").get(0)).asString()))
            return;
        CustomTnt tnt = TNTCrafting.getInstance().getTnts().get(((MetadataValue)ee.getMetadata("customTntId").get(0)).asString());
        e.setRadius((float)tnt.getPower());
        tnt.compileExplosion(e.getEntity().getLocation());
        e.setFire(tnt.getFire());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        if (e.getClickedBlock().hasMetadata("customTntId") && e.getClickedBlock().getType() != Material.TNT)
            e.getClickedBlock().removeMetadata("customTntId", (Plugin)TNTCrafting.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPistonMoveE(BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            if (b.hasMetadata("customTntId")) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPistonMoveR(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            if (b.hasMetadata("customTntId")) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
