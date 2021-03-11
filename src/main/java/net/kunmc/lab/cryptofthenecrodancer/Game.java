package net.kunmc.lab.cryptofthenecrodancer;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class Game
{
    private final Song song;
    private GameTimer timer;

    public Game(Song song) {
        this.song = song;
    }

    public void run() {
        if (timer != null) {
            return;
        }

        timer = new GameTimer();
        timer.runTaskTimer(CryptOfTheNecroDancer.plugin, (long) song.getDelay(), 1);
    }

    public void stop() {
        if (timer == null) {
            return;
        }

        timer.cancel();
        timer = null;
    }

    private class GameTimer extends BukkitRunnable
    {
        private final long bpm = (long) (song.getSpeed() * 15);
        private long prevTime = System.currentTimeMillis();

        @Override
        public void run()
        {
            if (CryptOfTheNecroDancer.playingSong == null)
            {
                this.cancel();
                return;
            }

            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - prevTime;

            if (elapsedTime > 60 / bpm * 1000) {
                Bukkit.getOnlinePlayers()
                        .forEach(player -> player.playSound(player.getLocation(),
                                Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1.0f, 1.0f));
                prevTime = currentTime + elapsedTime - 60 / bpm * 1000;
            }
        }
    }

}
