package net.hypejet.combat.util;

import net.hypejet.combat.entity.CombatEntity;
import net.hypejet.combat.knockback.cause.KnockbackCause;
import net.hypejet.combat.knockback.event.EntityKnockbackEvent;
import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.EventDispatcher;
import org.jetbrains.annotations.NotNull;

/**
 * An utility for knock-backing entities.
 *
 * @since 1.0
 * @author Codestech
 */
public final class KnockbackUtil {

    private KnockbackUtil() {}

    /**
     * Knocks back an entity.
     *
     * @param strength a strength of the knockback
     * @param x a knockback on the x-axis
     * @param z a knockback on the z-axis
     * @param victim the entity
     * @param cause a cause of the knockback
     * @since 1.0
     */
    public static void knockback(double strength, double x, double z, @NotNull CombatEntity victim,
                                 @NotNull KnockbackCause cause) {
        if (!(victim.asMinestomEntity() instanceof LivingEntity minestomEntity))
            throw new IllegalArgumentException("A victim of the combat should be a living entity");

        strength *= 1 - minestomEntity.getAttributeValue(Attribute.GENERIC_KNOCKBACK_RESISTANCE);

        if (strength > 0) {
            Vec initialVelocity = minestomEntity.getVelocity();

            Vec velocity = new Vec(x, 0, z)
                    .mul(ServerFlag.SERVER_TICKS_PER_SECOND)
                    .normalize()
                    .mul(strength);

            velocity = new Vec(
                    initialVelocity.x() / 2 - velocity.x(),
                    minestomEntity.isOnGround()
                            ? Math.min(0.4, initialVelocity.y() / 2 + strength)
                            : initialVelocity.y(),
                    initialVelocity.z() / 2 - velocity.z()
            );

            EntityKnockbackEvent event = new EntityKnockbackEvent(victim, cause, strength, velocity);

            EventDispatcher.callCancellable(event, () ->
                    victim.setDeltaMovement(event.getVelocity().mul(ServerFlag.SERVER_TICKS_PER_SECOND))
            );
        }
    }
}