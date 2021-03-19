package net.kunmc.lab.cryptofthenecrodancer;

import net.kunmc.lab.cryptofthenecrodancer.judger.ActionType;
import net.kunmc.lab.cryptofthenecrodancer.judger.Judge;
import net.kunmc.lab.cryptofthenecrodancer.judger.JudgeResult;
import net.kunmc.lab.cryptofthenecrodancer.judger.Judger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
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

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        Projectile entity = event.getEntity();
        if (!(entity.getShooter() instanceof Player))
            return;

        JudgeResult result = Judger.onPlayerAction(ActionType.MOVE_GROUND, (Player) entity.getShooter());

        event.setCancelled(result.isCancel);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player))
            return;

        Player attacker = (Player) event.getDamager();

        if (attacker.getGameMode() != GameMode.SURVIVAL && attacker.getGameMode() != GameMode.ADVENTURE)
            return;

        //TODO: 体力管理つくる

        JudgeResult result = Judger.onPlayerAction(ActionType.MOVE_GROUND, attacker);

        event.setCancelled(result.isCancel);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event)
    {
        if (CryptOfTheNecroDancer.game == null || !CryptOfTheNecroDancer.game.isRunning())
            return;

        if (!(event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE) &&
                !event.getPlayer().isOnGround())
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

        event.setCancelled(result.isCancel);

        lastMoveTime.put(event.getPlayer(), current);
    }

}
