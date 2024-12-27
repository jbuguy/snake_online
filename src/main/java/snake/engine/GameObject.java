package snake.engine;

import java.util.ArrayList;
import java.util.List;

import snake.components.Component;

public class GameObject {
    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex=0;

    }

    public GameObject(String name, Transform transform,int zIndex) {
        this.name = name;
        this.zIndex=zIndex;
        this.components = new ArrayList<>();
        this.transform = transform;

    }

    public int getzIndex() {
        return zIndex;
    }

    public String getName() {
        return name;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);

                } catch (Exception e) {
                    e.printStackTrace();
                    assert false : "error casting  component";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        synchronized (components) {
            for (int i = 0; i < components.size(); i++) {
                if (componentClass.isAssignableFrom(components.get(i).getClass())) {
                    components.remove(i);
                    return;
                }
            }
        }
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
    }
    public void imgui(){
        components.forEach(Component::imgui);
    }

    public void update(float dt) {
        components.forEach(c -> c.update(dt));
    }

    public void start() {
        components.forEach(Component::start);
    }

}
