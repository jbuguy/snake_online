package snake.engine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Vector2f;

import snake.components.SpriteRenderer;
import snake.components.SpriteSheet;
import snake.util.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject mario;

    public LevelEditorScene() {
        super();
    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        SpriteSheet sprites = AssetPool.getSpriteSheet("./assets/spritesheet.png");
        mario = new GameObject("mario", new Transform(new Vector2f(960, 540), new Vector2f(50, 50)));
        mario.addComponent(new SpriteRenderer(sprites.getSprite(15)));
        this.addGameObjectToScene(mario);
    }

    private void loadResources() {
        AssetPool.getShader("./assets/default.glsl");
        AssetPool.addSpriteSheet("./assets/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("./assets/spritesheet.png"), 16, 16, 26, 0));
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            mario.transform.position.y += 40 * dt;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            mario.transform.position.y -= 40 * dt;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            mario.transform.position.x += 40 * dt;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            mario.transform.position.x -= 40 * dt;
        }
        this.gameObjects.forEach(go -> go.update(dt));
        this.renderer.render();
    }

}