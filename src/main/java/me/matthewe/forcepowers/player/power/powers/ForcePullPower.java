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
public class ForcePullPower extends Power {

    public ForcePullPower() {
        super("Force Pull",Side.BOTH,  PowerType.OTHER, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            registerPowerLevel(new PowerLevel(this, level, (1 + level), 10) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {

                    int coolDown = 10;
                    if (level == 2) {
                        coolDown = 12;
                    } else if (level == 3) {
                        coolDown = 14;
                    } else if (level == 4) {
                        coolDown = 16;
                    } else if (level == 5) {
                        coolDown = 18;
                    } else if (level == 6) {
                        coolDown = 20;
                    }
                    final int pullNumber =level;
                    if (target.getVelocity().distanceSquared(forcePlayer.getLocation().toVector()) < pullNumber) {
                        target.teleport(forcePlayer.getLocation());
                    } else {
                        target.setVelocity(forcePlayer.getLocation().getDirection().multiply(-(double)(pullNumber/2)));

                    }
                    forcePlayer.setCoolDown(getName(), coolDown);
                }

            });
        }
    }

    @Override
    public Material getMaterial() {
        return Material.GLOWSTONE_DUST;
    }

    @Override
    public String getDescription(int level) {
        return "Pull in your enemies from up to "+level+" blocks!";
    }
}
