package me.matthewe.forcepowers.player.power;

import me.matthewe.forcepowers.player.power.powers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class PowerManager  {
    private static PowerManager instance;
    private Map<String, Power> powerMap;

    public static PowerManager getInstance() {
        if (instance == null) {
            instance = new PowerManager();
        }
        return instance;
    }

    public PowerManager() {
        instance = this;
        powerMap = new HashMap<>();
        registerPower(new ForcePushPower());
        registerPower(new ForceLeapPower());
        registerPower(new ForceHealPower());
        registerPower(new ForcePullPower());
        registerPower(new ForceLevitatePower());
        registerPower(new ForceChokePower());
        registerPower(new ForceDrainPower());
        registerPower(new ForceWoundPower());
    }

    public void registerPower(Power power) {
        if (!powerMap.containsKey(power.getName())) {
            powerMap.put(power.getName(), power);
        }
    }

    public boolean isPower(String powerName) {
        return powerName.contains(powerName);
    }

    public Power getPower(String wandPower) {
        return powerMap.get(wandPower);
    }

    public List<Power> getPowers() {
        return new ArrayList<>(powerMap.values());
    }
}
