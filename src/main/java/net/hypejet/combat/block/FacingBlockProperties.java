package net.hypejet.combat.block;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public sealed interface FacingBlockProperties extends BlockProperties
        permits FacingBlockProperties.UnknownFacingBlockProperties, TrapdoorBlockProperties {

    default @NotNull BlockFace blockFace() {
        String property = this.block().getProperty("facing");
        return switch (property) {
            case "down" -> BlockFace.BOTTOM;
            case "up" -> BlockFace.TOP;
            case "north" -> BlockFace.NORTH;
            case "west" -> BlockFace.WEST;
            case "south" -> BlockFace.SOUTH;
            case "east" -> BlockFace.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + property);
        };
    }

    static @NotNull FacingBlockProperties ofUnknown(@NotNull Block block) {
        return new UnknownFacingBlockProperties(block);
    }

    record UnknownFacingBlockProperties(@NotNull Block block) implements FacingBlockProperties {}
}