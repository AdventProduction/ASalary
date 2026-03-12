package kz.ifihtich.asalary;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class SalaryManager {

    public final Map<UUID, List<BukkitTask>> activeTasks = new HashMap<>();
    private final Economy economy;

    public SalaryManager(Economy economy) {
        this.economy = economy;
    }

    public void start(Player player){

        stop(player);
        ConfigurationSection configurationSection = ASalary.getInstance().getConfig().getConfigurationSection("salaries");
        if (configurationSection == null) return;

        List<BukkitTask> tasks = new ArrayList<>();

        List<String> paths = new ArrayList<>(configurationSection.getKeys(false));
        Collections.reverse(paths);

        for (String path : paths){
            ConfigurationSection salary = configurationSection.getConfigurationSection(path);

            if (salary == null) continue;
            long time = salary.getInt("time");
            String permission = salary.getString("permission");
            double money = salary.getDouble("money");
            List<String> list = salary.getStringList("message");
            List<String> cmdList = salary.getStringList("commands");

            if (permission != null && !player.hasPermission(permission)) continue;

            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    economy.depositPlayer(player, money);
                    for (String line : list){
                        player.sendMessage(Utils.color(line, player));
                    }
                    for (String cmd : cmdList){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.getName()));
                    }
                }
            }.runTaskTimer(ASalary.getInstance(), time * 20, time * 20);

            tasks.add(task);
            break;
        }
        if (!tasks.isEmpty()){
            activeTasks.put(player.getUniqueId(), tasks);
        }
    }

    public void stop(Player player){
        List<BukkitTask> tasks = activeTasks.remove(player.getUniqueId());
        if (tasks != null){
            for (BukkitTask task : tasks){
                task.cancel();
            }
        }
    }

    public void stopAll() {
        for (List<BukkitTask> tasks : activeTasks.values()) {
            for (BukkitTask task : tasks) {
                task.cancel();
            }
        }
        activeTasks.clear();
    }

    public void online(){
        for (Player player : Bukkit.getOnlinePlayers()){
            start(player);
        }
    }
}
