package net.kunmc.lab.cryptofnecromancer;

import net.kunmc.lab.cryptofnecromancer.commands.CommandMain;
import org.bukkit.plugin.java.JavaPlugin;

public final class CryptOfNecromancer extends JavaPlugin
{
    public static CryptOfNecromancer instance;

    @Override
    public void onEnable()
    {
        instance = this;

        CommandMain command = new CommandMain();
        getServer().getPluginCommand("crypt").setExecutor(command);
        getServer().getPluginCommand("crypt").setTabCompleter(command);
    }

    @Override
    public void onDisable()
    {
    }
}
