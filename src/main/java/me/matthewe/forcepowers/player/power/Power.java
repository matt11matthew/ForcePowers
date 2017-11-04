package me.matthewe.forcepowers.player.power;

import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew E on 11/4/2017.
 */
public abstract class Power {
    private String name;
    private Side side;
    private PowerType powerType;
    private Map<Integer, PowerLevel> powerLevelMap;
    private int maxLevel;

    public Power(String name, Side side, PowerType powerType, int maxLevel) {
        this.name = name;
        this.side = side;
        this.powerType = powerType;
        this.maxLevel = maxLevel;
        this.powerLevelMap = new HashMap<>();
        init();
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public PowerType getPowerType() {
        return powerType;
    }

    public abstract void init();

    public String getName() {
        return name;
    }

    public Side getSide() {
        return side;
    }

    public Map<Integer, PowerLevel> getPowerLevelMap() {
        return powerLevelMap;
    }

    public void registerLevels(PowerLevel... powerLevels) {
        for (PowerLevel powerLevel : powerLevels) {
            registerPowerLevel(powerLevel);
        }
    }

    public void registerPowerLevel(PowerLevel powerLevel) {
        if (!powerLevelMap.containsKey(powerLevel.getLevel())) {
            powerLevelMap.put(powerLevel.getLevel(), powerLevel);
        }
    }


    public void use(ForcePlayer forcePlayer, LivingEntity livingEntity) {
        int level = forcePlayer.getLevel(name);
        if (powerLevelMap.containsKey(level)) {
            PowerLevel powerLevel = powerLevelMap.get(level);
            if (powerLevel != null) {
                powerLevel.onUse(forcePlayer, livingEntity);
            }
        }
    }

    public abstract Material getMaterial();

    public abstract String getDescription(int level);
}

