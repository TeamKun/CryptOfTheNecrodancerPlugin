package net.kunmc.lab.cryptofthenecrodancer.utils;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLUtils
{
    public static Song asSong(URL url)
    {
        HttpURLConnection con = null;
        try
        {

            con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/8.10; Safari/Chrome/Opera/Edge/KungleBot-Peyang; Mobile-Desktop");
            con.connect();

            try(InputStream s = con.getInputStream())
            {
                return NBSDecoder.parse(s);
            }
        }
        catch (Exception e)
        {
            return null;
        }
        finally
        {
            if (con != null)
                con.disconnect();
        }
    }
}
