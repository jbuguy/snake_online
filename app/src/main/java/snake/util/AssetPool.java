package snake.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import snake.engine.SpriteSheet;
import snake.renderer.Shader;
import snake.renderer.Texture;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    public static Shader getShader(String name) {
        File file = new File(name);
        if (shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        }
        Shader shader = new Shader(name);
        shader.compile();
        AssetPool.shaders.put(file.getAbsolutePath(), shader);
        return shader;
    }

    public static Texture getTexture(String name) {
        File file = new File(name);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        }
        Texture texture = new Texture(name);
        AssetPool.textures.put(file.getAbsolutePath(), texture);
        return texture;

    }
    public static void addSpriteSheet(String filename,SpriteSheet spriteSheet){
        File file=new File(filename);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())) {
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    } 
    public static SpriteSheet getSpriteSheet(String name){
        File file=new File(name);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())) {
            assert false:"Error: tried to access spriteSheet";
        }
        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(),null);
    }
}
