package mobcatchers.mobs;

import mobcatchers.ai.CollisionShooterHostileChaserWandererAI;
import mobcatchers.ai.ConfusedCollisionHostileChaserWandererAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.leaves.CooldownAttackTargetAINode;
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionShooterPlayerChaserWandererAI;
import necesse.entity.mobs.hostile.GiantCaveSpiderMob;
import necesse.entity.mobs.hostile.ZombieMob;
import necesse.entity.particle.Particle;
import necesse.entity.projectile.CaveSpiderSpitProjectile;
import necesse.entity.projectile.CaveSpiderWebProjectile;
import necesse.inventory.item.toolItem.projectileToolItem.ProjectileToolItem;

import java.awt.*;
import java.util.function.Supplier;

public class BwGiantCaveSpider extends GiantCaveSpiderMob {

    public BwGiantCaveSpider() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public BwGiantCaveSpider(Variant variant, int health, GameDamage meleeDamage, GameDamage spitDamage) {
        super(variant, health, meleeDamage, spitDamage);
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        this.ai = new BehaviourTreeAI<>(this, new CollisionShooterHostileChaserWandererAI<BwGiantCaveSpider>(null, 256, this.meleeDamage, 100, CooldownAttackTargetAINode.CooldownTimer.HAS_TARGET, 3500, 480, 40000) {
            public boolean shootAtTarget(BwGiantCaveSpider mob, Mob target) {
                if (mob.canAttack() && !mob.inLiquid()) {
                    int targetDistance = (int)mob.getDistance(target);
                    mob.attack(target.getX(), target.getY(), false);
                    Point point = ProjectileToolItem.controlledRangePosition(GameRandom.globalRandom, mob.getX(), mob.getY(), target.getX(), target.getY(), Math.max(320, targetDistance + 32), 32, 16);
                    int pointDistance = (int)mob.getDistance((float)point.x, (float)point.y);
                    if (GameRandom.globalRandom.nextBoolean()) {
                        mob.getLevel().entityManager.projectiles.add(new CaveSpiderWebProjectile(mob.x, mob.y, (float)point.x, (float)point.y, mob.spitDamage, mob, pointDistance));
                    } else {
                        mob.getLevel().entityManager.projectiles.add(new CaveSpiderSpitProjectile(mob.variant, mob.x, mob.y, (float)point.x, (float)point.y, mob.spitDamage, mob, pointDistance));
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
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
