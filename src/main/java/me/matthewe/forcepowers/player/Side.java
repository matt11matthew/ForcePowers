package me.matthewe.forcepowers.player;

import org.bukkit.ChatColor;

/**
 * Created by Matthew E on 11/4/2017.
 */
public enum Side {
    SITH(0, "Sith",ChatColor.DARK_RED + ""),JEDI(1, "Jedi", ChatColor.AQUA + ""), NONE(2, "None", ChatColor.RED + ""), BOTH(3, "Both", ChatColor.GREEN + ChatColor.BOLD.toString());

    private int id;
    private String name;
    private String chatColor;

    Side(int id, String name, String chatColor) {
        this.id = id;
        this.name = name;
        this.chatColor = chatColor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChatColor() {
        return chatColor;
    }
}
