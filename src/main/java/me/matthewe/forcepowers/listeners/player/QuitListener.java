package me.matthewe.forcepowers.listeners.player;

import me.matthewe.forcepowers.ForcePowers;
import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.ForcePlayers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Matthew E on 9/26/2017.
 */
public class QuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (ForcePowers.REBOOTING) {
            return;
        }
        event.setQuitMessage(null);
        ForcePlayer infurePlayer = ForcePlayers.getInstance().get(event.getPlayer().getUniqueId());
        if (infurePlayer != null) {
            event.getPlayer().closeInventory();
            ForcePlayers.getInstance().save(infurePlayer);
            ForcePlayers.getInstance().remove(infurePlayer.getUuid());
        }
    }
}
