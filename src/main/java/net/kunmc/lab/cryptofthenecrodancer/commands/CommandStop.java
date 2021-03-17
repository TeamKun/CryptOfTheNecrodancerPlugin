package net.kunmc.lab.cryptofthenecrodancer.commands;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class CommandStop implements CommandBase {
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (CryptOfTheNecroDancer.game == null || !CryptOfTheNecroDancer.game.isRunning()) {
            sender.sendMessage(ChatColor.RED + "ゲームが開始されていないようです。");
            return true;
        }

        CryptOfTheNecroDancer.game.stop();
        CryptOfTheNecroDancer.game = null;

        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return Collections.emptyList();
    }
}
