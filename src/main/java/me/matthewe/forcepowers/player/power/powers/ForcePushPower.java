package me.matthewe.forcepowers.player.power.powers;

import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerLevel;
import me.matthewe.forcepowers.player.power.PowerType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForcePushPower extends Power {

    public ForcePushPower() {
        super("Force Push",Side.BOTH,  PowerType.OTHER, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            registerPowerLevel(new PowerLevel(this, level, (1 + level), 10) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {


                    int coolDown = 10;
                    int pushNumber = 3;
                    if (level == 2) {
                        coolDown = 12;
                        pushNumber = 4;
                    } else if (level == 3) {
                        coolDown = 14;
                        pushNumber = 5;
                    } else if (level == 4) {
                        coolDown = 16;
                        pushNumber = 6;
                    } else if (level == 5) {
                        coolDown = 18;
                        pushNumber = 7;
                    } else if (level == 6) {
                        coolDown = 20;
                        pushNumber = 8;
                    }
                    target.setVelocity(forcePlayer.getLocation().getDirection().multiply(pushNumber));
                    forcePlayer.setCoolDown(getName(), coolDown);
                }

            });
        }
    }

    @Override
    public Material getMaterial() {
        return Material.REDSTONE;
    }

    @Override
    public String getDescription(int level) {
        int pushNumber = 3;
        if (level == 2) {
            pushNumber = 4;
        } else if (level == 3) {
            pushNumber = 5;
        } else if (level == 4) {
            pushNumber = 6;
        } else if (level == 5) {
            pushNumber = 7;
        } else if (level == 6) {
            pushNumber = 8;
        }
        return "Force push entities up to "+pushNumber+" blocks!";
    }
}
