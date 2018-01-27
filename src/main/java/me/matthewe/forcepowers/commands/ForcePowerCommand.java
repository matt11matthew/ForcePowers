package me.matthewe.forcepowers.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.contexts.OnlinePlayer;
import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.ForcePlayers;
import me.matthewe.forcepowers.player.Side;
import me.matthewe.forcepowers.player.power.PowerManager;
import me.matthewe.forcepowers.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 11/4/2017.
 */
@CommandAlias("forcepowers|fp")
public class ForcePowerCommand extends BaseCommand  {
    @Subcommand("help")
    @CommandPermission("forcepower.help")
    public void onHelp(Player player) {
        List<String> helpStringList = new ArrayList<>();
        helpStringList.add("&7&m------------------------------------------------");
        helpStringList.add("&6&l/fp jedi menu &f- A menu of all Jedi force powers");
        helpStringList.add("&6&l/fp sith menu &f- A menu of all Sith force powers");
        helpStringList.add("&6&l/fp give sp [player] [amount] &f- Gives skill points to a player");
        helpStringList.add("&6&l/fp spawn sp [amount] &f- Spawns skill points.");
        helpStringList.add("&6&l/fp give wand [type] [player] [amount] &f- Spawns a wand.");
        helpStringList.add("&6&l/fp sp [player] &f- Views player skill points.");
        helpStringList.add("&6&l/fp unlock [power] &f- Fully maxes out that power");
        helpStringList.add("&6&l/fp unlockall &f- Unlocks all force powers to max level");
        helpStringList.add("&7&m------------------------------------------------");
        for (String s : helpStringList) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }
    @Subcommand("give wand")
    @CommandPermission("forcepower.wand.give")
    public void onGiveWand(CommandSender commandSender, String wandType, OnlinePlayer onlinePlayer, Integer amount) {
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(onlinePlayer.player.getUniqueId());
        if (forcePlayer != null) {
            if (amount < 1) {
                amount = 1;
            }
            if (amount > 64) {
                amount = 64;
            }
            if (!wandType.equalsIgnoreCase("jedi")&&(!wandType.equalsIgnoreCase("sith"))) {
                commandSender.sendMessage(ChatColor.RED + "The wand type " + wandType + " doesn't exist.");
                commandSender.sendMessage(ChatColor.GRAY + "Wand Types: [JEDI, SITH]");
                return;
            }
            Side side = Side.JEDI;
            if (wandType.equalsIgnoreCase("sith")) {
                side = Side.SITH;
            } else if (wandType.equalsIgnoreCase("jedi")) {
                side = Side.JEDI;
            }
            String randomPower = forcePlayer.getRandomPower(side);
            if (randomPower==null) {
                commandSender.sendMessage(ChatColor.RED + onlinePlayer.player.getName() + " must have powers unlocked.");
                return;
            }
            onlinePlayer.player.getInventory().addItem(ItemUtils.createWand(forcePlayer,side,randomPower));
        }
    }
    @Subcommand("sith menu")
    @CommandPermission("forcepower.sithmenu")
    public void onSithMenu(Player player) {
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
        if (forcePlayer != null) {
            forcePlayer.openSithMenu(player);
        }
    }
    @Subcommand("jedi menu")
    @CommandPermission("forcepower.jedimenu")
    public void onJediMenu(Player player) {
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
        if (forcePlayer != null) {
            forcePlayer.openJediMenu(player);
        }
    }
    @Subcommand("sp")
    @CommandPermission("forcepower.skillpoint.check")
    public void onGetSp(Player player, OnlinePlayer onlinePlayer) {
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(onlinePlayer.player.getUniqueId());
        if (forcePlayer != null) {
            if (onlinePlayer.player.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.DARK_AQUA + "You have " + ChatColor.AQUA + forcePlayer.getSkillPoints() + ChatColor.DARK_AQUA + " skill points.");
                return;
            }
            player.sendMessage(ChatColor.AQUA +  onlinePlayer.getPlayer().getName() + " " + ChatColor.DARK_AQUA + "has " + ChatColor.AQUA + forcePlayer.getSkillPoints() + ChatColor.DARK_AQUA + " skill points.");
        }
    }

    @Subcommand("sp")
    @CommandPermission("forcepower.skillpoint.check")
    public void onGetSelfSp(Player player) {
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
        if (forcePlayer != null) {
            player.sendMessage(ChatColor.DARK_AQUA + "You have " + ChatColor.AQUA + forcePlayer.getSkillPoints() + ChatColor.DARK_AQUA + " skill points.");
        }
    }

    @CommandPermission("forcepower.unlock")
    @Subcommand("unlock")
    public void onUnlock(Player player, String power) {
        power = power.replaceAll("_", " ");
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
        if (forcePlayer != null) {
            if (!PowerManager.getInstance().isPower(power)) {
                player.sendMessage(ChatColor.RED +"The power "+ power + " doesn't exist.");
                return;
            }
            forcePlayer.unlockAndMaxPower(power);
            player.sendMessage(ChatColor.DARK_AQUA +"Unlocked power " + ChatColor.AQUA + power + ChatColor.DARK_AQUA + ".");
        }
    }
    @CommandPermission("forcepower.unlock.all")
    @Subcommand("unlockall")
    public void onUnlockAll(Player player) {
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
        if (forcePlayer != null) {
            forcePlayer.unlockAllPowers();
            player.sendMessage(ChatColor.DARK_AQUA +"Unlocked all powers.");
        }
    }

    @CommandPermission("forcepower.skillpoint.spawn")
    @Syntax("&b<amount> &8- &3Skill Points")
    @Subcommand("spawn sp")
    public void onSpawnSp(Player player, Integer amount) {
        if (amount < 1) {
            player.sendMessage(ChatColor.RED +"You must enter at least 1 skill point.");
            return;
        }
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(player.getUniqueId());
        if (forcePlayer != null) {
            forcePlayer.setSkillPoints((forcePlayer.getSkillPoints() + amount));
            player.sendMessage(ChatColor.DARK_AQUA +"Spawned in " + ChatColor.AQUA + amount + ChatColor.DARK_AQUA + " skill points.");
        }
    }

    @CommandPermission("forcepower.skillpoint.spawn")
    @Syntax("&b<player> <amount> &8- &3Skill Points")
    @Subcommand("give sp")
    public void onSpawnSpOther(CommandSender sender, OnlinePlayer onlinePlayer, Integer amount) {
        if (amount < 1) {
            sender.sendMessage(ChatColor.RED +"You must enter at least 1 skill point.");
            return;
        }
        ForcePlayer forcePlayer = ForcePlayers.getInstance().get(onlinePlayer.player.getUniqueId());
        if (forcePlayer != null) {
            forcePlayer.setSkillPoints((forcePlayer.getSkillPoints() + amount));
            sender.sendMessage(ChatColor.DARK_AQUA +"Spawned in " + ChatColor.AQUA + amount + ChatColor.DARK_AQUA + " skill points.");
        }
    }
}
