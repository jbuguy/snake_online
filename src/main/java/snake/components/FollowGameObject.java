package snake.components;

import org.joml.Vector2f;

import snake.engine.GameObject;

public class FollowGameObject extends Component {
    GameObject following;
    float speed;
    public FollowGameObject(GameObject following,float speed) {
        this.following = following;
        this.speed=speed;
    }

    @Override
    public void update(float dt) {
        Vector2f position = this.gameObject.transform.position;
        if (position.distance(following.transform.position)>20) {
            position.add(new Vector2f(following.transform.position)
                        .sub(position).normalize().mul(speed * dt));
        }
    }
    
}
