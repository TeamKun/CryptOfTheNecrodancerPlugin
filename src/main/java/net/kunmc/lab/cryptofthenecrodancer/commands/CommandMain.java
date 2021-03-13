package net.kunmc.lab.cryptofthenecrodancer.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Collectors;

public class CommandMain implements CommandExecutor, TabCompleter {

    private final List<CommandBase> commands = new ArrayList<>();

    public CommandMain() {
        commands.add(new GameStart());
        commands.add(new GameStop());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("crypt.command")) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "コマンドの引数が足りないようです。");
            return true;
        }

        Optional<CommandBase> selectCommand = commands.stream()
                .parallel()
                .filter(c -> c.getName().equalsIgnoreCase(args[0]))
                .findAny();

        if (!selectCommand.isPresent()) {
            sender.sendMessage(ChatColor.RED + "そのようなコマンドは存在しません。");
            return true;
        }

        return selectCommand.get().onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result;

        if (!sender.hasPermission("crypt.command")) {
            return Collections.emptyList();
        }

        switch (args.length) {
            case 0:
                return Collections.emptyList();
            case 1:
                result = new ArrayList<>();
                commands.forEach(c -> result.add(c.getName()));
                break;
            default:
                {
                    Optional<CommandBase> selectCommand = commands.stream()
                            .parallel()
                            .filter(c -> c.getName().equalsIgnoreCase(args[0]))
                            .findAny();

                    if (!selectCommand.isPresent()) {
                        return Collections.emptyList();
                    }

                    result = selectCommand.get().onTabComplete(Arrays.copyOfRange(args, 1, args.length));
                }
                break;
        }

        return result.stream()
                .parallel()
                .filter(str -> str.startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

}
