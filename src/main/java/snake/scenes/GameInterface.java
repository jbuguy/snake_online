package snake.scenes;

import org.joml.Vector2f;

import snake.components.SpriteRenderer;
import snake.components.SpriteSheet;
import snake.engine.Camera;
import snake.engine.GameObject;
import snake.engine.MouseListener;
import snake.engine.Transform;
import snake.util.AssetPool;

public class GameInterface extends Scene {
    SpriteSheet sprites;
    private GameObject mario;

    public GameInterface() {
        super();
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(0,0));
        sprites = AssetPool.getSpriteSheet("./assets/images/spritesheet.png");
        mario = new GameObject("mario", new Transform(new Vector2f(540, 400), new Vector2f(100, 100)), 0);
        mario.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(mario);
        this.activeGameObject = mario;
    }

    private void loadResources() {
        AssetPool.getShader("./assets/shader/default.glsl");
        AssetPool.addSpriteSheet("./assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("./assets/images/spritesheet.png"), 16, 16, 26, 0));
    }

    @Override
    public void update(float dt) {
        System.out.println("fps:"+1/dt);
        Vector2f direction=new Vector2f(MouseListener.getorthoX()-mario.transform.position.x,MouseListener.getOrthoY()-mario.transform.position.y);
        direction.normalize();
        float angle=(float)Math.atan2(direction.y,direction.x );
        mario.transform.setAngle((float)(angle*180/Math.PI));
        mario.transform.position.add(direction);

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