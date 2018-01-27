package me.matthewe.forcepowers.player.power.powers;

import me.matthewe.forcepowers.ForcePowers;
import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerLevel;
import me.matthewe.forcepowers.player.power.PowerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForceCombustion extends Power {

    public ForceCombustion() {
        super("Force Combustion",Side.JEDI,  PowerType.SELF, 6);
    }

    @Override
    public void init() {
        for (int i = 0; i < 7; i++) {
            final int level = i;
            final int[] coolDown1 = {40};
            PowerLevel powerLevel = new PowerLevel(this, level, (3*level)) {
                @Override
                public void onUse(ForcePlayer forcePlayer, LivingEntity target) {
                    int coolDown = 40;
                    coolDown+= (5 * level);
                    int resistance = 5;
                    resistance+= level;
                    int hearts = 0;
                    hearts+= (4+level);

                    coolDown1[0] = coolDown;
                    forcePlayer.setCoolDown(getName(), coolDown);
                    Player player = Bukkit.getPlayer(forcePlayer.getUuid());
                    if (player != null && (player.isOnline())) {
                        Entity entity = player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
                        entity.setMetadata("COMBUSTION", new FixedMetadataValue(ForcePowers.getInstance(), hearts));
                    }
                }

            };
            powerLevel.setCoolDown(coolDown1[0]);
            registerPowerLevel(powerLevel);
        }
    }

    @Override
    public Material getMaterial() {
        return Material.TNT;
    }

    @Override
    public String getDescription(int level) {
        int coolDown = 40;
        coolDown+= (5 * level);
        int hearts = 0;
        hearts+= (4+level);
        return "Make your enemies explode, dealing "+hearts+" hearts, with additional fire damage!";
    }
}
