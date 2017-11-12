package me.matthewe.forcepowers.listeners.player;

import me.matthewe.forcepowers.ForcePowers;
import me.matthewe.forcepowers.player.ForcePlayers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (ForcePowers.REBOOTING) {
            return;
        }
        event.setJoinMessage(null);
        event.getPlayer().addAttachment(ForcePowers.getInstance(), "forcepower.sith", true);
        ForcePlayers.getInstance().loadProfile(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (ForcePowers.REBOOTING) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Rebooting");
        }
    }

}
