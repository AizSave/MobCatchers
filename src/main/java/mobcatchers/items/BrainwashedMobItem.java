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
import necesse.gfx.Renderer;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.placeableItem.consumableItem.ConsumableItem;
import necesse.level.maps.Level;

import java.awt.*;

public class BrainwashedMobItem extends CatchedMobItem {
    public BrainwashedMobItem(Rarity rarity, String summonMob) {
        super(rarity, summonMob);
    }

    @Override
    public float getBrokerValue(InventoryItem item) {
        return super.getBrokerValue(item) * 2;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "brainwashedmob"));
        return tooltips;
    }

    @Override
    public void summon(Level level, float x, float y) {
        level.entityManager.addMob(MobRegistry.getMob(summonMob + "_brainwashed", level), x, y);
    }

    public String getTranslatedTypeName() {
        return Localization.translate("item", "brainwashedmob");
    }

    @Override
    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        Renderer.initQuadDraw(size, size).color(255.0F, 0.0F, 255.0F, 0.5F).draw(x, y);
        super.drawIcon(item, perspective, x, y, size, color);
    }

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

        return new StaticMessage(Localization.translate("item", "brainwashedmobname", "mob", MobRegistry.getLocalization(summonMob).translate()));
    }

    public static void registerCatchedMob(Rarity rarity, String mob) {
        if(!isRegistered(mob)) {
            ItemRegistry.registerItem(mob + "_brainwashed", new BrainwashedMobItem(rarity, mob), 0F, rarity != Rarity.QUEST);
        }
    }

    public static boolean isRegistered(String mob) {
        return ItemRegistry.itemExists(mob + "_brainwashed");
    }
}
