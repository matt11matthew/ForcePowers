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
public class ForceHealPower extends Power {

    public ForceHealPower() {
        super("Force Heal",Side.JEDI,  PowerType.SELF, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            final int[] coolDown1 = {10};
            PowerLevel powerLevel = new PowerLevel(this, level, (1 + level)) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {

//                    Player target = player;
                    int coolDown = 30;
                    int healAmount = 2;
                    if (level == 2) {
                        coolDown = 35;
                        healAmount = 3;
                    } else if (level == 3) {
                        coolDown = 40;
                        healAmount = 4;
                    } else if (level == 4) {
                        coolDown = 45;
                        healAmount = 6;
                    } else if (level == 5) {
                        coolDown = 50;
                        healAmount = 7;
                    } else if (level == 6) {
                        coolDown = 55;
                        healAmount = 8;
                    }
                    double newHealth = (target.getHealth() + healAmount);
                    if (newHealth >= target.getMaxHealth()) {
                        target.setHealth(target.getMaxHealth());
                    } else {
                        target.setHealth(newHealth);
                    }
                    coolDown1[0] = coolDown;
                    forcePlayer.setCoolDown(getName(), coolDown);
                }

            };
            powerLevel.setCoolDown(coolDown1[0]);
            registerPowerLevel(powerLevel);
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
