package net.hypejet.combat.attack;

import net.hypejet.combat.player.CombatPlayer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import org.jetbrains.annotations.NotNull;

public final class AttackManager {

    private final EventNode<Event> parentNode, node;

    public AttackManager(@NotNull EventNode<Event> parentNode) {
        this.parentNode = parentNode;
        this.node = EventNode.all("attack-manager")
                .addListener(EntityAttackEvent.class, this::handleAttack);
    }

    public void enable() {
        this.parentNode.addChild(this.node);
    }

    public void disable() {
        this.parentNode.removeChild(this.node);
    }

    public void handleAttack(@NotNull EntityAttackEvent event) {
        if (event.getEntity() instanceof CombatPlayer attacker) attacker.attack(event.getTarget());
    }
}