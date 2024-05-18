package net.hypejet.combat.attack.event;

import net.hypejet.combat.attack.values.AttackValues;
import net.hypejet.combat.entity.CombatEntity;
import net.hypejet.combat.entity.CombatPlayer;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event called while a player attacks an entity.
 * </p>
 * The main difference between this and {@link PlayerPreAttackEvent} is that this
 * event additionally provides attack values and is called later.
 *
 * @since 1.0
 * @author Codestech
 */
public final class PlayerAttackEvent implements PlayerEvent, CancellableEvent {

    private final CombatPlayer player;
    private final CombatEntity target;

    private AttackValues values;

    private boolean cancelled;

    /**
     * Constructs a {@link PlayerAttackEvent}.
     *
     * @param player a player that tries to attack the target
     * @param target an entity that is being attacked
     * @param values a values of the attack
     * @since 1.0
     */
    public PlayerAttackEvent(@NotNull CombatPlayer player, @NotNull CombatEntity target, @NotNull AttackValues values) {
        this.player = player;
        this.target = target;
        this.values = values;
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

    /**
     * Gets a values of the attack.
     *
     * @return the values
     * @since 1.0
     */
    public @NotNull AttackValues getValues() {
        return this.values;
    }

    /**
     * Sets a values of the attack.
     *
     * @param values the values
     * @since 1.0
     */
    public void setValues(@NotNull AttackValues values) {
        this.values = values;
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
}