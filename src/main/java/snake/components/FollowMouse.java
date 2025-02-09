package snake.components;

import org.joml.Vector2f;

import snake.engine.MouseListener;

public class FollowMouse extends Component {
    Float speed;

    public FollowMouse(Float speed) {
        this.speed = speed;
    }

    @Override
    public void update(float dt) {
        Vector2f position = this.gameObject.transform.position;
        position.add(new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY())
                .sub(position).normalize().mul(speed * dt));
    }

}
