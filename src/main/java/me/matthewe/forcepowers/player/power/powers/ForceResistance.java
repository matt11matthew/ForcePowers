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
public class ForceResistance extends Power {

    public ForceResistance() {
        super("Force Resistance",Side.JEDI,  PowerType.SELF, 6);
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
                    int resistance = 5;
                    resistance+= level;

                    coolDown1[0] = coolDown;
                    forcePlayer.setCoolDown(getName(), coolDown);
                    Player player = Bukkit.getPlayer(forcePlayer.getUuid());
                    if (player != null && (player.isOnline())) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*resistance, 2, false, false));
                    }
                }

            };
            powerLevel.setCoolDown(coolDown1[0]);
            registerPowerLevel(powerLevel);
        }
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public String getDescription(int level) {
        int coolDown = 25;
        coolDown+= (5 * level);
        return "Make your body stronger for "+coolDown+" seconds!";
    }
}
