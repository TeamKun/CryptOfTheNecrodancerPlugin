package net.kunmc.lab.cryptofnecromancer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        if (CryptOfNecromancer.playingSong != null)
            CryptOfNecromancer.playingSong.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        if (CryptOfNecromancer.playingSong != null)
            CryptOfNecromancer.playingSong.removePlayer(event.getPlayer());
    }
}
