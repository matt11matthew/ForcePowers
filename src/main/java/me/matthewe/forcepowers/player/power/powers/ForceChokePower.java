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
public class ForceChokePower extends Power {

    public ForceChokePower() {
        super("Force Choke",Side.SITH,  PowerType.OTHER, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            registerPowerLevel(new PowerLevel(this, level, (1 + level), 10) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {

                    int effect =(level+2);
                    int coolDown = 30;
                    int potionEffect = (level + 1);
                    if (level == 2) {
                        coolDown =35;
                    } else if (level == 3) {
                        coolDown = 40;
                    } else if (level == 4) {
                        coolDown = 45;
                    } else if (level == 5) {
                        coolDown = 50;
                    } else if (level == 6) {
                        coolDown = 55;
                    }
                    target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, effect*20, 1,false,false));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, potionEffect*20, 1,false,false));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, potionEffect*20, 1,false,false));
                    forcePlayer.setCoolDown(getName(), coolDown);
                }

            });
        }
    }

    @Override
    public Material getMaterial() {
        return Material.SNOW_BALL;
    }

    @Override
    public String getDescription(int level) {

        int effect =(level+2);
        int coolDown = 30;
        int potionEffect = (level + 1);
        if (level == 2) {
            coolDown =35;
        } else if (level == 3) {
            coolDown = 40;
        } else if (level == 4) {
            coolDown = 45;
        } else if (level == 5) {
            coolDown = 50;
        } else if (level == 6) {
            coolDown = 55;
        }
        return "Levitate your enemies into the air and choke them for "+effect+" seconds!";
    }
}
