package snake.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public abstract void init();

    public abstract void update(float dt);

    public void start() {
        if (!isRunning) {
            gameObjects.forEach(GameObject::start);
        }
        isRunning=true;
    };

    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (isRunning) {
            gameObject.start();
        }
    };
}