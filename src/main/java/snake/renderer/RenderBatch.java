package snake.renderer;

import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;

import snake.components.SpriteRenderer;
import snake.engine.Window;
import snake.util.AssetPool;

public class RenderBatch implements Comparable<RenderBatch>{
    private final int POS_SIZE = 2;
    private final int POS_OFFSET = 0;
    private final int TEXTURE_COORDS_SIZE = 2;
    private final int TEXTURE_ID_SIZE = 1;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int COLOR_SIZE = 4;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private final int TEXTURE_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_ID_OFFSET = TEXTURE_OFFSET + TEXTURE_COORDS_SIZE * Float.BYTES;
    private List<Texture> textures;
    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = { 0, 1, 2, 3, 4, 5, 6, 7 };
    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;
    private int zIndex;

    public RenderBatch(int maxBatchSize, int zIndex) {
        shader = AssetPool.getShader("./assets/shader/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
        this.zIndex = zIndex;
    }

    public void start() {
        // create and bind vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        // allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // create and upload indices
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // enable buffer array pointer
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, TEXTURE_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_OFFSET);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void render() {
        boolean rebuffer = false;
        for (int i = 0; i < numSprites; i++) {
            SpriteRenderer spr = sprites[i];
            if (spr.isDirty()) {
                loadVertexProp(i);
                spr.setClean();
                rebuffer = true;
            }
        }
        if (rebuffer) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectonMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());
        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTexture", texSlots);
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    public void addSprite(SpriteRenderer spriteRenderer) {
        this.sprites[this.numSprites++] = spriteRenderer;
        if (spriteRenderer.getTexture() != null && !textures.contains(spriteRenderer.getTexture())) {
            textures.add(spriteRenderer.getTexture());
        }
        loadVertexProp(this.numSprites - 1);

        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }

    public boolean hasTextureRoom() {
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture texture) {
        return this.textures.contains(texture);
    }

    private void loadVertexProp(int index) {
        SpriteRenderer sprite = this.sprites[index];
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = sprite.getColor();
        Vector2f[] textureCoords = sprite.getTexCoords();
        int texId = 0;
        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == sprite.getTexture()) {
                    texId = i + 1;
                    break;
                }
            }
        }

        float xadd = 0.5f;
        float yadd = 0.5f;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1:
                    yadd = -0.5f;
                    break;
                case 2:
                    xadd = -0.5f;
                    break;
                case 3:
                    yadd = 0.5f;
                    break;
                default:
                    break;
            }
            // vertex
            float relX = xadd * sprite.gameObject.transform.scale.x;
            float relY = yadd * sprite.gameObject.transform.scale.y;
            vertices[offset] = sprite.gameObject.transform.position.x + relX * sprite.gameObject.transform.getCosAngle()
                    - relY * sprite.gameObject.transform.getSinAngle();
            vertices[offset + 1] = sprite.gameObject.transform.position.y
                    + relX * sprite.gameObject.transform.getSinAngle()
                    + relY * sprite.gameObject.transform.getCosAngle();
            // color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // texture
            vertices[offset + 6] = textureCoords[i].x;
            vertices[offset + 7] = textureCoords[i].y;
            // texture id
            vertices[offset + 8] = texId;
            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArray = 6 * index;
        int offset = 4 * index;

        elements[offsetArray] = offset + 3;
        elements[offsetArray + 1] = offset + 2;
        elements[offsetArray + 2] = offset + 0;

        elements[offsetArray + 3] = offset + 0;
        elements[offsetArray + 4] = offset + 2;
        elements[offsetArray + 5] = offset + 1;
    }

    public int getzIndex() {
        return this.zIndex;
    }


    @Override
    public int compareTo(RenderBatch o) {
        return this.zIndex-o.zIndex;
    }
}