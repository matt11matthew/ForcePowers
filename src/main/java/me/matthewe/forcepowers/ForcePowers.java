package me.matthewe.forcepowers;

import org.bukkit.plugin.java.JavaPlugin;

public final class ForcePowers extends JavaPlugin {

    private static ForcePowers instance;

    public static ForcePowers getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;


    }

    @Override
    public void onDisable() {

    }
}
