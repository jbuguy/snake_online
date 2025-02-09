package snake.scenes;

import org.joml.Vector2f;
import org.joml.Vector4f;

import snake.components.FollowGameObject;
import snake.components.FollowMouse;
import snake.components.SpriteRenderer;
import snake.components.SpriteSheet;
import snake.engine.Camera;
import snake.engine.GameObject;
import snake.engine.MouseListener;
import snake.engine.Transform;
import snake.util.AssetPool;

public class MainMenuScene extends Scene {
    SpriteSheet sprites;
    private GameObject mario;
    private GameObject stuff=new GameObject("stuff",new Transform(new Vector2f(540,400),new Vector2f(1920, 1080)),-2);
    public MainMenuScene() {
        super();
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-400,-60));
        sprites = AssetPool.getSpriteSheet("./assets/images/spritesheet.png");
        stuff.addComponent(new SpriteRenderer(new Vector4f(0.83f, 0.83f, 0.9341f, 1.0f),AssetPool.getShader("./assets/shader/grid.glsl")));
        // stuff.addComponent(new FollowMouse(250.0f));
        this.addGameObjectToScene(stuff);
        mario = new GameObject("mario", new Transform(new Vector2f(600, 350), new Vector2f(50, 50)), 0);
        mario.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1),AssetPool.getShader("./assets/shader/circle.glsl")));
        mario.addComponent(new FollowMouse(250.0f));
        this.addGameObjectToScene(mario);
        GameObject s1=new GameObject("s1", new Transform(new Vector2f(600, 350), new Vector2f(50, 50)), 0);
        s1.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1),AssetPool.getShader("./assets/shader/circle.glsl")));
        // s1.addComponent(new FollowGameObject(mario, 250.0f));
        GameObject s2=new GameObject("s2", new Transform(new Vector2f(600, 350), new Vector2f(50, 50)), 0);
        s2.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1),AssetPool.getShader("./assets/shader/circle.glsl")));
        s2.addComponent(new FollowGameObject(s1, 250.0f));
        GameObject s3=new GameObject("s3", new Transform(new Vector2f(600, 350), new Vector2f(50, 50)), 0);
        s3.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1),AssetPool.getShader("./assets/shader/circle.glsl")));
        s3.addComponent(new FollowGameObject(s2, 250.0f));
        this.addGameObjectToScene(s1);
        this.addGameObjectToScene(s2);
        this.addGameObjectToScene(s3);
        this.activeGameObject = mario;
    }

    private void loadResources() {
        AssetPool.getShader("./assets/shader/default.glsl");
        AssetPool.addSpriteSheet("./assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("./assets/images/spritesheet.png"), 16, 16, 26, 0));
    }
    @Override
    public void update(float dt) {
        Vector2f direction=new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY()).sub(mario.transform.position).normalize().mul(250.0f*dt);
        // camera.position.add(direction);
        this.gameObjects.forEach(go -> go.update(dt));
        this.renderer.render();
    }

    @Override
    public void imgui() {
        // ImVec2 windwosPos=new ImVec2();
        // ImGui.getWindowPos(windwosPos);
        // ImVec2 windowSize =new ImVec2();
        // ImGui.getWindowSize(windowSize);
        // ImVec2 itemSpacing=new ImVec2();
        // ImGui.getStyle().getItemSpacing(itemSpacing);
        // float windowX2=windwosPos.x+windowSize.x;
        // for(int i=0 ;i<sprites.size();i++){
        // Sprite sprite=sprites.getSprite(i);
        // float spriteWidth=sprite.getWidth()*4;
        // float spriteheight =sprite.getHeight*4;
        // int id=sprite.getTexId();
        // }
    }

}