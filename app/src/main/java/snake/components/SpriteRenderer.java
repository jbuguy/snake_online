package snake.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import snake.engine.Component;
import snake.renderer.Texture;

public class SpriteRenderer extends Component {
    private Vector4f color;

    private Sprite sprite;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite=new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite=sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector4f getColor() {
        return color;
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

}
