package me.matthewe.forcepowers.player.power.powers;

import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerLevel;
import me.matthewe.forcepowers.player.power.PowerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForceRage extends Power {

    public ForceRage() {
        super("Force Rage",Side.SITH,  PowerType.SELF, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            final int[] coolDown1 = {25};
            PowerLevel powerLevel = new PowerLevel(this, level, (2 * level)) {
                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {
                    int coolDown = 25;
                    coolDown+= (5 * level);
                    int str = 4;
                    int speed = level;
                    str+= level;

                    coolDown1[0] = coolDown;
                    forcePlayer.setCoolDown(getName(), coolDown);
                    Player player = Bukkit.getPlayer(forcePlayer.getUuid());
                    if (player != null && (player.isOnline())) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*str, 1, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*speed, 0, false, false));
                    }
                }

            };
            powerLevel.setCoolDown(coolDown1[0]);
            registerPowerLevel(powerLevel);
        }
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public String getDescription(int level) {
        int coolDown = 25;
        coolDown+= (5 * level);
        return "Tap into your deep pain and hate, converting it to intense rage for [x] seconds!";
    }
}
