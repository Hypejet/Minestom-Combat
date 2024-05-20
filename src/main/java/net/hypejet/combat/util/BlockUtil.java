package net.hypejet.combat.util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.gamedata.tags.Tag;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public final class BlockUtil {

    private static final String CLIMBABLE_TAG_KEY = "minecraft:climbable";
    private static final String TRAPDOOR_TAG_KEY = "minecraft:trapdoors";

    private BlockUtil() {}

    /**
     * Gets whether a block has a climbable tag.
     *
     * @param block the block
     * @return true if the block has a climbable tag, false otherwise
     * @throws IllegalStateException if the climbable tag is null
     * @since 1.0
     */
    public static boolean isClimbable(@NotNull Block block) {
        return is(block, CLIMBABLE_TAG_KEY);
    }

    /**
     * Gets whether a block has a trapdoor tag.
     *
     * @param block the block
     * @return true if the block has a trapdoor tag, false otherwise
     * @throws IllegalStateException if the trapdoor tag is null
     * @since 1.0
     */
    public static boolean isTrapdoor(@NotNull Block block) {
        return is(block, TRAPDOOR_TAG_KEY);
    }

    private static boolean is(@NotNull Block block, @NotNull String namespace) {
        Tag tag = MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, namespace);

        if (tag == null) {
            throw new IllegalStateException("The tag with namespace of \"" + namespace + "\" is null");
        }

        for (NamespaceID id : tag.getValues())
            if (id.equals(block.namespace()))
                return true;

        return false;
    }
}