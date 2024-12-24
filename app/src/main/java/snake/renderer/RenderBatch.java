package snake.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Vector4f;

import snake.components.SpriteRenderer;
import snake.engine.Window;

public class RenderBatch {
    private final int POS_SIZE = 2;
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int COLOR_SIZE = 4;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        this.shader = new Shader("./app/assets/default.glsl");
        this.shader.compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
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
    }

    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectonMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

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
        loadVertexProp(this.numSprites - 1);
        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    private void loadVertexProp(int i) {
        SpriteRenderer sprite=this.sprites[i];
        int offset =i*4 *VERTEX_SIZE;
        Vector4f color=sprite.getColor();
        float xadd=1.0f;
        float yadd=1.0f;
        for (int j = 0; j < 4; j++) {
            switch (j) {
                case 1:
                    yadd=0.0f;
                    break;
                case 2:
                    xadd=0.0f;
                    break;
                case 3:
                    yadd=1.0f;
                    break;
                default:
                    break;
            }
            // vertex 
            vertices[offset]=sprite.GameObject.transform.position.x+(xadd*sprite.GameObject.transform.scale.x);
            vertices[offset+1]=sprite.GameObject.transform.position.y+(yadd*sprite.GameObject.transform.scale.y);
            // color
            vertices[offset+2]=color.x;
            vertices[offset+3]=color.y;
            vertices[offset+4]=color.z;
            vertices[offset+5]=color.w;
            
            offset+=VERTEX_SIZE;
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

    public boolean hasRoom() {
        return this.hasRoom;
    }
}