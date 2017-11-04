package me.matthewe.forcepowers.player;


import me.matthewe.forcepowers.ForcePowers;
import me.matthewe.forcepowers.player.power.Power;
import me.matthewe.forcepowers.player.power.PowerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.menubuilder.inventory.InventoryMenuBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForcePlayer {
    private UUID uuid;
    private String name;
    private Side side;
    private int skillPoints;
    private Map<String, Long> coolDownMap;
    private boolean isFalling;
    private boolean isViewingJediMenu;
    private boolean isViewingSithMenu;
    private Map<String, Integer> levelMap;

    public ForcePlayer(UUID uuid, String name, ConfigurationSection section) {
        this.uuid = uuid;
        this.name = name;
        this.side = Side.valueOf(section.getString("side").toUpperCase());
        this.skillPoints = section.getInt("skillPoints", 0);
        this.coolDownMap = new HashMap<>();
        this.levelMap = new HashMap<>();
        Player player = Bukkit.getPlayer(uuid);
        if (player!=null&&(player.isOnline())) {
            if (player.isOp()) {
                side = Side.JEDI;
            } else if (player.hasPermission("forcepower.jedi")) {
                side = Side.JEDI;
            } else if (player.hasPermission("forcepower.sith")) {

                side = Side.SITH;
            } else {
                side = Side.BOTH;
            }
        }
        if (section.isSet("coolDowns")) {
            List<String> coolDowns = section.getStringList("coolDowns");
            for (String coolDown : coolDowns) {
                String powerName = coolDown.split(":")[0];
                if (PowerManager.getInstance().isPower(powerName)) {
                    long time = Long.parseLong(coolDown.split(":")[1]);
                    if (System.currentTimeMillis() < time) {
                        coolDownMap.put(powerName, time);
                    }
                }
            }
        }
        if (section.isSet("levels")) {
            List<String> levels = section.getStringList("levels");
            for (String level : levels) {
                String powerName = level.split(":")[0];
                if (PowerManager.getInstance().isPower(powerName)) {
                    int levelNumber = Integer.parseInt(level.split(":")[1]);
                   levelMap.put(powerName, levelNumber);
                }
            }
        }

    }

    public boolean hasPower(String name) {
        return levelMap.containsKey(name);
    }

    public void levelUpPower(String name) {
        if (levelMap.containsKey(name)) {
            Integer integer = levelMap.get(name);
            Power power = PowerManager.getInstance().getPower(name);
            if (power != null) {
                if (integer ==power.getMaxLevel()) {
                    return;
                }
                integer++;
                levelMap.remove(name);
                levelMap.put(name, integer);
            }
        }

    }
    public Map<String, Long> getCoolDownMap() {
        return coolDownMap;
    }

    public Map<String, Integer> getLevelMap() {
        return levelMap;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public ForcePlayer setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
        return this;
    }

    public Side getSide() {
        return side;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setCoolDown(String powerName, long seconds) {
        if (coolDownMap.containsKey(powerName)) {
            coolDownMap.remove(powerName);
        }
        coolDownMap.put(powerName, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
    }

    public boolean isCoolDownExpired(String powerName) {
        if (coolDownMap.containsKey(powerName)) {
            Long aLong = coolDownMap.get(powerName);
            if (System.currentTimeMillis() > aLong) {
                coolDownMap.remove(powerName);
                return true;
            }
            return false;
        }
        return true;
    }

    public int getLevel(String wandPower) {
        if (levelMap.containsKey(wandPower)) {
            return levelMap.get(wandPower);
        }
        return -1;
    }

    public String getNextPower(String wandPower) {
        if (levelMap.containsKey(wandPower)) {
            List<String> collect = levelMap.keySet().stream().filter(s ->  {
                Side side = PowerManager.getInstance().getPower(s).getSide();
                Side side1 = PowerManager.getInstance().getPower(wandPower).getSide();
                return side == side1 || (side == Side.BOTH) || (side1 == Side.BOTH);
            }).collect(Collectors.toList());
            int i = collect.indexOf(wandPower);
            if (i>=collect.size()) {
                return collect.get(0);
            }
            if ((i + 1) >= collect.size()) {
                return collect.get(0);
            } else {
                if (collect.get(i+1) != null) {
                    return collect.get(i+1);
                }
            }
        }
        return null;
    }

    public void unlockPower(Power power) {
        if (levelMap.containsKey(power.getName())) {
            return;
        }
        levelMap.put(power.getName(), 1);

    }

    public int getCoolDownSeconds(String wandPower) {
        if (coolDownMap.containsKey(wandPower)) {
            Long aLong = coolDownMap.get(wandPower);
            if (System.currentTimeMillis() < aLong) {
                return (int) TimeUnit.MILLISECONDS.toSeconds(aLong-System.currentTimeMillis());
            }
        }
        return 0;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public ForcePlayer setFalling(boolean falling) {
        isFalling = falling;
        return this;
    }

    public void unlockAllPowers() {
        coolDownMap.clear();
        levelMap.clear();
        for (Power power : PowerManager.getInstance().getPowers()) {
            levelMap.put(power.getName(), power.getMaxLevel());
        }
    }

    public void unlockAndMaxPower(String power) {
        if (coolDownMap.containsKey(power)) {
            coolDownMap.remove(power);
        }
        if (levelMap.containsKey(power)) {
            levelMap.remove(power);
        }
        levelMap.put(power, PowerManager.getInstance().getPower(power).getMaxLevel());
    }
    public void openSithMenu(Player player) {
        if (isViewingSithMenu) {
            return;
        }
        isViewingSithMenu = true;
        InventoryMenuBuilder jediMenu = new InventoryMenuBuilder().withSize(27).withTitle("Sith Menu");
        int slot = 0;
        for (Power power : PowerManager.getInstance().getPowers()) {
            if (power.getSide()!=Side.SITH){
                continue;
            }
            int level = 0;
            Integer integer = levelMap.get(power.getName());
            if (levelMap.containsKey(power.getName())) {
                level = integer;
            }
            ItemStack itemStack = new ItemStack(power.getMaterial());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(power.getSide().getChatColor() + power.getName());
            List<String> loreStringList = new ArrayList<>();
            String levelString = "";
            if (level == power.getMaxLevel()) {
                levelString = ChatColor.RED + ChatColor.BOLD.toString() + "MAX";
            } else {
                levelString = String.valueOf(level + 1);
            }
            String coolDownString = "";
            if (level == power.getMaxLevel()) {
                coolDownString = String.valueOf(power.getPowerLevelMap().get(power.getMaxLevel()).getCoolDown());
            } else {
                coolDownString = String.valueOf(power.getPowerLevelMap().get(level + 1).getCoolDown());
            }
            String costString = "";
            if (level == power.getMaxLevel()) {
                costString = ChatColor.RED + ChatColor.BOLD.toString() + "MAX";
            } else {
                costString = String.valueOf(power.getPowerLevelMap().get(level + 1).getCost());
            }
            loreStringList.add("&6Level &f" + levelString);
            loreStringList.add("&6Cost &f" + costString);
            loreStringList.add("&6Cooldown &f" + coolDownString);
            if (level == power.getMaxLevel()) {
                loreStringList.add("&7" + power.getDescription(level));
            } else {
                loreStringList.add("&7" + power.getDescription(level + 1));
            }
            itemMeta.setLore(loreStringList.stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList()));
            itemStack.setItemMeta(itemMeta);
            int finalLevel = level;
            int finalLevel1 = level;
            int finalSlot = slot;
            jediMenu.withItem(slot, itemStack, (player1, action, item) -> {
                if (finalLevel !=power.getMaxLevel()) {
                    int cost = power.getPowerLevelMap().get(finalLevel + 1).getCost();
                    if (getSkillPoints()< cost) {
                        player1.sendMessage(ChatColor.RED + "You don't have enough skill points to purchase " + power.getName());
                    } else {
                        setSkillPoints(getSkillPoints()-cost);
                        if (!levelMap.containsKey(power.getName())) {
                            unlockPower(power);
                            player1.sendMessage(ChatColor.GREEN + "Congrats! You have unlocked " + ChatColor.BOLD + power.getName() + "!");
                        } else {
                            levelUpPower(power.getName());
                            player1.sendMessage(ChatColor.GREEN + "Congrats! You have leveled up " + ChatColor.BOLD + power.getName() + "" + ChatColor.GREEN + " to " + ChatColor.BOLD + getLevel(power.getName()));
                        }
                        ItemStack itemStack1 = new ItemStack(power.getMaterial());
                        ItemMeta itemMeta1 = itemStack1.getItemMeta();
                        itemMeta1.setDisplayName(power.getSide().getChatColor() + power.getName());
                        List<String> loreStringList1 = new ArrayList<>();
                        String levelString1 = "";
                        if (finalLevel1 == power.getMaxLevel()) {
                            levelString1 = ChatColor.RED + ChatColor.BOLD.toString() + "MAX";
                        } else {
                            levelString1 = String.valueOf(finalLevel1 + 1);
                        }
                        String coolDownString1 = "";
                        if (finalLevel1 == power.getMaxLevel()) {
                            coolDownString1 = String.valueOf(power.getPowerLevelMap().get(power.getMaxLevel()).getCoolDown());
                        } else {
                            coolDownString1 = String.valueOf(power.getPowerLevelMap().get(finalLevel1 + 1).getCoolDown());
                        }
                        String costString1 = "";
                        if (finalLevel1 == power.getMaxLevel()) {
                            costString1 = ChatColor.RED + ChatColor.BOLD.toString() + "MAX";
                        } else {
                            costString1 = String.valueOf(power.getPowerLevelMap().get(finalLevel1 + 1).getCost());
                        }
                        loreStringList1.add("&6Level &f" + levelString1);
                        loreStringList1.add("&6Cost &f" + costString1);
                        loreStringList1.add("&6Cooldown &f" + coolDownString1);
                        if (finalLevel1 == power.getMaxLevel()) {
                            loreStringList1.add("&7" + power.getDescription(finalLevel1));
                        } else {
                            loreStringList1.add("&7" + power.getDescription(finalLevel1 + 1));
                        }
                        itemMeta1.setLore(loreStringList1.stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList()));
                        itemStack1.setItemMeta(itemMeta1);
                        player1.closeInventory();
                        jediMenu.dispose();

                        new BukkitRunnable(){

                            @Override
                            public void run() {
                                openSithMenu(player1);
                            }
                        }.runTaskLater(ForcePowers.getInstance(), 2L);
                    }
                } else {
                    player1.sendMessage(ChatColor.RED + "That power is fully upgraded.");
                }
            }, ClickType.LEFT);
            slot++;
        }
        jediMenu.show(player);
    }
    public void openJediMenu(Player player) {
        if (isViewingJediMenu) {
            return;
        }
        isViewingJediMenu = true;
        InventoryMenuBuilder jediMenu = new InventoryMenuBuilder().withSize(27).withTitle("Jedi Menu");
        int slot = 0;
        for (Power power : PowerManager.getInstance().getPowers()) {
            if (power.getSide()!=Side.JEDI){
                continue;
            }
            int level = 0;
            Integer integer = levelMap.get(power.getName());
            if (levelMap.containsKey(power.getName())) {
                level = integer;
            }
            ItemStack itemStack = new ItemStack(power.getMaterial());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(power.getSide().getChatColor() + power.getName());
            List<String> loreStringList = new ArrayList<>();
            String levelString = "";
            if (level == power.getMaxLevel()) {
                levelString = ChatColor.RED + ChatColor.BOLD.toString() + "MAX";
            } else {
                levelString = String.valueOf(level + 1);
            }
            String coolDownString = "";
            if (level == power.getMaxLevel()) {
                coolDownString = String.valueOf(power.getPowerLevelMap().get(power.getMaxLevel()).getCoolDown());
            } else {
                coolDownString = String.valueOf(power.getPowerLevelMap().get(level + 1).getCoolDown());
            }
            String costString = "";
            if (level == power.getMaxLevel()) {
                costString = ChatColor.RED + ChatColor.BOLD.toString() + "MAX";
            } else {
                costString = String.valueOf(power.getPowerLevelMap().get(level + 1).getCost());
            }
            loreStringList.add("&6Level &f" + levelString);
            loreStringList.add("&6Cost &f" + costString);
            loreStringList.add("&6Cooldown &f" + coolDownString);
            if (level == power.getMaxLevel()) {
                loreStringList.add("&7" + power.getDescription(level));
            } else {
                loreStringList.add("&7" + power.getDescription(level + 1));
            }
            itemMeta.setLore(loreStringList.stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList()));
            itemStack.setItemMeta(itemMeta);
            int finalLevel = level;
            int finalLevel1 = level;
            int finalSlot = slot;
            jediMenu.withItem(slot, itemStack, (player1, action, item) -> {
                if (finalLevel !=power.getMaxLevel()) {
                    int cost = power.getPowerLevelMap().get(finalLevel + 1).getCost();
                    if (getSkillPoints()< cost) {
                        player1.sendMessage(ChatColor.RED + "You don't have enough skill points to purchase " + power.getName());
                    } else {
                        setSkillPoints(getSkillPoints()-cost);
                        if (!levelMap.containsKey(power.getName())) {
                            unlockPower(power);
                            player1.sendMessage(ChatColor.GREEN + "Congrats! You have unlocked " + ChatColor.BOLD + power.getName() + "!");
                        } else {
                            levelUpPower(power.getName());
                            player1.sendMessage(ChatColor.GREEN + "Congrats! You have leveled up " + ChatColor.BOLD + power.getName() + "" + ChatColor.GREEN + " to " + ChatColor.BOLD + getLevel(power.getName()));
                        }
                        ItemStack itemStack1 = new ItemStack(power.getMaterial());
                         ItemMeta itemMeta1 = itemStack1.getItemMeta();
                        itemMeta1.setDisplayName(power.getSide().getChatColor() + power.getName());
                       List<String> loreStringList1 = new ArrayList<>();
                        String levelString1 = "";
                        if (finalLevel1 == power.getMaxLevel()) {
                            levelString1 = ChatColor.RED + ChatColor.BOLD.toString() + "MAX";
                        } else {
                            levelString1 = String.valueOf(finalLevel1 + 1);
                        }
                        String coolDownString1 = "";
                        if (finalLevel1 == power.getMaxLevel()) {
                            coolDownString1 = String.valueOf(power.getPowerLevelMap().get(power.getMaxLevel()).getCoolDown());
                        } else {
                            coolDownString1 = String.valueOf(power.getPowerLevelMap().get(finalLevel1 + 1).getCoolDown());
                        }
                        String costString1 = "";
                        if (finalLevel1 == power.getMaxLevel()) {
                            costString1 = ChatColor.RED + ChatColor.BOLD.toString() + "MAX";
                        } else {
                            costString1 = String.valueOf(power.getPowerLevelMap().get(finalLevel1 + 1).getCost());
                        }
                        loreStringList1.add("&6Level &f" + levelString1);
                        loreStringList1.add("&6Cost &f" + costString1);
                        loreStringList1.add("&6Cooldown &f" + coolDownString1);
                        if (finalLevel1 == power.getMaxLevel()) {
                            loreStringList1.add("&7" + power.getDescription(finalLevel1));
                        } else {
                            loreStringList1.add("&7" + power.getDescription(finalLevel1 + 1));
                        }
                        itemMeta1.setLore(loreStringList1.stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList()));
                        itemStack1.setItemMeta(itemMeta1);
                        player1.closeInventory();
                        jediMenu.dispose();

                        new BukkitRunnable(){

                            @Override
                            public void run() {
                                openJediMenu(player1);
                            }
                        }.runTaskLater(ForcePowers.getInstance(), 2L);
                    }
                } else {
                    player1.sendMessage(ChatColor.RED + "That power is fully upgraded.");
                }
            }, ClickType.LEFT);
            slot++;
        }
        jediMenu.show(player);
    }

    public boolean isViewingJediMenu() {
        return isViewingJediMenu;
    }

    public ForcePlayer setViewingJediMenu(boolean viewingJediMenu) {
        isViewingJediMenu = viewingJediMenu;
        return this;
    }

    public boolean isViewingSithMenu() {
        return isViewingSithMenu;
    }

    public ForcePlayer setViewingSithMenu(boolean viewingSithMenu) {
        isViewingSithMenu = viewingSithMenu;
        return this;
    }

    public String getRandomPower(Side side) {
        List<Power> collect = levelMap.keySet().stream().map(s -> PowerManager.getInstance().getPower(s)).filter(power -> power.getSide()==Side.BOTH||(power.getSide()==side)).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return null;
        }

        Power currentPower = null;
        if (collect.size() == 1) {
            currentPower = collect.get(0);
            return currentPower.getName();
        }
        Random random = new Random();
        int i = random.nextInt(collect.size() - 1);
        currentPower = collect.get(i);
        if (currentPower != null) {
            return currentPower.getName();
        }
        return null;
    }

    public Location getLocation() {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && (player.isOnline())) {
            return player.getLocation();
        }
        return null;
    }
}
