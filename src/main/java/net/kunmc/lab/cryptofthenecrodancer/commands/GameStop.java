package net.kunmc.lab.cryptofthenecrodancer.commands;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class GameStop implements CommandBase
{
    @Override
    public String getName()
    {
        return "stop";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args)
    {
        if (CryptOfTheNecroDancer.currentGame == null || !CryptOfTheNecroDancer.currentGame.isStarted())
        {
            sender.sendMessage(ChatColor.RED + "エラー！既にゲームは停止しています。");
            return true;
        }

        CryptOfTheNecroDancer.currentGame.stop();
        CryptOfTheNecroDancer.currentGame = null;
        sender.sendMessage(ChatColor.GREEN + "ゲームを正常に停止しました。");

        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args)
    {
        return new ArrayList<>();
    }
}
