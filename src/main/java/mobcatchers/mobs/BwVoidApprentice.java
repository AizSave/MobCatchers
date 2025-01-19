package mobcatchers.mobs;

import mobcatchers.ai.ConfusedHostileChaserWandererAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.decorators.FailerAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.TeleportOnProjectileHitAINode;
import necesse.entity.mobs.hostile.VoidApprentice;
import necesse.entity.particle.Particle;
import necesse.entity.projectile.VoidApprenticeProjectile;

import java.awt.*;

public class BwVoidApprentice extends VoidApprentice {

    public BwVoidApprentice() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        ConfusedHostileChaserWandererAI<BwVoidApprentice> chaserAI = new ConfusedHostileChaserWandererAI<BwVoidApprentice>(null, 640, 320, 40000, true, false) {
            public boolean attackTarget(BwVoidApprentice mob, Mob target) {
                if (mob.canAttack() && !mob.isAccelerating() && !mob.hasCurrentMovement()) {
                    mob.attack(target.getX(), target.getY(), false);
                    mob.getLevel().entityManager.projectiles.add(new VoidApprenticeProjectile(mob.x, mob.y, target.x, target.y, new GameDamage(27.0F), mob));
                    this.wanderAfterAttack = GameRandom.globalRandom.getChance(0.75F);
                    return true;
                } else {
                    return false;
                }
            }
        };
        chaserAI.addChildFirst(new FailerAINode<>(new TeleportOnProjectileHitAINode<BwVoidApprentice>(5000, 7) {
            public boolean teleport(BwVoidApprentice mob, int x, int y) {
                if (mob.isServer()) {
                    mob.teleportAbility.runAndSend(x, y);
                    this.getBlackboard().mover.stopMoving(mob);
                }

                return true;
            }
        }));
        this.ai = new BehaviourTreeAI<>(this, chaserAI);
        this.isHostile = false;
        this.setTeam(-1);
    }

    @Override
    public GameMessage getLocalization() {
        return new StaticMessage(Localization.translate("item", "brainwashedmobname", "mob", MobRegistry.getLocalization(this.getStringID().replace("_brainwashed", "")).translate()));
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (this.isVisible()) {
            this.getLevel().entityManager.addParticle(this.x + GameRandom.globalRandom.nextInt(5) + (float) (GameRandom.globalRandom.nextGaussian() * 6.0), this.y + GameRandom.globalRandom.nextInt(5) + (float) (GameRandom.globalRandom.nextGaussian() * 8.0), Particle.GType.IMPORTANT_COSMETIC).movesFriction(this.dx / 10.0F, this.dy / 10.0F, GameRandom.globalRandom.getFloatBetween(0, 10F)).color(new Color(255, 0, 255)).height(GameRandom.globalRandom.getFloatBetween(this.getFlyingHeight(), 16F + this.getFlyingHeight()));
        }
    }
}
