package me.matthewe.forcepowers.player;

import me.matthewe.forcepowers.ForcePowers;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class ForcePlayers implements Listener {
    private static ForcePlayers instance;
    private Map<UUID, ForcePlayer> playerMap;
    private File file;
    private FileConfiguration configuration;


    public static ForcePlayers getInstance() {
        if (instance == null) {
            instance = new ForcePlayers();
        }
        return instance;
    }

    public Map<String, UUID> getNameUuidMap() {
        return nameUuidMap;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    private ForcePlayers() {
        instance = this;
        this.playerMap = new HashMap<>();
        loadConfiguration();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(ForcePowers.getInstance(), this::saveConfig, ForcePowers.SAVE_TIME, ForcePowers.SAVE_TIME);
    }

    public ForcePlayer get(UUID uuid) {
        if (this.playerMap.containsKey(uuid)) {
            return playerMap.get(uuid);
        }
        return null;
    }

    private boolean loadConfiguration() {
        boolean newFile = false;
        this.file = new File(ForcePowers.getInstance().getDataFolder() + "/", "players.yml");
        if (!this.file.exists()) {
            try {
                 newFile = this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        if (configuration.isSet("players")) {
            for (String players : configuration.getConfigurationSection("players").getKeys(false)) {
                String name = configuration.getString("players." + players + ".name");
                nameUuidMap.put(name.toLowerCase(), UUID.fromString(players));
            }
        }
        return newFile;
    }


    public void saveConfig() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Map<UUID, ForcePlayer> getPlayerMap() {
        return playerMap;
    }

    public void loadProfile(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (!configuration.isConfigurationSection("players." + player.getUniqueId().toString())) {
                    configuration.set("players." + player.getUniqueId().toString() + ".name", player.getName());
                    configuration.set("players." + player.getUniqueId().toString() + ".skillPoints", 0);
                    configuration.set("players." + player.getUniqueId().toString() + ".side", Side.NONE.toString());
                    configuration.set("players." + player.getUniqueId().toString() + ".levels", new ArrayList<String>());
                    configuration.set("players." + player.getUniqueId().toString() + ".coolDowns", new ArrayList<String>());



                    nameUuidMap.put(player.getName().toLowerCase(), UUID.fromString(player.getUniqueId().toString()));
                } else {

                    configuration.set("players." + player.getUniqueId().toString() + ".name", player.getName());
                }
                ConfigurationSection configurationSection = configuration.getConfigurationSection("players." + player.getUniqueId().toString());
                ForcePlayer infurePlayer = new ForcePlayer(player.getUniqueId(), player.getName(), configurationSection);
                add(infurePlayer);
            }
        }.runTaskAsynchronously(ForcePowers.getInstance());
    }

    public void  save(ForcePlayer infurePlayer) {
       saveFast(infurePlayer);
    }

    private void add(ForcePlayer infurePlayer) {
        playerMap.put(infurePlayer.getUuid(), infurePlayer);
    }

    public void logoutAllPlayers() {
        for (ForcePlayer infurePlayer : playerMap.values()) {
            saveFast(infurePlayer);
            Player player = Bukkit.getServer().getPlayer(infurePlayer.getUuid());
            if (player != null && (player.isOnline())) {
                logout(player);
                player.kickPlayer("Rebooting");
            }
        }
        saveConfig();
        playerMap.clear();
    }

    public void logout(Player player) {

        player.setSprinting(false);
    }

    private void saveFast(ForcePlayer infurePlayer) {
        configuration.set("players." + infurePlayer.getUuid().toString()  + ".name", infurePlayer.getName());
        configuration.set("players." + infurePlayer.getUuid().toString()  + ".skillPoints", infurePlayer.getSkillPoints());
        configuration.set("players." + infurePlayer.getUuid().toString()  + ".side", infurePlayer.getSide().toString());
        List<String> levelsStringList = new ArrayList<>();
        Map<String, Integer> levelMap = infurePlayer.getLevelMap();
        for (String s : levelMap.keySet()) {
            Integer integer = levelMap.get(s);
            levelsStringList.add(s + ":" + integer);
        }
        List<String> coolDownStringList = new ArrayList<>();
        Map<String, Long> coolDownMap = infurePlayer.getCoolDownMap();
        for (String s : coolDownMap.keySet()) {
            long aLong = coolDownMap.get(s);
            coolDownStringList.add(s + ":" + aLong);
        }
        configuration.set("players." + infurePlayer.getUuid().toString()  + ".levels",levelsStringList);
        configuration.set("players." + infurePlayer.getUuid().toString()  + ".coolDowns",coolDownStringList);


    }

    public void remove(UUID uuid) {
        if (playerMap.containsKey(uuid)) {
            playerMap.remove(uuid);
        }
    }

    private Map<String, UUID> nameUuidMap = new HashMap<>();

    public boolean isPlayer(String player1) {
       return nameUuidMap.containsKey(player1.toLowerCase());
    }

    public int getUniqueJoins() {
        return nameUuidMap.values().size();
    }
}
