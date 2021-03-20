package net.kunmc.lab.cryptofthenecrodancer.utils;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TitleNotification
{
    private final Map<UUID, String> mainTitleMap;
    private final Map<UUID, String> subTitleMap;
    private final List<UUID> isShowing;

    public TitleNotification()
    {
        this.mainTitleMap = new HashMap<>();
        this.subTitleMap = new HashMap<>();
        this.isShowing = new ArrayList<>();
    }

    public void setSubTitle(Player player, String string)
    {
        isShowing.add(player.getUniqueId());
        subTitleMap.put(player.getUniqueId(), string);

        player.sendTitle(
                mainTitleMap.getOrDefault(player.getUniqueId(), ""),
                string,
                0,
                10,
                0
        );

        new BukkitRunnable()
        {
            private final UUID uuid = player.getUniqueId();
            private int time = 0;
            @Override
            public void run()
            {
                if (!mainTitleMap.containsKey(uuid) && (!isShowing.contains(uuid) || !subTitleMap.containsKey(uuid)))
                {
                    isShowing.remove(uuid);
                    this.cancel();
                    return;
                }
                if (time++ < 10)
                    return;
                subTitleMap.remove(uuid);

                if (!mainTitleMap.containsKey(uuid))
                    isShowing.remove(uuid);
                this.cancel();
            }
        }.runTaskTimer(CryptOfTheNecroDancer.plugin, 0, 1);
    }


    public void setMainTitle(Player player, String string)
    {
        isShowing.add(player.getUniqueId());
        mainTitleMap.put(player.getUniqueId(), string);

        player.sendTitle(
                string,
                subTitleMap.getOrDefault(player.getUniqueId(), ""),
                0,
                10,
                0
        );

        new BukkitRunnable()
        {
            private UUID uuid = player.getUniqueId();
            private int time = 0;
            @Override
            public void run()
            {
                if (!subTitleMap.containsKey(uuid) && (!isShowing.contains(uuid) || !mainTitleMap.containsKey(uuid)))
                {
                    isShowing.remove(uuid);
                    this.cancel();
                    return;
                }
                if (time++ < 10)
                    return;
                mainTitleMap.remove(uuid);
                if (!subTitleMap.containsKey(uuid))
                    isShowing.remove(uuid);
            }
        }.runTaskTimer(CryptOfTheNecroDancer.plugin, 0, 1);
    }
}
