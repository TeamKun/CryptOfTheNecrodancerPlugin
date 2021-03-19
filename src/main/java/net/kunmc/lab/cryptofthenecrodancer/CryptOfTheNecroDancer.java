package net.kunmc.lab.cryptofthenecrodancer;

import net.kunmc.lab.cryptofthenecrodancer.commands.CommandMain;
import net.kunmc.lab.cryptofthenecrodancer.utils.TitleNotification;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CryptOfTheNecroDancer extends JavaPlugin
{
    public static Logger logger;
    public static CryptOfTheNecroDancer plugin;
    public static Game game;
    public static TitleNotification titleShower;

    @Override
    public void onEnable()
    {
        plugin = this;
        logger = getLogger();

        CommandMain command = new CommandMain();
        getServer().getPluginCommand("crypt").setExecutor(command);
        getServer().getPluginCommand("crypt").setTabCompleter(command);

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(new GrandEvents(), this);

        titleShower = new TitleNotification();

        logger.info("Crypt of the NecroDancer Plugin は正常にアクティベートされました！");
    }

    @Override
    public void onDisable()
    {
    }
}
