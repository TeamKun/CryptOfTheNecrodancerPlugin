package net.kunmc.lab.cryptofthenecrodancer.commands;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import net.kunmc.lab.cryptofthenecrodancer.enums.PlayType;
import net.kunmc.lab.cryptofthenecrodancer.utils.URLUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlaySong implements CommandExecutor, TabCompleter
{
    public static SongPlayer currentSong;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!sender.hasPermission("crypt.dbg.playsong"))
        {
            sender.sendMessage(ChatColor.RED + "あなたには権限が有りません！");
            return true;
        }

        if (args.length < 2)
        {
            sender.sendMessage(ChatColor.RED + "引数が不足しています！ 使用法：/playsong SELF|LOCAL_BROADCAST URL://");
            return true;
        }

        PlayType type;
        URL url;
        try
        {
            url = new URL(args[1]);
            type = PlayType.valueOf(args[0]);
        }
        catch (Exception e)
        {
            sender.sendMessage(ChatColor.RED + "引数がおかしいです！ 使用法：/playsong SELF|LOCAL_BROADCAST URL://");
            return true;
        }

        Song song = URLUtils.asSong(url);

        if (song == null)
        {
            sender.sendMessage(ChatColor.RED + "曲の読み込みに失敗しました！");
            return true;
        }

        SongPlayer player;
        switch (type)
        {
            case LOCAL_BLOADCAST:
                player = new RadioSongPlayer(song);
                Bukkit.getOnlinePlayers().stream().parallel().forEach(player::addPlayer);
                break;
            case SELF:
                player = new RadioSongPlayer(song);
                if (!(sender instanceof Player))
                {
                    sender.sendMessage(ChatColor.RED + "プレイヤーから実行する必要があります！");
                    return true;
                }
                player.addPlayer((Player) sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "再生方法が不明です。");
                return true;
        }

        if (currentSong != null)
            currentSong.setPlaying(false);

        sender.sendMessage(ChatColor.GREEN + "再生中：" + song.getTitle());

        currentSong = player;
        player.setPlaying(true);
        player.setRepeatMode(RepeatMode.ONE);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        ArrayList<String> completes = new ArrayList<>();

        if (!sender.hasPermission("con.dbg.playsong"))
            return completes;

        switch (args.length)
        {
            case 1:
                completes.addAll(Arrays.stream(PlayType.values()).parallel().map(PlayType::toString).collect(Collectors.toList()));
                break;
            case 2:
                completes.add("http://");
        }

        ArrayList<String> asCopy = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], completes, asCopy);
        Collections.sort(asCopy);
        return asCopy;

    }
}
