package snake.engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;

    public GameObject(String name) {
        this.name = name;
        this.components=new ArrayList<>();

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
    public void addComponent(Component component){
        this.components.add(component);
        component.GameObject=this;
    }
    public void update(float dt){
        components.forEach(c -> c.update(dt));
    }
    public void start(){
        components.forEach(Component::start);
    }
}
