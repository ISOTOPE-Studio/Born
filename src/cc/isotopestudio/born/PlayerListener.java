package cc.isotopestudio.born;
/*
 * Created by david on 2017/8/5.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static cc.isotopestudio.born.Born.econ;
import static cc.isotopestudio.born.Born.plugin;

class PlayerListener implements Listener {

    static final ItemStack BEDWARPITEM = Util.buildItem(Material.BED, false,
            S.toBoldGold("床地标"), S.toItalicYellow("右键打开"), S.toRed("不可移动"));
    static final ItemStack BACKITEM = Util.buildItem(Material.SKULL_ITEM, (short) 1, false,
            S.toBoldDarkAqua("回到死亡点"), S.toItalicYellow("右键使用"), S.toItalicYellow("花费100游戏币"), S.toRed("不可移动"));
    static final ItemStack TPITEM = Util.buildItem(Material.GRASS, false,
            S.toBoldGreen("随机传送"), S.toItalicYellow("右键使用"), S.toRed("不可移动"));

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop() != null &&
                (e.getItemDrop().getItemStack().isSimilar(BEDWARPITEM)
                        || e.getItemDrop().getItemStack().isSimilar(BACKITEM)
                        || e.getItemDrop().getItemStack().isSimilar(TPITEM))) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPick(PlayerPickupItemEvent e) {
        if (e.getItem().getItemStack() != null &&
                (e.getItem().getItemStack().isSimilar(BEDWARPITEM)
                        || e.getItem().getItemStack().isSimilar(BACKITEM)
                        || e.getItem().getItemStack().isSimilar(TPITEM))) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoyClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item != null &&
                (item.isSimilar(BEDWARPITEM) || item.isSimilar(BACKITEM) || item.isSimilar(TPITEM))) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlce(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (item != null &&
                (item.isSimilar(BEDWARPITEM) || item.isSimilar(BACKITEM) || item.isSimilar(TPITEM))) {
            e.setCancelled(true);
        }
    }

    private static final Map<Player, Location> PLAYER_LOCATION_MAP = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        int i = 0;
        while (i < e.getDrops().size()) {
            ItemStack item = e.getDrops().get(i);
            if (item != null &&
                    (item.isSimilar(BEDWARPITEM) || item.isSimilar(BACKITEM) || item.isSimilar(TPITEM))) {
                e.getDrops().remove(i);
            }
            ++i;
        }
        PLAYER_LOCATION_MAP.put(e.getEntity(), e.getEntity().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    p.teleport(Bukkit.getWorld("yomi").getSpawnLocation());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                addItem(p);
                            } catch (Exception ignored) {
                            }
                        }
                    }.runTaskLater(plugin, 2);
                } catch (Exception ignored) {
                }
            }
        }.runTaskLater(plugin, 2);
    }

    private void addItem(Player p) {
        PlayerInventory inv = p.getInventory();
        if (inv.getItem(0) != null) {
            ItemStack i1 = inv.getItem(0).clone();
            inv.setItem(0, TPITEM);
            if (!i1.isSimilar(TPITEM))
                inv.addItem(i1);
        } else {
            inv.setItem(0, TPITEM);
        }
        if (inv.getItem(4) != null) {
            ItemStack i1 = inv.getItem(4).clone();
            inv.setItem(4, BACKITEM);
            if (!i1.isSimilar(BACKITEM))
                inv.addItem(i1);
        } else {
            inv.setItem(4, BACKITEM);
        }
        if (inv.getItem(8) != null) {
            ItemStack i1 = inv.getItem(8).clone();
            inv.setItem(8, BEDWARPITEM);
            if (!i1.isSimilar(BEDWARPITEM))
                inv.addItem(i1);
        } else {
            inv.setItem(8, BEDWARPITEM);
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getPlayer().getWorld().getName().equals("yomi")) {
                    addItem(event.getPlayer());
                } else {
                    PlayerInventory inv = event.getPlayer().getInventory();
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
        }.runTaskLater(plugin, 4);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        Player player = e.getPlayer();
        if (item.isSimilar(TPITEM)) {
            e.setCancelled(true);
            player.performCommand("rt w:world");
        } else if (item.isSimilar(BACKITEM)) {
            e.setCancelled(true);
            if (!PLAYER_LOCATION_MAP.containsKey(player)) {
                player.sendMessage(S.toPrefixRed("没有死亡点记录"));
                return;
            }
            if (econ.withdrawPlayer(player, 100).transactionSuccess()) {
                player.teleport(PLAYER_LOCATION_MAP.remove(player));
                player.sendMessage(S.toPrefixGreen("成功传送"));
            } else {
                player.sendMessage(S.toPrefixRed("你的游戏币不足"));
            }
        } else if (item.isSimilar(BEDWARPITEM)) {
            e.setCancelled(true);
            player.performCommand("btp");
        }
    }
}
