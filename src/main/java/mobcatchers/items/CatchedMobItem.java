package mobcatchers.items;

import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.PacketReader;
import necesse.engine.network.gameNetworkData.GNDItem;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.placeableItem.consumableItem.ConsumableItem;
import necesse.level.maps.Level;

public class CatchedMobItem extends ConsumableItem {
    public String summonMob;

    public CatchedMobItem(Rarity rarity, String summonMob) {
        super(500, true);
        this.setItemCategory("consumable");
        this.dropsAsMatDeathPenalty = true;
        this.rarity = rarity;
        this.worldDrawSize = 32;

        this.summonMob = summonMob;
    }

    @Override
    public float getBrokerValue(InventoryItem item) {
        if(rarity == Rarity.NORMAL) {
            return 5F;
        } else if(rarity == Rarity.COMMON) {
            return 10F;
        } else if(rarity == Rarity.UNCOMMON) {
            return 20F;
        } else if(rarity == Rarity.RARE) {
            return 40F;
        } else if(rarity == Rarity.EPIC) {
            return 80F;
        } else if(rarity == Rarity.LEGENDARY) {
            return 160F;
        }
        return super.getBrokerValue(item);
    }

    @Override
    public String canPlace(Level level, int x, int y, PlayerMob player, InventoryItem item, GNDItemMap mapContent) {
        return null;
    }

    @Override
    public InventoryItem onPlace(Level level, int x, int y, PlayerMob player, int seed, InventoryItem item, GNDItemMap mapContent) {
        if (level.isServer()) {
            float summonX = x;
            float summonY = y;
            if(player.getDistance(summonX, summonY) > 300) {
                float angle = (float) Math.atan2(y - player.y, x - player.x);
                summonX = player.x + 300 * (float) Math.cos(angle);
                summonY = player.y + 300 * (float) Math.sin(angle);
            }
            summon(level, summonX, summonY);
        }

        if (this.singleUse) {
            item.setAmount(item.getAmount() - 1);
        }

        return item;
    }

    public void summon(Level level, float x, float y) {
        level.entityManager.addMob(MobRegistry.getMob(summonMob, level), x, y);
    }

    @Override
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "catchedmob", "mob", MobRegistry.getLocalization(summonMob).translate()));
        return tooltips;
    }

    @Override
    public String getTranslatedTypeName() {
        return Localization.translate("item", "catchedmob");
    }

    @Override
    public void loadItemTextures() {
        this.itemTexture = GameTexture.fromFile("mobs/icons/" + summonMob);
    }

    @Override
    public GameMessage getLocalization(InventoryItem item) {
        GNDItemMap gndData = item.getGndData();
        if (gndData.hasKey("name")) {
            GNDItem gndItem = gndData.getItem("name");
            if (!GNDItem.isDefault(gndItem)) {
                String name = gndItem.toString();
                if (!name.isEmpty()) {
                    return new StaticMessage(name);
                }
            }
        }

        return new StaticMessage(Localization.translate("item", "catchedmobname", "mob", MobRegistry.getLocalization(summonMob).translate()));
    }

    public static void registerCatchedMob(Rarity rarity, String mob) {
        if(!isRegistered(mob)) {
            ItemRegistry.registerItem(mob + "_catched", new CatchedMobItem(rarity, mob), 0F, rarity != Rarity.QUEST);
        }
    }

    public static boolean isRegistered(String mob) {
        return ItemRegistry.itemExists(mob + "_catched");
    }
}
