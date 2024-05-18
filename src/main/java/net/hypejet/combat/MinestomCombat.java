package net.hypejet.combat;

import net.hypejet.combat.entity.CombatEntity;
import net.hypejet.combat.entity.CombatPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a main class for managing Minestom combat.
 *
 * @since 1.0
 * @author Codestech
 */
public final class MinestomCombat {

    private static final ReentrantLock LOCK = new ReentrantLock();

    private static boolean enabled;

    private MinestomCombat() {}

    /**
     * Initializes Minestom combat.
     *
     * @since 1.0
     */
    public static void init() {
        try {
            LOCK.lock();

            if (enabled) throw new UnsupportedOperationException("Minestom combat is already initialized");

            MinecraftServer.getConnectionManager().setPlayerProvider(CombatPlayer::new);
            enabled = true;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Creates an event node with event listeners needed to make minestom combat work.
     *
     * @return the event node
     * @since 1.0
     */
    public static @NotNull EventNode<Event> createNode() {
        try {
            LOCK.lock();

            if (!enabled) throw new UnsupportedOperationException("You need to init the Minestom combat first");

            EventNode<Event> node = EventNode.all("minestom-combat");
            node.addListener(EntityAttackEvent.class, MinestomCombat::handleAttack);

            return node;
        } finally {
            LOCK.unlock();
        }
    }

    private static void handleAttack(@NotNull EntityAttackEvent event) {
        if (!(event.getEntity() instanceof CombatPlayer attacker)) return;
        if (!(event.getTarget() instanceof CombatEntity target)) return;
        attacker.attack(target);
    }
}