package net.hypejet.combat.entity;

import net.minestom.server.Viewable;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents an entity that can be attacked.
 *
 * @since 1.0
 * @author Codestech
 */
public interface CombatEntity extends Viewable {
    /**
     * Sets a velocity for the entity without sending a packet.
     *
     * @param velocity the velocity
     * @since 1.0
     */
    void setDeltaMovement(@NotNull Vec velocity);

    /**
     * Gets whether the entity is attackable.
     *
     * @return true if the entity is attackable, false otherwise
     * @since 1.0
     */
    @Contract(pure = true)
    default boolean isAttackable() {
        return true;
    }

    /**
     * Gets whether the entity can be attacked by a player.
     * .
     * @param player the player
     * @return true if the entity can be attacked by the player, false otherwise
     * @since 1.0
     */
    @Contract(pure = true)
    default boolean canBeAttackedBy(@NotNull CombatPlayer player) {
        return true;
    }

    /**
     * Gets the entity as a minestom entity.
     *
     * @return the minestom entity
     * @throws IllegalStateException if the combat entity does not extends a minestom entity and the method
     *                               wasn't overridden
     * @since 1.0
     */
    @Contract(pure = true)
    default @NotNull Entity asMinestomEntity() {
        if (!(this instanceof Entity entity))
            throw new IllegalStateException("The combat entity does not extend a minestom entity");
        return entity;
    }

    @Override
    default boolean addViewer(@NotNull Player player) {
        return this.asMinestomEntity().addViewer(player);
    }

    @Override
    default boolean removeViewer(@NotNull Player player) {
        return this.asMinestomEntity().removeViewer(player);
    }

    @Override
    default @NotNull Set<@NotNull Player> getViewers() {
        return this.asMinestomEntity().getViewers();
    }
}