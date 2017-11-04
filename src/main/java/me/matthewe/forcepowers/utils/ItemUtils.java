package me.matthewe.forcepowers.utils;

import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerManager;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ItemUtils {
    public static String getWandPower(ItemStack itemStack) {
        if (itemStack != null && (itemStack.getType() != Material.AIR)) {
            net.minecraft.server.v1_12_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
            if (itemStack1.hasTag()) {
                NBTTagCompound tag = itemStack1.getTag();
                boolean b = tag != null && tag.hasKey("wand") && (tag.getBoolean("wand"));
                if (b) {
                    if (tag.hasKey("power")) {
                        String power = tag.getString("power");
                        return power;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isForceWand(ItemStack itemStack) {
        if (itemStack != null && (itemStack.getType() != Material.AIR)) {
            net.minecraft.server.v1_12_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
            if (itemStack1.hasTag()) {
                NBTTagCompound tag = itemStack1.getTag();
                return tag != null && tag.hasKey("wand") && (tag.getBoolean("wand"));
            }
        }
        return false;
    }

    public static ItemStack createWand(ForcePlayer forcePlayer, Side side, String nextPower) {
        Material type = Material.BLAZE_ROD;
        if (side == Side.JEDI) {
            type = Material.NETHER_STAR;
        }
        if (side == Side.BOTH) {
            Player player = Bukkit.getPlayer(forcePlayer.getUuid());
            if (player != null && (player.isOnline())) {
                if (player.isOp()) {
                    type = Material.NETHER_STAR;
                    side = Side.JEDI;
                }
              else   if (player.hasPermission("forcepower.jedi")) {
                    type = Material.NETHER_STAR;
                    side = Side.JEDI;
                } else if (player.hasPermission("forcepower.sith")) {
                    type = Material.BLAZE_ROD;
                    side = Side.SITH;
                } else if (player.hasPermission("forcepower.neutral")) {
                    type = Material.INK_SACK;
                    side = Side.BOTH;
                }

            }
        }
        ItemStack itemStack = new ItemStack(type);
        net.minecraft.server.v1_12_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("wand", true);
        tag.setString("power", nextPower);
        tag.setString("side", side.toString());
        itemStack1.setTag(tag);
        itemStack = CraftItemStack.asBukkitCopy(itemStack1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Power power= PowerManager.getInstance().getPower(nextPower);
        itemMeta.setDisplayName(side.getChatColor() + side.getName()+ " Wand");
        itemMeta.setLore(Arrays.asList(ChatColor.GOLD + "Current Power: " + ChatColor.WHITE + nextPower, " ", ChatColor.GRAY + "Right click to use power", ChatColor.GRAY +"Left click to switch between powers"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
