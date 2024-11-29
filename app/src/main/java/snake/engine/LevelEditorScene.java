package snake.engine;

import static org.lwjgl.opengl.GL20.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_FLOAT;
import static org.lwjgl.opengl.GL20.GL_TRIANGLES;
import static org.lwjgl.opengl.GL20.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glDrawElements;
import static org.lwjgl.opengl.GL20.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glBindBuffer;
import static org.lwjgl.opengl.GL20.glBufferData;
import static org.lwjgl.opengl.GL20.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class LevelEditorScene extends Scene {
    private String vertexShaderSource = "#version 330 core\n" + //
            "layout (location=0) in vec3 aPos;\n" + //
            "layout (location=1) in vec4 aColor;\n" + //
            "\n" + //
            "out vec4 fColor;\n" + //
            "\n" + //
            "void main()\n" + //
            "{\n" + //
            "    fColor=aColor;\n" + //
            "    gl_Position=vec4(aPos, 1.0);\n" + //
            "}";
    private String fragmentShaderSource = "#version 330 core\n" + //
            "\n" + //
            "in vec4 fColor;\n" + //
            "out vec4 color;\n" + //
            "void main(){\n" + //
            "    color=fColor;\n" + //
            "}";
    private int vertexId, fragmentId, shaderId;
    private int vaoId, vboId, eboId;
    private float[] vertexArray = {
            // position:3 //color:4
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // buttom tight
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // top left
            0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
            -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f // buttom left
    };
    // must be counter clock wise
    private int[] elementArray = {
            2, 1, 0, // top right triangle
            0, 1, 3 // buttom left triangle
    };

    public LevelEditorScene() {
        super();
    }

    @Override
    public void init() {
        // load and compile vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // pass the source code of vertex shader
        glShaderSource(vertexId, vertexShaderSource);
        glCompileShader(vertexId);
        // check for errors
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.err.println("error:defaultshader.glsl\n\tvertex shader failed to compile");
            System.err.println(glGetShaderInfoLog(vertexId, len));
            assert false : "";
        }
        // load and compile fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // pass the source code of fragment shader
        glShaderSource(fragmentId, fragmentShaderSource);
        glCompileShader(fragmentId);
        // check for errors
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.err.println("error: defaultshader.glsl\n\tfragment shader failed to compile");
            System.err.println(glGetShaderInfoLog(fragmentId, len));
            assert false : "";
        }
        // link shader
        shaderId = glCreateProgram();
        glAttachShader(shaderId, vertexId);
        glAttachShader(shaderId, fragmentId);
        glLinkProgram(shaderId);
        success = glGetProgrami(shaderId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderId, GL_INFO_LOG_LENGTH);
            System.err.println("error: defaultshader.glsl\n\t cannot link shaders");
            System.err.println(glGetProgramInfoLog(shaderId, len));
            assert false : "";
        }
        // init vbo and vao
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        // create the vbo
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        
        // create the index buffer
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // vertex array pointers
        int positionSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        glUseProgram(shaderId);
        // bind the vao
        glBindVertexArray(vaoId);
        // gl enable pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length,GL_UNSIGNED_INT,0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);
    }

}