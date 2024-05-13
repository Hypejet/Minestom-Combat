package net.hypejet.combat.event;

import net.hypejet.combat.player.CombatPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An event called when a {@link CombatPlayer} swings a hand.
 * </p>
 * It's recommended to use when you want to handle all swings, even
 * by calling {@link Player#swingMainHand()} and {@link Player#swingOffHand()}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class PlayerSwingHandEvent implements PlayerEvent, CancellableEvent {

    private final CombatPlayer player;
    private final Player.Hand hand;

    private boolean cancelled;

    /**
     * Constructs a {@link PlayerSwingHandEvent}.
     *
     * @param player a player that swung the hand
     * @param hand the hand that swung
     * @since 1.0
     */
    public PlayerSwingHandEvent(@NotNull CombatPlayer player, @NotNull Player.Hand hand) {
        this.player = player;
        this.hand = hand;
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
     * Gets a hand that swung during the event.
     *
     * @return the hand
     * @since 1.0
     */
    public Player.@NotNull Hand getHand() {
        return this.hand;
    }
}