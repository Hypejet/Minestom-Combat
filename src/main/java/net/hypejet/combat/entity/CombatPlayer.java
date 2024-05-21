package net.hypejet.combat.entity;

import net.hypejet.combat.attack.event.EntityCombustByPlayerEvent;
import net.hypejet.combat.attack.event.PlayerAttackEvent;
import net.hypejet.combat.attack.event.PlayerPreAttackEvent;
import net.hypejet.combat.attack.event.PlayerSwingHandEvent;
import net.hypejet.combat.attack.values.AttackValues;
import net.hypejet.combat.block.FacingBlockProperties;
import net.hypejet.combat.block.TrapdoorBlockProperties;
import net.hypejet.combat.knockback.cause.KnockbackCause;
import net.hypejet.combat.util.BlockUtil;
import net.hypejet.combat.util.KnockbackUtil;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class CombatPlayer extends Player implements CombatEntity {

    private final ReentrantLock lock = new ReentrantLock();

    private int attackStrengthTicks;
    private ItemStack lastItemInMainHand;

    public CombatPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

    @Override
    public void swingMainHand() {
        this.swingHand(Hand.MAIN, super::swingMainHand);
    }

    @Override
    public void swingOffHand() {
        this.swingHand(Hand.OFF, super::swingOffHand);
    }

    @Override
    public void update(long time) {
        super.update(time);
        this.attackStrengthTick();
        this.sendMessage("On climbable: " + this.isOnClimbable());
    }

    /**
     * Attacks a target entity.
     *
     * @param target the entity
     * @since 1.0
     */
    public void attack(@NotNull CombatEntity target) {
        if (!target.isAttackable()) return;
        if (!target.canBeAttackedBy(this)) return;

        PlayerPreAttackEvent preAttackEvent = new PlayerPreAttackEvent(this, target);
        EventDispatcher.call(preAttackEvent);

        if (preAttackEvent.isCancelled()) return;

        float attackDamage = (float) this.getAttributeValue(Attribute.GENERIC_ATTACK_DAMAGE);
        float damageBonus = 0; // TODO: Enchantment manager
        float attackStrength = this.getAttackStrength();

        attackDamage *= 0.2f + attackStrength * attackStrength * 0.8f;
        damageBonus *= attackStrength;

        this.resetAttackStrengthTicks();

        // TODO: Projectile handling

        if (attackStrength <= 0 && damageBonus <= 0) return;

        boolean strongAttack = attackStrength > 0.9;
        boolean sprintAttack = this.isSprinting() && strongAttack;

        int knockbackBonus = 0; // TODO: Enchantment manager

        if (sprintAttack) {
            this.getInstance().playSound(
                    Sound.sound(SoundEvent.ENTITY_PLAYER_ATTACK_KNOCKBACK, Sound.Source.PLAYER, 1, 1),
                    this.getPosition()
            );
            knockbackBonus++;
        }

        attackDamage += 0; // TODO: Item attack damage bonus

        boolean criticalAttack = strongAttack && this.fallDistance() > 0 && !this.isOnGround() && !this.isOnClimbable()
                && !this.isInWater() && !this.hasEffect(PotionEffect.BLINDNESS) && !this.isPassenger()
                && target instanceof LivingEntity && this.isSprinting();

        if (criticalAttack)
            attackStrength *= 1.5f;

        attackStrength += damageBonus;

        double lastMoveDistance = 0; // TODO

        boolean sweepAttack = strongAttack && !criticalAttack && !sprintAttack && this.onGround
                && lastMoveDistance < this.getSpeed(); // TODO: Check if an item in the main hand is a sword

        int fireAspect = 0; // TODO: Enchantment manager

        AttackValues values = new AttackValues(
                attackDamage, damageBonus, attackStrength, strongAttack, sprintAttack, criticalAttack, sweepAttack,
                knockbackBonus, fireAspect
        );

        PlayerAttackEvent attackEvent = new PlayerAttackEvent(this, target, values);
        EventDispatcher.call(attackEvent);

        if (attackEvent.isCancelled()) return;

        double initialHealth = 0f;

        if (target instanceof LivingEntity entity) {
            initialHealth = entity.getHealth();
        }

        Entity targetEntity = target.asMinestomEntity();
        boolean damageSucceed = false;

        Vec velocity = targetEntity.getVelocity();

        if (targetEntity instanceof LivingEntity entity) {
            damageSucceed = entity.damage(DamageType.PLAYER_ATTACK, attackDamage); // TODO: Custom damage class
        }

        if (!damageSucceed) {
            this.instance.playSound(
                    Sound.sound(SoundEvent.ENTITY_PLAYER_ATTACK_NODAMAGE, Sound.Source.PLAYER, 1, 1),
                    this.getPosition()
            );
            this.getInventory().update();
            return;
        }

        boolean combusted = false;

        if (targetEntity instanceof LivingEntity entity && fireAspect > 0 && !entity.isOnFire()) {
            EntityCombustByPlayerEvent combustEvent = new EntityCombustByPlayerEvent(this, entity, 1);
            EventDispatcher.call(combustEvent);

            if (!combustEvent.isCancelled()) {
                combusted = true;
                entity.setFireForDuration(1, TimeUnit.SECOND);
            }
        }

        if (knockbackBonus > 0) {
            KnockbackUtil.knockback(
                    knockbackBonus * 0.5f,
                    Math.sin(this.getPosition().yaw() * 0.017453292f),
                    -(Math.cos(this.getPosition().yaw() * 0.017453292f)),
                    target,
                    new KnockbackCause.EntityAttack(this)
            );

            this.setDeltaMovement(this.getVelocity().mul(new Vec(0.6, 1, 0.6).mul(ServerFlag.SERVER_TICKS_PER_SECOND)));
            this.setSprinting(false);
        }
    }

    private void swingHand(Player.@NotNull Hand hand, @NotNull Runnable success) {
        EventDispatcher.callCancellable(new PlayerSwingHandEvent(this, hand), () -> {
            this.resetAttackStrengthTicks();
            success.run();
        });
    }

    private void resetAttackStrengthTicks() {
        try {
            this.lock.lock();
            this.attackStrengthTicks = 0;
        } finally {
            this.lock.unlock();
        }
    }

    private void attackStrengthTick() {
        try {
            this.lock.lock();

            int initialStrengthTicks = this.attackStrengthTicks;
            int newStrengthTicks = initialStrengthTicks + 1;

            ItemStack lastHandItem = Objects.requireNonNullElse(this.lastItemInMainHand, ItemStack.AIR);
            ItemStack currentHandItem = this.getItemInMainHand();

            if (!lastHandItem.equals(currentHandItem)) {
                if (lastHandItem.material() != currentHandItem.material()) {
                    newStrengthTicks = 0;
                }

                this.lastItemInMainHand = currentHandItem;
            }

            if (initialStrengthTicks != newStrengthTicks) {
                this.attackStrengthTicks = newStrengthTicks;
            }
        } finally {
            this.lock.unlock();
        }
    }

    private float getAttackStrength() {
        try {
            this.lock.lock();
            return Math.clamp(((float) this.attackStrengthTicks + 0.5f) / this.getCurrentItemAttackStrengthDelay(), 0, 1);
        } finally {
            this.lock.unlock();
        }
    }

    private float getCurrentItemAttackStrengthDelay() {
        return (float) (1D / this.getAttributeValue(Attribute.GENERIC_ATTACK_SPEED) * 20);
    }

    private float fallDistance() {
        return 0; // TODO
    }

    private boolean isOnClimbable() {
        if (this.getGameMode() == GameMode.SPECTATOR) return false;

        Pos position = this.position;
        Block block = this.instance.getBlock(position);

        if (BlockUtil.isClimbable(block)) return true;

        return BlockUtil.isTrapdoor(block) && this.isTrapdoorUsableAsLadder(position, block);
    }

    private boolean isTrapdoorUsableAsLadder(@NotNull Pos blockPosition, @NotNull Block block) {
        TrapdoorBlockProperties properties = TrapdoorBlockProperties.of(block);

        if (!properties.isOpen()) return false;

        Block blockBelow = this.instance.getBlock(blockPosition.sub(0, 1, 0));
        FacingBlockProperties belowProperties = FacingBlockProperties.ofUnknown(blockBelow);

        return blockBelow.compare(Block.LADDER) && belowProperties.blockFace() == properties.blockFace();
    }

    private boolean isInWater() {
        return false; // TODO
    }

    private boolean isPassenger() {
        return this.vehicle != null;
    }

    private double getSpeed() {
        return this.getAttributeValue(Attribute.GENERIC_MOVEMENT_SPEED);
    }

    @Override
    public void setDeltaMovement(@NotNull Vec velocity) {
        this.velocity = velocity;
    }
}
