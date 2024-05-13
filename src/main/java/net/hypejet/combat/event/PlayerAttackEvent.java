package net.hypejet.combat.event;

import net.hypejet.combat.player.CombatPlayer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event called when a player attacks an entity.
 *
 * @since 1.0
 * @author Codestech
 */
public final class PlayerAttackEvent implements PlayerEvent, CancellableEvent {

    private final CombatPlayer player;
    private final Entity target;

    private boolean cancelled;

    /**
     * Constructs a {@link PlayerAttackEvent}.
     *
     * @param player a player that attacked the target
     * @param target an entity that has been attacked
     * @since 1.0
     */
    public PlayerAttackEvent(@NotNull CombatPlayer player, @NotNull Entity target) {
        this.player = player;
        this.target = target;
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
     * Gets an entity that has been attacked.
     *
     * @return the entity
     * @since 1.0
     */
    public @NotNull Entity getTarget() {
        return this.target;
    }
}