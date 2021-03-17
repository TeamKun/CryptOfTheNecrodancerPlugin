package net.kunmc.lab.cryptofthenecrodancer;

import net.kunmc.lab.cryptofthenecrodancer.nbs.Music;
import net.kunmc.lab.cryptofthenecrodancer.nbs.Note;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class Game {
    private final Music music;
    private final List<Player> players = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private boolean running;
    private MusicPlayer musicPlayer;

    public Game(Music music) {
        this.music = music;
        this.running = false;
    }

    public void run() {
        if (running) {
            return;
        }

        Plugin plugin = CryptOfTheNecroDancer.plugin;
        musicPlayer = new MusicPlayer();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, musicPlayer);

        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }

        musicPlayer = null;
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void addPlayer(Player player) {
        lock.lock();
        try {
            if (players.contains(player)) {
                return;
            }
            players.add(player);
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Player player) {
        lock.lock();
        try {
            if (!players.contains(player)) {
                return;
            }
            players.remove(player);
        } finally {
            lock.unlock();
        }
    }

    private class MusicPlayer implements Runnable {
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
        private final Consumer<Player> function;
        private int tick;

        public MusicPlayer() {
            this(null);
        }

        public MusicPlayer(Consumer<Player> function) {
            this.function = function;
        }

        @Override
        public void run() {
            while (isRunning()) {
                long startTime = System.currentTimeMillis();

                lock.lock();
                try {
                    if (tick > music.getLength()) {
                        return;
                    }

                    players.forEach(player -> {
                        function.accept(player);
                        play(player, tick);
                    });
                } finally {
                    lock.unlock();
                }

                while (System.currentTimeMillis() - startTime < 1000 / music.getTempo()) {
                    // do nothing.
                }
                tick++;
            }
        }

        private void play(Player player, int tick) {
            music.getLayers().values().stream()
                    .filter(layer -> layer.getNote(tick) != null)
                    .forEach(layer -> {
                        Note note = layer.getNote(tick);
                        Sound sound = instruments.get(0);
                        if (note.getInstrument() < instruments.size()) {
                            sound = instruments.get(note.getInstrument());
                        }
                        float volume = layer.getVolume() * note.getVolume();
                        player.playSound(player.getLocation(), sound, volume, note.getPitch());
                    });
        }
    }
}
