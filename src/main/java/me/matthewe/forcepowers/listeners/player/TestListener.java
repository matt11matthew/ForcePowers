package me.matthewe.forcepowers.listeners.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class TestListener implements Listener {
    @EventHandler
    public void onBlockExplode(EntityExplodeEvent event) {
        TNTPrimed tntPrimed = (TNTPrimed) event.getEntity();
        if (tntPrimed.hasMetadata("COMBUSTION")) {
            List<Block> blockList  = new ArrayList<>();
            blockList.addAll(event.blockList());
            event.blockList().clear();
            for (Block block : blockList) {
                event.setYield(0);
                Block relative = block.getRelative(BlockFace.UP);
                if (relative.getType()== Material.AIR) {
                    relative.setType(Material.FIRE);
                }
            }

        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && (event.getDamager() instanceof LightningStrike)) {
            LightningStrike lightning = (LightningStrike) event.getDamager();
            if (lightning.hasMetadata("DAMAGE")) {
                int damage = lightning.getMetadata("DAMAGE").get(0).asInt();
                event.setDamage(damage);
            }
        }
        if (event.getEntity() instanceof Player && (event.getDamager() instanceof TNTPrimed)) {
            TNTPrimed tntPrimed = (TNTPrimed) event.getDamager();
            if (tntPrimed.hasMetadata("COMBUSTION")) {
                int combustion = tntPrimed.getMetadata("COMBUSTION").get(0).asInt();
                event.setDamage(combustion*2);

            }
        }
    }

    //    @EventHandler
//    public void onAsyncPlayerChat(PlayerChatEvent event) {
//        Player player = event.getPlayer();
//        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
//        if (forcePlayer != null) {
//            player.setItemInHand(ItemUtils.createWand(forcePlayer, "Force Leap"));
//            for (Power power : PowerManager.getInstance().getPowers()) {
//                if (!forcePlayer.hasPower(power.getName())) {
//                    forcePlayer.unlockPower(power);
//                }
//            }
//            for (String s : forcePlayer.getLevelMap().keySet()) {
//                PowerManager instance = PowerManager.getInstance();
//                if (instance.isPower(s)) {
//                    Power power = instance.getPower(s);
//                }
//            }
//        }
//    }
}
