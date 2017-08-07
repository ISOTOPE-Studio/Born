package cc.isotopestudio.born;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Born extends JavaPlugin {

    private static final String pluginName = "ϵͳ";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("Born").append("]").append(ChatColor.RED).toString();

    public static Born plugin;
    public static Economy econ = null;

//    public static PluginFile config;

    @Override
    public void onEnable() {
        plugin = this;
//        config = new PluginFile(this, "config.yml", "config.yml");
//        config.setEditable(false);

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - �����ر��޷����룬ԭ��Vaultδ��װ", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //this.getCommand("csclass").setExecutor(new CommandCsclass());

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        new ItemCheckTask().runTaskTimer(this, 20, 100);

        getLogger().info(pluginName + "�ɹ�����!");
        getLogger().info(pluginName + "��ISOTOPE Studio����!");
        getLogger().info("http://isotopestudio.cc");
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginName + "�ɹ�ж��!");
    }

    // Vault API
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
        return (econ != null);
    }
}
