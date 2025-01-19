package mobcatchers.mobs;

import mobcatchers.ai.CollisionShooterHostileChaserWandererAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.leaves.CooldownAttackTargetAINode;
import necesse.entity.mobs.ai.behaviourTree.util.FlyingAIMover;
import necesse.entity.mobs.hostile.CryoFlakeMob;
import necesse.entity.particle.Particle;
import necesse.entity.projectile.CryoMissileProjectile;
import necesse.level.maps.IncursionLevel;

import java.awt.*;

public class BwCryoFlake extends CryoFlakeMob {

    public BwCryoFlake() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        final GameDamage damage;
        if (this.getLevel() instanceof IncursionLevel) {
            this.setMaxHealth(350);
            this.setHealthHidden(this.getMaxHealth());
            this.setArmor(30);
            damage = incursionDamage;
        } else {
            damage = baseDamage;
        }

        this.ai = new BehaviourTreeAI<>(this, new CollisionShooterHostileChaserWandererAI<BwCryoFlake>(null, 448, damage, 100, CooldownAttackTargetAINode.CooldownTimer.CAN_ATTACK, 2000, 384, 40000) {
            public boolean shootAtTarget(BwCryoFlake mob, Mob target) {
                if (BwCryoFlake.this.canAttack()) {
                    BwCryoFlake.this.attackSoundAbility.runAndSend();
                    BwCryoFlake.this.startAttackCooldown();
                    mob.getLevel().entityManager.projectiles.add(new CryoMissileProjectile(mob.getLevel(), mob, mob.x, mob.y, target.x, target.y, 100.0F, 448, damage, 100));
                    return true;
                } else {
                    return false;
                }
            }
        }, new FlyingAIMover());
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
