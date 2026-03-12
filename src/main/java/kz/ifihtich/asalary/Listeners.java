package kz.ifihtich.asalary;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        ASalary.getInstance().getSalaryManager().start(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        ASalary.getInstance().getSalaryManager().stop(event.getPlayer());
    }
}
