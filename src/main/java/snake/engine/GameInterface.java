package snake.engine;

import org.joml.Vector2f;

import snake.components.SpriteSheet;
import snake.util.AssetPool;

public class GameInterface extends Scene {


    public GameInterface() {
        super();
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f());
    }

    private void loadResources() {
        AssetPool.getShader("./assets/default.glsl");
        AssetPool.addSpriteSheet("./assets/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("./assets/spritesheet.png"), 16, 16, 26, 0));
    }
    @Override
    public void update(float dt) {
        this.gameObjects.forEach(go -> go.update(dt));
        this.renderer.render();
    }

}