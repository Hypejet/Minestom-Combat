package net.hypejet.combat.attack.values;

/**
 * Represents a holder of the attack values.
 *
 * @param attackDamage a damage to deal to the target
 * @param damageBonus an enchantment bonus for the damage
 * @param attackStrength a strength of the attack
 *
 * @param strongAttack whether the attack strength is higher than 0.9
 * @param sprintAttack whether the player was sprinting while attacking and the attack was strong
 * @param criticalAttack whether the attack was critical
 * @param sweepAttack whether the sword sweep animation should be played
 *
 * @param knockbackBonus a strength of knockback to apply for a target
 * @param fireAspect a
 *
 * @since 1.0
 * @author Codestech
 */
public record AttackValues(float attackDamage, float damageBonus, float attackStrength,
                           boolean strongAttack, boolean sprintAttack, boolean criticalAttack,
                           boolean sweepAttack, int knockbackBonus, int fireAspect) {
    // TODO: A Builder?
}