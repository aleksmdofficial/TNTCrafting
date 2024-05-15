package com.aleksmd.tntcrafting;

import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

public class ConfigLoader {
    private Plugin plugin;

    public ConfigLoader(Plugin pl) {
        this.plugin = pl;
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration cfg = this.plugin.getConfig();
        TNTCrafting.getInstance().getTnts().clear();
        for (String id : cfg.getConfigurationSection("tnts").getKeys(false)) {
            ConfigurationSection tnt = cfg.getConfigurationSection("tnts." + id);
            String[] craft = toStringArray(tnt.getStringList("craft"));
            String name = tnt.getString("metaData.name");
            List<String> lore = tnt.getStringList("metaData.lore");
            int fuseTicks = tnt.getInt("params.fuseTicks");
            double power = tnt.getDouble("params.power");
            boolean fire = tnt.getBoolean("params.fire");
            boolean detonateOtherTnt = tnt.getBoolean("params.explosionFromOtherTnt");
            boolean ignoreWater = tnt.getBoolean("params.ignoreWater");
            List<String> notBreaksBlocks = tnt.getStringList("params.notBreaksBlocks");
            if (notBreaksBlocks.contains("%all%"))
                notBreaksBlocks = null;
            CustomTnt ctnt = new CustomTnt(craft, name, lore, fuseTicks, id, power, fire, notBreaksBlocks, detonateOtherTnt, ignoreWater);
            TNTCrafting.getInstance().getTnts().put(id, ctnt);
            this.plugin.getServer().addRecipe((Recipe)ctnt.getRecipe());
        }
    }

    private String[] toStringArray(List<String> l) {
        String[] a = new String[l.size()];
        for (int i = 0; i < l.size(); i++)
            a[i] = l.get(i);
        return a;
    }
}

