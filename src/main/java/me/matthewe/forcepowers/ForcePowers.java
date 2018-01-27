package me.matthewe.forcepowers;

import co.aikar.commands.BukkitCommandManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.matthewe.forcepowers.commands.ForcePowerCommand;
import me.matthewe.forcepowers.listeners.PowerListener;
import me.matthewe.forcepowers.listeners.player.*;
import me.matthewe.forcepowers.player.ForcePlayers;
import me.matthewe.forcepowers.player.power.PowerManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;

public final class ForcePowers extends JavaPlugin {
    public static final long SAVE_TIME = 1200;
    private static ForcePowers instance;
    private static WorldGuardPlugin guardPlugin;
    public static boolean REBOOTING = false;
    private static BukkitCommandManager commandManager;
    private static List<String> allowedRegions;

    public static ForcePowers getInstance() {
        return instance;
    }

    public static List<String> getAllowedRegions() {
        return allowedRegions;
    }

    public static WorldGuardPlugin getGuardPlugin() {
        return guardPlugin;
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
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
        guardPlugin = getWorldGuard();
        ForcePlayers.getInstance();
        PowerManager.getInstance();
        allowedRegions = getConfig().getStringList("allowedRegions");
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
