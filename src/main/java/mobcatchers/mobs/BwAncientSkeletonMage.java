package mobcatchers.mobs;

import mobcatchers.ai.ConfusedCollisionHostileChaserWandererAI;
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
import necesse.entity.mobs.ai.behaviourTree.trees.ConfusedPlayerChaserWandererAI;
import necesse.entity.mobs.hostile.AncientSkeletonMageMob;
import necesse.entity.mobs.hostile.ZombieMob;
import necesse.entity.particle.Particle;
import necesse.entity.projectile.AncientSkeletonMageProjectile;

import java.awt.*;
import java.util.function.Supplier;

public class BwAncientSkeletonMage extends AncientSkeletonMageMob {

    public BwAncientSkeletonMage() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        ConfusedHostileChaserWandererAI<BwAncientSkeletonMage> chaserAI = new ConfusedHostileChaserWandererAI<BwAncientSkeletonMage>(null, 640, 320, 40000, false, false) {
            public boolean attackTarget(BwAncientSkeletonMage mob, Mob target) {
                if (mob.canAttack()) {
                    mob.attack(target.getX(), target.getY(), false);
                    mob.getLevel().entityManager.projectiles.add(new AncientSkeletonMageProjectile(mob.getLevel(), mob, mob.x, mob.y, target.x, target.y, 120.0F, 640, new GameDamage(65.0F), 50));
                    this.wanderAfterAttack = GameRandom.globalRandom.getChance(0.75F);
                    return true;
                } else {
                    return false;
                }
            }
        };
        chaserAI.addChildFirst(new FailerAINode<>(new TeleportOnProjectileHitAINode<BwAncientSkeletonMage>(3000, 7) {
            public boolean teleport(BwAncientSkeletonMage mob, int x, int y) {
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
