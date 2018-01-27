package me.matthewe.forcepowers.player.power.powers;

import me.matthewe.forcepowers.ForcePowers;
import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerLevel;
import me.matthewe.forcepowers.player.power.PowerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForceLightningPower extends Power {

    public ForceLightningPower() {
        super("Force Lightning",Side.SITH,  PowerType.OTHER, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            registerPowerLevel(new PowerLevel(this, level, (3* level), 45) {

                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {

                    int effect =(level+2);
                    int coolDown = 40;
                    coolDown+=(level*5);
                    int damage = 8;
                    damage+=(2*level);
                    int stun = level;

                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, stun*20, 1,false,false));
                    LightningStrike lightningStrike = target.getWorld().strikeLightning(target.getLocation());
                    lightningStrike.setMetadata("DAMAGE", new FixedMetadataValue(ForcePowers.getInstance(), (int)(damage*2)));
                    forcePlayer.setCoolDown(getName(), coolDown);
                    Player player = Bukkit.getPlayer(forcePlayer.getUuid());
                    target.getWorld().createExplosion(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), 5, false,false);
                    if (player != null && (player.isOnline())) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3*20, 0, false,false));

                    }
                }

            });
        }
    }

    @Override
    public Material getMaterial() {
        return Material.MAGMA_CREAM;
    }

    @Override
    public String getDescription(int level) {
        int effect =(level+2);
        int coolDown = 40;
        coolDown+=(level*5);
        int damage = 8;
        damage+=(2*level);
        int stun = level;
        return "Summon lightning onto your enemies dealing "+damage+" hearts,\nand stunning them for "+stun+" seconds,\nbut your body weakens for 3 seconds!";
    }
}
