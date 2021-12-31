package de.hglabor.plugins.kitapi.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RotationUtils {

    public static Rotation getNeededRotations(Entity to, Player from) {
        Vector eyesPos = from.getEyeLocation().toVector();
        Vector vec = to.getLocation().toVector();
        double diffX = vec.getX() - eyesPos.getX();
        double diffY = vec.getY() - eyesPos.getY();
        double diffZ = vec.getZ() - eyesPos.getZ();
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getNeededRotations(Entity to, Entity from) {
        Vector eyesPos = from.getLocation().toVector();
        Vector vec = to.getLocation().toVector();
        double diffX = vec.getX() - eyesPos.getX();
        double diffY = vec.getY() - eyesPos.getY();
        double diffZ = vec.getZ() - eyesPos.getZ();
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getNeededRotations(Location to, Entity from) {
        Vector eyesPos = from.getLocation().toVector();
        Vector vec = to.toVector();
        double diffX = vec.getX() - eyesPos.getX();
        double diffY = vec.getY() - eyesPos.getY();
        double diffZ = vec.getZ() - eyesPos.getZ();
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getNeededRotations(Location to, Location from) {
        Vector eyesPos = from.toVector();
        Vector vec = to.toVector();
        double diffX = vec.getX() - eyesPos.getX();
        double diffY = vec.getY() - eyesPos.getY();
        double diffZ = vec.getZ() - eyesPos.getZ();
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new Rotation(yaw, pitch);
    }

    public static final class Rotation {
        private final float yaw;
        private final float pitch;

        public Rotation(float yaw, float pitch) {
            this.yaw = wrapDegrees(yaw);
            this.pitch = wrapDegrees(pitch);
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }
    }

    private static float wrapDegrees(float degrees) {
        float f = degrees % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

}
