package net.hypejet.combat;

import net.hypejet.combat.attack.event.PlayerAttackEvent;
import net.hypejet.combat.entity.CombatEntity;
import net.hypejet.combat.util.BlockUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public class Test {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        InstanceContainer container = MinecraftServer.getInstanceManager().createInstanceContainer();
        container.setChunkSupplier(LightingChunk::new);
        container.setGenerator(unit -> unit.modifier().fillHeight(-64, 0, Block.BAMBOO_BLOCK));

        LivingEntity entity = new TestEntity(EntityType.ZOMBIE);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
        entity.heal();

        MinestomCombat.init();
        EventNode<Event> node = MinestomCombat.createNode();

        EventNode<Event> global = MinecraftServer.getGlobalEventHandler();
        global.addListener(AsyncPlayerConfigurationEvent.class, event -> event.setSpawningInstance(container));
        global.addChild(node);

        global.addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            if (!entity.isActive()) entity.setInstance(container);
        });

        global.addListener(PlayerAttackEvent.class, event ->
                event.getPlayer().sendMessage("Values of the attack: " + event.getValues())
        );

        global.addListener(PlayerBlockPlaceEvent.class, event -> {
            Block block = event.getBlock();
            if (BlockUtil.isTrapdoor(block)) event.setBlock(block.withProperty("open", "true"));
        });

        server.start("localhost", 25565);

        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> global.removeChild(node));
    }

    private static final class TestEntity extends LivingEntity implements CombatEntity {

        private TestEntity(@NotNull EntityType entityType) {
            super(entityType);
        }

        @Override
        public void setDeltaMovement(@NotNull Vec velocity) {
            this.velocity = velocity;
        }
    }
}
