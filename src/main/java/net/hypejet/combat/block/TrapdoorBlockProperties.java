package net.hypejet.combat.block;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public final class TrapdoorBlockProperties implements FacingBlockProperties, OpenableBlockProperties {

    private final Block block;

    TrapdoorBlockProperties(@NotNull Block block) {
        this.block = block;
    }

    @Override
    public @NotNull Block block() {
        return this.block;
    }

    public static @NotNull TrapdoorBlockProperties of(@NotNull Block block) {
        return new TrapdoorBlockProperties(block);
    }
}