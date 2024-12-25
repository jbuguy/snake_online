package snake.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import snake.engine.Component;
import snake.engine.Transform;
import snake.renderer.Texture;

public class SpriteRenderer extends Component {
    private Vector4f color;

    private Sprite sprite;
    private Transform lastTransform;
    private boolean isDirty = true;

    public boolean isDirty() {
        return isDirty;
    }
    public void setClean(){
        this.isDirty=false;
    }

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
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
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }

    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Vector4f color) {
        if (this.color.equals(color)) {
            this.color.set(color);
            this.isDirty = true;
        }
    }
}
