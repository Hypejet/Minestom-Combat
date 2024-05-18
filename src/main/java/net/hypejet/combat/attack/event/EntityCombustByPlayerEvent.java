package net.hypejet.combat.attack.event;

import net.hypejet.combat.entity.CombatPlayer;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event called when a player combusts a living entity.
 *
 * @since 1.0
 * @author Codestech
 */
public final class EntityCombustByPlayerEvent implements PlayerInstanceEvent, CancellableEvent {

    private final CombatPlayer player;
    private final LivingEntity target;

    private int duration;

    private boolean cancelled;

    /**
     * Constructs a {@link EntityCombustByPlayerEvent}.
     *
     * @param player the player that combusts an entity
     * @param target the entity that is being combusted
     * @param duration time in seconds, in which the target will be alight for
     * @since 1.0
     */
    public EntityCombustByPlayerEvent(@NotNull CombatPlayer player, @NotNull LivingEntity target, int duration) {
        this.player = player;
        this.target = target;
        this.duration = duration;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull CombatPlayer getPlayer() {
        return this.player;
    }

    /**
     * Gets an entity that is being combusted.
     *
     * @return the entity
     * @since 1.0
     */
    public @NotNull LivingEntity getTarget() {
        return this.target;
    }

    /**
     * Gets time in seconds, in which the target will be alight for.
     *
     * @return the time
     * @since 1.0
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Sets time in seconds, in which the target will be alight for.
     *
     * @param duration the time
     * @since 1.0
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}