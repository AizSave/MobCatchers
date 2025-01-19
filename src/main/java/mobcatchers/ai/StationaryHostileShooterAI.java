package mobcatchers.ai;

import necesse.engine.util.GameUtils;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.leaves.TargetFinderShooterAINode;

import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract public class StationaryHostileShooterAI<T extends Mob> extends TargetFinderShooterAINode<T> {
    public StationaryHostileShooterAI(int shootDistance, String focusTargetKey) {
        super(shootDistance, focusTargetKey);
    }

    public StationaryHostileShooterAI(int shootDistance) {
        super(shootDistance);
    }

    public Stream<Mob> streamTargets(T mob, int shootDistance) {
        return mob.getLevel().entityManager.streamAreaMobsAndPlayers(mob.x, mob.y, shootDistance).filter(
                m -> m.isHostile
        ).collect(Collectors.toList()).stream();
    }
}
