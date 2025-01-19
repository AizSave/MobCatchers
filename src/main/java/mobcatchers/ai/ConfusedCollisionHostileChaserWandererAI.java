package mobcatchers.ai;

import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.event.ConfuseWanderAIEvent;
import necesse.entity.mobs.ai.behaviourTree.leaves.ConfusedWandererAINode;

import java.awt.geom.Point2D;
import java.util.function.Supplier;

public class ConfusedCollisionHostileChaserWandererAI<T extends Mob> extends CollisionHostileChaseWandererAI<T> {
    public final ConfusedWandererAINode<T> confusedWandererNode;

    public ConfusedCollisionHostileChaserWandererAI(Supplier<Boolean> shouldEscape, int searchDistance, GameDamage damage, int knockback, int wanderFrequency) {
        super(shouldEscape, searchDistance, damage, knockback, wanderFrequency);
        this.addChildFirst(this.confusedWandererNode = new ConfusedWandererAINode<>());
    }

    protected int getRandomConfuseTime() {
        return GameRandom.globalRandom.getChance(0.1F) ? GameRandom.globalRandom.getIntBetween(3000, 5000) : GameRandom.globalRandom.getIntBetween(500, 1000);
    }

    public boolean attackTarget(T mob, Mob target) {
        boolean success = super.attackTarget(mob, target);
        if (success && GameRandom.globalRandom.getChance(0.5F)) {
            Point2D.Float attackDir = GameMath.normalize(target.x - mob.x, target.y - mob.y);
            float attackAngle = GameMath.getAngle(attackDir);
            float runAwayAngle;
            if (GameRandom.globalRandom.nextBoolean()) {
                runAwayAngle = GameRandom.globalRandom.getFloatBetween(attackAngle - 90.0F, attackAngle - 110.0F);
            } else {
                runAwayAngle = GameRandom.globalRandom.getFloatBetween(attackAngle + 90.0F, attackAngle + 110.0F);
            }

            runAwayAngle = GameMath.fixAngle(runAwayAngle);
            Point2D.Float runAwayDir = GameMath.getAngleDir(runAwayAngle);
            int confuseTime = this.getRandomConfuseTime();
            this.getBlackboard().submitEvent("confuseWander", new ConfuseWanderAIEvent((long) confuseTime, runAwayDir));
        }

        return success;
    }
}
