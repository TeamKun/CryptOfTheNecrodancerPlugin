package net.kunmc.lab.cryptofthenecrodancer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class Events implements Listener
{
    private final HashMap<Player, Long> lastMoveTime = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        if (CryptOfTheNecroDancer.game != null) {
            CryptOfTheNecroDancer.game.addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        if (CryptOfTheNecroDancer.game != null) {
            CryptOfTheNecroDancer.game.removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (CryptOfTheNecroDancer.game == null || !CryptOfTheNecroDancer.game.isRunning()) {
            return;
        }

        if (!event.getPlayer().isOnGround()) {
            return;
        }

        long last = lastMoveTime.containsKey(event.getPlayer()) ? lastMoveTime.get(event.getPlayer()) : 0;
        long current = System.currentTimeMillis();

        if (current - last < 200) {
            event.setCancelled(true);
            return;
        }

        switch (CryptOfTheNecroDancer.game.judge()) {
            case PERFECT:
                event.getPlayer().sendMessage("PERFECT");
                break;
            case GREAT:
                event.getPlayer().sendMessage("GREAT");
                break;
            case GOOD:
                event.getPlayer().sendMessage("GOOD");
                break;
            case MISS:
                event.getPlayer().sendMessage("MISS");
                break;
            default:
                break;
        }

        lastMoveTime.put(event.getPlayer(), current);
    }
}
