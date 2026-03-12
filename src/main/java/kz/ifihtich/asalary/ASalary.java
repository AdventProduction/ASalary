package kz.ifihtich.asalary;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ASalary extends JavaPlugin {

    private static ASalary instance;

    private SalaryManager salaryManager;
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        setupEconomy();

        getCommand("asalary").setExecutor(new Reload());

        salaryManager = new SalaryManager(economy);
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        salaryManager.online();

        Utils.logo();

    }

    @Override
    public void onDisable() {
        if (salaryManager != null) {
            salaryManager.stopAll();
        }
    }

    public void onReload(){
        if (salaryManager != null) {
            salaryManager.stopAll();
        }
        reloadConfig();
        salaryManager = new SalaryManager(economy);
        salaryManager.online();
        Utils.logo();
    }

    private void setupEconomy(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        economy = rsp.getProvider();
    }

    public static ASalary getInstance(){
        return instance;
    }

    public SalaryManager getSalaryManager() {
        return salaryManager;
    }
}
