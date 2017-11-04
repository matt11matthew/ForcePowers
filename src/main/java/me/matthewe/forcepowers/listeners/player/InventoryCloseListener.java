package me.matthewe.forcepowers.listeners.player;

import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.ForcePlayers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getInventory().getType() != InventoryType.PLAYER) {
                ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
                if (forcePlayer != null) {
                    if (forcePlayer.isViewingJediMenu()) {
                        forcePlayer.setViewingJediMenu(false);
                    }
                    if (forcePlayer.isViewingSithMenu()) {
                        forcePlayer.setViewingSithMenu(false);
                    }
                }
            }
        }
    }
}
