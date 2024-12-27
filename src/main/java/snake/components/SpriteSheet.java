package snake.components;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import snake.renderer.Texture;

public class SpriteSheet {
    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteheight, int numSprite, int spacing) {
        this.sprites = new ArrayList<>();
        this.texture = texture;
        int currentX = 0;
        int currentY = 0;
        for (int i = 0; i < numSprite; i++) {
            float topY = (currentY + spriteheight) / ((float) texture.getHeight());
            float rightX = (currentX + spriteWidth) / ((float) texture.getWidth());
            float leftX = currentX / ((float) texture.getWidth());
            float bottomY = currentY / ((float) texture.getHeight());
            Vector2f[] texCoords = {
                    new Vector2f(rightX, bottomY),
                    new Vector2f(rightX, topY),
                    new Vector2f(leftX, topY),
                    new Vector2f(leftX, bottomY),
            };
            Sprite sprite = new Sprite(this.texture, texCoords);
            this.sprites.add(sprite);
            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY += spriteheight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    public int size() {
        return this.sprites.size();
    }
}
