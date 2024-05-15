package com.aleksmd.tntcrafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class CustomTnt {
    private ItemStack item;

    private ShapedRecipe recipe;

    private String displayName;

    private String id;

    private List<String> lore;

    private List<Material> notBreaksBlocks;

    private int fuseTicks;

    private double power;

    private boolean fire;

    private boolean explosionFromOtherTnt;

    private boolean ignoreWater;

    private Random rnd;

    public CustomTnt(String[] recipe, String displayName, List<String> lore, int fuseTicks, String id, double power, boolean fire, List<String> notBreaksBlocks, boolean explosionFromOtherTnt, boolean ignoreWater) {
        this.displayName = displayName;
        this.lore = lore;
        this.fire = fire;
        this.power = power;
        this.fuseTicks = fuseTicks;
        this.explosionFromOtherTnt = explosionFromOtherTnt;
        this.ignoreWater = ignoreWater;
        this.notBreaksBlocks = (notBreaksBlocks != null) ? new ArrayList<>() : null;
        this.id = id;
        this.item = new ItemStack(Material.TNT);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(displayName.replace('&', '§') + "§r");
        meta.setLore((List) lore.stream().map(x -> "§r" + x.replace('&', '§') + "§r").collect(Collectors.toList()));
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        this.item.setItemMeta(meta);
        for (String str : notBreaksBlocks) {
            Material mat = Material.getMaterial(str.toUpperCase());
            if (mat == null)
                continue;
            this.notBreaksBlocks.add(mat);
        }
        this.recipe = new ShapedRecipe(new NamespacedKey((Plugin) TNTCrafting.getInstance(), id.toLowerCase()), this.item);
        char[] craft = new char[9];
        String symbols = "ASDFGHJKL";
        HashMap<Material, Character> buff = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            String c = recipe[i];
            Material mat = Material.getMaterial(c.toUpperCase());
            char j = symbols.toCharArray()[i];
            if (mat != null && mat != Material.AIR) {
                if (!buff.containsKey(mat)) {
                    buff.put(mat, Character.valueOf(j));
                    craft[i] = j;
                } else {
                    craft[i] = ((Character) buff.get(mat)).charValue();
                }
            } else {
                craft[i] = ' ';
            }
        }
        this.recipe.shape(
                new String(new char[]{craft[0], craft[1], craft[2]}),
                new String(new char[]{craft[3], craft[4], craft[5]}),
                new String(new char[]{craft[6], craft[7], craft[8]})
        );
        for (Map.Entry<Material, Character> e : buff.entrySet()) {
            ItemStack itemStack = new ItemStack(e.getKey());
            RecipeChoice.ExactChoice choice = new RecipeChoice.ExactChoice(itemStack);
            this.recipe.setIngredient(e.getValue(), choice);
        }
        this.rnd = new Random();
    }

    public TNTPrimed createPrimedTnt(Location l, boolean primedFromExplosion) {
        TNTPrimed tnt = (TNTPrimed) l.getWorld().spawnEntity(l.add(0.5D, 0.25D, 0.5D), EntityType.PRIMED_TNT);
        tnt.setCustomNameVisible(true);
        tnt.setCustomName(this.displayName.replace('&', '§') + "§r");
        tnt.setMetadata("customTntId", (MetadataValue) new FixedMetadataValue((Plugin) TNTCrafting.getInstance(), this.id));
        tnt.setFuseTicks(primedFromExplosion ? (this.rnd.nextInt(this.fuseTicks / 2) + 10) : this.fuseTicks);
        return tnt;
    }

    public void compileExplosion(Location l) {
        if (this.notBreaksBlocks == null)
            return;
        if (l.getBlock().getType() == Material.WATER && !this.ignoreWater)
            return;
        int fx = (int) (l.getBlockX() - this.power);
        int fy = (int) (l.getBlockY() - this.power);
        int fz = (int) (l.getBlockZ() - this.power);
        int tx = (int) (l.getBlockX() + this.power);
        int tz = (int) (l.getBlockZ() + this.power);
        int ty = (int) (l.getBlockY() + this.power);
        for (int x = fx; x < tx; x++) {
            for (int z = fz; z < tz; z++) {
                for (int y = fy; y < ty; y++) {
                    Location p = new Location(l.getWorld(), x, y, z);
                    Block b = p.getBlock();
                    if (!this.notBreaksBlocks.contains(b.getType()) && b.getType() != Material.AIR) {
                        int dur = b.getType().getMaxDurability();
                        double dist = l.distance(p) / this.power;
                        double coff = dist * dur;
                        if (this.power >= coff &&
                                this.rnd.nextDouble() > dist)
                            b.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public ItemStack getItemStack() {
        return this.item;
    }

    public ShapedRecipe getRecipe() {
        return this.recipe;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public List<Material> getNotBreaksBlocks() {
        return this.notBreaksBlocks;
    }

    public int getFuseTicks() {
        return this.fuseTicks;
    }

    public boolean getIgnoreWater() {
        return this.ignoreWater;
    }

    public double getPower() {
        return this.power;
    }

    public boolean getFire() {
        return this.fire;
    }

    public boolean getExplosionFromOtherTnt() {
        return this.explosionFromOtherTnt;
    }

    public String getId() {
        return this.id;
    }
}

