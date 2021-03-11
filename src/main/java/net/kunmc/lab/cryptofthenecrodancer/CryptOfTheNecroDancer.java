package net.kunmc.lab.cryptofthenecrodancer;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.kunmc.lab.cryptofthenecrodancer.commands.PlaySong;
import net.kunmc.lab.cryptofthenecrodancer.commands.StopSong;
import org.bukkit.Bukkit;
import net.kunmc.lab.cryptofthenecrodancer.commands.CommandMain;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class CryptOfTheNecroDancer extends JavaPlugin
{
    public static Logger logger;
    public static CryptOfTheNecroDancer plugin;
    public static SongPlayer playingSong;

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
        getServer().getPluginCommand("playsong").setExecutor(new PlaySong());
        getServer().getPluginCommand("playsong").setTabCompleter(new PlaySong());
        getServer().getPluginCommand("stopsong").setExecutor(new StopSong());

        Bukkit.getPluginManager().registerEvents(new Events(), this);

        logger.info("CryptOfNecrodancerは正常にアクティベートされました！");
    }

    @Override
    public void onDisable()
    {
    }
}
