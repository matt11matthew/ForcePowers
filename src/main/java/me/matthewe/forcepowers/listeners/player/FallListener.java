package me.matthewe.forcepowers.listeners.player;

import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.ForcePlayers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class FallListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && (event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
            Player player= (Player) event.getEntity();
            ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
            if (forcePlayer != null) {
                if (forcePlayer.isFalling()) {
                    forcePlayer.setFalling(false);
                    event.setCancelled(true);
                }
            }
        }
    }
}
