package net.kunmc.lab.cryptofthenecrodancer.utils;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import net.kunmc.lab.cryptofthenecrodancer.nbs.Music;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

    private static final Vector[] definedVectors = {BlockFace.NORTH.getDirection(),
            BlockFace.EAST.getDirection(),
            BlockFace.SOUTH.getDirection(),
            BlockFace.WEST.getDirection()};
    private static final double limit = 0.5;


    public static Vector getMoveVec(Vector moved)
    {
        Vector cl = null;
        double lowestLimit = -Double.MIN_VALUE;

        for (Vector vector: definedVectors)
        {
            double tempDir = vector.dot(moved);
            if (tempDir >= limit)
                return vector;
            else if (tempDir > lowestLimit)
            {
                lowestLimit = tempDir;
                cl = vector;
            }
        }

        return cl;
    }
}
