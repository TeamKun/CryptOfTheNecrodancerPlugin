package net.kunmc.lab.cryptofnecromancer.game;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.kunmc.lab.cryptofnecromancer.CryptOfNecromancer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class Sounds
{
    public static volatile boolean isStarted = false;
    
    public static void startGame(Song song)
    {

        if (CryptOfNecromancer.playingSong != null)
            CryptOfNecromancer.playingSong.setPlaying(false);

        CryptOfNecromancer.playingSong = new SongPlayer(song);
        CryptOfNecromancer.playingSong.setRepeatMode(RepeatMode.ONE);
        Bukkit.getOnlinePlayers().stream().parallel().forEach(CryptOfNecromancer.playingSong::addPlayer);
        CryptOfNecromancer.playingSong.setPlaying(true);

        Bukkit.broadcastMessage("tempo:" + song.getSpeed());
        Bukkit.broadcastMessage("height:" + song.getSongHeight());

        new BukkitRunnable()
        {
            private long prevTime = System.currentTimeMillis();
            private final long bpm = (long) (song.getSpeed() * 7.67f);

            @Override
            public void run()
            {
                if (!isStarted)
                    return;

                if (CryptOfNecromancer.playingSong == null)
                {
                    this.cancel();
                    return;
                }

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - prevTime;

                if (elapsedTime > 60 * 1000 / bpm) {
                    Bukkit.getOnlinePlayers()
                            .forEach(player -> player.playSound(player.getLocation(),
                                    Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1.0f, 1.0f));
                    prevTime = currentTime + elapsedTime - 60 * 1000 / bpm;
                }
            }
        }.runTaskTimer(CryptOfNecromancer.plugin, (long) song.getDelay(), 1);
    }
}
