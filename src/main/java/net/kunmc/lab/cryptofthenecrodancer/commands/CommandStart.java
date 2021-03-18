package net.kunmc.lab.cryptofthenecrodancer.commands;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import net.kunmc.lab.cryptofthenecrodancer.Game;
import net.kunmc.lab.cryptofthenecrodancer.nbs.Music;
import net.kunmc.lab.cryptofthenecrodancer.utils.URLUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class CommandStart implements CommandBase
{

    @Override
    public String getName()
    {
        return "start";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args)
    {
        if (CryptOfTheNecroDancer.game != null && CryptOfTheNecroDancer.game.isRunning())
        {
            sender.sendMessage(ChatColor.RED + "既に開始されています。");
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(ChatColor.RED + "引数の形式が間違っています。");
            return true;
        }

        Music music;
        try
        {
            music = URLUtils.asMusic(new URL(args[0]));
        }
        catch (MalformedURLException e)
        {
            sender.sendMessage(ChatColor.RED + "引数の形式が間違っています。");
            return true;
        }

        if (music == null)
        {
            sender.sendMessage(ChatColor.RED + "曲が読み込めません！");
        }

        CryptOfTheNecroDancer.game = new Game(music);
        CryptOfTheNecroDancer.game.run();
        sender.sendMessage(ChatColor.GREEN + "ゲームを開始します！");

        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 1:
                return Collections.singletonList("<url>");
            default:
                return Collections.emptyList();
        }
    }
}
