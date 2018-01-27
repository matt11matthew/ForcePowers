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

import java.text.DecimalFormat;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForceDrainPower extends Power {

    public ForceDrainPower() {
        super("Force Drain",Side.SITH,  PowerType.OTHER, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            registerPowerLevel(new PowerLevel(this, level, (2* level), 10) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {

                    int debuff = (level + 5);
                    int coolDown = 30;
                    int hearts = 1;
                    if (level > 1) {
                        for (int i = 1; i < level; i++) {
                            hearts++;
                        }
                    }
                    if (level == 2) {
                        coolDown = 35;
                    } else if (level == 3) {
                        coolDown = 40;
                    } else if (level == 4) {
                        coolDown = 45;
                    } else if (level == 5) {
                        coolDown = 50;
                    } else if (level == 6) {
                        coolDown = 55;
                    }
                    if (target instanceof Player) {

                        if (target.getHealth() - hearts > 1) {
                            target.setHealth(target.getHeight() - hearts);
                        } else {
                            target.setHealth(0.0D);
                        }
                        Player player = Bukkit.getPlayer(forcePlayer.getUuid());
                        if (player != null && (player.isOnline())) {
                            if (player.getHealth() + hearts >= player.getMaxHealth()) {
                                player.setHealth(player.getMaxHealth());
                            } else {
                                player.setHealth(player.getHealth() + hearts);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * debuff, 1, false, false));
                        }

                        forcePlayer.setCoolDown(getName(), coolDown);
                    }
                }

            });
        }
    }

    @Override
    public Material getMaterial() {
        return Material.FERMENTED_SPIDER_EYE;
    }

    @Override
    public String getDescription(int level) {

        int debuff =(level+5);
        int coolDown = 30;
        int hearts = 1;
        if (level > 1) {
            for (int i = 0; i < level; i++) {
                hearts++;
            }
        }
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
        double heart = 0.5D;
        if (hearts > 1) {
            for (int i = 1; i < hearts; i++) {
                heart+=0.5D;
            }
        }
        return "Drain "+new DecimalFormat("##.#").format(heart)+" hearts from your enemy and gain that health, but it weakens your body for "+debuff+" seconds!";
    }
}
