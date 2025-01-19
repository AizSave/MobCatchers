package mobcatchers.objects;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.localization.Localization;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.FueledInventoryObjectEntity;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.particle.Particle;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.container.object.CraftingStationContainer;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.ObjectDamagedTextureArray;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class Brainwasher extends GameObject {
    public ObjectDamagedTextureArray texture;

    public Brainwasher() {
        super(new Rectangle(32, 32));
        this.isLightTransparent = true;
        this.lightHue = 300.0F;
        this.lightSat = 0.2F;
        this.hoverHitbox = new Rectangle(0, -16, 32, 48);
    }

    public int getLightLevel(Level level, int layerID, int tileX, int tileY) {
        BrainwasherObjectEntity fueledObjectEntity = this.getForgeObjectEntity(level, tileX, tileY);
        return fueledObjectEntity != null && fueledObjectEntity.isFuelRunning() ? 100 : 0;
    }

    public void tickEffect(Level level, int layerID, int tileX, int tileY) {
        super.tickEffect(level, layerID, tileX, tileY);
        if (GameRandom.globalRandom.nextInt(10) == 0) {
            ObjectEntity objectEntity = level.entityManager.getObjectEntity(tileX, tileY);
            if (objectEntity instanceof BrainwasherObjectEntity && ((BrainwasherObjectEntity) objectEntity).isFuelRunning()) {
                int startHeight = 16 + GameRandom.globalRandom.nextInt(16);
                level.entityManager.addParticle((float) (tileX * 32 + GameRandom.globalRandom.getIntBetween(8, 24)), (float) (tileY * 32 + 32), Particle.GType.COSMETIC).color(new Color(255, 0, 255)).heightMoves((float) startHeight, (float) (startHeight + 20)).lifeTime(1000);
            }
        }

    }

    public void loadTextures() {
        super.loadTextures();
        this.texture = ObjectDamagedTextureArray.loadAndApplyOverlay(this, "objects/brainwasher");
    }

    public Rectangle getCollision(Level level, int x, int y, int rotation) {
        return rotation % 2 == 0 ? new Rectangle(x * 32 + 2, y * 32 + 6, 28, 20) : new Rectangle(x * 32 + 6, y * 32 + 2, 20, 28);
    }

    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        GameTexture texture = this.texture.getDamagedTexture(this, level, tileX, tileY);
        boolean isFueled = false;
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(tileX, tileY);
        if (objectEntity instanceof BrainwasherObjectEntity) {
            isFueled = ((BrainwasherObjectEntity) objectEntity).isFuelRunning();
        }

        int spriteHeight = texture.getHeight();
        final TextureDrawOptions options = texture.initDraw().sprite(isFueled ? 1 : 0, 0, 32, spriteHeight).light(light).pos(drawX, drawY - (spriteHeight - 32));

        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            public int getSortY() {
                return 16;
            }

            public void draw(TickManager tickManager) {
                options.draw();
            }
        });
    }

    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha, PlayerMob player, GameCamera camera) {
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        GameTexture texture = this.texture.getDamagedTexture(0.0F);
        int spriteHeight = texture.getHeight();
        texture.initDraw().sprite(0, 0, 32, spriteHeight).alpha(alpha).draw(drawX, drawY - (spriteHeight - 32));
    }

    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new BrainwasherObjectEntity(level, x, y);
    }

    public String getInteractTip(Level level, int x, int y, PlayerMob perspective, boolean debug) {
        return Localization.translate("controls", "opentip");
    }

    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return true;
    }

    public void interact(Level level, int x, int y, PlayerMob player) {
        if (level.isServer()) {
            CraftingStationContainer.openAndSendContainer(ContainerRegistry.FUELED_PROCESSING_STATION_CONTAINER, player.getServerClient(), level, x, y);
        }

    }

    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = super.getItemTooltips(item, perspective);
        tooltips.add(Localization.translate("itemtooltip", "brainwashertip"));
        return tooltips;
    }

    public BrainwasherObjectEntity getForgeObjectEntity(Level level, int tileX, int tileY) {
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(tileX, tileY);
        return objectEntity instanceof BrainwasherObjectEntity ? (BrainwasherObjectEntity) objectEntity : null;
    }
}

