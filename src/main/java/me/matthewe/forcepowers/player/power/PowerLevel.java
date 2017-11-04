package me.matthewe.forcepowers.player.power;


import me.matthewe.forcepowers.player.ForcePlayer;
import me.matthewe.forcepowers.player.Side;
import org.bukkit.entity.LivingEntity;

/**
 * Created by Matthew E on 11/4/2017.
 */
public abstract class PowerLevel  {
    private Power power;
    private int level;
    private Side side;
    private int cost;
    private long coolDown;

    public PowerLevel(Power power, int level, int cost) {
        this.level = level;
        this.power = power;
        this.side = power.getSide();
        this.cost = cost;


    }

    public PowerLevel setSide(Side side) {
        this.side = side;
        return this;
    }

    public PowerLevel setCoolDown(long coolDown) {
        this.coolDown = coolDown;
        return this;
    }

    public PowerLevel setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public PowerLevel(Power power, int level, int cost, long coolDown) {
        this.level = level;
        this.power = power;
        this.side = power.getSide();
        this.cost = cost;

        this.coolDown = coolDown;
    }

    public Power getPower() {
        return power;
    }

    public int getLevel() {
        return level;
    }

    public Side getSide() {
        return side;
    }

    public int getCost() {
        return cost;
    }

    public long getCoolDown() {
        return coolDown;
    }


    public abstract void onUse(ForcePlayer forcePlayer, LivingEntity  livingEntity);
}
