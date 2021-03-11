package net.kunmc.lab.cryptofnecromancer;

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
        timer.runTaskTimer(CryptOfNecromancer.plugin, (long) song.getDelay(), 1);
    }

    public void stop() {

    }

    private class GameTimer extends BukkitRunnable
    {
        private long prevTime = System.currentTimeMillis();
        private final long bpm = (long) (song.getSpeed() * 7.67f);

        @Override
        public void run()
        {
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
    }

}
