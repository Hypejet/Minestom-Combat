package net.hypejet.combat.knockback.cause;

import net.minestom.server.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a cause of the knockback.
 *
 * @since 1.0
 * @author Codestech
 */
public sealed interface KnockbackCause {
    /**
     * Represents a knockback caused by an attacking entity.
     *
     * @param attacker the attacking entity
     * @since 1.0
     * @author Codestech
     */
    record EntityAttack(@NotNull Entity attacker) implements KnockbackCause {}

    /**
     * Represents a knockback with no cause specified.
     *
     * @since 1.0
     * @author Codeestech
     */
    final class Other implements KnockbackCause {
        private static final Other INSTANCE = new Other();
        private Other() {}
    }

    /**
     * Gets an instance of a {@link Other} knockback cause.
     *
     * @return the instance
     * @since 1.0
     */
    static @NotNull Other other() {
        return Other.INSTANCE;
    }
}