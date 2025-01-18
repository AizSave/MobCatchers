package mobcatchers.methodpatches;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.Mob;
import necesse.entity.pickup.ItemPickupEntity;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

import java.util.HashSet;

@ModMethodPatch(target = Level.class, name = "onMobDied", arguments = {Mob.class, Attacker.class, HashSet.class})
public class MobDropEssence {

    @Advice.OnMethodEnter
    static boolean onEnter(@Advice.This Level level, @Advice.Argument(0) Mob mob, @Advice.Argument(1) Attacker attacker, @Advice.Argument(2) HashSet<Attacker> attackers) {
        if(ItemRegistry.itemExists(mob.getStringID() + "_essence") && GameRandom.globalRandom.getChance(0.1F)) {
            level.entityManager.pickups.add(new ItemPickupEntity(level, new InventoryItem(mob.getStringID() + "_essence"), mob.x, mob.y, 0, 0));
        }
        return false;
    }
}
