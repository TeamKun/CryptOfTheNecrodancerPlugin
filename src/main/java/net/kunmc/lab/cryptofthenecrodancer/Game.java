package net.kunmc.lab.cryptofthenecrodancer;

import net.kunmc.lab.cryptofthenecrodancer.enums.Judge;
import net.kunmc.lab.cryptofthenecrodancer.nbs.Music;
import net.kunmc.lab.cryptofthenecrodancer.nbs.Note;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Game
{
    private final Music music;
    private final List<Player> players;
    private final List<Player> activePlayers;
    private final List<Player> judgedPlayers;
    private final Lock lock = new ReentrantLock();
    private boolean running;
    private MusicPlayer musicPlayer;
    private long judgeTime1;
    private long judgeTime2;

    public Game(Music music)
    {
        this.music = music;
        players = new ArrayList<>(Bukkit.getOnlinePlayers());
        activePlayers = new ArrayList<>();
        judgedPlayers = new ArrayList<>();
        running = false;
        judgeTime1 = -1;
        judgeTime2 = -1;
    }

    public void run()
    {
        if (running)
            return;

        Plugin plugin = CryptOfTheNecroDancer.plugin;
        musicPlayer = new MusicPlayer();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, musicPlayer);

        running = true;
    }

    public void stop()
    {
        if (!running)
            return;

        musicPlayer = null;
        running = false;
    }

    public boolean isRunning()
    {
        return running;
    }

    public void addPlayer(Player player)
    {
        lock.lock();
        try
        {
            if (!players.contains(player))
                players.add(player);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void removePlayer(Player player)
    {
        lock.lock();
        try
        {
            players.remove(player);
            activePlayers.remove(player);
        }
        finally
        {
            lock.unlock();
        }
    }

    public List<Player> getNotJudgedPlayers()
    {
        return activePlayers.stream().parallel().filter(player -> !judgedPlayers.contains(player)).collect(Collectors.toList());
    }

    public Judge judge(Player p)
    {
        if (!activePlayers.contains(p))
            activePlayers.add(p);
        judgedPlayers.add(p);

        long time1 = Math.abs(System.currentTimeMillis() - judgeTime1);
        long time2 = Math.abs(System.currentTimeMillis() - judgeTime2);

        if (time1 < Judge.PERFECT.getJudgeTime() || time2 < Judge.PERFECT.getJudgeTime())
            return Judge.PERFECT;
        else if (time1 < Judge.GREAT.getJudgeTime() || time2 < Judge.GREAT.getJudgeTime())
            return Judge.GREAT;
        else if (time1 < Judge.GOOD.getJudgeTime() || time2 < Judge.GOOD.getJudgeTime())
            return Judge.GOOD;
        else
            return Judge.MISS;
    }

    private class MusicPlayer implements Runnable
    {
        private final List<Sound> instruments = Arrays.asList(
                Sound.BLOCK_NOTE_BLOCK_HARP,
                Sound.BLOCK_NOTE_BLOCK_BASS,
                Sound.BLOCK_NOTE_BLOCK_BASEDRUM,
                Sound.BLOCK_NOTE_BLOCK_SNARE,
                Sound.BLOCK_NOTE_BLOCK_HAT,
                Sound.BLOCK_NOTE_BLOCK_GUITAR,
                Sound.BLOCK_NOTE_BLOCK_FLUTE,
                Sound.BLOCK_NOTE_BLOCK_BELL,
                Sound.BLOCK_NOTE_BLOCK_CHIME,
                Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,
                Sound.BLOCK_NOTE_BLOCK_PLING,
                Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,
                Sound.BLOCK_NOTE_BLOCK_COW_BELL,
                Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,
                Sound.BLOCK_NOTE_BLOCK_BIT,
                Sound.BLOCK_NOTE_BLOCK_BANJO
        );
        private int tick;

        public MusicPlayer()
        {

        }

        @Override
        public void run()
        {
            while (isRunning())
            {
                long startTime = System.currentTimeMillis();

                lock.lock();
                try
                {
                    if (tick > music.getLength())
                    {
                        running = false;
                        return;
                    }

                    players.forEach(player -> {
                        play(player, tick);
                    });
                    if (tick % (music.getTimeSignature() * 2) == 0)
                    {
                        onBeat(null);
                        judgedPlayers.clear();
                    }
                }
                finally
                {
                    lock.unlock();
                }

                try
                {
                    if ((1000 / music.getTempo()) > (System.currentTimeMillis() - startTime))
                        Thread.sleep((long) (1000 / music.getTempo()) - (System.currentTimeMillis() - startTime));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                tick++;
            }
        }

        private void onBeat(Player player)
        {
            if (player != null)
            {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1.0f, 1.0f);
                return;
            }

            getNotJudgedPlayers().stream().map(a -> "BEAT-SKIP").forEach(Bukkit::broadcastMessage);
        }

        private void play(Player player, int tick)
        {
            if (tick % (music.getTimeSignature() * 2) == 0)
            {
                // メトロノーム(メソッド分ける)
                judgeTime1 = System.currentTimeMillis();
                judgeTime2 = judgeTime1 + (long) (1000 / music.getTempo() * music.getTimeSignature() * 2);
                onBeat(player);
            }

            music.getLayers().values().stream()
                    .filter(layer -> layer.getNote(tick) != null)
                    .forEach(layer -> {
                        Note note = layer.getNote(tick);
                        Sound sound = instruments.get(0);
                        if (note.getInstrument() < instruments.size())
                            sound = instruments.get(note.getInstrument());

                        float volume = layer.getVolume() * note.getVolume();
                        player.playSound(player.getLocation(), sound, volume, note.getTransposedPitch());
                    });
        }
    }
}
