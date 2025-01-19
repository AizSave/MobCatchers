package mobcatchers.ai;

import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.entity.mobs.ai.behaviourTree.composites.SelectorAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.CooldownAttackTargetAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.EscapeAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.WandererAINode;

import java.util.function.Supplier;

abstract public class CollisionShooterHostileChaserWandererAI<T extends Mob> extends SelectorAINode<T> {
    public CollisionShooterHostileChaserWandererAI(final Supplier<Boolean> shouldEscape, int searchDistance, GameDamage damage, int knockback, CooldownAttackTargetAINode.CooldownTimer cooldownTimer, int shootCooldown, int shootDistance, int wanderFrequency) {
        this.addChild(new EscapeAINode<T>() {
            public boolean shouldEscape(T mob, Blackboard<T> blackboard) {
                return false;
            }
        });
        CollisionHostileChaserAI<T> chaser = new CollisionHostileChaserAI<>(searchDistance, damage, knockback);
        chaser.addChild(new CooldownAttackTargetAINode<T>(cooldownTimer, shootCooldown, shootDistance) {
            public boolean attackTarget(T mob, Mob target) {
                return CollisionShooterHostileChaserWandererAI.this.shootAtTarget(mob, target);
            }
        });
        this.addChild(chaser);
        this.addChild(new WandererAINode<>(wanderFrequency));
    }

    public abstract boolean shootAtTarget(T var1, Mob var2);
}
