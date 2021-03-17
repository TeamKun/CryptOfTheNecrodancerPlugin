package net.kunmc.lab.cryptofthenecrodancer.utils;

import net.kunmc.lab.cryptofthenecrodancer.nbs.Music;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLUtils
{
    public static Music asMusic(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/8.10; Safari/Chrome/Opera/Edge/KungleBot-Peyang; Mobile-Desktop");
            connection.connect();

            try (InputStream stream = connection.getInputStream()) {
                return new Music(stream);
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
