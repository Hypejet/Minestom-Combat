package net.hypejet.combat.block;

public sealed interface OpenableBlockProperties extends BlockProperties permits TrapdoorBlockProperties {
    default boolean isOpen() {
        return Boolean.parseBoolean(this.block().getProperty("open"));
    }
}