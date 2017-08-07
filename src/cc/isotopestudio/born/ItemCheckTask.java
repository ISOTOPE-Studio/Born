package cc.isotopestudio.born;
/*
 * Created by Mars Tan on 8/7/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.born.PlayerListener.*;

public class ItemCheckTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().getName().equals("yomi")) {
                PlayerInventory inv = player.getInventory();
                ItemStack item = inv.getItem(0);
                if (item != null && item.isSimilar(TPITEM)) {
                    inv.setItem(0, null);
                }
                item = inv.getItem(4);
                if (item != null && item.isSimilar(BACKITEM)) {
                    inv.setItem(4, null);
                }
                item = inv.getItem(8);
                if (item != null && item.isSimilar(BEDWARPITEM)) {
                    inv.setItem(8, null);
                }
            }
        }
    }
}
