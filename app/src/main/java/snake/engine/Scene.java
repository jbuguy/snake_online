package snake.engine;

import java.util.ArrayList;
import java.util.List;

import snake.renderer.Renderer;

public abstract class Scene {
    protected Camera camera;
    protected Renderer renderer=new Renderer();
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public abstract void init();

    public abstract void update(float dt);

    public void start() {
        for (GameObject gameObject : gameObjects) {
                gameObject.start();
                renderer.add(gameObject);
        }
        isRunning = true;
    };

    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (isRunning) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
    };

    public Camera getCamera() {
        return this.camera;
    }
}