package mobcatchers.items;

import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.gameNetworkData.GNDItem;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.Renderer;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.matItem.MatItem;

import java.awt.*;

public class MobEssence extends MatItem {
    public String mob;
    public MobEssence(Rarity rarity, String mob) {
        super(500, "anymobessence");
        this.rarity = rarity;
        this.mob = mob;
    }

    @Override
    public float getBrokerValue(InventoryItem item) {
        if(rarity == Rarity.NORMAL) {
            return 2F;
        } else if(rarity == Rarity.COMMON) {
            return 4F;
        } else if(rarity == Rarity.UNCOMMON) {
            return 8F;
        } else if(rarity == Rarity.RARE) {
            return 16F;
        } else if(rarity == Rarity.EPIC) {
            return 32F;
        } else if(rarity == Rarity.LEGENDARY) {
            return 64F;
        }
        return super.getBrokerValue(item);
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

        return new StaticMessage(Localization.translate("item", "mobessence", "mob", MobRegistry.getLocalization(mob).translate()));
    }

    @Override
    public void loadItemTextures() {
        this.itemTexture = GameTexture.fromFile("mobs/icons/" + mob);
    }

    @Override
    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        Renderer.initQuadDraw(size, size).color(0.0F, 255.0F, 255.0F, 0.5F).draw(x, y);
        super.drawIcon(item, perspective, x, y, size, color);
    }

    @Override
    public void draw(InventoryItem item, PlayerMob perspective, int x, int y, boolean inInventory) {
        super.draw(item, perspective, x, y, inInventory);
    }

    public static void registerEssence(Rarity rarity, String mob) {
        if(!isRegistered(mob)) {
            ItemRegistry.registerItem(mob + "_essence", new MobEssence(rarity, mob), 0F, true);
        }
    }

    public static boolean isRegistered(String mob) {
        return ItemRegistry.itemExists(mob + "_essence");
    }
}
