package mobcatchers.items;

import necesse.engine.localization.Localization;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.levelEvent.mobAbilityLevelEvent.ToolItemMobAbilityEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.pickup.ItemPickupEntity;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.miscToolItem.NetToolItem;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.level.maps.Level;
import necesse.level.maps.levelBuffManager.LevelModifiers;

import java.util.ArrayList;

public class MobCatcher extends NetToolItem {

    public MobCatcher() {
        this.rarity = Rarity.LEGENDARY;
    }

    public boolean canHitMob(Mob mob, ToolItemMobAbilityEvent event) {
        return ItemRegistry.itemExists(mob.getStringID() + "_essence") && event.getAttackOwner() != null && event.getAttackOwner().isPlayer && ((PlayerMob) event.getAttackOwner()).getInv().getAmount(ItemRegistry.getItem(mob.getStringID() + "_essence"), false, false, false, false, "mobessence") > 0;
    }

    @Override
    public void hitMob(InventoryItem item, ToolItemMobAbilityEvent event, Level level, Mob target, Mob attacker) {
        if(attacker != null && attacker.isPlayer && ((PlayerMob) attacker).getInv().removeItems(ItemRegistry.getItem(target.getStringID() + "_essence"), 1, false, false, false, false, "mobessence") > 0) {
            target.dropsLoot = false;
            super.hitMob(item, event, level, target, attacker);
            dropLoot(level, target);
        }
    }

    public void dropLoot(Level level, Mob target) {
        ArrayList<InventoryItem> drops = this.getLootTable(level, target).getNewList(GameRandom.globalRandom, level.buffManager.getModifier(LevelModifiers.LOOT));
        drops.forEach(
                drop -> level.entityManager.pickups.add(new ItemPickupEntity(level, drop, target.x, target.y, 0, 0))
        );
    }

    public LootTable getLootTable(Level level, Mob target) {
        LootTable lootTable = new LootTable();
        lootTable.items.add(new LootItem(target.getStringID() + "_catched"));
        return lootTable;
    }

    public String getTranslatedTypeName() {
        return Localization.translate("item", "mobchatcher");
    }

    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.removeLast();
        tooltips.add(Localization.translate("itemtooltip", "mobcatcher"));
        return tooltips;
    }
}
