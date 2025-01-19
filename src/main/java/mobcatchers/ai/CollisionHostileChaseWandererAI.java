package mobcatchers.ai;

import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.entity.mobs.ai.behaviourTree.composites.SelectorAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.CollisionChaserAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.EscapeAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.WandererAINode;

import java.util.function.Supplier;

public class CollisionHostileChaseWandererAI<T extends Mob> extends SelectorAINode<T> {
    public final EscapeAINode<T> escapeAINode;
    public final CollisionHostileChaserAI<T> collisionHostileChaserAI;
    public final WandererAINode<T> wandererAINode;

    public CollisionHostileChaseWandererAI(final Supplier<Boolean> shouldEscape, int searchDistance, GameDamage damage, int knockback, int wanderFrequency) {
        this.addChild(this.escapeAINode = new EscapeAINode<T>() {
            public boolean shouldEscape(T mob, Blackboard<T> blackboard) {
                return false;
            }
        });
        this.addChild(this.collisionHostileChaserAI = new CollisionHostileChaserAI<T>(searchDistance, damage, knockback) {
            public boolean attackTarget(T mob, Mob target) {
                return CollisionHostileChaseWandererAI.this.attackTarget(mob, target);
            }
        });
        this.addChild(this.wandererAINode = new WandererAINode<>(wanderFrequency));
    }

    public boolean attackTarget(T mob, Mob target) {
        return CollisionChaserAINode.simpleAttack(mob, target, this.collisionHostileChaserAI.damage, this.collisionHostileChaserAI.knockback);
    }
}
