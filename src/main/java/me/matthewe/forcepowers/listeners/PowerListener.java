package me.matthewe.forcepowers.listeners;

import me.matthewe.forcepowers.ForcePowers;
import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.ForcePlayers;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerManager;
import me.matthewe.forcepowers.player.power.PowerType;
import me.matthewe.forcepowers.utils.ItemUtils;
import me.matthewe.forcepowers.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class PowerListener implements Listener {
    private static List<UUID> waitList = new ArrayList<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getItemInHand() != null && (ItemUtils.isForceWand(player.getItemInHand()))) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            if (waitList.contains(player.getUniqueId())) {
                return;
            }
            if (!event.getAction().toString().contains("RIGHT_CLICK")) {
                ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
                if (forcePlayer != null) {
                    String wandPower = ItemUtils.getWandPower(player.getItemInHand());
                    String nextPower = forcePlayer.getNextPower(wandPower);
                    if (nextPower !=null) {
                        player.setItemInHand(ItemUtils.createWand(forcePlayer, PowerManager.getInstance().getPower(nextPower).getSide(), nextPower));
                    }
                }
                waitList.add(player.getUniqueId());
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        waitList.remove(player.getUniqueId());
                    }
                }.runTaskLater(ForcePowers.getInstance(), 5L);
                return;
            }
            ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
            if (forcePlayer != null) {
                String wandPower = ItemUtils.getWandPower(player.getItemInHand());
                PowerManager powerManager = PowerManager.getInstance();
                if (wandPower != null && powerManager.isPower(wandPower) && forcePlayer.hasPower(wandPower)) {
                    int level = forcePlayer.getLevel(wandPower);
                    if (level != -1 ) {
                        if (!forcePlayer.isCoolDownExpired(wandPower)) {
                            player.sendMessage(ChatColor.RED +"Please wait " + forcePlayer.getCoolDownSeconds(wandPower) + "s.");
                            waitList.add(player.getUniqueId());
                            new BukkitRunnable(){

                                @Override
                                public void run() {
                                    waitList.remove(player.getUniqueId());
                                }
                            }.runTaskLater(ForcePowers.getInstance(), 5L);
                            return;
                        }
                        Power power = PowerManager.getInstance().getPower(wandPower);
                        if (power.getPowerType() == PowerType.SELF) {
                            power.use(forcePlayer, player);
                        } else if (power.getPowerType()==PowerType.OTHER) {
                            LivingEntity target = PlayerUtils.getTarget(player);
                            if (target != null) {
                                power.use(forcePlayer, target);
                            }
                        }
                        waitList.add(player.getUniqueId());
                        new BukkitRunnable(){

                            @Override
                            public void run() {
                                waitList.remove(player.getUniqueId());
                            }
                        }.runTaskLater(ForcePowers.getInstance(), 5L);
                    }
                }
            }
        }
    }
}
