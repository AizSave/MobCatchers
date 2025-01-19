package mobcatchers.mobs;

import mobcatchers.ai.ConfusedHostileChaserWandererAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.engine.util.gameAreaSearch.GameAreaStream;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.util.TargetFinderDistance;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.hostile.StabbyBushMob;
import necesse.entity.particle.Particle;

import java.awt.*;

public class BwStabbyBushMonster extends StabbyBushMob {

    public BwStabbyBushMonster() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        this.ai = new BehaviourTreeAI<>(this, new ConfusedHostileChaserWandererAI<BwStabbyBushMonster>(null, 576, 64, -1, false, false) {
            public boolean attackTarget(BwStabbyBushMonster mob, Mob target) {
                if (mob.canAttack()) {
                    mob.attack(target.getX(), target.getY(), false);
                    target.isServerHit(new GameDamage(20.0F), mob.dx, mob.dy, 15.0F, mob);
                    mob.buffManager.addBuff(new ActiveBuff(BuffRegistry.STABBY_BUSH_FRENZY_BUFF, mob, 20.0F, (Attacker) null), true);
                    return true;
                } else {
                    return false;
                }
            }

            public GameAreaStream<Mob> streamPossibleTargets(BwStabbyBushMonster mob, Point base, TargetFinderDistance<BwStabbyBushMonster> distance) {
                return mob.getLevel().entityManager.streamAreaMobsAndPlayers(base.x, base.y, distance.searchDistance).filter(
                        m -> m.isHostile || BwStabbyBushMonster.this.isAttacker(m)
                );
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
