package net.kunmc.lab.cryptofthenecrodancer;

import net.kunmc.lab.cryptofthenecrodancer.judger.ActionType;
import net.kunmc.lab.cryptofthenecrodancer.judger.Judge;
import net.kunmc.lab.cryptofthenecrodancer.judger.JudgeResult;
import net.kunmc.lab.cryptofthenecrodancer.judger.Judger;
import net.kunmc.lab.cryptofthenecrodancer.utils.Calculator;
import net.kunmc.lab.cryptofthenecrodancer.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

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


    @EventHandler(ignoreCancelled = true)
    @SuppressWarnings("ConstantConditions")
    public void onBreak(PlayerInteractEvent event)
    {
        if (CryptOfTheNecroDancer.game == null || !CryptOfTheNecroDancer.game.isRunning())
            return;

        if (event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        Block block = event.getClickedBlock();

        Player player = event.getPlayer();

        if (block == null)
            return;

        if (block.getType() == Material.WATER || block.getType() == Material.LAVA)
            return;

        event.setCancelled(true);

        Judge judge = CryptOfTheNecroDancer.game.judge(player);

        //ダミージャッジ (BEAT_SKIP回避,MISS以外は表示しないため)
        if (judge  == Judge.MISS)
        {
            Judger.showTitleAndActionbar(player, Judge.MISS);
            return;
        }

        int count = 0;

        if (block.hasMetadata(CryptOfTheNecroDancer.NAMESPACE_KEY + ":count"))
            count = Utils.getBlockMeta(block, CryptOfTheNecroDancer.NAMESPACE_KEY + ":count");

        int targetCount;

        if (block.hasMetadata(CryptOfTheNecroDancer.NAMESPACE_KEY + ":targetCount"))
            targetCount = Utils.getBlockMeta(block, CryptOfTheNecroDancer.NAMESPACE_KEY + ":targetCount");
        else
         targetCount = Calculator.calculateBlockAll(block, player.getInventory().getItemInMainHand());

        UUID playerUniqueId = null;

        if (block.hasMetadata(CryptOfTheNecroDancer.NAMESPACE_KEY + ":player"))
            playerUniqueId = Utils.getBlockMeta(block, CryptOfTheNecroDancer.NAMESPACE_KEY + ":player");

        count++;

        String baseText = ChatColor.GREEN + "破壊まであと %s " + ChatColor.GREEN + "回";


        if (targetCount - count < 1)
        {
            block.breakNaturally();
            return;
        }

        if (playerUniqueId == null || playerUniqueId != player.getUniqueId())
        {
            Utils.setMetadata(block, "count", 1);
            Utils.setMetadata(block, "targetCount", targetCount);
            Utils.setMetadata(block, "player", player.getUniqueId());

            CryptOfTheNecroDancer.game.blockModify(block.getLocation());
            CryptOfTheNecroDancer.titleShower.setSubTitle(player, String.format(baseText, Utils.getSnipetColor( targetCount - 1).toString() + (targetCount - 1)));
            return;
        }

        Utils.setMetadata(block, "count", count);
        CryptOfTheNecroDancer.titleShower.setSubTitle(player, String.format(baseText, Utils.getSnipetColor( targetCount - count).toString() + (targetCount - count)));
        CryptOfTheNecroDancer.game.blockModify(block.getLocation());
    }
}
