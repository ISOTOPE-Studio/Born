package cc.isotopestudio.born;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Born extends JavaPlugin {

    private static final String pluginName = "Born";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("Born").append("]").append(ChatColor.RED).toString();

    public static Born plugin;

//    public static PluginFile config;

    @Override
    public void onEnable() {
        plugin = this;
//        config = new PluginFile(this, "config.yml", "config.yml");
//        config.setEditable(false);

        //this.getCommand("csclass").setExecutor(new CommandCsclass());

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().info(pluginName + "成功加载!");
        getLogger().info(pluginName + "由ISOTOPE Studio制作!");
        getLogger().info("http://isotopestudio.cc");
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginName + "成功卸载!");
    }

}
