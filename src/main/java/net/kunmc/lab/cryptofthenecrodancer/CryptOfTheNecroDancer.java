package net.kunmc.lab.cryptofthenecrodancer;

import net.kunmc.lab.cryptofthenecrodancer.commands.CommandMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CryptOfTheNecroDancer extends JavaPlugin
{
    public static Logger logger;
    public static CryptOfTheNecroDancer plugin;
    public static Game game;

    @Override
    public void onEnable()
    {
        plugin = this;
        logger = getLogger();

        if (!Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) //NoteBlockAPiがあるかどうかちぇっく
        {
            logger.severe("NoteBlockAPIが見つかりませんでした。\n" +
                    "/kpm i NoteBlockAPI を実行するか、\n" +
                    "https://www.spigotmc.org/resources/noteblockapi.19287/ からダウンロードしてください。");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        CommandMain command = new CommandMain();
        getServer().getPluginCommand("crypt").setExecutor(command);
        getServer().getPluginCommand("crypt").setTabCompleter(command);

        Bukkit.getPluginManager().registerEvents(new Events(), this);

        logger.info("CryptOfNecrodancerは正常にアクティベートされました！");
    }

    @Override
    public void onDisable()
    {
    }
}
