package me.matthewe.forcepowers.utils;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


/**
 * Created by Matthew E on 11/4/2017.
 */
public class PlayerUtils {
    public static LivingEntity getTarget(Player from) {
        assert from != null;
        // SOME FIXED VALUES (maybe define them globally somewhere):
        // the radius^2:
        double radius2 = 10.0D * 10.0D;
        // the min. dot product (defines the min. angle to the target player)
        // higher value means lower angle means that the player is looking "more directly" at the target):
        // do some experiments, which angle / dotProduct value fits best for your case
        double minDot = 0.98D;

        String fromName = from.getName();
        Location fromLocation = from.getEyeLocation();
        String fromWorldName = fromLocation.getWorld().getName();
        Vector fromDirection = fromLocation.getDirection().normalize();
        Vector fromVectorPos = fromLocation.toVector();

        LivingEntity target = null;
        double minDistance2 = Double.MAX_VALUE;
        for (LivingEntity somePlayer : from.getWorld().getLivingEntities()) {
            if (somePlayer.getUniqueId().equals(from.getUniqueId())) continue;
            Location newTargetLocation = somePlayer.getEyeLocation();
            // check the world:
            if (!newTargetLocation.getWorld().getName().equals(fromWorldName)) continue;
            // check distance:
            double newTargetDistance2 = newTargetLocation.distanceSquared(fromLocation);
            if (newTargetDistance2 > radius2) continue;
            // check angle to target:
            Vector toTarget = newTargetLocation.toVector().subtract(fromVectorPos).normalize();
            // check the dotProduct instead of the angle, because it's faster:
            double dotProduct = toTarget.dot(fromDirection);
            if (dotProduct > minDot && from.hasLineOfSight(somePlayer) && (target == null || newTargetDistance2 < minDistance2)) {
                target = somePlayer;
                minDistance2 = newTargetDistance2;
            }
        }

        // can return null, if no player was found, which meets the conditions:
        return target;
    }

}
