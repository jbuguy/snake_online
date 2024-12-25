package snake.engine;

import org.joml.Vector2f;

import snake.components.Sprite;
import snake.components.SpriteRenderer;
import snake.util.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {
        super();
    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        SpriteSheet sprites=AssetPool.getSpriteSheet("./app/assets/spritesheet.png");
        GameObject mario = new GameObject("mario", new Transform(new Vector2f(100, 100), new Vector2f(500, 500)));
        mario.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(mario);

        GameObject gomba = new GameObject("gomba", new Transform(new Vector2f(200, 100), new Vector2f(50, 50)));
        gomba.addComponent(new SpriteRenderer(sprites.getSprite(15)));
        this.addGameObjectToScene(gomba);
    }

    private void loadResources() {
        AssetPool.getShader("./app/assets/default.glsl");
        AssetPool.addSpriteSheet("./app/assets/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("./app/assets/spritesheet.png"), 16, 16, 26, 0));
    }

    @Override
    public void update(float dt) {
        this.gameObjects.forEach(go -> go.update(dt));
        this.renderer.render();
    }

}