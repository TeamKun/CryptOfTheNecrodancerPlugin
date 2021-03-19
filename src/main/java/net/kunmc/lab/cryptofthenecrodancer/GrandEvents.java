package net.kunmc.lab.cryptofthenecrodancer;

import net.kunmc.lab.cryptofthenecrodancer.judger.ActionType;
import net.kunmc.lab.cryptofthenecrodancer.judger.JudgeResult;
import net.kunmc.lab.cryptofthenecrodancer.judger.Judger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 * 特にメソッドを肥大化させることのないイベント郡
 * 極力イベント親クラスを使用し、メソッドを減らす
 */
public class GrandEvents implements Listener
{
    @EventHandler(ignoreCancelled = true)
    public void blockPlace(BlockPlaceEvent event) { event.setCancelled(grandEventHandler(event.getPlayer())); }

    @EventHandler(ignoreCancelled = true)
    public void intEntity(PlayerInteractEntityEvent event) { event.setCancelled(grandEventHandler(event.getPlayer())); }

    @EventHandler(ignoreCancelled = true)
    public void leaveBed(PlayerBedLeaveEvent event) { grandEventHandler(event.getPlayer()); } //Cancelないからジャッジのみ

    @EventHandler(ignoreCancelled = true)
    public void enterBed(PlayerBedEnterEvent event) { event.setCancelled(grandEventHandler(event.getPlayer())); }

    @EventHandler(ignoreCancelled = true)
    public void changeBucketState(PlayerBucketEmptyEvent event) { event.setCancelled(grandEventHandler(event.getPlayer())); }

    @EventHandler(ignoreCancelled = true)
    public void changeBucketState(PlayerBucketFillEvent event) { event.setCancelled(grandEventHandler(event.getPlayer())); }

    @EventHandler(ignoreCancelled = true)
    public void dropItem(PlayerDropItemEvent event) { event.setCancelled(grandEventHandler(event.getPlayer())); }

    @EventHandler(ignoreCancelled = true)
    public void shearEntity(PlayerShearEntityEvent event) { event.setCancelled(grandEventHandler(event.getPlayer())); }

    @EventHandler(ignoreCancelled = true)
    public void sneak(PlayerToggleSneakEvent event) { grandEventHandler(event.getPlayer()); } //スニークキャンセルとかバグる

    private static boolean grandEventHandler(Player p)
    {
        JudgeResult result = Judger.onPlayerAction(ActionType.OTHER, p);
        return result.isCancel;
    }
}
