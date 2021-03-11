package net.kunmc.lab.cryptofnecromancer.commands;

import net.kunmc.lab.cryptofnecromancer.CryptOfNecromancer;
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
        if (CryptOfNecromancer.playingSong != null)
            CryptOfNecromancer.playingSong.setPlaying(false);
        CryptOfNecromancer.playingSong = null;
        sender.sendMessage(ChatColor.GREEN + "再生中の曲を停止しました。");
        return true;
    }
}
