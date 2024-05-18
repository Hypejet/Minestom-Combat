package net.hypejet.combat.attack.event;

import net.hypejet.combat.entity.CombatEntity;
import net.hypejet.combat.entity.CombatPlayer;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event called before a player attacks an entity.
 *
 * @since 1.0
 * @author Codestech
 */
public final class PlayerPreAttackEvent implements PlayerEvent, CancellableEvent {

    private final CombatPlayer player;
    private final CombatEntity target;

    private boolean cancelled;

    /**
     * Constructs a {@link PlayerPreAttackEvent}.
     *
     * @param player a player that tried to attack the target
     * @param target an entity that is being attacked
     * @since 1.0
     */
    public PlayerPreAttackEvent(@NotNull CombatPlayer player, @NotNull CombatEntity target) {
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
     * Gets an entity that is being attacked.
     *
     * @return the entity
     * @since 1.0
     */
    public @NotNull CombatEntity getTarget() {
        return this.target;
    }
}