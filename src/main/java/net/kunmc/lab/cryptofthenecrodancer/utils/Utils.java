package net.kunmc.lab.cryptofthenecrodancer.utils;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import net.kunmc.lab.cryptofthenecrodancer.nbs.Music;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils
{
    public static ChatColor getSnipetColor(int val)
    {
        if (val < 3)
            return ChatColor.RED;
        else if (val < 7)
            return ChatColor.BLUE;
        else
            return ChatColor.GREEN;
    }

    public static void setMetadata(Block block, String name, Object value)
    {
        block.setMetadata(CryptOfTheNecroDancer.NAMESPACE_KEY + ":" + name,
                new FixedMetadataValue(CryptOfTheNecroDancer.plugin, value)
        );
    }

    public static <T> T getBlockMeta(Block block, String name)
    {
        for (MetadataValue val: block.getMetadata(name))
        {
            if (!val.getOwningPlugin().getName().equals(CryptOfTheNecroDancer.plugin.getName()))
                continue;
            return (T) val.value();
        }

        return null;
    }

    public static Music asMusic(URL url)
    {
        HttpURLConnection connection = null;
        try
        {
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/8.10; Safari/Chrome/Opera/Edge/KungleBot-Peyang; Mobile-Desktop");
            connection.connect();

            try (InputStream stream = connection.getInputStream())
            {
                return new Music(stream);
            }
        }
        catch (IOException e)
        {
            return null;
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }

    public static Vector loc2vec(Location from, Location to) {
        return new Vector((to.getX() - from.getX()), to.getY() - from.getY(), (to.getZ() - from.getZ()));
    }

    /*public static Vector dir2vec(Location center, Location as)
    {
        Vector vecCenter = center.toVector().normalize();
        Vector vecAs = as.toVector().normalize();

        Vector vecResult = vecAs.subtract(vecCenter);

        //vecResult.setX(vecCenter.getX());
        //vecResult.setZ(vecCenter.getZ());
        vecResult.setY(vecResult.getY() + 0.1);
        vecResult.normalize();

        return vecResult;//.multiply(3);

    }*/
}
