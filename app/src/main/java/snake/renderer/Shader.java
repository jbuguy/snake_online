package snake.renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {
    private int shaderProgramID;
    private String vertexSrc, freagmentSrc;
    private String filePath;
    private boolean beingUsed = false;

    public Shader(String filepath) {
        this.filePath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitStrings = source.split("(#type)( )+([a-zA-Z]+)");
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSrc = splitStrings[1];
            } else if (firstPattern.equals("fragment")) {
                freagmentSrc = splitStrings[1];
            } else {
                throw new IOException();
            }
            if (secondPattern.equals("vertex")) {
                vertexSrc = splitStrings[2];
            } else if (secondPattern.equals("fragment")) {
                freagmentSrc = splitStrings[2];
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            assert false : "the file " + this.filePath + "error";
            e.printStackTrace();
        }
    }

    public void compile() {
        int vertexId, fragmentId;
        // load and compile vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // pass the source code of vertex shader
        glShaderSource(vertexId, vertexSrc);
        glCompileShader(vertexId);
        // check for errors
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.err.println("error:" + this.filePath + "\n\tvertex shader failed to compile");
            System.err.println(glGetShaderInfoLog(vertexId, len));
            assert false : "";
        }
        // load and compile fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // pass the source code of fragment shader
        glShaderSource(fragmentId, freagmentSrc);
        glCompileShader(fragmentId);
        // check for errors
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.err.println("error: " + this.filePath + "\n\tfragment shader failed to compile");
            System.err.println(glGetShaderInfoLog(fragmentId, len));
            assert false : "";
        }
        // link shader
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexId);
        glAttachShader(shaderProgramID, fragmentId);
        glLinkProgram(shaderProgramID);
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("error: " + this.filePath + "\n\t cannot link shaders");
            System.err.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }

    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f matrix4f) {
        int varlocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        matrix4f.get(matBuffer);
        glUniformMatrix4fv(varlocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varlocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varlocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadFloat(String varName, float vec) {
        int varlocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varlocation, vec);
    }

    public void uploadInt(String varName, int vec) {
        int varlocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varlocation, vec);
    }
    public void uploadTexture(String varName,int slot){
        int varlocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varlocation, slot);
    }
}