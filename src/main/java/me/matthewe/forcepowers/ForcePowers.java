package me.matthewe.forcepowers;

import co.aikar.commands.BukkitCommandManager;
import me.matthewe.forcepowers.commands.ForcePowerCommand;
import me.matthewe.forcepowers.listeners.PowerListener;
import me.matthewe.forcepowers.listeners.player.*;
import me.matthewe.forcepowers.player.ForcePlayers;
import me.matthewe.forcepowers.player.power.PowerManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public final class ForcePowers extends JavaPlugin {
    public static final long SAVE_TIME = 1200;
    private static ForcePowers instance;
    public static boolean REBOOTING = false;
    private static BukkitCommandManager commandManager;

    public static ForcePowers getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        REBOOTING = true;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            getLogger().info("config.yml not found, creating!");
            saveDefaultConfig();
        } else {
            getLogger().info("config.yml found, loading!");
        }
        ForcePlayers.getInstance();
        PowerManager.getInstance();
        this.registerListeners();
        this.registerCommands();
        new BukkitRunnable() {
            @Override
            public void run() {
                REBOOTING = false;
            }
        }.runTaskLater(this, 40L);

    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new TestListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new PowerListener(), this);
        pluginManager.registerEvents(new FallListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);
    }

    private void registerCommands() {
        commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new ForcePowerCommand().setExceptionHandler((command, registeredCommand, sender, args, t) -> {

            return true;
        }));
    }

    @Override
    public void onDisable() {
        REBOOTING = true;
        ForcePlayers.getInstance().logoutAllPlayers();
    }
}
