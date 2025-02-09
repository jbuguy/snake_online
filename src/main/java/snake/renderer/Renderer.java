package snake.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import snake.components.FontRenderer;
import snake.components.SpriteRenderer;
import snake.engine.GameObject;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batchs;

    public Renderer() {
        this.batchs = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
        FontRenderer fontRenderer=gameObject.getComponent(FontRenderer.class);
        if (fontRenderer != null) {
            ArrayList<SpriteRenderer> sprites=fontRenderer.getSprites();
            for (SpriteRenderer sprite: sprites) {
                add(sprite);
            }
        }
    }

    private void add(SpriteRenderer spr) {
        boolean added = false;
        for (RenderBatch batch : batchs) {
            if (batch.hasRoom() && batch.hasShader(spr.getShader())&& batch.getzIndex()==spr.gameObject.getzIndex()) {
                if (batch.hasTexture(spr.getTexture()) || batch.hasTextureRoom() ) {
                    batch.addSprite(spr);
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE,spr.gameObject.getzIndex(),spr.getShader());
            batch.start();
            batchs.add(batch);
            batch.addSprite(spr);
            Collections.sort(batchs);
        }
    }

    public void render() {
        batchs.forEach(RenderBatch::render);
    }
}
