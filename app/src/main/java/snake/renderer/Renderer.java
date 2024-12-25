package snake.renderer;

import java.util.ArrayList;
import java.util.List;

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
    }

    private void add(SpriteRenderer spr) {
        boolean added = false;
        for (RenderBatch batch : batchs) {
            if (batch.hasRoom()) {
                if (batch.hasTexture(spr.getTexture()) || batch.hasTextureRoom()) {
                    batch.addSprite(spr);
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE);
            batch.start();
            batchs.add(batch);
            batch.addSprite(spr);
        }
    }

    public void render() {
        batchs.forEach(RenderBatch::render);
    }
}
