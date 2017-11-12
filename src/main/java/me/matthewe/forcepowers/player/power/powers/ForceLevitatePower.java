package me.matthewe.forcepowers.player.power.powers;

import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerLevel;
import me.matthewe.forcepowers.player.power.PowerType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForceLevitatePower extends Power {

    public ForceLevitatePower() {
        super("Force Levitate",Side.BOTH,  PowerType.OTHER, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            registerPowerLevel(new PowerLevel(this, level, (1 + level), 10) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {

                    int effect = 3;
                    int coolDown = 10;
                    if (level == 2) {
                        coolDown = 12;
                        effect=4;
                    } else if (level == 3) {
                        coolDown = 14;
                        effect=5;
                    } else if (level == 4) {
                        coolDown = 16;
                        effect=6;
                    } else if (level == 5) {
                        coolDown = 18;
                        effect=7;
                    } else if (level == 6) {
                        effect=8;
                        coolDown = 20;
                    }
                    target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, effect*20, 1,false,false));
                    forcePlayer.setCoolDown(getName(), coolDown);
                }

            });
        }
    }

    @Override
    public Material getMaterial() {
        return Material.BEACON;
    }

    @Override
    public String getDescription(int level) {

        int effect = 3;
        int coolDown = 10;
        if (level == 2) {
            coolDown = 12;
            effect=4;
        } else if (level == 3) {
            coolDown = 14;
            effect=5;
        } else if (level == 4) {
            coolDown = 16;
            effect=6;
        } else if (level == 5) {
            coolDown = 18;
            effect=7;
        } else if (level == 6) {
            effect=8;
            coolDown = 20;
        }
        return "Control your enemies in the air for " + effect + " seconds!";
    }
}
