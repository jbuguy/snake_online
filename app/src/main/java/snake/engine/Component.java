package snake.engine;

public abstract class Component {
    public GameObject GameObject = null;

    public abstract void update(float dt);
    public abstract void start();

}
