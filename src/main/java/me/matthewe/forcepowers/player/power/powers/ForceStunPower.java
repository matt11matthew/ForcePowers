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
public class ForceStunPower extends Power {

    public ForceStunPower() {
        super("Force Stun",Side.JEDI,  PowerType.OTHER, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            registerPowerLevel(new PowerLevel(this, level, (2* level), 30) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {

                    int effect =(level+1);
                    int coolDown = 25;
                    coolDown+=(level*5);
                    int damage = 8;
                    damage+=(2*level);
                    int stun = level;

                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, effect*20, 0,false,false));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, effect*20, 0,false,false));

                }

            });
        }
    }

    @Override
    public Material getMaterial() {
        return Material.SEA_LANTERN;
    }

    @Override
    public String getDescription(int level) {
        int effect =(level+2);
        int coolDown = 25;
        coolDown+=(level*5);
        int damage = 8;
        damage+=(2*level);
        int stun = level;
        return "Freeze your enemy for up to "+coolDown+" seconds!";
    }
}
