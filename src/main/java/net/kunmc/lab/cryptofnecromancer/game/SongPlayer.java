package net.kunmc.lab.cryptofnecromancer.game;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import org.bukkit.entity.Player;

public class SongPlayer extends RadioSongPlayer
{
    public SongPlayer(Song song)
    {
        super(song);
    }

    @Override
    public void playTick(Player player, int tick)
    {
        super.playTick(player, tick);
        Sounds.isStarted = true;
    }
}
