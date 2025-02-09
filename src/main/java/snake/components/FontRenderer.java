package snake.components;

import java.util.ArrayList;

import org.joml.Vector4f;

import snake.engine.Transform;
import snake.renderer.Shader;
import snake.util.AssetPool;

public class FontRenderer extends Component {
    private Transform lastTransform;
    private String text;
    private SpriteSheet spriteSheet;

    private ArrayList<SpriteRenderer> sprites;

    public ArrayList<SpriteRenderer> getSprites() {
        return sprites;
    }

    public void setText(String text) {
        this.text = text;
        isDirty = true;
    }

    private Shader shader;
    private Vector4f color;

    public Shader getShader() {
        return shader;
    }

    public FontRenderer(Vector4f color) {
        this.color = color;
        spriteSheet = AssetPool.getSpriteSheet("Segoe UI.ttf");
        this.shader = AssetPool.getShader("./assets/shader/default.glsl");
    }

    private boolean isDirty = true;

    public void setClean() {
        isDirty = false;
    }

    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
        addSpritesFromText();
    }

    private void addSpritesFromText() {
        sprites.clear();
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                sprites.add(new SpriteRenderer(spriteSheet.getSprite(26)));
            } else {
                sprites.add(new SpriteRenderer(spriteSheet.getSprite(c - 'a')));
            }
        }
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

}
