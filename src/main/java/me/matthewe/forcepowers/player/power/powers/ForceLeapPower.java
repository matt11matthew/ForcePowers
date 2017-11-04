package me.matthewe.forcepowers.player.power.powers;

import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerLevel;
import me.matthewe.forcepowers.player.power.PowerType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForceLeapPower extends Power {

    public ForceLeapPower() {
        super("Force Leap", Side.BOTH, PowerType.SELF, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            registerPowerLevel(new PowerLevel(this, level, (1 + level), 10) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity self) {
                    int coolDown = 10;
                    int leap = 5;
                    if (level == 2) {
                        coolDown = 12;
                        leap = 6;
                    } else if (level == 3) {
                        coolDown = 14;
                        leap = 7;
                    } else if (level == 4) {
                        coolDown = 16;
                        leap = 8;
                    } else if (level == 5) {
                        coolDown = 18;
                        leap = 9;
                    } else if (level == 6) {
                        coolDown = 20;
                        leap = 10;
                    }

                    Vector velocity = self.getVelocity();
                    self.setVelocity(new Vector(velocity.getX(), leap / 3, velocity.getZ()));
                    forcePlayer.setCoolDown(getName(), coolDown);
                    forcePlayer.setFalling(true);
                }

            });

        }
    }

    @Override
    public Material getMaterial() {
        return Material.FEATHER;
    }

    @Override
    public String getDescription(int level) {
        int leap = 5;
        if (level == 2) {
            leap = 6;
        } else if (level == 3) {
            leap = 7;
        } else if (level == 4) {
            leap = 8;
        } else if (level == 5) {
            leap = 9;
        } else if (level == 6) {
            leap = 10;
        }
        return "Leap up to distances of " + leap + " blocks!";
    }
}
