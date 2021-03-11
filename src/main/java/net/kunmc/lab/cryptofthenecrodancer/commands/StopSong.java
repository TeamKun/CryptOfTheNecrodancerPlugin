package net.kunmc.lab.cryptofthenecrodancer.commands;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopSong implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!sender.hasPermission("crypt.dbg.stopsong"))
        {
            sender.sendMessage(ChatColor.RED + "あなたには権限が有りません！");
            return true;
        }
        if (CryptOfTheNecroDancer.playingSong != null)
            CryptOfTheNecroDancer.playingSong.setPlaying(false);
        CryptOfTheNecroDancer.playingSong = null;
        sender.sendMessage(ChatColor.GREEN + "再生中の曲を停止しました。");
        return true;
    }
}
