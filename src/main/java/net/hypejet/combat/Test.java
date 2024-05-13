package net.hypejet.combat;

import net.hypejet.combat.attack.AttackManager;
import net.hypejet.combat.event.PlayerAttackEvent;
import net.hypejet.combat.event.PlayerSwingHandEvent;
import net.hypejet.combat.player.CombatPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class Test {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        InstanceContainer container = MinecraftServer.getInstanceManager().createInstanceContainer();
        container.setChunkSupplier(LightingChunk::new);
        container.setGenerator(unit -> unit.modifier().fillHeight(-64, 0, Block.BAMBOO_BLOCK));

        LivingEntity entity = new LivingEntity(EntityType.ZOMBIE);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
        entity.heal();

        EventNode<Event> node = EventNode.all("combat");
        AttackManager attackManager = new AttackManager(node);
        attackManager.enable();

        EventNode<Event> global = MinecraftServer.getGlobalEventHandler();
        global.addListener(AsyncPlayerConfigurationEvent.class, event -> event.setSpawningInstance(container));
        global.addChild(node);

        global.addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            if (!entity.isActive()) entity.setInstance(container);
        });

        MinecraftServer.getConnectionManager().setPlayerProvider(CombatPlayer::new);

        server.start("localhost", 25565);

        MinecraftServer.getSchedulerManager().buildShutdownTask(attackManager::disable);
    }
}
