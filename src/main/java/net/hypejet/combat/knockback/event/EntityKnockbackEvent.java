package net.hypejet.combat.knockback.event;

import net.hypejet.combat.entity.CombatEntity;
import net.hypejet.combat.knockback.cause.KnockbackCause;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event called when a living entity is getting a knockback.
 *
 * @since 1.0
 * @author Codestech
 */
public final class EntityKnockbackEvent implements EntityInstanceEvent, CancellableEvent {

    private final CombatEntity victim;

    private final KnockbackCause cause;
    private final double strength;

    private Vec velocity;

    private boolean cancelled;

    /**
     * Constructs a {@link EntityKnockbackEvent}.
     *
     * @param victim an entity that is getting the knockback
     * @param cause a cause of the knockback
     * @param strength a strength of the knockback
     * @param velocity a velocity of the knockback
     */
    public EntityKnockbackEvent(@NotNull CombatEntity victim, @NotNull KnockbackCause cause, double strength,
                                @NotNull Vec velocity) {
        this.victim = victim;
        this.cause = cause;
        this.strength = strength;
        this.velocity = velocity;
    }

    /**
     * Gets an entity that is getting the knockback/.
     *
     * @return the entity
     * @since 1.0
     */
    public @NotNull CombatEntity getVictim() {
        return this.victim;
    }

    /**
     * Gets a cause of the knockback.
     *
     * @return the cause
     * @since 1.0
     */
    public @NotNull KnockbackCause getCause() {
        return this.cause;
    }

    /**
     * Gets a strength of the knockback.
     *
     * @return the strength
     * @since 1.0
     */
    public double getStrength() {
        return this.strength;
    }

    /**
     * Gets a velocity of the knockback.
     *
     * @return the velocity
     * @since 1.0
     */
    public @NotNull Vec getVelocity() {
        return this.velocity;
    }

    /**
     * Sets a velocity of the knockback.
     *
     * @param velocity the velocity
     * @since 1.0
     */
    public void setKnockback(@NotNull Vec velocity) {
        this.velocity = velocity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = true;
    }

    @Override
    public @NotNull Entity getEntity() {
        return this.victim.asMinestomEntity();
    }
}