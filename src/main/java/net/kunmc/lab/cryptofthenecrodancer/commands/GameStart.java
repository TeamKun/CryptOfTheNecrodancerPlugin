package net.kunmc.lab.cryptofthenecrodancer.commands;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.kunmc.lab.cryptofthenecrodancer.game.Sounds;
import net.kunmc.lab.cryptofthenecrodancer.utils.URLUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameStart implements CommandBase
{
    @Override
    public String getName()
    {
        return "start";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(ChatColor.RED + "引数が不足しています！ 使用法：/constart URL://");
            return true;
        }

        URL url;
        try
        {
            url = new URL(args[0]);
        }
        catch (Exception e)
        {
            sender.sendMessage(ChatColor.RED + "引数がおかしいです！ 使用法：/constart URL://");
            return true;
        }

        Song song = URLUtils.asSong(url);

        if (song == null)
        {
            sender.sendMessage(ChatColor.RED + "曲の読み込みに失敗しました！");
            return true;
        }

        Sounds.startGame(song);
        return true;
    }


    @Override
    public List<String> onTabComplete(String[] args)
    {
        ArrayList<String> completes = new ArrayList<>();


        if (args.length == 1)
            completes.add("http://");

        ArrayList<String> asCopy = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], completes, asCopy);
        Collections.sort(asCopy);
        return asCopy;

    }
}
