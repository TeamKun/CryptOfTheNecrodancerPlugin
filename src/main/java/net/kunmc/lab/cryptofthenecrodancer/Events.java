package net.kunmc.lab.cryptofthenecrodancer;

import net.kunmc.lab.cryptofthenecrodancer.judger.ActionType;
import net.kunmc.lab.cryptofthenecrodancer.judger.Judge;
import net.kunmc.lab.cryptofthenecrodancer.judger.JudgeResult;
import net.kunmc.lab.cryptofthenecrodancer.judger.Judger;
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
        if (CryptOfTheNecroDancer.game != null)
            CryptOfTheNecroDancer.game.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        if (CryptOfTheNecroDancer.game != null)
            CryptOfTheNecroDancer.game.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        if (CryptOfTheNecroDancer.game == null || !CryptOfTheNecroDancer.game.isRunning())
            return;

        if (!event.getPlayer().isOnGround())
            return;

        if (Math.abs(event.getTo().getX() - event.getFrom().getX()) < Double.MIN_VALUE * 20.0 &&
                Math.abs(event.getTo().getY() - event.getFrom().getY()) < Double.MIN_VALUE * 20.0 &&
                Math.abs(event.getTo().getZ() - event.getFrom().getZ()) < Double.MIN_VALUE * 20.0)
            return;

        long last = lastMoveTime.containsKey(event.getPlayer()) ? lastMoveTime.get(event.getPlayer()): 0;
        long current = System.currentTimeMillis();

        if (current - last < 200)
        {
            event.setCancelled(true);
            return;
        }

        JudgeResult result = Judger.onPlayerAction(ActionType.MOVE_GROUND, event.getPlayer());

        if (result.isCancel)
            event.setCancelled(true);

        lastMoveTime.put(event.getPlayer(), current);
    }
}
