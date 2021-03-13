package net.kunmc.lab.cryptofthenecrodancer;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.NoteBlockSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Game
{
    private boolean hasStarted;
    private final Song song;
    private GameTimer timer;
    private SongPlayer player;

    public Game(Song song) {
        this.song = song;
        this.hasStarted = false;
    }

    public void startGame() {
        if (timer != null)
            return;

        player = new NoteBlockSongPlayer(song)
        {
            @Override
            public void playTick(Player player, int tick)
            {
                hasStarted = true;
                super.playTick(player, tick);
            }
        };
        player.setRepeatMode(RepeatMode.ONE); //ループ
        Bukkit.getOnlinePlayers().stream().parallel().forEach(player::addPlayer);
        //↑プレイヤー強制参加
        Bukkit.broadcastMessage("tempo:" + song.getSpeed());
        Bukkit.broadcastMessage("height:" + song.getSongHeight());

        player.setPlaying(true);
        timer = new GameTimer();
        timer.runTaskTimer(CryptOfTheNecroDancer.plugin, (long) song.getDelay(), 1);
    }

    public void stop() {
        if (timer == null) {
            return;
        }

        player.setPlaying(false);
        timer.cancel();
        timer = null;
        hasStarted = false;
    }

    public boolean isStarted()
    {
        return hasStarted;
    }

    public void addPlayer(Player p)
    {
        this.player.addPlayer(p);
    }

    public void removePlayer(Player p)
    {
        this.player.removePlayer(p);
    }

    private class GameTimer extends BukkitRunnable
    {
        private long prevTime = System.currentTimeMillis();
        private final long bpm = (long) (song.getSpeed() * 7.67f);

        @Override
        public void run()
        {
            if (!hasStarted)
                return;

            if (player == null)
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
