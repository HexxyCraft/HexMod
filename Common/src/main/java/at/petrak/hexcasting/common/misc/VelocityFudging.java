package at.petrak.hexcasting.common.misc;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.mixin.accessor.AccessorAbstractArrow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;

public class VelocityFudging {
    public static void register() {
        HexAPI.instance().registerSpecialVelocityGetter(EntityType.PLAYER, player -> {
            if (player instanceof ServerPlayer splayer) {
                return PlayerPositionRecorder.getMotion(splayer);
            } else {
                // bruh
                throw new IllegalStateException("Call this only on the server side, silly");
            }
        });

        HexAPI.instance().registerSpecialVelocityGetter(EntityType.ARROW, VelocityFudging::arrowVelocitizer);
        HexAPI.instance().registerSpecialVelocityGetter(EntityType.SPECTRAL_ARROW, VelocityFudging::arrowVelocitizer);
        // this is an arrow apparently
        HexAPI.instance().registerSpecialVelocityGetter(EntityType.TRIDENT, VelocityFudging::arrowVelocitizer);
    }

    private static Vec3 arrowVelocitizer(AbstractArrow arrow) {
        if (((AccessorAbstractArrow) arrow).hex$isInGround()) {
            return Vec3.ZERO;
        } else {
            return arrow.getDeltaMovement();
        }
    }
}
