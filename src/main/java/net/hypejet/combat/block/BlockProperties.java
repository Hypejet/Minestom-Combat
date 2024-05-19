package net.hypejet.combat.block;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public sealed interface BlockProperties permits FacingBlockProperties, OpenableBlockProperties {
    @NotNull Block block();
}