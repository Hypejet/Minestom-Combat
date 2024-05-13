package net.hypejet.combat.player;

import net.hypejet.combat.event.PlayerAttackEvent;
import net.hypejet.combat.event.PlayerSwingHandEvent;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class CombatPlayer extends Player {

    private final ReentrantLock lock = new ReentrantLock();

    private int attackStrengthTicks;
    private ItemStack lastItemInMainHand;

    public CombatPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

    @Override
    public void swingMainHand() {
        this.swingHand(Hand.MAIN, super::swingMainHand);
    }

    @Override
    public void swingOffHand() {
        this.swingHand(Hand.OFF, super::swingOffHand);
    }

    @Override
    public void update(long time) {
        super.update(time);
        this.attackStrengthTick();
    }

    /**
     * Attacks a target entity.
     *
     * @param target the entity
     * @since 1.0
     */
    public void attack(@NotNull Entity target) {
        EventDispatcher.callCancellable(new PlayerAttackEvent(this, target), () -> {
            try {
                this.lock.lock();
                this.attackStrengthTicks = 0;
            } finally {
                this.lock.unlock();
            }
        });
    }

    private void swingHand(Player.@NotNull Hand hand, @NotNull Runnable success) {
        EventDispatcher.callCancellable(new PlayerSwingHandEvent(this, hand), () -> {
            this.resetAttackStrengthTicks();
            success.run();
        });
    }

    private void resetAttackStrengthTicks() {
        try {
            this.lock.lock();
            this.attackStrengthTicks = 0;
        } finally {
            this.lock.unlock();
        }
    }

    private void attackStrengthTick() {
        try {
            this.lock.lock();

            int initialStrengthTicks = this.attackStrengthTicks;
            int newStrengthTicks = initialStrengthTicks + 1;

            ItemStack lastHandItem = Objects.requireNonNullElse(this.lastItemInMainHand, ItemStack.AIR);
            ItemStack currentHandItem = this.getItemInMainHand();

            if (!lastHandItem.equals(currentHandItem)) {
                if (lastHandItem.material() != currentHandItem.material()) {
                    newStrengthTicks = 0;
                }

                this.lastItemInMainHand = currentHandItem;
            }

            if (initialStrengthTicks != newStrengthTicks) {
                this.attackStrengthTicks = newStrengthTicks;
            }

            this.sendMessage(Component.text("New strength ticks - " + newStrengthTicks));
            this.sendMessage(Component.text("Tick thread - " + Thread.currentThread().getName()));
        } finally {
            this.lock.unlock();
        }
    }
}
