package snake.engine;

public abstract class Component {
    public GameObject gameObject = null;

    public abstract void update(float dt);

    public abstract void start();

}
