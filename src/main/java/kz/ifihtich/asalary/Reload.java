package kz.ifihtich.asalary;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Reload implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) return true;
        if (!commandSender.hasPermission("asalary.reload")) return true;
        if (strings[0].equalsIgnoreCase("reload")){
            ASalary.getInstance().onReload();
            commandSender.sendMessage(Utils.color(ASalary.getInstance().getConfig().getString("messages.reload")));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 1 || !commandSender.hasPermission("asalary.reload")) return Collections.emptyList();
        return Collections.singletonList("reload");
    }
}
