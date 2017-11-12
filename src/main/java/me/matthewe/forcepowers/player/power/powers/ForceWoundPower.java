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
public class ForceWoundPower extends Power {

    public ForceWoundPower() {
        super("Force Drain",Side.SITH,  PowerType.OTHER, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            int coolDown = 15;

            int weakness = 5;
            int poison = (level+1);

            if (level == 2) {
                coolDown =18;
                weakness = 7;
            } else if (level == 3) {
                coolDown = 21;
                weakness = 9;
            } else if (level == 4) {
                coolDown = 24;
                weakness = 11;
            } else if (level == 5) {
                coolDown = 27;
                weakness = 13;
            } else if (level == 6) {
                coolDown = 30;
                weakness = 15;
            }
            int finalCoolDown = coolDown;
            int finalWeakness = weakness;
            registerPowerLevel(new PowerLevel(this, level, level, finalCoolDown) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {


                    target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, finalWeakness *20, 0, false,false));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, poison*20, 1, false,false));

                    forcePlayer.setCoolDown(getName(), finalCoolDown);
                }

            });
        }
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_POWDER;
    }

    @Override
    public String getDescription(int level) {

        int coolDown = 15;

        int weakness = 5;
        int poison = (level+1);

        if (level == 2) {
            coolDown =18;
            weakness = 7;
        } else if (level == 3) {
            coolDown = 21;
            weakness = 9;
        } else if (level == 4) {
            coolDown = 24;
            weakness = 11;
        } else if (level == 5) {
            coolDown = 27;
            weakness = 13;
        } else if (level == 6) {
            coolDown = 30;
            weakness = 15;
        }
        int finalCoolDown = coolDown;
        int finalWeakness = weakness;
        return "Crush your enemies lung, giving a weakness effect for "+weakness+" seconds, and poison II effect for "+poison+" seconds!";
    }
}
