package net.kunmc.lab.cryptofthenecrodancer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener
{
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
}
