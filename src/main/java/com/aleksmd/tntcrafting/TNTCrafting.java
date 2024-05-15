package com.aleksmd.tntcrafting;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TNTCrafting extends JavaPlugin {
    private HashMap<String, CustomTnt> tnt;

    private static ConfigLoader cfgLoader;

    private static TNTCrafting plugin;

    public HashMap<String, CustomTnt> getTnts() {
        return this.tnt;
    }

    public static TNTCrafting getInstance() {
        return plugin;
    }

    public static ConfigLoader getConfigLoader() {
        return cfgLoader;
    }

    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        this.tnt = new HashMap<>();
        cfgLoader = new ConfigLoader((Plugin)this);
        Bukkit.getPluginManager().registerEvents(new CustomTntListener(), (Plugin)this);
    }

    public CustomTnt getTntFromItem(ItemStack i) {
        for (Map.Entry<String, CustomTnt> t : this.tnt.entrySet()) {
            if (compareItems(((CustomTnt)t.getValue()).getItemStack(), i))
                return t.getValue();
        }
        return null;
    }

    private boolean compareItems(ItemStack i, ItemStack i1) {
        if (!i.hasItemMeta() || !i1.hasItemMeta())
            return false;
        if (i.getType() != i1.getType())
            return false;
        if (!i.getItemMeta().getDisplayName().equals(i1.getItemMeta().getDisplayName()))
            return false;
        return i.getItemMeta().getLore().equals(i1.getItemMeta().getLore());
    }

    public void onDisable() {}

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock().getType() != Material.TNT)
            return;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getBlock() == null || e.getBlock().getType() != Material.TNT)
            return;
        Block b = e.getBlock();
    }
}

